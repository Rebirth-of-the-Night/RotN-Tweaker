package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import com.rebirthofthenight.rotntweaker.render.overlay.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class PotionScorching extends PotionBase {

    private static final UUID HEALTH_UUID  = UUID.fromString("a670b5b6-1011-4f23-8d58-3ecb2669d140");
    private int internalCounter;
    private static final ResourceLocation SCREEN_OVERLAY_TEXTURE_0 = new ResourceLocation(RotNTweaker.MODID, "textures/gui/scorching_overlay.png");
    private static final Map<EntityPlayer, PlayerPotionData> playerPotionDataMap = new WeakHashMap<>();

    public PotionScorching() {
        super("scorching", false, 0xFF4500);
    }

    @Override
    public boolean hasStatusIcon() {
        return true;
    }



    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % RotNConfig.POTIONS.scorching.scorchingInterval == 0;
    }


        @Override
        public void performEffect(EntityLivingBase entity, int amplifier) {

            if (!RotNTweaker.SWELTERING_BIOME_DATA.containsKey(entity.world.getBiome(entity.getPosition()))) {
                entity.removePotionEffect(this);
                return;
            }

            if (!entity.world.isRemote) {

                this.internalCounter++;

                IAttributeInstance healthAttribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

                double currentMaxHealth = healthAttribute.getAttributeValue();

                if (healthAttribute.getModifier(HEALTH_UUID) != null) {
                    healthAttribute.removeModifier(HEALTH_UUID);
                }

                double newMaxHealth = currentMaxHealth;
                if (entity.getHealth() > newMaxHealth) entity.setHealth((float) newMaxHealth);

                if (currentMaxHealth > 10) {
                    if (this.internalCounter >= RotNConfig.POTIONS.scorching.scorchingHealthDrainCyclesNeeded) {
                        newMaxHealth = Math.max(currentMaxHealth - 2.0, 10.0);
                        this.internalCounter = 0;
                    }
                } else if (amplifier > 0) entity.attackEntityFrom(DamageSource.ON_FIRE, 2.0F);

                double reductionAmount = newMaxHealth - entity.getMaxHealth();

                AttributeModifier healthModifier = new AttributeModifier(HEALTH_UUID, "scorching_health", reductionAmount, 0);
                healthAttribute.applyModifier(healthModifier);
            }
        }

    @SubscribeEvent
    public static void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {
        if (!event.getEntity().world.isRemote) {

            if (event.getPotion().equals(RotNTweaker.POTION_SCORCHING)) {
                event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(HEALTH_UUID);
            }
        }
    }

    @SubscribeEvent
    public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
        if (!event.getEntity().world.isRemote) {

            if (event.getPotionEffect().getPotion().equals(RotNTweaker.POTION_SCORCHING)) {
                event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(HEALTH_UUID);
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

        if (effect.getAmplifier() >= 2) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, alpha);
            GlStateManager.translate(0, 0, 301);


            // Bind the fire texture
            TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            // Calculate adjusted x and y positions for the bottom-center alignment
            int centerX = x + 9; // Center horizontally (texture width is 16px)
            int bottomY = y + 18; // Bottom vertically (move texture down)

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            // Draw the texture manually
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(centerX - 8, bottomY, 0).tex(sprite.getMinU(), sprite.getMaxV()).endVertex(); // Bottom-left
            buffer.pos(centerX + 8, bottomY, 0).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex(); // Bottom-right
            buffer.pos(centerX + 8, bottomY - 16, 0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex(); // Top-right
            buffer.pos(centerX - 8, bottomY - 16, 0).tex(sprite.getMinU(), sprite.getMinV()).endVertex(); // Top-left
            tessellator.draw();

            GlStateManager.popMatrix();
        }

        mc.getTextureManager().bindTexture(new ResourceLocation(RotNTweaker.MODID, "textures/gui/icon_sweltering.png"));

        GlStateManager.pushMatrix();
        // Translate the GLState to the center position of where the texture will be rendered
        // so that rotations and scaling are done from the center point
        GlStateManager.translate(x + 9, y + 9, 0);

        // Use sine or cosine here to convert a static increasing value, here using the player tick count into an oscillating value
        float t = (float) Math.sin((float) mc.player.ticksExisted / 2f);

        // I want to stretch the texture twice as fast as I rotate it, so using a different sine function
        float t2 = (float) Math.sin((float) mc.player.ticksExisted / 5f);

        // Scale the texture between 0.9 and 1 times its normal size
        GlStateManager.scale(0.95 + 0.05 * t2, 0.95 - 0.05 * t2, 1);

        // Rotate the texture up to 2 degrees in either direction
        GlStateManager.rotate(2 * t, 0, 0, 1);

        // Set the color and bind the texture
        GlStateManager.color(1, 1, 1, alpha);
        mc.getTextureManager().bindTexture(new ResourceLocation(RotNTweaker.MODID, "textures/gui/scorching_heat.png"));

        // Draw the texture at the current position minus 9 in both axes so it's centered on the current GLState position
        Gui.drawScaledCustomSizeModalRect(-9, -9, 0, 0, 18, 18, 18, 18, 18, 18);

        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            boolean isPotionActive = player.isPotionActive(RotNTweaker.POTION_SCORCHING);

            PlayerPotionData potionData = playerPotionDataMap.computeIfAbsent(player, p -> new PlayerPotionData());

            if (isPotionActive) {
                if (!potionData.wasPotionActive) {
                    potionData.fadeStartTime = System.nanoTime();
                    potionData.potEndTime = -1;
                }

                long elapsedTime = (System.nanoTime() - potionData.fadeStartTime) / 1_000_000L;
                float alpha = Math.min(1.0f, (float) elapsedTime / RotNConfig.POTIONS.scorching.scorchingFadeIn);

                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                GlStateManager.color(1, 1, 1, alpha);
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
                float alpha = Math.max(0.0f, 1.0f - (float) elapsedTime / RotNConfig.POTIONS.scorching.scorchingFadeOut);

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