package com.axxes.garagebandprototype.model.instrument;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Snare extends Instrument {

    public Snare(String soundResource, String image){
        super();
        this.soundResource = soundResource;
        this.image = image;
        this.volume = 1;
    }

    public Snare(){
        super();
        this.soundResource = "snare.wav";
        image = "/images/snare.png";
        this.volume = 1;
    }

    @PostConstruct
    private void audioInit() {
        this.bufferPointer = this.audioDevice.createBuffer(this.soundResource);
        this.sourcePointer = this.audioDevice.createSource(this.bufferPointer);
    }
}
