package com.rebirthofthenight.rotntweaker.modules.betternether.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.rebirthofthenight.rotntweaker.modules.betternether.blocks.BlocksRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.entities.render.EntityRenderRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.items.ItemsRegister;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		EntityRenderRegister.register();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		BlocksRegister.registerRender();
		ItemsRegister.registerRender();
		//EntityRenderRegister.register();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
}