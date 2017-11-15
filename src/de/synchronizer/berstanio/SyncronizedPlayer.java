package de.synchronizer.berstanio;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class SyncronizedPlayer {

    private Location location;
    private GameProfile gameProfile;
    private int entityID;
    private float live;
    private HashMap<Integer, ItemStack> itemStackHashMap;

    public SyncronizedPlayer(String name, Location location, HashMap<Integer, ItemStack> itemStackHashMap, float live, WorldSettings.EnumGamemode gamemode) {
        setLocation(location);
        setEntityID(new Random().nextInt(1000) + 3000);
        setGameProfile(new GameProfile(UUID.randomUUID(), name));
        setLive(live);
        setItemStackHashMap(itemStackHashMap);
        spawnSyncronizedPlayer(live,gamemode);
    }
    public void spawnSyncronizedPlayer(float live, WorldSettings.EnumGamemode gamemode){
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();

        setValue(packet, "a", getEntityID());
        setValue(packet, "b", getGameProfile().getId());
        setValue(packet, "c", MathHelper.floor(getLocation().getX() * 32.0D));
        setValue(packet, "d", MathHelper.floor(getLocation().getY() * 32.0D));
        setValue(packet, "e", MathHelper.floor(getLocation().getZ() * 32.0D));
        setValue(packet, "f", (byte) ((int) (getLocation().getYaw() * 256.0F / 360.0F)));
        setValue(packet, "g", (byte) ((int) (getLocation().getPitch() * 256.0F / 360.0F)));
        setValue(packet, "h", Item.getId(getItemStackHashMap().get(0).getItem()));
        DataWatcher w = new DataWatcher(null);
        w.a(6,live);
        w.a(10,(byte)127);
        setValue(packet, "i", w);
        changeSkin();
        addTablist(gamemode);
        sendPacket(packet);
        headRotation(getLocation().getYaw(),getLocation().getPitch());
    }
    

    public void teleport(Location location){
        if (!getLocation().equals(location)){
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
            setValue(packet, "a", entityID);
            setValue(packet, "b", getFixLocation(location.getX()));
            setValue(packet, "c", getFixLocation(location.getY()));
            setValue(packet, "d", getFixLocation(location.getZ()));
            setValue(packet, "e", getFixRotation(location.getYaw()));
            setValue(packet, "f", getFixRotation(location.getPitch()));

            sendPacket(packet);
            headRotation(location.getYaw(), location.getPitch());
            setLocation(location);
        }
    }


    public void updatePlayer(MyPlayer myPlayer){
        if (myPlayer.getAnimation() != 10){
            animation(myPlayer.getAnimation());
            //PacketPlayOutEntity.PacketPlayOutRelEntityMove
        }
        if (myPlayer.getStatus() != 10){
            status(myPlayer.getStatus());
        }
        teleport(myPlayer.getLocation());
        // TODO: 11.11.17 Weiter!
        setLive(myPlayer.getLive());
        for (Integer integer : myPlayer.getItemStackHashMap().keySet()) {
            changeItems(integer, myPlayer.getItemStackHashMap().get(integer));
        }
    }

    public void animation(int animation){
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        setValue(packet, "a", getEntityID());
        setValue(packet, "b", (byte)animation);
        sendPacket(packet);
    }

    public void status(int status){
        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
        setValue(packet, "a", getEntityID());
        setValue(packet, "b", (byte)status);
        sendPacket(packet);
    }

    public void changeItems(int slot, ItemStack itemStack){
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
        setValue(packet, "a", getEntityID());
        setValue(packet, "b", slot);
        setValue(packet, "c", itemStack);
        sendPacket(packet);
    }

    public void changeSkin(){
        // TODO: 08.11.17 UUIDFetcher mit JsonReader
        String value = "eyJ0aW1lc3RhbXAiOjE1MTAxNjc1MTg3MjEsInByb2ZpbGVJZCI6IjdiNTUzZWZkMWRhMjQ1Mjk4ODc1NTAzODIwNjZjZWFiIiwicHJvZmlsZU5hbWUiOiJOeXFsYXpGYW4iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FjNjY1YmY1ZjFhZWQ5NmVhZGJmOTJiOWFhYmJjYmUxNTczZWE5Nzg0Y2FmZTZiODVjYmQyYjRlNDIxM2MifX19";
        String signature = "Eq4qHN/poEu4gjy0Clm/MHASB3W676UD56f9hq3aNe0Bj63Wu3bwc7px+qfQiXpBn9PjDWibQnxyngoG0EJr8/SMjeuFFhtmEyC4nLj/yOQMNkmMGw1Uhmj8cnzC56uEQyJ9N6/gro7/Cjqrn3SWPXyLkzO4pfqxJaNsrUay8OWrCJs6e95QxSJx2RT2ZThDbawi6XNZD7EMYsPXJRCp8UP+Zta2hJWhVYFhL/5u5fglJ43uhWpaRb2e6M+KIISFQkebOpa1ySZi39R/uX95sB11Jm1WIUOgzqGOi6qN/YAJJ31F5Ujy6CansHHXwzRoQrKYDCLIn/F15n3sbdTzw7/7bKnNmhQqjhewQAcnn8eRYlDXQXECQNJ1i28iypHjMZs+gsK827D+kfcHyjhlH7bm0kk1ypi3pVUPIFDmJuUIP27zGiJ/2RWXFyjAqk2p7OufZHMJZYZECBgh251fErbYElmDZPfSPbA+sWOCUwS7kRB/4NhPSvVBQ1PcFwrEv/OUxDtH5txHx2T0jN1Dg1t31UvorU0SqDXOlNRE6dQzNWWIRtsepRg9ogVJc3g3S++0BmOepLBftLlzFAEf+bD7ZH7A1MRgV168ssQau+Ik8bIMZarYk20w65yGMdUonRZdUPGQVTka1c7aZBsN25dk/uKFpr2Xgtmz6eb3v68=";
        getGameProfile().getProperties().put("textures", new Property("textures", value, signature));
    }

    public void headRotation(float yaw,float pitch) {
        PacketPlayOutEntity.PacketPlayOutEntityLook packet = new PacketPlayOutEntity.PacketPlayOutEntityLook(getEntityID(), (byte) ((int) (getLocation().getYaw() * 256.0F / 360.0F)), (byte) ((int) (getLocation().getYaw() * 256.0F / 360.0F)), true);
        PacketPlayOutEntityHeadRotation packetHead = new PacketPlayOutEntityHeadRotation();
        setValue(packetHead, "a", getEntityID());
        setValue(packetHead, "b", (byte) ((int) (getLocation().getYaw() * 256.0F / 360.0F)));

        sendPacket(packet);
        sendPacket(packetHead);
    }

    public void despawnSyncronizedPlayer(WorldSettings.EnumGamemode gamemode){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(getEntityID());
        removeTablist(gamemode);
        sendPacket(packet);
    }

    public void addTablist(WorldSettings.EnumGamemode gamemode) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(getGameProfile(), 1, gamemode, CraftChatMessage.fromString(getGameProfile().getName())[0]);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);

        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        setValue(packet, "b", players);

        sendPacket(packet);
    }

    public void removeTablist(WorldSettings.EnumGamemode gamemode){
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(getGameProfile(), 1, gamemode, CraftChatMessage.fromString(getGameProfile().getName())[0]);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);

        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(packet, "b", players);

        sendPacket(packet);
    }

    public int getFixLocation(double pos){
        return MathHelper.floor(pos * 32.0D);
    }

    public byte getFixRotation(float yawpitch){
        return (byte) ((int) (yawpitch * 256.0F / 360.0F));
    }

    public void setValue(Object obj, String name, Object value){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }catch(Exception e){}
    }

    public Object getValue(Object obj,String name){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception e){}
        return null;
    }

    public void sendPacket(Packet packet, Player player){
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendPacket(Packet packet){
        for(Player player : Bukkit.getOnlinePlayers()){
            sendPacket(packet,player);
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public float getLive() {
        return live;
    }

    public void setLive(float live) {
        this.live = live;
    }

    public HashMap<Integer, ItemStack> getItemStackHashMap() {
        return itemStackHashMap;
    }

    public void setItemStackHashMap(HashMap<Integer, ItemStack> itemStackHashMap) {
        this.itemStackHashMap = itemStackHashMap;
    }
}
