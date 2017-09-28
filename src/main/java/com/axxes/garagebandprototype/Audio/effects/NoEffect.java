package com.axxes.garagebandprototype.Audio.effects;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.lwjgl.openal.EXTEfx.AL_EFFECT_NULL;

@Component
public class NoEffect extends Effect {

    public NoEffect(){}

    @PostConstruct
    private void init(){
        this.effectType = AL_EFFECT_NULL;
        this.effect = audioDevice.createEffect(effectType);
        this.effectSlot = audioDevice.createEffectSlot(this.effect);
    }
}
