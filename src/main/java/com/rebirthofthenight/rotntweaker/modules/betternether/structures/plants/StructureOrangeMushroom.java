package com.rebirthofthenight.rotntweaker.modules.betternether.structures.plants;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.rebirthofthenight.rotntweaker.modules.betternether.blocks.BlockNetherMycelium;
import com.rebirthofthenight.rotntweaker.modules.betternether.blocks.BlockOrangeMushroom;
import com.rebirthofthenight.rotntweaker.modules.betternether.blocks.BlocksRegister;
import com.rebirthofthenight.rotntweaker.modules.betternether.structures.IStructure;

public class StructureOrangeMushroom implements IStructure
{
	@Override
	public void generate(World world, BlockPos pos, Random random)
	{
		Block under = world.getBlockState(pos).getBlock();
		if (under instanceof BlockNetherMycelium)
		{
			for (int i = 0; i < 10; i++)
			{
				int x = pos.getX() + (int) (random.nextGaussian() * 2);
				int z = pos.getZ() + (int) (random.nextGaussian() * 2);
				int y = pos.getY() + random.nextInt(6);
				for (int j = 0; j < 6; j++)
				{
					BlockPos npos = new BlockPos(x, y - j, z);
					if (npos.getY() > 31)
					{
						under = world.getBlockState(npos.down()).getBlock();
						if (under == BlocksRegister.BLOCK_NETHER_MYCELIUM && world.getBlockState(npos).getBlock() == Blocks.AIR)
						{
							world.setBlockState(npos, BlocksRegister
									.BLOCK_ORANGE_MUSHROOM
									.getDefaultState()
									.withProperty(BlockOrangeMushroom.SIZE, random.nextInt(4)));
						}
					}
					else
						break;
				}
			}
		}
	}
}
