package de.synchronizer.berstanio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class PacketCacher {

    private Player player;
    private Channel channel;

    public PacketCacher(Player player) {
        setPlayer(player);
    }

    public void inject() {
        CraftPlayer player = (CraftPlayer) getPlayer();
        setChannel(player.getHandle().playerConnection.networkManager.channel);
        getChannel().pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet>() {
                    @Override
                    protected void decode(ChannelHandlerContext channelHandlerContext, Packet packet, List<Object> packetList) throws Exception {
                        packetList.add(packet);
                        readPackets(packet);
                    }
                });
    }

    public void uninject() {
        if (getChannel().pipeline().get("PacketInjector") != null) {
            getChannel().pipeline().remove("PacketInjector");
        }
    }

    private void readPackets(Packet packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            int entityID = (int) getValue(packet, "a");
            for (SyncronizedPlayer syncronizedPlayer : ((Main)Bukkit.getPluginManager().getPlugin("Synchronizer")).getSyncronizedPlayers()) {
                if (syncronizedPlayer.getEntityID() == entityID){
                    if (getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")){
                        if (getPlayer().getItemInHand().getType() != org.bukkit.Material.AIR){
                            // TODO: 11.11.17 Schaden berechnen!
                        }else {
                            syncronizedPlayer.setLive(syncronizedPlayer.getLive() - (float) 0.5);
                        }

                        syncronizedPlayer.animation(1);
                        syncronizedPlayer.status(2);
                        if (syncronizedPlayer.getLive() <= 0){
                            syncronizedPlayer.status(3);
                            syncronizedPlayer.despawnSyncronizedPlayer(WorldSettings.EnumGamemode.NOT_SET);
                        }
                    }
                    break;
                }
            }
        }
    }


    public void komischerSwitch(Packet packet){
        String name = packet.getClass().getSimpleName();
        switch (name){
            /*case "PacketPlayOutAbilities":{
                PacketPlayOutAbilities castedPacket = (PacketPlayOutAbilities) packet;
                break;
            }
            case "PacketPlayOutAnimation":{
                PacketPlayOutAnimation castedPacket = (PacketPlayOutAnimation) packet;
                break;
            }
            case "PacketPlayOutAttachEntity":{
                PacketPlayOutAttachEntity castedPacket = (PacketPlayOutAttachEntity) packet;
                break;
            }
            case "PacketPlayOutBed":{
                PacketPlayOutBed castedPacket = (PacketPlayOutBed) packet;
                break;
            }
            case "PacketPlayOutBlockAction":{
                PacketPlayOutBlockAction castedPacket = (PacketPlayOutBlockAction) packet;
                break;
            }
            case "PacketPlayOutBlockBreakAnimation":{
                PacketPlayOutBlockBreakAnimation castedPacket = (PacketPlayOutBlockBreakAnimation) packet;
                break;
            }
            case "PacketPlayOutBlockChange":{
                PacketPlayOutBlockChange castedPacket = (PacketPlayOutBlockChange) packet;
                break;
            }
            case "PacketPlayOutCamera":{
                PacketPlayOutCamera castedPacket = (PacketPlayOutCamera) packet;
                break;
            }
            case "PacketPlayOutChat":{
                PacketPlayOutChat castedPacket = (PacketPlayOutChat) packet;
                break;
            }
            case "PacketPlayOutCloseWindow":{
                PacketPlayOutCloseWindow castedPacket = (PacketPlayOutCloseWindow) packet;
                break;
            }
            case "PacketPlayOutCollect":{
                PacketPlayOutCollect castedPacket = (PacketPlayOutCollect) packet;
                break;
            }
            case "PacketPlayOutCombatEvent":{
                PacketPlayOutCombatEvent castedPacket = (PacketPlayOutCombatEvent) packet;
                break;
            }
            case "PacketPlayOutCustomPayload":{
                PacketPlayOutCustomPayload castedPacket = (PacketPlayOutCustomPayload) packet;
                break;
            }
            case "PacketPlayOutEntity":{
                PacketPlayOutEntity castedPacket = (PacketPlayOutEntity) packet;
                break;
            }
            case "PacketPlayOutEntityDestroy":{
                PacketPlayOutEntityDestroy castedPacket = (PacketPlayOutEntityDestroy) packet;
                break;
            }
            case "PacketPlayOutEntityEffect":{
                PacketPlayOutEntityEffect castedPacket = (PacketPlayOutEntityEffect) packet;
                break;
            }
            case "PacketPlayOutEntityEquipment":{
                PacketPlayOutEntityEquipment castedPacket = (PacketPlayOutEntityEquipment) packet;
                break;
            }
            case "PacketPlayOutEntityHeadRotation":{
                PacketPlayOutEntityHeadRotation castedPacket = (PacketPlayOutEntityHeadRotation) packet;
                break;
            }
            case "PacketPlayOutEntityMetadata":{
                PacketPlayOutEntityMetadata castedPacket = (PacketPlayOutEntityMetadata) packet;
                break;
            }
            case "PacketPlayOutEntityStatus":{
                PacketPlayOutEntityStatus castedPacket = (PacketPlayOutEntityStatus) packet;
                break;
            }
            case "PacketPlayOutEntityTeleport":{
                PacketPlayOutEntityTeleport castedPacket = (PacketPlayOutEntityTeleport) packet;
                break;
            }
            case "PacketPlayOutEntityVelocity":{
                PacketPlayOutEntityVelocity castedPacket = (PacketPlayOutEntityVelocity) packet;
                break;
            }
            case "PacketPlayOutExperience":{
                PacketPlayOutExperience castedPacket = (PacketPlayOutExperience) packet;
                break;
            }
            case "PacketPlayOutExplosion":{
                PacketPlayOutExplosion castedPacket = (PacketPlayOutExplosion) packet;
                break;
            }
            case "PacketPlayOutGameStateChange":{
                PacketPlayOutGameStateChange castedPacket = (PacketPlayOutGameStateChange) packet;
                break;
            }
            case "PacketPlayOutHeldItemSlot":{
                PacketPlayOutHeldItemSlot castedPacket = (PacketPlayOutHeldItemSlot) packet;
                break;
            }
            case "PacketPlayOutKeepAlive":{
                PacketPlayOutKeepAlive castedPacket = (PacketPlayOutKeepAlive) packet;
                break;
            }
            case "PacketPlayOutKickDisconnect":{
                PacketPlayOutKickDisconnect castedPacket = (PacketPlayOutKickDisconnect) packet;
                break;
            }
            case "PacketPlayOutLogin":{
                PacketPlayOutLogin castedPacket = (PacketPlayOutLogin) packet;
                break;
            }
            case "PacketPlayOutMapChunkBulk":{
                PacketPlayOutMapChunkBulk castedPacket = (PacketPlayOutMapChunkBulk) packet;
                break;
            }
            case "PacketPlayOutMapChunk":{
                PacketPlayOutMapChunk castedPacket = (PacketPlayOutMapChunk) packet;
                break;
            }
            case "PacketPlayOutMap":{
                PacketPlayOutMap castedPacket = (PacketPlayOutMap) packet;
                break;
            }
            case "PacketPlayOutMultiBlockChange":{
                PacketPlayOutMultiBlockChange castedPacket = (PacketPlayOutMultiBlockChange) packet;
                break;
            }
            case "PacketPlayOutNamedEntitySpawn":{
                PacketPlayOutNamedEntitySpawn castedPacket = (PacketPlayOutNamedEntitySpawn) packet;
                break;
            }
            case "PacketPlayOutNamedSoundEffect":{
                PacketPlayOutNamedSoundEffect castedPacket = (PacketPlayOutNamedSoundEffect) packet;
                break;
            }
            case "PacketPlayOutOpenSignEditor":{
                PacketPlayOutOpenSignEditor castedPacket = (PacketPlayOutOpenSignEditor) packet;
                break;
            }
            case "PacketPlayOutOpenWindow":{
                PacketPlayOutOpenWindow castedPacket = (PacketPlayOutOpenWindow) packet;
                break;
            }
            case "PacketPlayOutPlayerInfo":{
                PacketPlayOutPlayerInfo castedPacket = (PacketPlayOutPlayerInfo) packet;
                break;
            }
            case "PacketPlayOutPlayerListHeaderFooter":{
                PacketPlayOutPlayerListHeaderFooter castedPacket = (PacketPlayOutPlayerListHeaderFooter) packet;
                break;
            }
            case "PacketPlayOutPosition":{
                PacketPlayOutPosition castedPacket = (PacketPlayOutPosition) packet;
                break;
            }
            case "PacketPlayOutRemoveEntityEffect":{
                PacketPlayOutRemoveEntityEffect castedPacket = (PacketPlayOutRemoveEntityEffect) packet;
                break;
            }
            case "PacketPlayOutResourcePackSend":{
                PacketPlayOutResourcePackSend castedPacket = (PacketPlayOutResourcePackSend) packet;
                break;
            }
            case "PacketPlayOutRespawn":{
                PacketPlayOutRespawn castedPacket = (PacketPlayOutRespawn) packet;
                break;
            }
            case "PacketPlayOutScoreboardDisplayObjective":{
                PacketPlayOutScoreboardDisplayObjective castedPacket = (PacketPlayOutScoreboardDisplayObjective) packet;
                break;
            }
            case "PacketPlayOutScoreboardObjective":{
                PacketPlayOutScoreboardObjective castedPacket = (PacketPlayOutScoreboardObjective) packet;
                break;
            }
            case "PacketPlayOutScoreboardScore":{
                PacketPlayOutScoreboardScore castedPacket = (PacketPlayOutScoreboardScore) packet;
                break;
            }
            case "PacketPlayOutScoreboardTeam":{
                PacketPlayOutScoreboardTeam castedPacket = (PacketPlayOutScoreboardTeam) packet;
                break;
            }
            case "PacketPlayOutServerDifficulty":{
                PacketPlayOutServerDifficulty castedPacket = (PacketPlayOutServerDifficulty) packet;
                break;
            }
            case "PacketPlayOutSetCompression":{
                PacketPlayOutSetCompression castedPacket = (PacketPlayOutSetCompression) packet;
                break;
            }
            case "PacketPlayOutSetSlot":{
                PacketPlayOutSetSlot castedPacket = (PacketPlayOutSetSlot) packet;
                break;
            }
            case "PacketPlayOutSpawnEntity":{
                PacketPlayOutSpawnEntity castedPacket = (PacketPlayOutSpawnEntity) packet;
                break;
            }
            case "PacketPlayOutSpawnEntityExperienceOrb":{
                PacketPlayOutSpawnEntityExperienceOrb castedPacket = (PacketPlayOutSpawnEntityExperienceOrb) packet;
                break;
            }
            case "PacketPlayOutSpawnEntityLiving":{
                PacketPlayOutSpawnEntityLiving castedPacket = (PacketPlayOutSpawnEntityLiving) packet;
                break;
            }
            case "PacketPlayOutSpawnEntityPainting":{
                PacketPlayOutSpawnEntityPainting castedPacket = (PacketPlayOutSpawnEntityPainting) packet;
                break;
            }
            case "PacketPlayOutSpawnEntityWeather":{
                PacketPlayOutSpawnEntityWeather castedPacket = (PacketPlayOutSpawnEntityWeather) packet;
                break;
            }
            case "PacketPlayOutSpawnPosition":{
                PacketPlayOutSpawnPosition castedPacket = (PacketPlayOutSpawnPosition) packet;
                break;
            }
            case "PacketPlayOutStatistic":{
                PacketPlayOutStatistic castedPacket = (PacketPlayOutStatistic) packet;
                break;
            }
            case "PacketPlayOutTabComplete":{
                PacketPlayOutTabComplete castedPacket = (PacketPlayOutTabComplete) packet;
                break;
            }
            case "PacketPlayOutTileEntityData":{
                PacketPlayOutTileEntityData castedPacket = (PacketPlayOutTileEntityData) packet;
                break;
            }
            case "PacketPlayOutTitle":{
                PacketPlayOutTitle castedPacket = (PacketPlayOutTitle) packet;
                break;
            }
            case "PacketPlayOutTransaction":{
                PacketPlayOutTransaction castedPacket = (PacketPlayOutTransaction) packet;
                break;
            }
            case "PacketPlayOutUpdateAttributes":{
                PacketPlayOutUpdateAttributes castedPacket = (PacketPlayOutUpdateAttributes) packet;
                break;
            }
            case "PacketPlayOutUpdateEntityNBT":{
                PacketPlayOutUpdateEntityNBT castedPacket = (PacketPlayOutUpdateEntityNBT) packet;
                break;
            }
            case "PacketPlayOutUpdateHealth":{
                PacketPlayOutUpdateHealth castedPacket = (PacketPlayOutUpdateHealth) packet;
                break;
            }
            case "PacketPlayOutUpdateSign":{
                PacketPlayOutUpdateSign castedPacket = (PacketPlayOutUpdateSign) packet;
                break;
            }
            case "PacketPlayOutUpdateTime":{
                PacketPlayOutUpdateTime castedPacket = (PacketPlayOutUpdateTime) packet;
                break;
            }
            case "PacketPlayOutWindowData":{
                PacketPlayOutWindowData castedPacket = (PacketPlayOutWindowData) packet;
                break;
            }
            case "PacketPlayOutWindowItems":{
                PacketPlayOutWindowItems castedPacket = (PacketPlayOutWindowItems) packet;
                break;
            }
            case "PacketPlayOutWorldBorder":{
                PacketPlayOutWorldBorder castedPacket = (PacketPlayOutWorldBorder) packet;
                break;
            }
            case "PacketPlayOutWorldEvent":{
                PacketPlayOutWorldEvent castedPacket = (PacketPlayOutWorldEvent) packet;
                break;
            }
            case "PacketPlayOutWorldParticles":{
                PacketPlayOutWorldParticles castedPacket = (PacketPlayOutWorldParticles) packet;
                break;
            }*/
            case "PacketPlayInAbilities":{
                PacketPlayInAbilities castedPacket = (PacketPlayInAbilities) packet;
                break;
            }
            case "PacketPlayInArmAnimation":{
                PacketPlayInArmAnimation castedPacket = (PacketPlayInArmAnimation) packet;
                break;
            }
            case "PacketPlayInBlockDig":{
                PacketPlayInBlockDig castedPacket = (PacketPlayInBlockDig) packet;
                break;
            }
            case "PacketPlayInBlockPlace":{
                PacketPlayInBlockPlace castedPacket = (PacketPlayInBlockPlace) packet;
                break;
            }
            case "PacketPlayInChat":{
                PacketPlayInChat castedPacket = (PacketPlayInChat) packet;
                break;
            }
            case "PacketPlayInClientCommand":{
                PacketPlayInClientCommand castedPacket = (PacketPlayInClientCommand) packet;
                break;
            }
            case "PacketPlayInCloseWindow":{
                PacketPlayInCloseWindow castedPacket = (PacketPlayInCloseWindow) packet;
                break;
            }
            case "PacketPlayInCustomPayload":{
                PacketPlayInCustomPayload castedPacket = (PacketPlayInCustomPayload) packet;
                break;
            }
            case "PacketPlayInEnchantItem":{
                PacketPlayInEnchantItem castedPacket = (PacketPlayInEnchantItem) packet;
                break;
            }
            case "PacketPlayInEntityAction":{
                PacketPlayInEntityAction castedPacket = (PacketPlayInEntityAction) packet;
                break;
            }
            case "PacketPlayInFlying":{
                PacketPlayInFlying castedPacket = (PacketPlayInFlying) packet;
                break;
            }
            case "PacketPlayInHeldItemSlot":{
                PacketPlayInHeldItemSlot castedPacket = (PacketPlayInHeldItemSlot) packet;
                break;
            }
            case "PacketPlayInKeepAlive":{
                PacketPlayInKeepAlive castedPacket = (PacketPlayInKeepAlive) packet;
                break;
            }
            case "PacketPlayInResourcePackStatus":{
                PacketPlayInResourcePackStatus castedPacket = (PacketPlayInResourcePackStatus) packet;
                break;
            }
            case "PacketPlayInSetCreativeSlot":{
                PacketPlayInSetCreativeSlot castedPacket = (PacketPlayInSetCreativeSlot) packet;
                break;
            }
            case "PacketPlayInSettings":{
                PacketPlayInSettings castedPacket = (PacketPlayInSettings) packet;
                break;
            }
            case "PacketPlayInSpectate":{
                PacketPlayInSpectate castedPacket = (PacketPlayInSpectate) packet;
                break;
            }
            case "PacketPlayInSteerVehicle":{
                PacketPlayInSteerVehicle castedPacket = (PacketPlayInSteerVehicle) packet;
                break;
            }
            case "PacketPlayInTabComplete":{
                PacketPlayInTabComplete castedPacket = (PacketPlayInTabComplete) packet;
                break;
            }
            case "PacketPlayInTransaction":{
                PacketPlayInTransaction castedPacket = (PacketPlayInTransaction) packet;
                break;
            }
            case "PacketPlayInUpdateSign":{
                PacketPlayInUpdateSign castedPacket = (PacketPlayInUpdateSign) packet;
                break;
            }
            case "PacketPlayInUseEntity":{
                PacketPlayInUseEntity castedPacket = (PacketPlayInUseEntity) packet;
                break;
            }
            case "PacketPlayInWindowClick":{
                PacketPlayInWindowClick castedPacket = (PacketPlayInWindowClick) packet;
                break;
            }
            default: {
                break;
            }
        }
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}