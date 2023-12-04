package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import com.google.common.base.Optional;
import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.LocalVariable;
import gloomyfolken.hooklib.api.OnBegin;
import gloomyfolken.hooklib.api.OnMethodCall;
import gloomyfolken.hooklib.api.OnReturn;
import gloomyfolken.hooklib.api.PrintLocalVariables;
import gloomyfolken.hooklib.api.ReturnConstant;
import gloomyfolken.hooklib.api.Shift;
import gloomyfolken.hooklib.asm.ReturnCondition;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nmd.primal.core.common.helper.BlockHelper;
import nmd.primal.core.common.helper.CommonUtils;
import nmd.primal.core.common.helper.FireHelper;
import nmd.primal.core.common.init.ModConfig;

@HookContainer
public class PrimalCoreImprovedConfig {

    private static Set<IBlockState> FIRE_SOURCE_BLOCKS;
    private static Set<IBlockState> HEAT_SOURCE_BLOCKS;

    private static Set<IBlockState> FIRE_SOURCE_BLOCKS() {
        if (FIRE_SOURCE_BLOCKS == null) {
            FIRE_SOURCE_BLOCKS = Arrays.stream(ModConfig.flammability.FIRE_SOURCES).flatMap(PrimalCoreImprovedConfig::parseBlockState).collect(Collectors.toSet());
        }
        return FIRE_SOURCE_BLOCKS;
    }

    private static Set<IBlockState> HEAT_SOURCE_BLOCKS() {
        if (HEAT_SOURCE_BLOCKS == null) {
            HEAT_SOURCE_BLOCKS = Arrays.stream(ModConfig.flammability.HEAT_SOURCE_BLOCKS).flatMap(PrimalCoreImprovedConfig::parseBlockState).collect(Collectors.toSet());
        }
        return HEAT_SOURCE_BLOCKS;
    }

    private static Stream<IBlockState> parseBlockState(String s) {
        int propStart = s.indexOf('[');
        Block block = Block.getBlockFromName(propStart > 0 ? s.substring(0, propStart) : s);
        if (block == null || block == Blocks.AIR) {
            return Stream.of();
        } else {
            BlockStateContainer blockStateContainer = block.getBlockState();
            if (propStart > 0) {
                IBlockState state = block.getDefaultState();
                for (String prop : s.substring(propStart + 1, s.length() - 1).split(",")) {

                    int eq = prop.indexOf(':');
                    String propName = prop.substring(0, eq);
                    String propValue = prop.substring(eq + 1);
                    IProperty<? extends Comparable> property = blockStateContainer.getProperty(propName);
                    if (property != null) {
                        state = setValueHelper(state, property, propValue);
                    }
                }
                return Stream.of(state);

            } else {
                return blockStateContainer.getValidStates().stream();

            }
        }
    }

    private static <T extends Comparable<T>> IBlockState setValueHelper(IBlockState base, IProperty<T> property, String propValue) {
        Optional<T> maybeValue = property.parseValue(propValue);
        if (maybeValue.isPresent()) {
            return base.withProperty(property, maybeValue.get());
        } else {
            return base;
        }
    }

    @Hook(returnCondition = ReturnCondition.ON_TRUE, returnConstant = @ReturnConstant(booleanValue = true))
    @OnBegin
    public static boolean isFireSource(FireHelper fireHelper, IBlockAccess world, BlockPos pos, IBlockState state) {
        return FIRE_SOURCE_BLOCKS().contains(state);
    }

    @Hook(returnCondition = ReturnCondition.ON_TRUE, returnConstant = @ReturnConstant(booleanValue = true))
    @OnMethodCall("getBlock")
    public static boolean hasNearByHeat(FireHelper fireHelper, World world, BlockPos pos, int horizontal, int vertical, boolean include_fire_sources,
                                        @LocalVariable(7) IBlockState check_state) {
        return HEAT_SOURCE_BLOCKS().contains(check_state);
    }

    @Hook(returnCondition = ReturnCondition.ON_TRUE, returnConstant = @ReturnConstant(booleanValue = true))
    @OnMethodCall("getBlock")
    public static boolean hasDirectHeat(FireHelper fireHelper, World world, BlockPos pos, EnumFacing facing,
                                        @LocalVariable(4) IBlockState check_state) {
        return HEAT_SOURCE_BLOCKS().contains(check_state);
    }


    @Hook
    @OnMethodCall(value = "getBlockFromName", shift = Shift.INSTEAD)
    public static Block getBlockList(CommonUtils commonUtils, String[] array, @LocalVariable(6) java.lang.String name) {
        int propStart = name.indexOf('[');
        return Block.getBlockFromName(propStart > 0 ? name.substring(0, propStart) : name);
    }

    @Hook
    @OnReturn
    public static void postInit(BlockHelper blockHelper) {
        FireHelper.FIRE_SOURCE_BLOCKS.clear();
        FireHelper.HEAT_SOURCE_BLOCKS.clear();
    }


}
