package com.axxes.garagebandprototype.Audio.effects;

import com.axxes.garagebandprototype.Audio.AudioDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Effect {

    @Autowired
    protected AudioDevice audioDevice;

    protected int effectType;
    protected int effect;
    protected int effectSlot;


    public int getEffectSlot() {
        return effectSlot;
    }

    public int getEffectType() {
        return effectType;
    }
}
