package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBinWorker;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalMulchSpreader;
import gloomyfolken.hooklib.api.FieldLens;
import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.OnBegin;
import gloomyfolken.hooklib.api.OnReturn;
import gloomyfolken.hooklib.api.ReturnValue;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;

@HookContainer
public class PyrotechCapabilitySupport {

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    @OnReturn
    public static boolean hasCapability(TileMechanicalMulchSpreader tile, Capability<?> capability, EnumFacing facing, @ReturnValue boolean r) {
        return hasCapabilityResult(capability, facing, r, capabilities(tile));
    }

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    @OnReturn
    public static boolean hasCapability(TileMechanicalCompactingBinWorker tile, Capability<?> capability, EnumFacing facing, @ReturnValue boolean r) {
        return hasCapabilityResult(capability, facing, r, capabilities(tile));
    }

    private static boolean hasCapabilityResult(Capability<?> capability, EnumFacing facing, boolean r, CapabilityDispatcher capabilities) {
        return r || (capabilities != null && capabilities.hasCapability(capability, facing));
    }

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    @OnBegin
    public static <T> T getCapability(TileMechanicalMulchSpreader tile, Capability<T> capability, EnumFacing facing) {
        if (capability == ITEM_HANDLER_CAPABILITY && facing == EnumFacing.UP) {
            return ITEM_HANDLER_CAPABILITY.cast(mulchStackHandler(tile));
        } else {
            return capabilities(tile) == null ? null : capabilities(tile).getCapability(capability, facing);
        }
    }

    @FieldLens
    public static TileMechanicalMulchSpreader.MulchStackHandler mulchStackHandler(TileMechanicalMulchSpreader tile) {
        return null;
    }

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    @OnReturn
    public static <T> T getCapability(TileMechanicalCompactingBinWorker tile, Capability<T> capability, EnumFacing facing, @ReturnValue T r) {
        if (r != null) {
            return r;
        } else {
            return capabilities(tile) == null ? null : capabilities(tile).getCapability(capability, facing);
        }
    }

    @FieldLens
    public static CapabilityDispatcher capabilities(TileEntity tile) {
        return null;
    }
}
