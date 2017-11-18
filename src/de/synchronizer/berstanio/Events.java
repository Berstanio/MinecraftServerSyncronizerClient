package de.synchronizer.berstanio;

import de.berstanio.com.handling.Utils;
import net.minecraft.server.v1_8_R3.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        PacketCatcher packetCatcher = new PacketCatcher(event.getPlayer());
        packetCatcher.inject();
        ((Main) Bukkit.getPluginManager().getPlugin("Synchronizer")).getPacketCatcherHashMap().put(event.getPlayer(), packetCatcher);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        ((Main) Bukkit.getPluginManager().getPlugin("Synchronizer")).getPacketCatcherHashMap().get(event.getPlayer()).uninject();
        Utils.sendeMessageAnServer(new Disconnecter(event.getPlayer().getName()));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){//2 Shchaden 3 tot
        // TODO: 18.11.17 Hier halt mit Itemstack berechnen oder so
        Player player = event.getPlayer();
        Utils.sendeMessageAnServer(new MyPlayer(player.getName(), player.getLocation(),null, (float) player.getHealth(), WorldSettings.EnumGamemode.NOT_SET, 10, 10));
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event){//2 Shchaden 3 tot
        assert event.getEntity() instanceof Player;
        Player player = (Player) event.getEntity();
        Utils.sendeMessageAnServer(new MyPlayer(player.getName(), player.getLocation(),null, (float) player.getHealth(), WorldSettings.EnumGamemode.NOT_SET, 1, 2));
    }

    @EventHandler
    public void onAnimation(PlayerAnimationEvent event){//2 Shchaden 3 tot
        Player player = event.getPlayer();
        Utils.sendeMessageAnServer(new MyPlayer(player.getName(), player.getLocation(),null, (float) player.getHealth(), WorldSettings.EnumGamemode.NOT_SET, 0, 10));
    }
}
