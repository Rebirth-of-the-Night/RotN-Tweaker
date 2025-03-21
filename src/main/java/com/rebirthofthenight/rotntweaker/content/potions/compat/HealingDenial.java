package com.rebirthofthenight.rotntweaker.content.potions.compat;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.HealthRegenEvent;

public class HealingDenial {

    //Optional functionality with AppleCore to stop all naturalRegen and saturationRegen when CardiacArrest is active
    @SubscribeEvent
    public void allowSaturatedRegen(HealthRegenEvent.AllowSaturatedRegen event) {
        if (event.player.isPotionActive(RotNTweaker.POTION_CARDIAC_ARREST)) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void allowHealthRegen(HealthRegenEvent.AllowRegen event) {
        if (event.player.isPotionActive(RotNTweaker.POTION_CARDIAC_ARREST)) {
            event.setResult(Event.Result.DENY);
        }
    }
}
