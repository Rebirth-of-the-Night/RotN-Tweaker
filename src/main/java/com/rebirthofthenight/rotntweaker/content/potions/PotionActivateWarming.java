package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PotionActivateWarming extends PotionBase {
    public PotionActivateWarming() {
        super("activate_warming", true, 0xFF0000);
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
            player.getEntityData().setInteger("ticksWarming", RotNConfig.POTIONS.warming.warmingAmplifierMultiplier*20);
            player.removePotionEffect(this);
        }
    }
}
