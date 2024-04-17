package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import com.google.common.base.Optional;
import gloomyfolken.hooklib.api.*;
import java.util.*;
import java.util.stream.*;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import nmd.primal.core.common.helper.*;
import nmd.primal.core.common.init.*;

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

    @Hook
    @OnBegin
    public static ReturnSolve<Boolean> isFireSource(FireHelper fireHelper, IBlockAccess world, BlockPos pos, IBlockState state) {
        if (FIRE_SOURCE_BLOCKS().contains(state)) {
            return ReturnSolve.yes(true);
        } else {
            return ReturnSolve.no();
        }
    }

    @Hook
    @OnMethodCall("getBlock")
    public static ReturnSolve<Boolean> hasNearByHeat(FireHelper fireHelper, World world, BlockPos pos, int horizontal, int vertical, boolean include_fire_sources,
                                                     @LocalVariable(id = 7) IBlockState check_state) {
        if (HEAT_SOURCE_BLOCKS().contains(check_state)) {
            return ReturnSolve.yes(true);
        } else {
            return ReturnSolve.no();
        }
    }

    @Hook
    @OnMethodCall("getBlock")
    public static ReturnSolve<Boolean> hasDirectHeat(FireHelper fireHelper, World world, BlockPos pos, EnumFacing facing,
                                                     @LocalVariable(id = 4) IBlockState check_state) {
        if (HEAT_SOURCE_BLOCKS().contains(check_state)) {
            return ReturnSolve.yes(true);
        } else {
            return ReturnSolve.no();
        }
    }


    @Hook
    @OnMethodCall(value = "getBlockFromName", shift = Shift.INSTEAD)
    public static Block getBlockList(CommonUtils commonUtils, String[] array, @LocalVariable(id = 6) java.lang.String name) {
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
