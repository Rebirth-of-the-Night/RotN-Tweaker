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
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class PotionFreezing extends PotionBase {
    public static final DamageSource COLD_DAMAGE = new DamageSource("cold").setDamageBypassesArmor();


    public PotionFreezing() {
        super("freezing", false, 0x429E9D);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % RotNConfig.POTIONS.freezing.freezeDamageCooldown == 0;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {

        if (!entity.world.isRemote) {
            entity.attackEntityFrom(COLD_DAMAGE, 1);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void renderFrozenHearts(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH
                && player.isPotionActive(RotNTweaker.POTION_FREEZING)) {

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
            GlStateManager.translate(0, 0, 300);
            mc.getTextureManager().bindTexture(new ResourceLocation(RotNTweaker.MODID, "textures/gui/hud_atlas.png"));

            for (int i = 0; i < MathHelper.ceil((maxHealth + absorption) / 2.0F); i++) {
                int row = i / 10;
                int x = left + (i % 10) * 8;
                int y = top - row * rowHeight;

                if (health <= 4) y += rand.nextInt(2);
                if (i == regen) y -= 2;

                if (i < health / 2) {
                    GlStateManager.enableBlend();
                    mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, 9, 9);
                    GlStateManager.disableBlend();
                } else if (i == health / 2 && health % 2 == 1) {
                    GlStateManager.enableBlend();
                    mc.ingameGUI.drawTexturedModalRect(x, y, 9, 0, 9, 9);
                    GlStateManager.disableBlend();
                } else {
                    continue;
                }
            }
            GlStateManager.disableBlend();
            mc.getTextureManager().bindTexture(Gui.ICONS);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect p_shouldRenderHUD_1_) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        renderEffect(effect, x+3, y+3, alpha);
    }

    //All credit for the animated icon goes to Smileycorp!
    @SideOnly(Side.CLIENT)
    @Override
    protected void renderEffect(PotionEffect effect, int x, int y, float alpha) {
        // Effect rendering
        GlStateManager.pushMatrix();
        // Translate the GLState to the center position of where the texture will be rendered
        // so that rotations and scaling are done from the center point
        GlStateManager.translate(x + 9, y + 9, 0);

        // Use sine or cosine here to convert a static increasing value, here using the player tick count into an oscillating value
        Minecraft mc = Minecraft.getMinecraft();
        float t = (float) Math.sin((float) mc.player.ticksExisted / 10f);

        // I want to stretch the texture twice as fast as I rotate it, so using a different sine function
        float t2 = (float) Math.sin((float) mc.player.ticksExisted / 5f);

        // Scale the texture between 0.9 and 1 times its normal size
        GlStateManager.scale(0.95 + 0.05 * t2, 0.95 - 0.05 * t2, 1);

        // Rotate the texture up to 2 degrees in either direction
        GlStateManager.rotate(2 * t, 0, 0, 1);

        // Set the color and bind the texture
        GlStateManager.color(1, 1, 1, alpha);
        mc.getTextureManager().bindTexture(new ResourceLocation(RotNTweaker.MODID, "textures/gui/icon_freezing.png"));

        // Draw the texture at the current position minus 9 in both axes so it's centered on the current GLState position
        Gui.drawScaledCustomSizeModalRect(-9, -9, 0, 0, 18, 18, 18, 18, 18, 18);

        GlStateManager.popMatrix();
    }

}
