package com.rebirthofthenight.rotntweaker;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = RotNTweaker.MODID, name = RotNTweaker.NAME, version = RotNTweaker.VERSION)
public class RotNTweaker
{
    public static final String MODID = "rotntweaker";
    public static final String NAME = "RotN Tweaker";
    public static final String VERSION = "2.78-1";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
