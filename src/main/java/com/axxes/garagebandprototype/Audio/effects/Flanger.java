package com.axxes.garagebandprototype.Audio.effects;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.lwjgl.openal.EXTEfx.*;

@Component
public class Flanger extends Effect {

    public Flanger(){}

    @PostConstruct
    private void init(){
        this.effect = audioDevice.createEffect(AL_EFFECT_FLANGER);
        this.effectSlot = audioDevice.createEffectSlot(this.effect);
        alEffectf(effect, AL_FLANGER_RATE, 5);
    }
}
