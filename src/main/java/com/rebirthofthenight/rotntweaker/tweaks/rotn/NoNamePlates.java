package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class NoNamePlates {

    @SubscribeEvent
    public static void noNameplates(RenderLivingEvent.Specials.Pre event) {
        if (RotNConfig.TWEAKS.noNameplates) {
            event.setCanceled(true);
        }
    }
}
