package com.axxes.garagebandprototype.model.measures;

import com.axxes.garagebandprototype.Audio.effects.Effect;
import com.axxes.garagebandprototype.model.instrument.Instrument;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Measure {

    private int beatsPerMeasure;
    private List<Beat> beats = new ArrayList<>();
    private int currentBeat;

    public Measure() {
        this.beatsPerMeasure = 4;
        this.currentBeat = 0;

        for (int i = 0; i < beatsPerMeasure; i++) {
            this.beats.add(new Beat());
        }
    }

    public void addBeatOnLocation(Beat beat, int location) {
        this.beats.set(location, beat);
    }

    public void playMeasure() {
        Logger.getLogger(Measure.class).info("Playing beat: " + currentBeat + ".");
        this.beats.get(currentBeat).playBeat();

        if (this.currentBeat == (beatsPerMeasure - 1)) {
            this.currentBeat = 0;
        } else {
            this.currentBeat++;
        }
    }

    public boolean isEndOfMeasure(){
        return currentBeat == (beatsPerMeasure - 1);
    }

    public List<Beat> getBeats() {
        return beats;
    }

    public void setBeats(List<Beat> beats) {
        this.beats = beats;
    }

    public void addInstrument(Instrument instrument, int beatCount, Effect effect) {
        beats.get(beatCount).addInstrument(instrument, effect);
    }

    public void removeInstrument(Instrument instrument, int beatCount) {
        beats.get(beatCount).removeInstrument(instrument);

    }

    public boolean hasInstrument(Instrument instrument, int beatCount) {
        return beats.get(beatCount).getInstruments().contains(instrument);
    }

    public int getCurrentBeat() {
        return currentBeat;
    }

    public int getBeatsPerMeasure() {
        return beatsPerMeasure;
    }

    public void setCurrentBeat(int currentBeat) {
        this.currentBeat = currentBeat;
    }

}
