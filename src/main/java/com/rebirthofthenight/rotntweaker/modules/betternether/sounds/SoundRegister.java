package com.rebirthofthenight.rotntweaker.modules.betternether.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundRegister
{
	public static final SoundEvent FLY_SOUND = new SoundEvent(new ResourceLocation("rotntweaker", "mob.firefly.fly")).setRegistryName("bn_fly");
	
	public static void register()
	{
		ForgeRegistries.SOUND_EVENTS.register(FLY_SOUND);
	}
}
