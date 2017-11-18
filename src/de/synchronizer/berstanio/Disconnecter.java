package de.synchronizer.berstanio;

import com.jme3.network.serializing.Serializable;

@Serializable
public class Disconnecter {
    private String name;

    public Disconnecter(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
