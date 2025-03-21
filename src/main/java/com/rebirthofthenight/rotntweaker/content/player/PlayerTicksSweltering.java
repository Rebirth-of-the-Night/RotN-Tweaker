package com.rebirthofthenight.rotntweaker.content.player;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerTicksSweltering {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player.ticksExisted % 120 != 0) return;
        //System.out.println("TRIGGERED");
        Biome currBiome = player.world.getBiome(player.getPosition());
        if (RotNTweaker.SWELTERING_BIOME_DATA.containsKey(currBiome)) {
            //System.out.println("CORRECT BIOME");

            int minY = RotNTweaker.SWELTERING_BIOME_DATA.get(currBiome).get(0);
            int maxY = RotNTweaker.SWELTERING_BIOME_DATA.get(currBiome).get(1);
            int duration = RotNTweaker.SWELTERING_BIOME_DATA.get(currBiome).get(2);
            int amp = RotNTweaker.SWELTERING_BIOME_DATA.get(currBiome).get(3);
            Potion potionSweltering = RotNTweaker.POTION_SWELTERING;
            Potion potionScorching = RotNTweaker.POTION_SCORCHING;

            if (!player.isPotionActive(potionSweltering) && !player.isPotionActive(potionScorching)) {
                if (minY < player.getPosition().getY() && player.getPosition().getY() < maxY) {

                    //System.out.println("CORRECT Y");
                    player.addPotionEffect(new PotionEffect(potionSweltering, duration, amp));
                } else {
                    if (player.isPotionActive(potionScorching)) player.removePotionEffect(potionScorching);
                }
            } else if (player.isPotionActive(potionSweltering) && player.getActivePotionEffect(potionSweltering).getAmplifier() < amp) {
                PotionEffect currentSweltering = player.getActivePotionEffect(potionSweltering);
                //player.removePotionEffect(potionSweltering);
                player.addPotionEffect(new PotionEffect(currentSweltering.getPotion(), currentSweltering.getDuration(), amp));

            } else if (player.isPotionActive(potionScorching) && player.getActivePotionEffect(potionScorching).getAmplifier() < amp) {
                PotionEffect currentScorching = player.getActivePotionEffect(potionScorching);
                //player.removePotionEffect(potionScorching);
                player.addPotionEffect(new PotionEffect(currentScorching.getPotion(), 100000, amp));

            } else if (player.isPotionActive(potionSweltering) && player.getActivePotionEffect(potionSweltering).getAmplifier() > amp) {
                PotionEffect currentSweltering = player.getActivePotionEffect(potionSweltering);
                player.removePotionEffect(potionSweltering);
                player.addPotionEffect(new PotionEffect(currentSweltering.getPotion(), currentSweltering.getDuration(), amp));

            } else if (player.isPotionActive(potionScorching) && player.getActivePotionEffect(potionScorching).getAmplifier() > amp) {
                PotionEffect currentScorching = player.getActivePotionEffect(potionScorching);
                player.removePotionEffect(potionScorching);
                player.addPotionEffect(new PotionEffect(currentScorching.getPotion(), 100000, amp));

            }
        }
    }
}
