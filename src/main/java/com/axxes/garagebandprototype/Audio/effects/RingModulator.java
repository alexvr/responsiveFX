package com.axxes.garagebandprototype.Audio.effects;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.lwjgl.openal.EXTEfx.*;

@Component
public class RingModulator extends Effect{

    public RingModulator(){}

    @PostConstruct
    private void init(){
        this.effect = audioDevice.createEffect(AL_EFFECT_RING_MODULATOR);
        this.effectSlot = audioDevice.createEffectSlot(this.effect);
        alEffectf(effect, AL_RING_MODULATOR_FREQUENCY, 50);
    }
}
