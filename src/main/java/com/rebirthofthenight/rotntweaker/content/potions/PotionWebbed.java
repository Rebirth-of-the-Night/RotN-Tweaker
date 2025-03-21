package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import com.rebirthofthenight.rotntweaker.render.overlay.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;


public class PotionWebbed extends PotionBase {
    private static final UUID SLOWNESS_UUID  = UUID.fromString("5e721e0c-e9c5-4549-9505-1bc01cb0c6f2");
    private static final ResourceLocation SCREEN_OVERLAY_TEXTURE = new ResourceLocation(RotNTweaker.MODID, "textures/gui/webbed_overlay.png");
    private static final Map<EntityPlayer, PlayerPotionData> playerPotionDataMap = new WeakHashMap<>();


    public PotionWebbed() {
        super("webbed", false, 0xD3D3D3);
    }

    @Override
    public boolean hasStatusIcon() {
        return true;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        World world = entity.world;

        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (isGracePeriodActive(player, entity.world)) {
                player.removePotionEffect(RotNTweaker.POTION_WEBBED);
                return;
            }
        }

        //Parts taken from Fullmetal effect from Rustic
        entity.setJumping(false);
        entity.setSprinting(false);
        if ((entity.getRidingEntity() != null) && (entity.getRidingEntity() instanceof EntityLivingBase)) {
            entity.dismountRidingEntity();
        }
        if (!entity.onGround && !entity.hasNoGravity()) {
            if ((entity.isInWater() || entity.isInLava())) {
                entity.motionY -= 0.27 / 4;
                entity.velocityChanged = true;
            } else {
                entity.motionY = Math.min(entity.motionY, 0);
                entity.velocityChanged = true;
            }
        }
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            player.capabilities.isFlying = false;
        }

        if (!world.isRemote) {

            IAttributeInstance speedAttribute = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                if (speedAttribute.getModifier(SLOWNESS_UUID) == null) {
                    double slowdown = Math.max(-RotNConfig.POTIONS.webbed.webbedSlowDownPerLevel * (amplifier + 1), -RotNConfig.POTIONS.webbed.webbedMaxSlowdown);

                    entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_UUID);

                    AttributeModifier modifier = new AttributeModifier(SLOWNESS_UUID, "webbed_slowdown", slowdown, 2);
                    entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(modifier);

                }

            //Handling of struggle mechanics
            if (!(entity instanceof EntityPlayer)) return;
            EntityPlayer player = (EntityPlayer) entity;

            if (player.getEntityData().getInteger("webbed_struggle") >= RotNConfig.POTIONS.webbed.webbedStruggleCount) {
                player.removePotionEffect(this);
                player.getEntityData().setLong("webbed_grace_period_end", world.getTotalWorldTime() + RotNConfig.POTIONS.webbed.webbedGraceTicks);
                //Reset tick counter to 0
                player.getEntityData().setInteger("webbed_struggle", 0);
                Minecraft.getMinecraft().player.getEntityData().setInteger("webbed_struggle", 0);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (event.getSource().isFireDamage()) {
                if (player.isPotionActive(RotNTweaker.POTION_WEBBED)) {
                    player.removePotionEffect(RotNTweaker.POTION_WEBBED);
                    player.getEntityData().setLong("webbed_grace_period_end", player.getEntityWorld().getTotalWorldTime() + RotNConfig.POTIONS.webbed.webbedGraceTicks);
                    Minecraft.getMinecraft().player.getEntityData().setLong("webbed_grace_period_end", player.getEntityWorld().getTotalWorldTime() + RotNConfig.POTIONS.webbed.webbedGraceTicks);
                }
            }
        }
    }

    private static boolean isGracePeriodActive(EntityPlayer player, World world) {
        long gracePeriodEnd = player.getEntityData().getLong("webbed_grace_period_end");
        return world.getTotalWorldTime() < gracePeriodEnd;
    }

    private static boolean isGracePeriodActiveClient() {
        long gracePeriodEnd = Minecraft.getMinecraft().player.getEntityData().getLong("webbed_grace_period_end");
        return Minecraft.getMinecraft().world.getTotalWorldTime() < gracePeriodEnd;
    }

    @SubscribeEvent
    public static void onNewWebbed(PotionEvent.PotionAddedEvent event) {
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer) {

            EntityLivingBase entity = event.getEntityLiving();
            PotionEffect currentEffect = event.getPotionEffect();
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (!currentEffect.getPotion().equals(RotNTweaker.POTION_WEBBED)) return;

            if (entity.isPotionActive(RotNTweaker.POTION_WEBBED)) {
                PotionEffect oldWebbed = player.getActivePotionEffect(RotNTweaker.POTION_WEBBED);
                int newAmp = Math.min(oldWebbed.getAmplifier() + 1, 2);
                int newDuration = Math.max(oldWebbed.getDuration(), event.getPotionEffect().getDuration());

                player.removePotionEffect(RotNTweaker.POTION_WEBBED);
                player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_UUID);

                player.addPotionEffect(new PotionEffect(RotNTweaker.POTION_WEBBED, newDuration, newAmp));
            }
        }
    }

    @SubscribeEvent
    public static void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {
        if (!event.getEntity().world.isRemote) {


            if (event.getPotion().equals(RotNTweaker.POTION_WEBBED)) {
                event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_UUID);
                if (event.getEntity() instanceof EntityPlayer) {
                    event.getEntity().getEntityData().setInteger("webbed_struggle", 0);
                    Minecraft.getMinecraft().player.getEntityData().setInteger("webbed_struggle", 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
        if (!event.getEntity().world.isRemote) {

            if (event.getPotionEffect().getPotion().equals(RotNTweaker.POTION_WEBBED)) {
                event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_UUID);
                if (event.getEntity() instanceof EntityPlayer) {
                    event.getEntity().getEntityData().setInteger("webbed_struggle", 0);
                    Minecraft.getMinecraft().player.getEntityData().setInteger("webbed_struggle", 0);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
    public static void onRenderExperienceEvent(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            if (mc.player.isPotionActive(RotNTweaker.POTION_WEBBED) && !isGracePeriodActiveClient() && RotNConfig.POTIONS.webbed.webbedBarEnabled) {
                ScaledResolution resolution = event.getResolution();
                int width = resolution.getScaledWidth();
                int height = resolution.getScaledHeight();

                int x = (width / 2 - 91) - RotNConfig.POTIONS.webbed.webbedBarXOffset;
                int y = (height - 29) - RotNConfig.POTIONS.webbed.webbedBarYOffset;
                int textureWidth = 182;
                int textureHeight = 5;

                mc.getTextureManager().bindTexture(new ResourceLocation(RotNTweaker.MODID, "textures/gui/hud_atlas.png"));

                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                // Render the background texture
                Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(
                        x, y,
                        0, 64,
                        textureWidth, textureHeight
                );

                int webbedStruggle = mc.player.getEntityData().getInteger("webbed_struggle");

                int adjustedWidth = (int) (textureWidth * (1.0 - ((1.0 * webbedStruggle) / RotNConfig.POTIONS.webbed.webbedStruggleCount)));
                if (adjustedWidth < 0) adjustedWidth = 0;

                //Render the overlay texture (progress bar)
                Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(
                        x, y,
                        0, 69,
                        adjustedWidth, textureHeight
                );

                GlStateManager.enableDepth();

                if (RotNConfig.POTIONS.webbed.webbedWasdIconEnabled) {

                    //Render the animated texture (WASD)
                    int animationFrame = (mc.player.ticksExisted / 7) % 3;
                    int animationTextureY = 74 + (animationFrame * 12);

                    int animationX = (width / 2 - 9) + RotNConfig.POTIONS.webbed.webbedWasdIconXOffset;
                    int animationY = (height - 40) + RotNConfig.POTIONS.webbed.webbedWasdIconYOffset;

                    Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(
                            animationX, animationY,
                            0, animationTextureY,
                            18, 12
                    );

                    GlStateManager.disableBlend();
                }

                if (!RotNConfig.POTIONS.webbed.webbedTextEnabled) return;

                // Render the text prompt
                String text = I18n.format("message.webbed_mash_text");
                int textWidth = mc.fontRenderer.getStringWidth(text);
                int textX = width / 2 - textWidth / 2;

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

                mc.fontRenderer.drawStringWithShadow(text, textX + RotNConfig.POTIONS.webbed.webbedTextXOffset, y - 23 + RotNConfig.POTIONS.webbed.webbedTextYOffset, 0xFFFFFF);

                GlStateManager.disableBlend();
            }
        }
    }

    //TODO: Add short fade-in and -out
    //TODO: Add configs for fade-in and fade-out
    //Handle rendering of overlay
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            boolean isPotionActive = player.isPotionActive(RotNTweaker.POTION_WEBBED);

            PlayerPotionData potionData = playerPotionDataMap.computeIfAbsent(player, p -> new PlayerPotionData());

            if (isPotionActive) {
                if (!potionData.wasPotionActive) {
                    potionData.fadeStartTime = System.nanoTime();
                    potionData.potEndTime = -1;
                }

                long elapsedTime = (System.nanoTime() - potionData.fadeStartTime) / 1_000_000L;
                float alpha = Math.min(1.0f, (float) elapsedTime / RotNConfig.POTIONS.webbed.webbedFadeIn);

                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                GlStateManager.color(1, 1, 1, alpha);
                GlStateManager.disableAlpha();

                ClientEventHandler.renderScreenOverlay(event.getResolution(), SCREEN_OVERLAY_TEXTURE);

                GlStateManager.enableAlpha();
                GlStateManager.color(1, 1, 1, 1);

                potionData.wasPotionActive = true;
            } else if (potionData.wasPotionActive) {
                if (potionData.potEndTime == -1) {
                    potionData.potEndTime = System.nanoTime();
                }

                long elapsedTime = (System.nanoTime() - potionData.potEndTime) / 1_000_000L;
                float alpha = Math.max(0.0f, 1.0f - (float) elapsedTime / RotNConfig.POTIONS.webbed.webbedFadeOut);

                if (alpha > 0.0f) {
                    OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                    GlStateManager.color(1, 1, 1, alpha);
                    GlStateManager.disableAlpha();

                    ClientEventHandler.renderScreenOverlay(event.getResolution(), SCREEN_OVERLAY_TEXTURE);

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

    //Handle rendering of web bar
//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void onRenderGameOverlayEventCancel(RenderGameOverlayEvent.Pre event) {
//        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
//            EntityPlayer player = Minecraft.getMinecraft().player;
//
//            if (player != null && player.isPotionActive(RotNTweaker.POTION_WEBBED) && !isGracePeriodActiveClient()) {
//                event.setCanceled(true);
//            }
//        }
//    }
}
