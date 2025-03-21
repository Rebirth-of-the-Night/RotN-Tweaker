package com.rebirthofthenight.rotntweaker.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TicksFrozenMessage implements IMessage {
    private int ticksFrozen;

    public TicksFrozenMessage() {}

    public TicksFrozenMessage(int ticksFrozen) {
        this.ticksFrozen = ticksFrozen;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.ticksFrozen = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.ticksFrozen);
    }

    public int getTicksFrozen() {
        return ticksFrozen;
    }
}