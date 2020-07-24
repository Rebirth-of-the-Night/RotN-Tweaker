package com.rebirthofthenight.rotntweaker.modules.betternether.entities.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import com.rebirthofthenight.rotntweaker.modules.betternether.entities.EntityFirefly;
import com.rebirthofthenight.rotntweaker.modules.betternether.entities.models.ModelFirefly;

public class RenderFirefly extends RenderLiving<EntityFirefly>
{
	private ResourceLocation texture;
	
	public RenderFirefly(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelFirefly(), 0.1F);
		texture = new ResourceLocation("betternether", "textures/entities/firefly.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFirefly entity)
	{
		return texture;
	}
}
