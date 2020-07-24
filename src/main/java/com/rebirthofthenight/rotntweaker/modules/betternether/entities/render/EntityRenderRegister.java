package com.rebirthofthenight.rotntweaker.modules.betternether.entities.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import com.rebirthofthenight.rotntweaker.modules.betternether.entities.EntityFirefly;

public class EntityRenderRegister
{
	public static void register()
	{
		IRenderFactory<EntityFirefly> renderFactory = new IRenderFactory<EntityFirefly>()
		{
			@Override
			public Render<? super EntityFirefly> createRenderFor(RenderManager manager)
			{
				return new RenderFirefly(manager);
			}
		};
		RenderingRegistry.registerEntityRenderingHandler(EntityFirefly.class, renderFactory);
	}
}
