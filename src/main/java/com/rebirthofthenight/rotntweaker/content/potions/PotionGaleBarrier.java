package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class PotionGaleBarrier extends PotionBase {

    public PotionGaleBarrier() {
        super("gale_barrier", true, 0x85CEE2);
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        World world = entity.world;

        if (!world.isRemote) {
            if (entity.isPotionActive(RotNTweaker.POTION_SWELTERING)) {
                PotionEffect currentSweltering = entity.getActivePotionEffect(RotNTweaker.POTION_SWELTERING);
                //if same level as sweltering or higher
                if (currentSweltering.getAmplifier() <= amplifier)
                    entity.addPotionEffect(new PotionEffect(RotNTweaker.POTION_SWELTERING, currentSweltering.getDuration()+20, currentSweltering.getAmplifier()));
            }
        }
    }
}
