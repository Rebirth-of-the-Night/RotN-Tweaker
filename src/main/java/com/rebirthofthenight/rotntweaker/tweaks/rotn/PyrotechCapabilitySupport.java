package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import static net.minecraftforge.items.CapabilityItemHandler.*;

import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.*;
import gloomyfolken.hooklib.api.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.*;

@HookContainer
public class PyrotechCapabilitySupport {

    @Hook
    @OnReturn
    public static boolean hasCapability(TileMechanicalMulchSpreader tile, Capability<?> capability, EnumFacing facing, @ReturnValue boolean r) {
        return hasCapabilityResult(capability, facing, r, capabilities.get(tile));
    }

    @Hook
    @OnReturn
    public static boolean hasCapability(TileMechanicalCompactingBinWorker tile, Capability<?> capability, EnumFacing facing, @ReturnValue boolean r) {
        return hasCapabilityResult(capability, facing, r, capabilities.get(tile));
    }

    private static boolean hasCapabilityResult(Capability<?> capability, EnumFacing facing, boolean r, CapabilityDispatcher capabilities) {
        return r || (capabilities != null && capabilities.hasCapability(capability, facing));
    }

    @Hook
    @OnBegin
    public static <T> T getCapability(TileMechanicalMulchSpreader tile, Capability<T> capability, EnumFacing facing) {
        if (capability == ITEM_HANDLER_CAPABILITY && facing == EnumFacing.UP) {
            return ITEM_HANDLER_CAPABILITY.cast(mulchStackHandler.get(tile));
        } else {
            return capabilities.get(tile) == null ? null : capabilities.get(tile).getCapability(capability, facing);
        }
    }

    @FieldLens
    public static FieldAccessor<TileMechanicalMulchSpreader, TileMechanicalMulchSpreader.MulchStackHandler> mulchStackHandler;

    @Hook
    @OnReturn
    public static <T> T getCapability(TileMechanicalCompactingBinWorker tile, Capability<T> capability, EnumFacing facing, @ReturnValue T r) {
        if (r != null) {
            return r;
        } else {
            return capabilities.get(tile) == null ? null : capabilities.get(tile).getCapability(capability, facing);
        }
    }

    @FieldLens
    public static FieldAccessor<TileEntity, CapabilityDispatcher> capabilities;
}
