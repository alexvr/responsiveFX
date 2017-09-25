package com.axxes.garagebandprototype.Audio.effects;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.lwjgl.openal.EXTEfx.*;

@Component
public class Distortion extends Effect {

    public Distortion(){}

    @PostConstruct
    private void init(){
        this.effect = audioDevice.createEffect(AL_EFFECT_DISTORTION);
        this.effectSlot = audioDevice.createEffectSlot(this.effect);
        alEffectf(effect, AL_DISTORTION_EDGE, 0.6f);

    }
}
