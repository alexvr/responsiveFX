package com.axxes.garagebandprototype.model.instrument;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class Cymbal extends Instrument {

    public Cymbal(String soundResource, String image) {
        super();
        this.soundResource = soundResource;
        this.image = image;
        this.volume = 0.1f;
    }

    public Cymbal() {
        super();
        this.soundResource = "cymbal.wav";
        this.image = "images/cymbal.png";
        this.volume = 0.1f;
    }

    @PostConstruct
    private void audioInit() {
        this.bufferPointer = this.audioDevice.createBuffer(this.soundResource);
        this.sourcePointer = this.audioDevice.createSource(this.bufferPointer);
    }
}
