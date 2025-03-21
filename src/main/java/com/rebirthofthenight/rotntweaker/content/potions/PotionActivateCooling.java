package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PotionActivateCooling extends PotionBase {
    public PotionActivateCooling() {
        super("activate_cooling", false, 0x429E9D);
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        World world = entity.world;

        if (!world.isRemote) {
            if (!(entity instanceof EntityPlayer)) return;
            EntityPlayer player = (EntityPlayer) entity;
            player.getEntityData().setInteger("ticksCooling", amplifier* RotNConfig.POTIONS.cooling.coolingAmplifierMultiplier);
            player.removePotionEffect(this);
        }
    }
}
