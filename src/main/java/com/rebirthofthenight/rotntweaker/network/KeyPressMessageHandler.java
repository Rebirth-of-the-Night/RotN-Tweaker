package com.rebirthofthenight.rotntweaker.network;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyPressMessageHandler implements IMessageHandler<KeyPressMessage, IMessage> {
    @Override
    public IMessage onMessage(KeyPressMessage message, MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().player;
        player.mcServer.addScheduledTask(() -> {
            if (!player.isPotionActive(RotNTweaker.POTION_WEBBED)) return;
            player.getEntityData().setInteger("webbed_struggle", player.getEntityData().getInteger("webbed_struggle") + 1);
        });
        return null;
    }
}
