package com.rebirthofthenight.rotntweaker.content.entity;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderSmeltingVesselProjectile extends RenderSnowball<EntitySmeltingVesselProjectile> {

    public static class Factory implements IRenderFactory<EntitySmeltingVesselProjectile> {
        @Override
        public Render<EntitySmeltingVesselProjectile> createRenderFor(RenderManager manager) {
            return new RenderSmeltingVesselProjectile(manager);
        }
    }

    public RenderSmeltingVesselProjectile(RenderManager renderManager) {
        super(renderManager, RotNTweaker.Items.smeltingVessel, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public ItemStack getStackToRender(EntitySmeltingVesselProjectile vesselProjectile) {
        return new ItemStack(RotNTweaker.Items.smeltingVessel);
    }
}
