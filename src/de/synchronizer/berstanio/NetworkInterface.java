package de.synchronizer.berstanio;

import net.minecraft.server.v1_8_R3.WorldSettings;
import org.bukkit.Bukkit;

public class NetworkInterface {
    public static void getSendetDaten(Object o){
        if (o instanceof MyPlayer) {
            MyPlayer myPlayer = (MyPlayer) o;
            for (SyncronizedPlayer syncronizedPlayer : ((Main) Bukkit.getPluginManager().getPlugin("Synchronizer")).getSyncronizedPlayers()) {
                if (syncronizedPlayer.getGameProfile().getName().equalsIgnoreCase(myPlayer.getName())) {
                    syncronizedPlayer.updatePlayer(myPlayer);
                    return;
                }
            }
            ((Main) Bukkit.getPluginManager().getPlugin("Synchronizer")).getSyncronizedPlayers().add(new SyncronizedPlayer(myPlayer.getName(),
                    myPlayer.getLocation(),
                    myPlayer.getItemStackHashMap(),
                    myPlayer.getLive(),
                    myPlayer.getGamemode()));
        }else {
            Disconnecter disconnecter = (Disconnecter) o;
            for (SyncronizedPlayer syncronizedPlayer : ((Main) Bukkit.getPluginManager().getPlugin("Synchronizer")).getSyncronizedPlayers()){
                if (syncronizedPlayer.getGameProfile().getName().equalsIgnoreCase(disconnecter.getName())){
                    syncronizedPlayer.despawnSyncronizedPlayer(WorldSettings.EnumGamemode.NOT_SET);
                }
            }
        }
    }
}
