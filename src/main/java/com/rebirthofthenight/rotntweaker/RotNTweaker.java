package com.rebirthofthenight.rotntweaker;

import com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles.ParticleFlame2;
import com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles.ParticleNone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = RotNTweaker.MODID, name = RotNTweaker.NAME, version = RotNTweaker.VERSION)
public class RotNTweaker {

    // -Dlegacy.debugClassLoading=true -Dlegacy.debugClassLoadingSave=true
    public static final String MODID = "rotntweaker";
    public static final String NAME = "RotN Tweaker";
    public static final String VERSION = "0.0.1";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ParticleFlame2.FLAME2 = registerParticle("flame2", new ParticleFlame2.Factory());
        ParticleNone.NONE = registerParticle("none", new ParticleNone.Factory());
    }

    @SideOnly(Side.CLIENT)
    private static EnumParticleTypes registerParticle(String name, IParticleFactory factory) {
        EnumParticleTypes r = EnumHelper.addEnum(EnumParticleTypes.class, name.toUpperCase(),
                new Class[]{String.class, int.class, boolean.class},
                name, EnumParticleTypes.values().length, false
        );
        Minecraft.getMinecraft().effectRenderer.registerParticle(r.getParticleID(), factory);
        return r;
    }
}
