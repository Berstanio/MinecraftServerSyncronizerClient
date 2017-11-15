package de.synchronizer.berstanio;

import de.berstanio.com.core.Creator;
import de.berstanio.com.handling.Utils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
        new PacketCacher(event.getPlayer()).inject();
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
                    // TODO: 15.11.17 Eigene Daten senden
                }else {
                    sender.sendMessage(ChatColor.RED + "Bitte geben sie die IP vom Server, einen Port, den Username, das Passwort und einen Oberbegriff, mit dem man diesen Server zu einer Gruppe zuorden kann, mit der er Synchronisiert wird an!");
                }
            }else {
                sender.sendMessage(ChatColor.RED + "Dieser Command existiert nicht!");
            }
        }
        return true;
    }

    public ArrayList<SyncronizedPlayer> getSyncronizedPlayers() {
        return syncronizedPlayers;
    }

    public void setSyncronizedPlayers(ArrayList<SyncronizedPlayer> syncronizedPlayers) {
        this.syncronizedPlayers = syncronizedPlayers;
    }
}
