package com.axxes.garagebandprototype.view;

import com.axxes.garagebandprototype.Audio.effects.*;
import com.axxes.garagebandprototype.model.instrument.Instrument;
import com.axxes.garagebandprototype.model.loop.Drumloop;
import com.axxes.garagebandprototype.model.measures.Measure;
import com.axxes.garagebandprototype.presenter.Presenter;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Component
public class BeatGrid implements ResponsiveView {

    private ReadOnlyDoubleProperty rootWidth;
    private ReadOnlyDoubleProperty rootHeight;

    private Pane beatGridContainer;
    private GridPane beatGrid;
    private Highlighter highlighter;
    private int rowCount;

    @Autowired
    private Presenter presenter;
    @Autowired
    private Drumloop drumloop;
    private int beats;
    private int beatsPerMeasure;

    @Autowired
    private Echo echoEffect;
    @Autowired
    private NoEffect noEffect;
    @Autowired
    private Reverb reverbEffect;
    @Autowired
    private RingModulator ringModulatorEffect;
    @Autowired
    private Flanger flangerEffect;
    @Autowired
    private Distortion distortionEffect;

    public BeatGrid() {
        this.rootWidth = new SimpleDoubleProperty(0);
        this.rootHeight = new SimpleDoubleProperty(0);

        this.beatGridContainer = new Pane();
        this.beatGridContainer.setStyle("-fx-background-color: #33c2ff;");
        this.beatGrid = new GridPane();
        this.highlighter = new Highlighter();
    }

    @PostConstruct
    public void init() {
        this.beats = drumloop.getMeasures().stream().map(Measure::getBeats).mapToInt(Collection::size).sum();
        this.beatsPerMeasure = drumloop.getBeatsPerMeasure();
        this.buildLayout();
    }

    @Override
    public Pane getSmallView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        this.rootWidth = width;
        this.rootHeight = height;

        this.createBaseGrid();
        this.initialiseSmallLayout();

        return this.beatGridContainer;
    }

    @Override
    public Pane getLargeView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        this.rootWidth = width;
        this.rootHeight = height;

        this.createBaseGrid();
        this.initialiseLargeLayout();

        return this.beatGridContainer;
    }

    private void initialiseSmallLayout() {
        this.setBeatGridSize(rootWidth.subtract(20), rootHeight.multiply(0.75));
        this.highlighter.setWidthProperty(rootWidth.subtract(20).divide(this.beats + 1));
        this.highlighter.setHeightProperty(rootHeight.multiply(0.75));
    }

    private void setBeatGridSize(DoubleBinding width, DoubleBinding height) {
        this.beatGrid.setStyle("-fx-background-color: #ffca28;");
        this.beatGrid.prefWidthProperty().bind(width);
        this.beatGrid.prefHeightProperty().bind(height);
    }

    private void createBaseGrid() {
        if (beatGrid.getChildren().size() == 0 && rootWidth.getValue() > 0) {
            this.beatGrid.add(createLabel("Beat"), 0, 0);
            for (int i = 1; i <= this.beats; i++) {
                int currentBeat = ((i - 1) % this.beatsPerMeasure) + 1;
                this.beatGrid.add(createLabel(String.valueOf(currentBeat)), i, 0);
            }
            this.beatGrid.setBorder(Border.EMPTY);
            this.rowCount = 1;
        }
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-background-color: #f200ff;");

        DoubleBinding labelWidth = this.rootWidth.subtract(20).divide(this.beats);

        label.prefWidthProperty().bind(labelWidth);
        return label;
    }

    private void buildLayout() {
        this.beatGridContainer.getChildren().addAll(beatGrid, highlighter.getHighlighter());
    }

    private void initialiseLargeLayout() {
        this.setBeatGridSize(rootWidth.multiply(0.75).subtract(15), rootHeight.subtract(20));
        this.highlighter.setWidthProperty(rootWidth.multiply(0.75).subtract(15).divide(this.beats + 1));
        this.highlighter.setHeightProperty(rootHeight.subtract(20));
    }

    public void addLabel(String description, int column, int row) {
        this.beatGrid.add(createLabel(description), column, row);
    }

    public void addInstrumentButtons(Instrument instrument) {
        for (int i = 0; i < this.beats; i++) {
            int measureCount = i / 4;
            int beatCount = i % 4;
            ToggleButton button = createToggleInstrumentButton(instrument, measureCount, beatCount);
            if (hasBeatInstrument(measureCount, beatCount, instrument)){
                button.setSelected(true);
                button.setStyle("-fx-background-color: darkgray");
            }
            this.beatGrid.add(button, i + 1, rowCount);

            createEffectsContextMenu(instrument, button, measureCount, beatCount);
        }
    }

    private boolean hasBeatInstrument(int measureCount, int beatCount, Instrument instrument) {
        return this.drumloop.getMeasures().get(measureCount).getBeats().get(beatCount).hasInstrument(instrument);
    }

    private ToggleButton createToggleInstrumentButton(Instrument instrument, int measureCount, int beatCount) {
        ToggleButton instrumentButton = new ToggleButton();
        DoubleBinding buttonWidth = this.rootWidth.subtract(20).divide(this.beats);
        // TODO: Fix width is same than height.
        instrumentButton.prefWidthProperty().bind(buttonWidth);
        instrumentButton.prefHeightProperty().bind(buttonWidth);
        instrumentButton.getStyleClass().add(instrument.getClass().getSimpleName().toLowerCase());
        instrumentButton.setOnAction(event -> presenter.instrumentToggle(instrument, measureCount, beatCount, instrumentButton));

        return instrumentButton;
    }

    private void createEffectsContextMenu(Instrument instrument, ToggleButton button, int measureCount, int beatCount) {
        ContextMenu effectsMenu = new ContextMenu();

        MenuItem noEffectItem = createMenuItem("No effect", Color.DARKGRAY);
        noEffectItem.setOnAction(event -> {
            presenter.instrumentAddEffect(instrument, measureCount, beatCount, noEffect);
            if (button.isSelected()){
                button.setStyle("-fx-background-color: darkgray");
            }
        });

        MenuItem echoEffectItem = createMenuItem("Echo", Color.AQUA);
        echoEffectItem.setOnAction(event -> {
            presenter.instrumentAddEffect(instrument, measureCount, beatCount, echoEffect);
            if (button.isSelected()){
                button.setStyle("-fx-background-color: aqua");
            }
        });

        MenuItem reverbEffectItem = createMenuItem("Reverb", Color.CHOCOLATE);
        reverbEffectItem.setOnAction(event -> {
            presenter.instrumentAddEffect(instrument, measureCount, beatCount, reverbEffect);
            if (button.isSelected()) {
                button.setStyle("-fx-background-color: chocolate");
            }
        });

        MenuItem ringModulatorEffectItem = createMenuItem("Ring Modulator", Color.DARKGRAY);
        ringModulatorEffectItem.setOnAction(event -> {
            presenter.instrumentAddEffect(instrument, measureCount, beatCount, ringModulatorEffect);
            if (button.isSelected()){
                button.setStyle("-fx-background-color: darkgray");
            }
        });

        MenuItem flangerEffectItem = createMenuItem("Flanger", Color.DARKGRAY);
        flangerEffectItem.setOnAction(event -> {
            presenter.instrumentAddEffect(instrument, measureCount, beatCount, flangerEffect);
            if (button.isSelected()){
                button.setStyle("-fx-background-color: darkgray");
            }
        });

        MenuItem distortionEffectItem = createMenuItem("Distortion", Color.DARKGRAY);
        distortionEffectItem.setOnAction(event -> {
            presenter.instrumentAddEffect(instrument, measureCount, beatCount, distortionEffect);
            if (button.isSelected()){
                button.setStyle("-fx-background-color: darkgray");
            }
        });

        effectsMenu.getItems().addAll(noEffectItem, echoEffectItem, reverbEffectItem, ringModulatorEffectItem, flangerEffectItem, distortionEffectItem);
        button.setContextMenu(effectsMenu);
    }

    private MenuItem createMenuItem(String effect, Color color) {
        MenuItem menuItem = new MenuItem(effect);
        Rectangle rectangle = new Rectangle(10, 10);
        rectangle.setFill(color);
        menuItem.setGraphic(rectangle);
        return menuItem;
    }

    public void incrementRowCount() {
        this.rowCount++;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public ReadOnlyDoubleProperty getRootWidth() {
        return rootWidth;
    }

    public void resetInstruments() {
        this.beatGrid.getChildren().clear();
        createBaseGrid();
        this.rowCount = 1;
    }

    public void stepHighlight(int beat) {
        this.highlighter.setHighlightStep(beat);
    }

}
