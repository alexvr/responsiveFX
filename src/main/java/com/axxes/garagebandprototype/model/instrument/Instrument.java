package com.axxes.garagebandprototype.model.instrument;

import com.axxes.garagebandprototype.Audio.AudioDevice;
import com.axxes.garagebandprototype.Audio.effects.Effect;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Instrument {

    protected String soundResource;
    protected String image;
    protected float volume;
    protected int bufferPointer;
    protected int sourcePointer;

    @Autowired
    protected AudioDevice audioDevice;
    protected ExecutorService executor;

    public Instrument(){
        executor = Executors.newSingleThreadExecutor();
    }


    public void play(Effect effect) {
        Logger.getLogger(this.getClass()).info(this.getClass().getSimpleName() + " plays sound.");

        Runnable playTask = () -> this.audioDevice.play(sourcePointer, volume, effect);
        executor.execute(playTask);

    }

    public String getImage() {
        return image;
    }

    public void shutdownExecutor() {
        executor.shutdown();
    }

}
