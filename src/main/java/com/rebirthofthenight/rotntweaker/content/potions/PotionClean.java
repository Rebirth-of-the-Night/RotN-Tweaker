package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = RotNTweaker.MODID)
public class PotionClean extends PotionBase {

    public static final float VOLUME = 0.1F;
    public static final float PITCH = 1.0F;
    private final List<Potion> potionRemoval = new ArrayList<>();

    public PotionClean() {
        super("clean", true, 0x6BE49E);
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return !potionRemoval.isEmpty();
    }

    public void setPotionRemoval(List<Potion> oldRemovals) {
        potionRemoval.clear();
        potionRemoval.addAll(oldRemovals);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (entity.getEntityWorld().isRemote) return;

        while (!potionRemoval.isEmpty()) {
            entity.removePotionEffect(potionRemoval.remove(0));

            //Decrease Clean's amplifier and break if no levels remain
            if (amplifier <= 0) {
                entity.removePotionEffect(RotNTweaker.POTION_CLEAN);
                return;
            }

            //Apply a new Clean effect with a reduced amplifier
            PotionEffect cleanEffect = entity.getActivePotionEffect(RotNTweaker.POTION_CLEAN);

            entity.removePotionEffect(RotNTweaker.POTION_CLEAN);
            if (cleanEffect != null) {
                entity.addPotionEffect(new PotionEffect(RotNTweaker.POTION_CLEAN, cleanEffect.getDuration(), amplifier - 1, cleanEffect.getIsAmbient(), cleanEffect.doesShowParticles()));
            }

            return;
        }
    }



    @SubscribeEvent
    public static void PotionApplicableEvent(PotionEvent.PotionApplicableEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote) return;

        PotionEffect currentEffect = event.getPotionEffect();

        // Check if the added potion is part of the removal list and if the entity has the Clean potion
        if (RotNTweaker.CLEAN_REMOVED_POTIONS.contains(currentEffect.getPotion())
                && !entity.isPotionActive(currentEffect.getPotion())
                && entity.isPotionActive(RotNTweaker.POTION_CLEAN)) {

            PotionEffect cleanEffect = entity.getActivePotionEffect(RotNTweaker.POTION_CLEAN);
            if (cleanEffect != null && cleanEffect.getPotion() instanceof PotionClean) {
                // Add the current potion to the removal list via the public method
                ((PotionClean) cleanEffect.getPotion()).addPotionToRemove(currentEffect.getPotion());
            }
        }
    }

    public void addPotionToRemove(Potion potion) {
        if (!potionRemoval.contains(potion)) {
            potionRemoval.add(potion);
        }
    }

    @SubscribeEvent
    public static void onPotionRemoveEvent(PotionEvent.PotionRemoveEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        Potion potion = event.getPotion();

        if (!entity.world.isRemote && potion == RotNTweaker.POTION_CLEAN) {
            playSound(entity, entity.getPosition(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.HOSTILE, VOLUME);
        }
    }

    public static void playSound(EntityLivingBase entityLivingBase, BlockPos pos, SoundEvent soundIn, SoundCategory cat, float volume) {
        if (entityLivingBase == null) {
            return;
        }
        entityLivingBase.getEntityWorld().playSound(null, pos, soundIn, cat, volume, PITCH);
    }
}
