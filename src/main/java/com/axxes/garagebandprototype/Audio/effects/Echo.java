package com.axxes.garagebandprototype.Audio.effects;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.lwjgl.openal.EXTEfx.*;

@Component
public class Echo extends Effect{

    public Echo(){}

    @PostConstruct
    private void init(){
        this.effect = audioDevice.createEffect(AL_EFFECT_ECHO);
        this.effectSlot = audioDevice.createEffectSlot(this.effect);
        alEffectf(effect, AL_ECHO_DELAY, 0.01f);
        alEffectf(effect, AL_ECHO_LRDELAY, 0.01f);
        alEffectf(effect, AL_ECHO_FEEDBACK, 0.1f);
        alEffectf(effect, AL_ECHO_DAMPING, 0.9f);
        alEffectf(effect, AL_ECHO_SPREAD, -0.5f);
    }
}
