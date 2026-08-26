package com.rebirthofthenight.rotntweaker;

import com.rebirthofthenight.rotntweaker.content.player.InputHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void onInit(final FMLInitializationEvent event) {
        super.onInit(event);
        MinecraftForge.EVENT_BUS.register(new InputHandler());
    }
}
