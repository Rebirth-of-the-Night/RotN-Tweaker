package com.rebirthofthenight.rotntweaker.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class KeyPressMessageHandler implements IMessageHandler<KeyPressMessage, IMessage> {

    @Override
    public IMessage onMessage(KeyPressMessage message, MessageContext ctx) {
        UUID playerUUID = message.getPlayerUUID();

        MinecraftServer server = ctx.getServerHandler().player.mcServer;
        EntityPlayer player = null;

        for (WorldServer world : server.worlds) {
            player = world.getPlayerEntityByUUID(playerUUID);
            if (player != null) break;
        }

        if (player == null) {
            System.out.println("Player not found with UUID: " + playerUUID);
            return null;
        }

        int keyCode = message.getKeyCode();

        //System.out.println("Received key press packet with key code: " + keyCode + " from player: " + player.getName());
        player.getEntityData().setInteger("webbed_struggle",player.getEntityData().getInteger("webbed_struggle")+1);
        Minecraft.getMinecraft().player.getEntityData().setInteger("webbed_struggle", Minecraft.getMinecraft().player.getEntityData().getInteger("webbed_struggle")+1);


        return null;
    }
}