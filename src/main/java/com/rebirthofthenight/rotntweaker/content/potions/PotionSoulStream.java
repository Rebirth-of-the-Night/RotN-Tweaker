package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionSoulStream extends PotionBase {

    //Implementation taken nearly verbatim from Cyclic. All credit for this code goes to Lothrazar!

    private static final double VERTICAL_MOMENTUM_FACTOR = 0.917;
    private static final float DAMAGE_REDUCTION = 0.1f;
    private static final int MIN_HEIGHT_START_BOUNCE = 3;
    private static final double PERCENT_HEIGHT_BOUNCED = 0.95;
    private static final String NBT_MOTIONY = "motionY";//is float stored as int so we use 100 factor each way
    private static final String NBT_TICK = "ticksExisted";

    public static final float VOLUME = 1.0F;
    public static final float PITCH = 1.0F;

    private static final double RANDOM_HORIZ = 0.8;
    private static final double RANDOM_VERT = 1.5;
    private static final int PARTICLE_COUNT = 12;

    public PotionSoulStream() {
        super("soul_stream", true, 0xF18AC6);
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer) || entity.isSneaking()
                || !entity.isPotionActive(RotNTweaker.POTION_SOULSTREAM)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) entity;
        if (event.getDistance() >= MIN_HEIGHT_START_BOUNCE) {
            event.setDamageMultiplier(0);
            if (entity.getEntityWorld().isRemote == false) {
                event.setCanceled(true); //nada serverside
            }
            else {
                playSound(player, player.getPosition(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, VOLUME / event.getDistance());
                spawnParticle(player.world, EnumParticleTypes.FLAME, player.getPosition());
                event.setDistance(0);// fall distance
                if (player.isElytraFlying() == false) {
                    player.motionY *= -PERCENT_HEIGHT_BOUNCED;
                    player.isAirBorne = true;
                    player.onGround = false;
                    //postpone until one tick later. otherwise there is vanilla code internally that says "ok you finished falldamage so motionY=0;"
                    player.posY += 0.01;
                    player.getEntityData().setInteger(NBT_TICK, player.ticksExisted + 1);
                    player.getEntityData().setInteger(NBT_MOTIONY, (int) (player.motionY * 100f));
                }
            }
        }
        else if (!entity.getEntityWorld().isRemote && entity.isSneaking()) {
            event.setDamageMultiplier(DAMAGE_REDUCTION);
        }
    }

    @SubscribeEvent
    public void rebounceTick(TickEvent.PlayerTickEvent event) {
        //catch a rebounce that was postponed from last tick
        if (event.player == null || event.player.isDead) {
            return;
        }
        EntityPlayer player = event.player;
        if (player.isPotionActive(RotNTweaker.POTION_SOULSTREAM) && player.isElytraFlying() == false
                && event.phase == TickEvent.Phase.END && !player.isElytraFlying()) {
            // now rebounce
            int old = (player.getEntityData() == null) ? 1 : player.getEntityData().getInteger(NBT_MOTIONY);
            float motionY = old / 100F;
            if (player.getEntityData().getInteger(NBT_TICK) == player.ticksExisted && motionY > 0) {
                player.getEntityData().setInteger(NBT_TICK, -1);
                player.motionY = motionY;
            }
        }
    }

    @Override
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (entity.onGround == false && entity.isPotionActive(RotNTweaker.POTION_SOULSTREAM)
                && entity.isElytraFlying() == false) {//preserve momentum, otherwise it will be like regular falling/gravity
            //yes this works if drank potion and not just from launcher but is ok
            dragEntityMomentum(entity, VERTICAL_MOMENTUM_FACTOR);
        }
    };

    public static void dragEntityMomentum(EntityLivingBase entity, double verticalMomentumFactor) {
        entity.motionX = entity.motionX / verticalMomentumFactor;
        entity.motionZ = entity.motionZ / verticalMomentumFactor;
    }

    public static void playSound(EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory cat, float volume) {
        if (player == null) {
            return;
        }
        player.getEntityWorld().playSound(player, pos, soundIn, cat, volume, PITCH);
    }


    private static void spawnParticle(World world, EnumParticleTypes sparkle, BlockPos pos) {
        if (pos == null) {
            //      ModCyclic.logger.warn("Particle at position null");
            return;
        }
        spawnParticle(world, sparkle, pos.getX(), pos.getY(), pos.getZ());
    }

    private static void spawnParticle(World world, EnumParticleTypes sparkle, double x, double y, double z) {
        spawnParticle(world, sparkle, x, y, z, PARTICLE_COUNT);
    }

    private static void spawnParticle(World world, EnumParticleTypes sparkle, double x, double y, double z, int count) {
        // client side
        // http://www.minecraftforge.net/forum/index.php?topic=9744.0
        for (int countparticles = 0; countparticles <= count; ++countparticles) {
            world.spawnParticle(sparkle,
                    x + getHorizRandom(world, RANDOM_HORIZ),
                    y + getVertRandom(world, RANDOM_VERT),
                    z + getHorizRandom(world, RANDOM_HORIZ),
                    0.0D, 0.0D, 0.0D);
        }
    }

    private static double getVertRandom(World world, double rando) {
        return world.rand.nextDouble() * (double) rando - (double) 0.1;
    }

    private static double getHorizRandom(World world, double rando) {
        return (world.rand.nextDouble() - 0.5D) * (double) rando;
    }

}
