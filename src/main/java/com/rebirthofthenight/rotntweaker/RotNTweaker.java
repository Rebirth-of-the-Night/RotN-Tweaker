package com.rebirthofthenight.rotntweaker;

import com.rebirthofthenight.rotntweaker.tweaks.rotn.*;
import com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles.*;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.*;
import org.apache.logging.log4j.*;

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

        if (Loader.isModLoaded("pyrotech") && Loader.isModLoaded("betterwithmods")) {
            MinecraftForge.EVENT_BUS.register(new BWM2Pyrotech());
        }
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
