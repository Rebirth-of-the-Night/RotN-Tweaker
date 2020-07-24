package com.rebirthofthenight.rotntweaker.modules.betternether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.rebirthofthenight.rotntweaker.modules.betternether.blocks.BlockStalagnateBowl;
import com.rebirthofthenight.rotntweaker.modules.betternether.blocks.BlockStalagnateBowl.EnumFood;
import com.rebirthofthenight.rotntweaker.modules.betternether.blocks.BlocksRegister;

public class ItemBowlFood extends ItemSoupStandart
{
	private EnumFood food;
	
	public ItemBowlFood(String name, int amount, EnumFood food)
	{
		super(name, amount);
		this.food = food;
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		pos = pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos, facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else if (player.isSneaking())
        {
            if (BlocksRegister.BLOCK_STALAGNATE_BOWL != Blocks.AIR && worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
            {
                worldIn.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                worldIn.setBlockState(pos, BlocksRegister.BLOCK_STALAGNATE_BOWL.getDefaultState().withProperty(BlockStalagnateBowl.FOOD, this.food), 2);
                if (!player.isCreative())
                	itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }
}
