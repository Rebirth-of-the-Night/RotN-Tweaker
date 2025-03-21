package com.rebirthofthenight.rotntweaker.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class KeyPressMessage implements IMessage {

    private int keyCode;
    private UUID playerUUID;


    public KeyPressMessage() {}

    public KeyPressMessage(int keyCode, UUID playerUUID) {
        this.keyCode = keyCode;
        this.playerUUID = playerUUID;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        keyCode = buf.readInt();
        playerUUID = new UUID(buf.readLong(), buf.readLong());

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(keyCode);
        buf.writeLong(playerUUID.getMostSignificantBits());
        buf.writeLong(playerUUID.getLeastSignificantBits());
    }
}