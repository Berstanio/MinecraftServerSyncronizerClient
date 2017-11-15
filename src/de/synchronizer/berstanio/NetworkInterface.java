package de.synchronizer.berstanio;

import org.bukkit.Bukkit;

public class NetworkInterface {
    public static void getSendetDaten(Object o){
        MyPlayer myPlayer = (MyPlayer) o;
        for (SyncronizedPlayer syncronizedPlayer : ((Main) Bukkit.getPluginManager().getPlugin("Synchronizer")).getSyncronizedPlayers()) {
            if (syncronizedPlayer.getGameProfile().getName().equalsIgnoreCase(myPlayer.getName())){
                syncronizedPlayer.updatePlayer(myPlayer);
                return;
            }
        }
        ((Main) Bukkit.getPluginManager().getPlugin("Synchronizer")).getSyncronizedPlayers().add(new SyncronizedPlayer(myPlayer.getName(),
                myPlayer.getLocation(),
                myPlayer.getItemStackHashMap(),
                myPlayer.getLive(),
                myPlayer.getGamemode()));
    }
}
