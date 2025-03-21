package com.rebirthofthenight.rotntweaker.tweaks.rotn;


import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WaterBottleEffect {

    @SubscribeEvent
    public static void onWaterDrink(LivingEntityUseItemEvent.Finish event) {

            ItemStack itemStack = event.getItem();

            if (itemStack.getItem().equals(Items.POTIONITEM) && itemStack.hasTagCompound() && itemStack.getTagCompound().getString("Potion").equals("minecraft:water")) {
                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation(RotNConfig.POTIONS.waterBottleTweak.waterBottlePotion), RotNConfig.POTIONS.waterBottleTweak.waterBottleDuration, RotNConfig.POTIONS.waterBottleTweak.waterBottleAmp));
        }
    }
}
