package com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles;

import com.rebirthofthenight.rotntweaker.config.RotNConfig;
import glaretorch.BlockGlareTorch;
import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.OnExpression;
import gloomyfolken.hooklib.api.OnMethodCall;
import gloomyfolken.hooklib.api.Shift;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nmd.primal.core.common.compat.vanilla.VanillaTorchBlock;

@HookContainer
@SideOnly(Side.CLIENT)
public class PrimalCore {

    @Hook(targetMethod = "randomDisplayTick")
    @OnExpression(expressionPattern = "replaceFlamePattern", shift = Shift.INSTEAD)
    public static EnumParticleTypes replaceFlame(VanillaTorchBlock torch, IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        return RotNConfig.TWEAKS.torchParticles.vanilla ? ParticleFlame2.FLAME2 : EnumParticleTypes.FLAME;
    }

    public static EnumParticleTypes replaceFlamePattern() {
        return EnumParticleTypes.FLAME;
    }

    @Hook(targetMethod = "randomDisplayTick")
    @OnExpression(expressionPattern = "removeSmokePattern", shift = Shift.INSTEAD)
    public static EnumParticleTypes removeSmoke(VanillaTorchBlock torch, IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        return RotNConfig.TWEAKS.torchParticles.vanilla ? ParticleNone.NONE : EnumParticleTypes.SMOKE_NORMAL;
    }

    public static EnumParticleTypes removeSmokePattern() {
        return EnumParticleTypes.SMOKE_NORMAL;
    }
}
