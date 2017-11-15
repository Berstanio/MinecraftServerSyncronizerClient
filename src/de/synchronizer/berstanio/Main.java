package de.synchronizer.berstanio;

import de.berstanio.com.core.Creator;
import de.berstanio.com.handling.Utils;
import net.minecraft.server.v1_8_R3.WorldSettings;
import org.apache.commons.codec.digest.Md5Crypt;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener{

    private ArrayList<SyncronizedPlayer> syncronizedPlayers = new ArrayList<>();
    @Override
    public void onEnable() {
        System.out.println("Aktiviert!");

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        new PacketCatcher(event.getPlayer()).inject();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: 07.11.17 TESTEN!!! 
        if (command.getName().equalsIgnoreCase("start")) {
            if (sender.hasPermission("synchronizer.start")) {
                if (args.length == 5){
                    ArrayList<Class> arrayList = new ArrayList<>();
                    arrayList.add(MyPlayer.class);
                    arrayList.add(Identificator.class);
                    Creator.create(args[0], Integer.parseInt(args[1]), false, "de.synchronizer.berstanio", arrayList);
                    Identificator identificator = new Identificator();
                    identificator.setUserName(Md5Crypt.md5Crypt(args[2].getBytes()));
                    identificator.setPassWord(Md5Crypt.md5Crypt(args[3].getBytes()));
                    identificator.setPassWord(args[4]);
                    Utils.sendeMessageAnServer(identificator);
                }else {
                    sender.sendMessage(ChatColor.RED + "Bitte geben sie die IP vom Server, einen Port, den Username, das Passwort und einen Oberbegriff, mit dem man diesen Server zu einer Gruppe zuorden kann, mit der er Synchronisiert wird an!");
                }
            }else {
                sender.sendMessage(ChatColor.RED + "Dieser Command existiert nicht!");
            }
        }
        return true;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event){//2 Shchaden 3 tot
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


    public ArrayList<SyncronizedPlayer> getSyncronizedPlayers() {
        return syncronizedPlayers;
    }

    public void setSyncronizedPlayers(ArrayList<SyncronizedPlayer> syncronizedPlayers) {
        this.syncronizedPlayers = syncronizedPlayers;
    }
}
