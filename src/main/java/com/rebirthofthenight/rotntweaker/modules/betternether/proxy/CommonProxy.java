package com.rebirthofthenight.rotntweaker.modules.betternether.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.rebirthofthenight.rotntweaker.modules.betternether.biomes.BiomeRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.blocks.BlocksRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.config.ConfigLoader;
import com.rebirthofthenight.rotntweaker.modules.betternether.entities.EntityRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.events.EventsHandler;
import com.rebirthofthenight.rotntweaker.modules.betternether.items.ItemsRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.recipes.RecipeRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.sounds.SoundRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.tileentities.TileEntityRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.world.BNWorldGenerator;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		ConfigLoader.load(event.getSuggestedConfigurationFile());
		EventsHandler.init();
		BlocksRegister.register();
		BlocksRegister.registerOreDictionary();
		ItemsRegister.register();
		BiomeRegister.registerBiomes();
		ConfigLoader.postBiomeInit();
		RecipeRegister.register();
		SoundRegister.register();
		EntityRegister.register();
		TileEntityRegister.register();
		MinecraftForge.EVENT_BUS.register(new EventsHandler());
	}

	public void init(FMLInitializationEvent event)
	{
		
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		BNWorldGenerator.updateGenSettings();
		ConfigLoader.dispose();
	}
}