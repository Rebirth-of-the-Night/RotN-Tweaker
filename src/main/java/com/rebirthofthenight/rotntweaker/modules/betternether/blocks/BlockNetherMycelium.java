package com.rebirthofthenight.rotntweaker.modules.betternether.blocks;

import java.util.Random;

import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class BlockNetherMycelium extends BlockNetherrack
{
	public BlockNetherMycelium()
	{
		this.setRegistryName("nether_mycelium");
		this.setUnlocalizedName("nether_mycelium");
		this.setCreativeTab(RotNTweaker.BN_TAB);
		this.setHardness(0.5F);
	}
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);

        if (rand.nextInt(10) == 0)
        {
            worldIn.spawnParticle(EnumParticleTypes.TOWN_AURA, (double)((float)pos.getX() + rand.nextFloat()), (double)((float)pos.getY() + 1.1F), (double)((float)pos.getZ() + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
        }
    }
}
