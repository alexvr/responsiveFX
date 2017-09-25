package com.axxes.garagebandprototype.model.loop;

import com.axxes.garagebandprototype.Audio.effects.NoEffect;
import com.axxes.garagebandprototype.model.instrument.Instrument;
import com.axxes.garagebandprototype.model.measures.Beat;
import com.axxes.garagebandprototype.model.measures.Measure;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Drumloop implements Loop {

    private List<Measure> measures;
    private int currentMeasure;
    private int numberOfMeasures;
    private IntegerProperty bpm;
    @Autowired
    private NoEffect noEffect;

    public Drumloop() {
        this.measures = new ArrayList<>();
        this.numberOfMeasures = 2;
        this.currentMeasure = 0;
        this.bpm = new SimpleIntegerProperty(120);
        for (int i = 0; i < numberOfMeasures; i++) {
            this.measures.add(new Measure());
        }
    }

    public void step() {
        Logger.getLogger(Drumloop.class).info("Step into measure: " + currentMeasure + ".");

        Measure measure = this.measures.get(currentMeasure);

        if (measure.isEndOfMeasure()) {
            if (currentMeasure == (numberOfMeasures - 1)) {
                currentMeasure = 0;
            } else {
                currentMeasure++;
            }
        }
        measure.playMeasure();
    }

    public List<Measure> getMeasures() {
        return this.measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public void addInstrument(Instrument instrument, int measureCount, int beatCount) {
        measures.get(measureCount).addInstrument(instrument, beatCount, noEffect);
    }

    public void removeInstrument(Instrument instrument, int measureCount, int beatCount) {
        measures.get(measureCount).removeInstrument(instrument, beatCount);

    }

    public boolean hasInstrument(Instrument instrument, int measureCount, int beatCount) {
        return measures.get(measureCount).hasInstrument(instrument, beatCount);
    }

    public int getCurrentMeasure() {
        return currentMeasure;
    }

    public int getCurrentBeat() {
        return measures.get(currentMeasure).getCurrentBeat();
    }

    public void setCurrentMeasure(int currentMeasure) {
        this.currentMeasure = currentMeasure;
    }

    public IntegerProperty getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm.set(bpm);
    }

    public int getBeatsPerMeasure() {
        return measures.get(0).getBeatsPerMeasure();
    }

    public Set<Instrument> getInstrumentSet() {
        Set<Instrument> instrumentSet = new HashSet<>();
        for (Measure measure : measures) {
            measure.getBeats()
                    .stream()
                    .map(Beat::getInstruments)
                    .forEach(instrumentSet::addAll);
        }
        return instrumentSet;
    }

}
