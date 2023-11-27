package com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles;

import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.OnExpression;
import gloomyfolken.hooklib.api.Shift;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.decoration.block.BlockCandle;

@HookContainer
@SideOnly(Side.CLIENT)
public class Quark {

    @Hook
    @OnExpression(expressionPattern = "randomDisplayTickPattern", shift = Shift.INSTEAD)
    public static EnumParticleTypes randomDisplayTick(BlockCandle torch, IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        return RotNConfig.TWEAKS.torchParticles.quark ? ParticleFlame2.FLAME2 : EnumParticleTypes.FLAME;
    }

    public static EnumParticleTypes randomDisplayTickPattern() {
        return EnumParticleTypes.FLAME;
    }
}
