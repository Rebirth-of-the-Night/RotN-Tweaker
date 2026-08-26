package com.rebirthofthenight.rotntweaker.content.player;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import com.rebirthofthenight.rotntweaker.network.KeyPressMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class InputHandler {

    private int lastPressedKey = -1;

    KeyBinding keyForward = net.minecraft.client.Minecraft.getMinecraft().gameSettings.keyBindForward;
    KeyBinding keyLeft = net.minecraft.client.Minecraft.getMinecraft().gameSettings.keyBindLeft;
    KeyBinding keyBack = net.minecraft.client.Minecraft.getMinecraft().gameSettings.keyBindBack;
    KeyBinding keyRight = net.minecraft.client.Minecraft.getMinecraft().gameSettings.keyBindRight;

    @SubscribeEvent
    public void onWebbedMovementInput(InputEvent.KeyInputEvent event) {

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        PotionEffect webbedEffect = mc.player.getActivePotionEffect(RotNTweaker.POTION_WEBBED);
        if (webbedEffect == null) return;

        if (keyForward.isKeyDown() && lastPressedKey != Keyboard.KEY_W) {
            lastPressedKey = Keyboard.KEY_W;
            sendKeyPressMessage(Keyboard.KEY_W);
            //System.out.println("Sent a packet");
        }
        else if (keyLeft.isKeyDown() && lastPressedKey != Keyboard.KEY_A) {
            lastPressedKey = Keyboard.KEY_A;
            sendKeyPressMessage(Keyboard.KEY_A);
            //System.out.println("Sent a packet");

        }
        else if (keyBack.isKeyDown() && lastPressedKey != Keyboard.KEY_S) {
            lastPressedKey = Keyboard.KEY_S;
            sendKeyPressMessage(Keyboard.KEY_S);
            //System.out.println("Sent a packet");

        }
        else if (keyRight.isKeyDown() && lastPressedKey != Keyboard.KEY_D) {
            lastPressedKey = Keyboard.KEY_D;
            sendKeyPressMessage(Keyboard.KEY_D);
            //System.out.println("Sent a packet");

        }
    }

    private void sendKeyPressMessage(int keyCode) {
        // in principle, could send the key code to the server too, but it's trivial for a malicious client to fake it
        // anyways, so there's not really a good reason to
        RotNTweaker.NETWORK.sendToServer(new KeyPressMessage());
        final NBTTagCompound data = Minecraft.getMinecraft().player.getEntityData();
        data.setInteger("webbed_struggle", data.getInteger("webbed_struggle") + 1);
    }
}
