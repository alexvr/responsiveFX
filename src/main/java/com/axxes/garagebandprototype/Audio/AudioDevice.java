package com.axxes.garagebandprototype.Audio;

import com.axxes.garagebandprototype.Audio.effects.Effect;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.springframework.stereotype.Component;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.alSource3i;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTEfx.*;

@Component
public class AudioDevice {

    private long device;
    private long context;

    public AudioDevice(){
        init();
    }

    private void init(){
        device = alcOpenDevice(alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER));

        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        alcMakeContextCurrent(context);
        AL.createCapabilities(alcCapabilities);

    }

    public int createBuffer(String soundResource){
        int bufferPointer = alGenBuffers();
        WaveData waveData = WaveData.create(soundResource);
        alBufferData(bufferPointer, waveData.format, waveData.data, waveData.samplerate);
        waveData.dispose();
        return bufferPointer;
    }

    public int createSource(int bufferPointer){
        int sourcePointer = alGenSources();
        alSourcei(sourcePointer, AL_BUFFER, bufferPointer);
        return sourcePointer;
    }

    public int createEffect(int effectType){
       int effect = alGenEffects();
       alEffecti(effect, AL_EFFECT_TYPE, effectType);
       return effect;
    }

    public int createEffectSlot(int effect){
        int effectSlot = alGenAuxiliaryEffectSlots();
        alAuxiliaryEffectSloti(effectSlot, AL_EFFECTSLOT_EFFECT, effect);
        return effectSlot;
    }

    public void play(int sourcePointer, float volume, Effect effect){
        alSource3i(sourcePointer, AL_AUXILIARY_SEND_FILTER, effect.getEffectSlot(), 0, AL_FILTER_NULL);
        alSourcef(sourcePointer, AL_GAIN, volume);

        alSourcePlay(sourcePointer);

    }

    public void destroy() {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}



