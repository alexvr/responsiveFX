package com.axxes.garagebandprototype.Audio.effects;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.lwjgl.openal.EXTEfx.*;
import static org.lwjgl.openal.EXTEfx.AL_EFFECT_NULL;

@Component
public class NoEffect extends Effect {

    public NoEffect(){}

    @PostConstruct
    private void init(){
        this.effect = audioDevice.createEffect(AL_EFFECT_NULL);
        this.effectSlot = audioDevice.createEffectSlot(this.effect);
    }
}
