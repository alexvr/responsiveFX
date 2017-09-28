package com.axxes.garagebandprototype.presenter;

import com.axxes.garagebandprototype.Audio.AudioDevice;
import com.axxes.garagebandprototype.Audio.effects.*;
import com.axxes.garagebandprototype.model.instrument.*;
import com.axxes.garagebandprototype.model.loop.Drumloop;
import com.axxes.garagebandprototype.model.measures.Beat;
import com.axxes.garagebandprototype.model.measures.Measure;
import com.axxes.garagebandprototype.util.MusicXmlParser;
import com.axxes.garagebandprototype.util.MusicXmlWriter;
import com.axxes.garagebandprototype.view.BeatGrid;
import com.axxes.garagebandprototype.view.InstrumentSelection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collection;
import java.util.Set;

@Controller
public class Presenter {

    @Autowired
    private BeatGrid beatGrid;
    @Autowired
    private InstrumentSelection instrumentSelection;

    private Rectangle highLighter;
    private int highlighterPosition;

    private Slider slider;
    private boolean programmaticSliderChange;

    private final MusicXmlParser parser;
    private final MusicXmlWriter writer;

    private Timeline loopTimeline;
    private IntegerProperty bpm;
    private int beats;
    private int beatsPerMeasure;

    private final AudioDevice audioDevice;

    private final Drumloop drumloop;
    private final Kick kick;
    private final Cymbal cymbal;
    private final HiHat hiHat;
    private final Snare snare;

    private final Echo echoEffect;
    private final NoEffect noEffect;
    private final Reverb reverbEffect;
    private final RingModulator ringModulatorEffect;
    private final Flanger flangerEffect;
    private final Distortion distortionEffect;

    @Autowired
    public Presenter(Drumloop drumloop, MusicXmlParser parser, MusicXmlWriter writer, AudioDevice audioDevice, Kick kick, Cymbal cymbal, HiHat hiHat, Flanger flangerEffect, Snare snare, Echo echoEffect, NoEffect noEffect, RingModulator ringModulatorEffect, Reverb reverbEffect, Distortion distortionEffect) {
        this.parser = parser;
        this.writer = writer;
        this.audioDevice = audioDevice;
        this.drumloop = drumloop;
        this.kick = kick;
        this.cymbal = cymbal;
        this.hiHat = hiHat;
        this.snare = snare;
        this.flangerEffect = flangerEffect;
        this.echoEffect = echoEffect;
        this.noEffect = noEffect;
        this.ringModulatorEffect = ringModulatorEffect;
        this.reverbEffect = reverbEffect;
        this.distortionEffect = distortionEffect;
        this.highlighterPosition = 0;
    }

    @PostConstruct
    public void init() {
        this.bpm = new SimpleIntegerProperty();
        this.bpm.bindBidirectional(drumloop.getBpm());
        Bindings.bindBidirectional(instrumentSelection.getBpmProperty(), this.bpm, new NumberStringConverter());
        this.beats = drumloop.getMeasures().stream().map(Measure::getBeats).mapToInt(Collection::size).sum();
    }

    private void createLoop() {
        if (this.loopTimeline != null) {
            this.loopTimeline.stop();
        }
        int timeBetweenBeats = 60000 / this.bpm.get();

        this.loopTimeline = new Timeline(new KeyFrame(
                Duration.millis(timeBetweenBeats),
                ae -> {
                    Logger.getLogger(Presenter.class).info("Drumloop step.");
                    stepHighlighterAndSlider();
                    this.drumloop.step();
                }));
        this.loopTimeline.setCycleCount(Animation.INDEFINITE);
    }


    private void createSlider(){
        this.slider = new Slider(0, this.beats - 1, this.highlighterPosition);
        this.slider.setShowTickMarks(true);
        this.slider.setBlockIncrement(1);
        this.slider.setSnapToTicks(true);
        this.slider.setMajorTickUnit(1);
        this.slider.setMinorTickCount(0);
        this.slider.setLayoutX(105);
        this.slider.setLayoutY(40);
        this.slider.setPrefWidth(this.beats*60 - 40);
        this.slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!programmaticSliderChange){
                changeHighlighterPosition(newValue.intValue());
            }
        });

        //root.getChildren().add(slider);
    }

    private void changeHighlighterPosition(int position){
        this.highlighterPosition = position;
        int measureCount = position / this.beatsPerMeasure;
        int beatCount = position % this.beatsPerMeasure;
        drumloop.setCurrentMeasure(measureCount);
        drumloop.getMeasures().get(measureCount).setCurrentBeat(beatCount);
        this.highLighter.setX(85 + (60*this.highlighterPosition));
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        ReadOnlyDoubleProperty width = this.beatGrid.getRootWidth();
        DoubleBinding db = width.subtract(20).divide(this.beats);
        label.prefWidthProperty().bind(db);
        return label;
    }

    private void createHighlighter() {
        this.highLighter = new Rectangle(85, 65, 60, 30);
        this.highlighterPosition = 0;
        this.highLighter.setMouseTransparent(true);
        this.highLighter.setFill(Color.RED);
        this.highLighter.setOpacity(0.5);
        //this.root.getChildren().add(highLighter);
    }

    private void stepHighlighterAndSlider(){
        /*if (this.highlighterPosition == this.beats){
            this.highlighterPosition = 0;
        }
        this.highLighter.setX(85 + (60*this.highlighterPosition));
        programmaticSliderChange = true;
        this.slider.setValue(this.highlighterPosition);
        programmaticSliderChange = false;
        this.highlighterPosition++;*/
        this.beatGrid.stepHighlight(highlighterPosition);
        this.highlighterPosition++;
        if (this.highlighterPosition == this.beats) {
            this.highlighterPosition = 0;
        }
    }

    private void disableAddInstrumentButton(Instrument instrument) {
        if (instrument.getClass().equals(HiHat.class)) {
            this.instrumentSelection.setDisabledHiHat(true);
        } else if (instrument.getClass().equals(Snare.class)) {
            this.instrumentSelection.setDisabledSnare(true);
        } else if (instrument.getClass().equals(Kick.class)) {
            this.instrumentSelection.setDisabledKick(true);
        } else if (instrument.getClass().equals(Cymbal.class)) {
            this.instrumentSelection.setDisabledCymbal(true);
        }
    }

    private void enableAddInstrumentButton() {
        this.instrumentSelection.setDisabledCymbal(false);
        this.instrumentSelection.setDisabledKick(false);
        this.instrumentSelection.setDisabledSnare(false);
        this.instrumentSelection.setDisabledHiHat(false);
    }

    public void addInstrumentLine(Instrument instrument) {
        // this.highLighter.setHeight(this.highLighter.getHeight()+50);
        disableAddInstrumentButton(instrument);
        int rowCount = this.beatGrid.getRowCount();
        this.beatGrid.addLabel(instrument.getClass().getSimpleName(), 0, rowCount);
        this.beatGrid.addInstrumentButtons(instrument);

        this.beatGrid.incrementRowCount();
    }

    private void createEffectsContextMenu(Instrument instrument, Button button, int measureCount, int beatCount) {
        ContextMenu effectsMenu = new ContextMenu();

        MenuItem noEffectItem = new MenuItem("No effect");
        Rectangle noEffectRectangle = new Rectangle(10, 10);
        noEffectRectangle.setFill(Color.DARKGRAY);
        noEffectItem.setGraphic(noEffectRectangle);
        noEffectItem.setOnAction(event -> {
            instrumentAddEffect(instrument, measureCount, beatCount, noEffect);
            if (!button.getStyle().equals("")){
                button.setStyle("-fx-background-color: darkgray");
            }
        });

        MenuItem echoEffectItem = new MenuItem("Echo");
        Rectangle echoRectangle = new Rectangle(10, 10);
        echoRectangle.setFill(Color.AQUA);
        echoEffectItem.setGraphic(echoRectangle);
        echoEffectItem.setOnAction(event -> {
            instrumentAddEffect(instrument, measureCount, beatCount, echoEffect);
            if (!button.getStyle().equals("")){
                button.setStyle("-fx-background-color: aqua");
            }
        });

        MenuItem reverbEffectItem = new MenuItem("Reverb");
        reverbEffectItem.setOnAction(event -> {
            instrumentAddEffect(instrument, measureCount, beatCount, reverbEffect);
            if (!button.getStyle().equals("")) {
                button.setStyle("-fx-background-color: chocolate");
            }
        });

        MenuItem ringModulatorEffectItem = new MenuItem("Ring Modulator");
        ringModulatorEffectItem.setOnAction(event -> {
            instrumentAddEffect(instrument, measureCount, beatCount, ringModulatorEffect);
        });

        MenuItem flangerEffectItem = new MenuItem("Flanger");
        flangerEffectItem.setOnAction(event -> instrumentAddEffect(instrument, measureCount, beatCount, flangerEffect));

        MenuItem distortionEffectItem = new MenuItem(("Distortion"));
        distortionEffectItem.setOnAction(event -> instrumentAddEffect(instrument, measureCount, beatCount, distortionEffect));

        effectsMenu.getItems().addAll(noEffectItem, echoEffectItem, reverbEffectItem, ringModulatorEffectItem, flangerEffectItem, distortionEffectItem);
        button.setContextMenu(effectsMenu);
    }

    private void instrumentAddEffect(Instrument instrument, int measureCount, int beatCount, Effect effect) {
        this.drumloop.getMeasures()
                .get(measureCount)
                .getBeats()
                .get(beatCount)
                .setAudioEffect(instrument, effect);
    }


    public void instrumentToggle(Instrument instrument, int measureCount, int beatCount) {
        if (this.drumloop.hasInstrument(instrument, measureCount, beatCount)) {
            this.drumloop.removeInstrument(instrument, measureCount, beatCount);
        } else {
            this.drumloop.addInstrument(instrument, measureCount, beatCount);
        }
    }

    public void bindBeatToButton(Instrument instrument, Button button, int measureCount, int beatCount) {
        Beat beat = this.drumloop.getMeasures().get(measureCount).getBeats().get(beatCount);
        BooleanBinding hasInstrument = Bindings.createBooleanBinding(() -> beat.getInstruments().contains(instrument), beat.getInstruments());
        hasInstrument.addListener(observable -> button.styleProperty().set(hasInstrument.getValue() ? "-fx-background-color: darkgray" : ""));
//        button.styleProperty().bind(Bindings.when(hasInstrument).then("-fx-background-color: darkgray").otherwise(""));
    }


    public void menuButtonSave() {
        final Stage dialog = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("drumloops"));
        fileChooser.setInitialFileName("drumloop.xml");
        fileChooser.setTitle("Save music file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("xml", "*.xml", "*.XML");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        File file = fileChooser.showSaveDialog(dialog);
        if (file != null) {
            writer.writeXMLFromDrumloop(drumloop, file);
        }
    }

    public void menuButtonLoad() {
        final Stage dialog = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("drumloops"));
        fileChooser.setTitle("Open music file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("xml", "*.xml", ".XML");
        fileChooser.getExtensionFilters().addAll(filter);
        fileChooser.setSelectedExtensionFilter(filter);

        File file = fileChooser.showOpenDialog(dialog);

//        this.highlighterPosition = 0;
//        this.highLighter.setX(85);
//        this.highLighter.setHeight(30);
        if (file != null) {
            deleteInstrumentLines();
            parser.parserDrumloopFromXml(file);
            createInstrumentLines();
            setBeatButtonState();
            if (instrumentSelection.isPlayPauseSelected()) {
                playLoop();
            }
        }
    }

    private void setBeatButtonState() {

    }

    private void deleteInstrumentLines() {
        this.beatGrid.resetInstruments();
    }

    private void createInstrumentLines() {
        enableAddInstrumentButton();
        Set<Instrument> instrumentSet = this.drumloop.getInstrumentSet();
        for (Instrument i : instrumentSet) {
            addInstrumentLine(i);
        }
    }

    public void exit() {
       kick.shutdownExecutor();
       cymbal.shutdownExecutor();
       hiHat.shutdownExecutor();
       snare.shutdownExecutor();
       audioDevice.destroy();
       Platform.exit();
    }

    public void kickPressed() {
        addInstrumentLine(kick);
    }

    public void snarePressed() {
        addInstrumentLine(snare);
    }

    public void hihatPressed() {
        addInstrumentLine(hiHat);
    }

    public void cymbalPressed() {
        addInstrumentLine(cymbal);
    }

    public void playLoop() {
        createLoop();
        this.loopTimeline.play();
    }

    public void stopLoop() {
        this.loopTimeline.stop();
    }
}
