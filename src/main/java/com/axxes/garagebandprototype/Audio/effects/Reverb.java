package com.axxes.garagebandprototype.Audio.effects;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.lwjgl.openal.EXTEfx.*;

@Component
public class Reverb extends Effect {

    public Reverb(){}

    @PostConstruct
    private void init(){
        this.effectType = AL_EFFECT_REVERB;
        this.effect = audioDevice.createEffect(effectType);
        this.effectSlot = audioDevice.createEffectSlot(this.effect);
        alEffectf(this.effect, AL_REVERB_DENSITY, 0.8f);
        alEffectf(this.effect, AL_REVERB_DIFFUSION, 0.2f);
        alEffectf(this.effect, AL_REVERB_GAIN, 0.95f);
    }
}
