package com.axxes.garagebandprototype.presenter;

import com.axxes.garagebandprototype.Audio.AudioDevice;
import com.axxes.garagebandprototype.Audio.effects.Effect;
import com.axxes.garagebandprototype.Audio.effects.NoEffect;
import com.axxes.garagebandprototype.model.instrument.*;
import com.axxes.garagebandprototype.model.loop.Drumloop;
import com.axxes.garagebandprototype.model.measures.Measure;
import com.axxes.garagebandprototype.util.MusicXmlParser;
import com.axxes.garagebandprototype.util.MusicXmlWriter;
import com.axxes.garagebandprototype.view.BeatGrid;
import com.axxes.garagebandprototype.view.Highlighter;
import com.axxes.garagebandprototype.view.InstrumentSelection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ToggleButton;
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

    private int highlighterPosition;

    private final MusicXmlParser parser;
    private final MusicXmlWriter writer;

    private Timeline loopTimeline;
    private IntegerProperty bpm;
    private int beats;

    private final AudioDevice audioDevice;

    private final Drumloop drumloop;
    private final Kick kick;
    private final Cymbal cymbal;
    private final HiHat hiHat;
    private final Snare snare;

    private final NoEffect noEffect;
    @Autowired
    private Highlighter highlighter;

    @Autowired
    public Presenter(Drumloop drumloop, MusicXmlParser parser, MusicXmlWriter writer, AudioDevice audioDevice, Kick kick, Cymbal cymbal, HiHat hiHat, Snare snare, NoEffect noEffect) {
        this.parser = parser;
        this.writer = writer;
        this.audioDevice = audioDevice;
        this.drumloop = drumloop;
        this.kick = kick;
        this.cymbal = cymbal;
        this.hiHat = hiHat;
        this.snare = snare;
        this.noEffect = noEffect;
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
                    stepHighlighter();
                    this.drumloop.step();
                }));
        this.loopTimeline.setCycleCount(Animation.INDEFINITE);
    }

    private void stepHighlighter(){
        this.beatGrid.stepHighlight(highlighterPosition);
        this.highlighterPosition++;
        if (this.highlighterPosition == this.beats) {
            this.highlighterPosition = 0;
        }
    }

    private void resetHighlighter() {
        this.highlighterPosition = 0;
        this.highlighter.setSliderValue(0);
        this.beatGrid.stepHighlight(0);
    }
    
    public void changeHighlighterPosition(int position){
        this.highlighterPosition = position;
        int measureCount = position / drumloop.getBeatsPerMeasure();
        int beatCount = position % drumloop.getBeatsPerMeasure();
        drumloop.setCurrentMeasure(measureCount);
        drumloop.getMeasures().get(measureCount).setCurrentBeat(beatCount);
        this.beatGrid.stepHighlight(position);
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
        disableAddInstrumentButton(instrument);
        int rowCount = this.beatGrid.getRowCount();
        this.beatGrid.addLabel(instrument.getClass().getSimpleName(), 0, rowCount);
        this.beatGrid.addInstrumentButtons(instrument);

        this.beatGrid.incrementRowCount();
    }

    public void instrumentAddEffect(Instrument instrument, int measureCount, int beatCount, Effect effect) {
        this.drumloop.getMeasures()
                .get(measureCount)
                .getBeats()
                .get(beatCount)
                .setAudioEffect(instrument, effect);
    }


    public void instrumentToggle(Instrument instrument, int measureCount, int beatCount, ToggleButton button) {
        if (this.drumloop.hasInstrument(instrument, measureCount, beatCount)) {
            button.setStyle("");
            this.drumloop.removeInstrument(instrument, measureCount, beatCount);
        } else {
            this.drumloop.addInstrument(instrument, measureCount, beatCount, noEffect);
            button.setStyle("-fx-background-color: darkgray");
        }
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

        this.resetHighlighter();
        if (file != null) {
            deleteInstrumentLines();
            parser.parserDrumloopFromXml(file);
            createInstrumentLines();
            if (instrumentSelection.isPlayPauseSelected()) {
                playLoop();
            }
        }
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

    public int getHighlighterPosition() {
        return highlighterPosition;
    }
}
