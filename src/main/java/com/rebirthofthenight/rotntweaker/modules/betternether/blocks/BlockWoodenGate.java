package com.rebirthofthenight.rotntweaker.modules.betternether.blocks;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class BlockWoodenGate extends BlockFenceGate
{
	public BlockWoodenGate(String name)
	{
		super(BlockPlanks.EnumType.OAK);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setSoundType(SoundType.WOOD);
		this.setHardness(2.0F);
		this.setCreativeTab(RotNTweaker.BN_TAB);
	}
}
