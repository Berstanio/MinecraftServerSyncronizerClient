package de.synchronizer.berstanio;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.WorldSettings;
import org.bukkit.Location;

import java.util.HashMap;

@Serializable
public class MyPlayer extends AbstractMessage {
    private String name;
    private Location location;
    private HashMap<Integer, ItemStack> itemStackHashMap;
    private float live;
    private WorldSettings.EnumGamemode gamemode;
    private int animation;
    private int status;

    public MyPlayer(String name, Location location, HashMap<Integer, ItemStack> itemStackHashMap, float live, WorldSettings.EnumGamemode gamemode, int animation, int status) {
        setName(name);
        setLocation(location);
        setItemStackHashMap(itemStackHashMap);
        setLive(live);
        setGamemode(gamemode);
        setAnimation(animation);
        setStatus(status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }



    public float getLive() {
        return live;
    }

    public void setLive(float live) {
        this.live = live;
    }

    public WorldSettings.EnumGamemode getGamemode() {
        return gamemode;
    }

    public void setGamemode(WorldSettings.EnumGamemode gamemode) {
        this.gamemode = gamemode;
    }

    public int getAnimation() {
        return animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<Integer, ItemStack> getItemStackHashMap() {
        return itemStackHashMap;
    }

    public void setItemStackHashMap(HashMap<Integer, ItemStack> itemStackHashMap) {
        this.itemStackHashMap = itemStackHashMap;
    }
}
