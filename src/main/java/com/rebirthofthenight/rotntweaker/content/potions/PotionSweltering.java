package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import com.rebirthofthenight.rotntweaker.render.overlay.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.WeakHashMap;

public class PotionSweltering extends PotionBase {

    private static final ResourceLocation SCREEN_OVERLAY_TEXTURE_0 = new ResourceLocation(RotNTweaker.MODID, "textures/gui/sweltering_overlay.png");

    private static final Map<EntityPlayer, PlayerPotionData> playerPotionDataMap = new WeakHashMap<>();

    public PotionSweltering() {
        super("sweltering", false, 0xFFBC27);
    }

    @Override
    public boolean hasStatusIcon() {
        return true;
    }



    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration <= 1;
    }


    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        World world = entity.world;

        if (!world.isRemote) {
            if (RotNTweaker.SWELTERING_BIOME_DATA.containsKey(entity.world.getBiome(entity.getPosition()))) {
                entity.addPotionEffect(new PotionEffect(RotNTweaker.POTION_SCORCHING, 100000, amplifier, false, true));

            }
        }
    }

    @SubscribeEvent
    public static void onNewSweltering(PotionEvent.PotionAddedEvent event) {
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer) {

            EntityLivingBase entity = event.getEntityLiving();
            PotionEffect currentEffect = event.getPotionEffect();
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (currentEffect.getPotion().equals(RotNTweaker.POTION_SWELTERING) && entity.isPotionActive(RotNTweaker.POTION_SCORCHING)) {
                if (currentEffect.getAmplifier() >= player.getActivePotionEffect(RotNTweaker.POTION_SCORCHING).getAmplifier()) {
                    player.removePotionEffect(RotNTweaker.POTION_SCORCHING);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        renderEffect(effect, x+3, y+3, alpha);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void renderEffect(PotionEffect effect, int x, int y, float alpha) {
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.pushMatrix();
        // Translate the GLState to the center position of where the texture will be rendered
        // so that rotations and scaling are done from the center point
        GlStateManager.translate(x + 9, y + 9, 0);

        // Use sine or cosine here to convert a static increasing value, here using the player tick count into an oscillating value
        float t = (float) Math.sin((float) mc.player.ticksExisted / 10f);

        // I want to stretch the texture twice as fast as I rotate it, so using a different sine function
        float t2 = (float) Math.sin((float) mc.player.ticksExisted / 5f);

        // Scale the texture between 0.9 and 1 times its normal size
        GlStateManager.scale(0.95 + 0.05 * t2, 0.95 - 0.05 * t2, 1);

        // Rotate the texture up to 2 degrees in either direction
        GlStateManager.rotate(2 * t, 0, 0, 1);

        // Set the color and bind the texture
        GlStateManager.color(1, 1, 1, alpha);
        mc.getTextureManager().bindTexture(new ResourceLocation(RotNTweaker.MODID, "textures/gui/sweltering_heat.png"));

        // Draw the texture at the current position minus 9 in both axes so it's centered on the current GLState position
        Gui.drawScaledCustomSizeModalRect(-9, -9, 0, 0, 18, 18, 18, 18, 18, 18);

        GlStateManager.popMatrix();

        mc.getTextureManager().bindTexture(new ResourceLocation(RotNTweaker.MODID, "textures/gui/icon_sweltering.png"));

        int totalFrames = 6;
        int frameHeight = 18;
        int currentFrame = (int) ((mc.player.ticksExisted / 5) % totalFrames);
        int textureY = currentFrame * frameHeight;

        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, alpha);
        Gui.drawScaledCustomSizeModalRect(
                x, y,
                0, textureY,
                18, frameHeight,
                18, 18,
                18, 108
        );
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            boolean isPotionActive = player.isPotionActive(RotNTweaker.POTION_SWELTERING);

            PlayerPotionData potionData = playerPotionDataMap.computeIfAbsent(player, p -> new PlayerPotionData());

            if (isPotionActive) {
                if (!potionData.wasPotionActive) {
                    potionData.fadeStartTime = System.nanoTime();
                    potionData.potEndTime = -1;
                }

                long elapsedTime = (System.nanoTime() - potionData.fadeStartTime) / 1_000_000L;

                float alpha = Math.min(1.0f, (float) elapsedTime / RotNConfig.POTIONS.sweltering.swelteringFadeIn);

                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                GlStateManager.color(1, 1, 1, alpha);
                GlStateManager.translate(0, 0, 301);
                GlStateManager.disableAlpha();

                ClientEventHandler.renderScreenOverlay(event.getResolution(), SCREEN_OVERLAY_TEXTURE_0);

                GlStateManager.enableAlpha();
                GlStateManager.color(1, 1, 1, 1);

                potionData.wasPotionActive = true;
            } else if (potionData.wasPotionActive) {
                if (potionData.potEndTime == -1) {
                    potionData.potEndTime = System.nanoTime();
                }

                long elapsedTime = (System.nanoTime() - potionData.potEndTime) / 1_000_000L;

                float alpha = Math.max(0.0f, 1.0f - (float) elapsedTime / RotNConfig.POTIONS.sweltering.swelteringFadeOut);

                if (alpha > 0.0f) {
                    OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                    GlStateManager.color(1, 1, 1, alpha);
                    GlStateManager.disableAlpha();

                    ClientEventHandler.renderScreenOverlay(event.getResolution(), SCREEN_OVERLAY_TEXTURE_0);

                    GlStateManager.enableAlpha();
                    GlStateManager.color(1, 1, 1, 1);
                } else {
                    potionData.potEndTime = -1;
                    potionData.wasPotionActive = false;
                }
            }
        }
    }

    private static class PlayerPotionData {
        long fadeStartTime = -1;
        long potEndTime = -1;
        boolean wasPotionActive = false;
    }

}
