package com.rebirthofthenight.rotntweaker;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.rebirthofthenight.rotntweaker.modules.betternether.proxy.CommonProxy;
import com.rebirthofthenight.rotntweaker.modules.betternether.tab.CreativeTab;

import org.apache.logging.log4j.Logger;

@Mod(modid = RotNTweaker.MODID, name = RotNTweaker.NAME, version = RotNTweaker.VERSION)
public class RotNTweaker
{
    public static final String MODID = "rotntweaker";
    public static final String NAME = "RotN Tweaker";
    public static final String VERSION = "2.78-1";

    public static final CreativeTabs BN_TAB = new CreativeTab();
    @SidedProxy(clientSide = "com.rebirthofthenight.rotntweaker.modules.betternether.proxy.ClientProxy", serverSide = "com.rebirthofthenight.rotntweaker.modules.betternether.proxy.CommonProxy")
    public static CommonProxy proxy;
    private static Object mod;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        mod = this;
        proxy.preInit(event);

        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static Object getMod() {
        return mod;
    }
}
