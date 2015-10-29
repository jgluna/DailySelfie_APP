package com.github.jgluna.dailyselfie.model;

import java.io.Serializable;
import java.util.Set;

public class EffectsRequestWrapper implements Serializable {
    private Selfie selfie;
    private Set<String> effects;

    public Set<String> getEffects() {
        return effects;
    }

    public void setEffects(Set<String> effects) {
        this.effects = effects;
    }

    public Selfie getSelfie() {
        return selfie;
    }

    public void setSelfie(Selfie selfie) {
        this.selfie = selfie;
    }
}
