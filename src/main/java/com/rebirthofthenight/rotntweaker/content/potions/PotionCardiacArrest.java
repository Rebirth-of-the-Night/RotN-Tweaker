package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;


public class PotionCardiacArrest extends PotionBase {

    public PotionCardiacArrest() {
        super("cardiac_arrest", false, 0x243145);
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

            if (entity.isPotionActive(RotNTweaker.POTION_STRONG_HEART)) {
                EntityPlayer player = (EntityPlayer) entity;
                NBTTagCompound playerData = player.getEntityData();
                player.setHealth(player.getHealth()+playerData.getInteger("stolenHealth"));
                playerData.setInteger("stolenHealth", 0);
                entity.removePotionEffect(this);
                return;
            }

            EntityPlayer player = (EntityPlayer) entity;
            if (player.getHealth() > 1) {

                NBTTagCompound playerData = player.getEntityData();
                playerData.setInteger("stolenHealth", playerData.getInteger("stolenHealth")+(int)player.getHealth()-1);

                player.setHealth(1);
            }
            player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:blindness"), 45, 0));
        }
    }

    @SubscribeEvent
    public static void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {
        if (!event.getEntity().world.isRemote) {

            if (event.getPotion().equals(RotNTweaker.POTION_CARDIAC_ARREST) && event.getEntity() instanceof EntityPlayer) {
                restoreStolenHealth((EntityPlayer) event.getEntity());

            }
        }
    }

    @SubscribeEvent
    public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
        if (!event.getEntity().world.isRemote) {

            if (event.getPotionEffect().getPotion().equals(RotNTweaker.POTION_CARDIAC_ARREST) && event.getEntity() instanceof EntityPlayer) {
                restoreStolenHealth((EntityPlayer) event.getEntity());
            }
        }
    }


    public static void restoreStolenHealth(EntityPlayer player) {
        NBTTagCompound playerData = player.getEntityData();
        player.setHealth(player.getHealth()+playerData.getInteger("stolenHealth"));
        playerData.setInteger("stolenHealth", 0);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void renderFrozenHearts(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH
                && player.isPotionActive(RotNTweaker.POTION_CARDIAC_ARREST)) {

            ScaledResolution scaledRes = new ScaledResolution(mc);
            int screenWidth = scaledRes.getScaledWidth();
            int screenHeight = scaledRes.getScaledHeight();

            int left = screenWidth / 2 - 91;
            int top = screenHeight - 39;

            int health = MathHelper.ceil(player.getHealth());
            int maxHealth = MathHelper.ceil(player.getMaxHealth());
            if (RotNConfig.POTIONS.heartsRenderSingleRow) {
                maxHealth = Math.min(maxHealth, 20);
                health = Math.min(health, 20);
            }
            int absorption = MathHelper.ceil(player.getAbsorptionAmount());

            int ticks = mc.ingameGUI.getUpdateCounter();
            Random rand = new Random();
            rand.setSeed(ticks * 312871L);

            int regen = -1;
            if (player.isPotionActive(MobEffects.REGENERATION)) {
                regen = ticks % 25;
            }

            int healthRows = MathHelper.ceil((maxHealth + absorption) / 20.0F);
            int rowHeight = Math.max(10 - (healthRows - 2), 3);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 301);
            mc.getTextureManager().bindTexture(new ResourceLocation(RotNTweaker.MODID, "textures/gui/hud_atlas.png"));

            for (int i = 0; i < MathHelper.ceil((maxHealth + absorption) / 2.0F); i++) {
                int row = i / 10;
                int x = left + (i % 10) * 8;
                int y = top - row * rowHeight;

                if (health <= 4) y += rand.nextInt(2);
                if (i == regen) y -= 2;

                // Use the 3rd texture in the atlas (starting at x = 27, y = 0) for full hearts
                if (i < health / 2) {
                    GlStateManager.enableBlend();
                    mc.ingameGUI.drawTexturedModalRect(x, y, 27, 0, 9, 9); // Full heart with the 3rd texture
                    GlStateManager.disableBlend();
                } else if (i == health / 2 && health % 2 == 1) {
                    continue;
                } else {
                    GlStateManager.enableBlend();
                    mc.ingameGUI.drawTexturedModalRect(x, y, 18, 0, 9, 9); // Empty heart with the 1st texture
                    GlStateManager.disableBlend();
                }
            }
            GlStateManager.disableBlend();
            mc.getTextureManager().bindTexture(Gui.ICONS);
            GlStateManager.popMatrix();
        }
    }
}
