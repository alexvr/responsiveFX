package com.axxes.garagebandprototype.view;

import com.axxes.garagebandprototype.model.instrument.Instrument;
import com.axxes.garagebandprototype.model.loop.Drumloop;
import com.axxes.garagebandprototype.model.measures.Measure;
import com.axxes.garagebandprototype.presenter.Presenter;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Component
public class BeatGrid implements ResponsiveView {

    private ReadOnlyDoubleProperty rootWidth;
    private ReadOnlyDoubleProperty rootHeight;

    @Autowired
    private Presenter presenter;

    private Pane beatGridContainer;
    private GridPane beatGrid;
    private int rowCount;

    @Autowired
    private Drumloop drumloop;
    private int beats;
    private int beatsPerMeasure;

    public BeatGrid() {
        this.beatGridContainer = new Pane();
        this.beatGridContainer.setStyle("-fx-background-color: #33c2ff;");
        this.beatGrid = new GridPane();
        this.rootWidth = new SimpleDoubleProperty(0);
        this.rootHeight = new SimpleDoubleProperty(0);
    }

    @PostConstruct
    public void init() {
        this.beats = drumloop.getMeasures().stream().map(Measure::getBeats).mapToInt(Collection::size).sum();
        this.beatsPerMeasure = drumloop.getBeatsPerMeasure();
        // this.createBaseGrid();
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
        //this.setContainerSize(rootWidth.multiply(1), rootHeight.multiply(0.75));
        this.setBeatGridSize(rootWidth.subtract(20), rootHeight.multiply(0.75));
    }

    private void setContainerSize(DoubleBinding width, DoubleBinding height) {
        //this.beatGridContainer.prefWidthProperty().bind(width);
        this.beatGridContainer.prefHeightProperty().bind(height);
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
        this.beatGridContainer.getChildren().addAll(beatGrid);
    }

    private void initialiseLargeLayout() {
        //this.setContainerSize(rootWidth.multiply(0.75), rootHeight.multiply(1));
        this.setBeatGridSize(rootWidth.multiply(0.75).subtract(15), rootHeight.subtract(20));
    }

    public void add(Node node, int column, int row) {
        this.beatGrid.add(node, column, row);
    }

    public void addLabel(String description, int column, int row) {
        this.beatGrid.add(createLabel(description), column, row);
    }

    public void addInstrumentButtons(Instrument instrument, int row) {
        for (int i = 0; i < this.beats; i++) {
            int measureCount = i / 4;
            int beatCount = i % 4;
            Button button = createToggleInstrumentButton(instrument, measureCount, beatCount);
            this.beatGrid.add(button, i + 1, rowCount);

            // createEffectsContextMenu(instrument, button, measureCount, beatCount);
        }
    }

    private Button createToggleInstrumentButton(Instrument instrument, int measureCount, int beatCount) {
        Button instrumentButton = new Button();
        DoubleBinding buttonWidth = this.rootWidth.subtract(20).divide(this.beats);
        // TODO: Fix width is same than height.
        instrumentButton.prefWidthProperty().bind(buttonWidth);
        instrumentButton.prefHeightProperty().bind(buttonWidth);
        instrumentButton.getStyleClass().add(instrument.getClass().getSimpleName().toLowerCase());
        instrumentButton.setOnAction(event -> presenter.instrumentToggle(instrument, measureCount, beatCount));
        presenter.bindBeatToButton(instrument, instrumentButton, measureCount, beatCount);

        return instrumentButton;
    }

    public void incrementRowCount() {
        this.rowCount++;
    }

    public void resetRowCount() {
        this.rowCount = 1;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public ReadOnlyDoubleProperty getRootWidth() {
        return rootWidth;
    }

    public ReadOnlyDoubleProperty getRootHeight() {
        return rootHeight;
    }

    public void resetInstruments() {
        this.beatGrid.getChildren().clear();
        this.rowCount = 1;
    }

}
