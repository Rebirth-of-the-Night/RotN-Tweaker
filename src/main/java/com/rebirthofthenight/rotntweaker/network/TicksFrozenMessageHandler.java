package com.rebirthofthenight.rotntweaker.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TicksFrozenMessageHandler implements IMessageHandler<TicksFrozenMessage, IMessage> {
    @Override
    public IMessage onMessage(TicksFrozenMessage message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(() -> handleClient(message));
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void handleClient(TicksFrozenMessage message) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            player.getEntityData().setInteger("ticksFrozen", message.getTicksFrozen());
        }
    }
}