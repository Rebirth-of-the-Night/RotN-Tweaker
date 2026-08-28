package com.rebirthofthenight.rotntweaker;

import com.rebirthofthenight.rotntweaker.content.entity.EntitySmeltingVesselProjectile;
import com.rebirthofthenight.rotntweaker.content.entity.RenderSmeltingVesselProjectile;
import com.rebirthofthenight.rotntweaker.content.player.InputHandler;
import com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles.ParticleFlame2;
import com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles.ParticleNone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
    @Override
    public void onInit(final FMLInitializationEvent event) {
        super.onInit(event);
        MinecraftForge.EVENT_BUS.register(new InputHandler());
    }

    @Override
    public void onPostInit(final FMLPostInitializationEvent event) {
        super.onPostInit(event);
        ParticleFlame2.FLAME2 = registerParticle("flame2", new ParticleFlame2.Factory());
        ParticleNone.NONE = registerParticle("none", new ParticleNone.Factory());
    }

    private static EnumParticleTypes registerParticle(String name, IParticleFactory factory) {
        EnumParticleTypes r = EnumHelper.addEnum(EnumParticleTypes.class, name.toUpperCase(),
                new Class[] {String.class, int.class, boolean.class},
                name, EnumParticleTypes.values().length, false
        );
        Minecraft.getMinecraft().effectRenderer.registerParticle(r.getParticleID(), factory);
        return r;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerRenderers(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntitySmeltingVesselProjectile.class, RenderSmeltingVesselProjectile::new);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerTextures(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(RotNTweaker.Items.smeltingVessel, 0, new ModelResourceLocation(RotNTweaker.Items.smeltingVessel.getRegistryName(), "inventory"));
    }
}
