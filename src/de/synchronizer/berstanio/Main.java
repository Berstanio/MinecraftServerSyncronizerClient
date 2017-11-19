package de.synchronizer.berstanio;

import de.berstanio.com.core.Creator;
import de.berstanio.com.handling.Utils;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.apache.commons.codec.digest.Md5Crypt;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    private ArrayList<SyncronizedPlayer> syncronizedPlayers = new ArrayList<>();
    private HashMap<Player, PacketCatcher> packetCatcherHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        System.out.println("Aktiviert!");
        getServer().getPluginManager().registerEvents(new Events(), this);
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
                    arrayList.add(Disconnecter.class);
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

    public static HashMap<Integer, ItemStack> convertPlayerToItemStackHashMap(Player player){
        HashMap<Integer, ItemStack> itemStackHashMap = new HashMap<>();
        itemStackHashMap.put(0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        itemStackHashMap.put(1, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        itemStackHashMap.put(2, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        itemStackHashMap.put(3, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        itemStackHashMap.put(4, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
        return itemStackHashMap;
    }

    public ArrayList<SyncronizedPlayer> getSyncronizedPlayers() {
        return syncronizedPlayers;
    }

    public void setSyncronizedPlayers(ArrayList<SyncronizedPlayer> syncronizedPlayers) {
        this.syncronizedPlayers = syncronizedPlayers;
    }

    public HashMap<Player, PacketCatcher> getPacketCatcherHashMap() {
        return packetCatcherHashMap;
    }

    public void setPacketCatcherHashMap(HashMap<Player, PacketCatcher> packetCatcherHashMap) {
        this.packetCatcherHashMap = packetCatcherHashMap;
    }
}
