package com.rebirthofthenight.rotntweaker.content.player;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import com.rebirthofthenight.rotntweaker.network.TicksFrozenMessage;
import com.rebirthofthenight.rotntweaker.render.overlay.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class PlayerTicksFrozen {

    private static final UUID SLOWNESS_UUID  = UUID.fromString("b352305a-822f-4503-a202-d4ed58cfbe0d");
    private static final ResourceLocation SCREEN_OVERLAY_TEXTURE = new ResourceLocation(RotNTweaker.MODID, "textures/gui/frost_overlay.png");
    private static final Map<EntityPlayer, PlayerFadeData> playerFadeDataMap = new WeakHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.player.ticksExisted % RotNConfig.POTIONS.freezing.playerFreezeUpdateInterval != 0) {
            return;
        }

        if (!player.world.isRemote) {
            //Grace period
            if (player.ticksExisted < RotNConfig.POTIONS.freezing.playerFreezeMaxCapacity && !player.isPotionActive(RotNTweaker.POTION_FREEZING)) {
                addToFrozenTicks(player, RotNConfig.POTIONS.freezing.playerFreezeMaxCapacity);
                return;
            }

            BlockPos playerPos = player.getPosition();
            NBTTagCompound playerData = player.getEntityData();


            //add to freezing ticks if player is in cold conditions or has ticksCooling, else subtract ticks
            if (RotNTweaker.FREEZING_BIOMES.contains(player.world.getBiome(playerPos)) || RotNTweaker.FREEZING_BLOCKS.contains(player.world.getBlockState(playerPos.down()))) {
                addToFrozenTicks(player, -1*RotNConfig.POTIONS.freezing.playerFreezeUpdateInterval);
            } else {
                addToFrozenTicks(player, RotNConfig.POTIONS.freezing.playerFreezeUpdateInterval);
            }

            //handle player freezing and unfreezing threshold
            if (playerData.getInteger("ticksFrozen") <= 0) {
                player.addPotionEffect(new PotionEffect(RotNTweaker.POTION_FREEZING, 100000, 0));
            } else if (playerData.getInteger("ticksFrozen") < RotNConfig.POTIONS.freezing.playerFreezeMaxCapacity/2) {

                IAttributeInstance speedAttribute = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

                if (speedAttribute.getModifier(SLOWNESS_UUID) == null) {

                    player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_UUID);

                    AttributeModifier modifier = new AttributeModifier(SLOWNESS_UUID, "freezing_slowdown", -RotNConfig.POTIONS.freezing.playerFreezeSlowdown, 2);
                    player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(modifier);

                }
            } else {
                player.removePotionEffect(RotNTweaker.POTION_FREEZING);
                player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SLOWNESS_UUID);
                //Remove incurable freeze vignette potion here
            }
            //System.out.println("DEBUG: Freezing, Cooling, Warming: " + playerData.getInteger("ticksFrozen") + " " + playerData.getInteger("ticksCooling") + " " + playerData.getInteger("ticksWarming"));

        }
    }

    //add to the ticksFrozen counter by addedAmount
    public static void addToFrozenTicks(EntityPlayer player, int addedAmount) {

        NBTTagCompound playerData = player.getEntityData();

        if (playerData.getInteger("ticksWarming") > 0) {
            playerData.setInteger("ticksFrozen", Math.min(playerData.getInteger("ticksFrozen") + RotNConfig.POTIONS.freezing.playerFreezeUpdateInterval *3, RotNConfig.POTIONS.freezing.playerFreezeMaxCapacity));
            playerData.setInteger("ticksWarming", Math.max(playerData.getInteger("ticksWarming") - RotNConfig.POTIONS.freezing.playerFreezeUpdateInterval, 0));
        } else if (playerData.getInteger("ticksCooling") > 0) {
            playerData.setInteger("ticksFrozen", Math.min(Math.max(playerData.getInteger("ticksFrozen") + RotNConfig.POTIONS.freezing.playerFreezeUpdateInterval *-2, 0), RotNConfig.POTIONS.freezing.playerFreezeMaxCapacity));
            playerData.setInteger("ticksCooling", Math.max(playerData.getInteger("ticksCooling") - RotNConfig.POTIONS.freezing.playerFreezeUpdateInterval, 0));
        } else {
            playerData.setInteger("ticksFrozen", Math.min(Math.max(playerData.getInteger("ticksFrozen") + addedAmount, 0), RotNConfig.POTIONS.freezing.playerFreezeMaxCapacity));
        }

        if (player instanceof EntityPlayerMP) {
            RotNTweaker.NETWORK.sendTo(new TicksFrozenMessage(playerData.getInteger("ticksFrozen")), (EntityPlayerMP) player);
        }
    }

    //TODO: Fix fade-out happening randomly?
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {

            int ticksFrozen = player.getEntityData().getInteger("ticksFrozen");
            int maxCapacity = RotNConfig.POTIONS.freezing.playerFreezeMaxCapacity;
            int halfCapacity = maxCapacity / 2;

            //if (player.ticksExisted < maxCapacity) ticksFrozen = maxCapacity;
            //System.out.println(ticksFrozen);

            boolean isBelowHalf = ticksFrozen < halfCapacity;

            PlayerFadeData fadeData = playerFadeDataMap.computeIfAbsent(player, p -> new PlayerFadeData());

            long currentTime = System.nanoTime();

            if (fadeData.fadeStartTime == 0) {
                fadeData.fadeStartTime = currentTime;
            }

            if (isBelowHalf) {
                //Start fade-in
                if (fadeData.wasAboveHalf) {
                    fadeData.fadeStartTime = currentTime;
                    fadeData.wasAboveHalf = false;
                }

                long elapsedTime = (currentTime - fadeData.fadeStartTime) / 1_000_000L;
                float alpha = Math.min(1.0f, (float) elapsedTime / RotNConfig.POTIONS.freezing.playerFreezeFadeIn);

                renderVignette(event, alpha);
            } else {
                //Start fadeout
                if (!fadeData.wasAboveHalf) {
                    //System.out.println("PlayerFreezeTicks on fadeOut = " + ticksFrozen);
                    fadeData.fadeStartTime = currentTime;
                    fadeData.wasAboveHalf = true;
                }

                long elapsedTime = (currentTime - fadeData.fadeStartTime) / 1_000_000L;
                float alpha = Math.max(0.0f, 1.0f - (float) elapsedTime / RotNConfig.POTIONS.freezing.playerFreezeFadeOut);

                if (alpha > 0.0f && player.ticksExisted > 80) {
                    renderVignette(event, alpha);
                }
            }
        }
    }

    private static void renderVignette(RenderGameOverlayEvent.Pre event, float alpha) {
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(1, 1, 1, alpha);
        GlStateManager.translate(0, 0, 301);
        GlStateManager.disableAlpha();

        ClientEventHandler.renderScreenOverlay(event.getResolution(), SCREEN_OVERLAY_TEXTURE);

        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1, 1);
    }

    private static class PlayerFadeData {
        long fadeStartTime = 0;
        boolean wasAboveHalf = true;
    }
}
