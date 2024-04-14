package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import static gloomyfolken.hooklib.api.Shift.INSTEAD;

import betterwithmods.api.*;
import betterwithmods.api.capabilities.*;
import betterwithmods.api.tile.*;
import com.codetaylor.mc.athenaeum.network.tile.data.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.*;
import com.google.common.base.*;
import com.rebirthofthenight.rotntweaker.*;
import gloomyfolken.hooklib.api.*;
import java.util.function.Supplier;
import javax.annotation.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.eventhandler.*;

@HookContainer
@EventBusSubscriber
public class BWM2Pyrotech {

    private static Supplier<ItemStack> fakeCog = Suppliers.memoize(() -> new ItemStack(Items.IRON_COG));

    @SubscribeEvent
    public static void attachCapa(AttachCapabilitiesEvent<TileEntity> event) {
        if (event.getObject() instanceof TileCogWorkerBase) {
            TileCogWorkerBase tile = (TileCogWorkerBase) event.getObject();
            event.addCapability(
                new ResourceLocation(RotNTweaker.MODID, "bwm_power"),
                new MechanicalPowerProvider(tile, tile instanceof TileMechanicalMulchSpreader ? 2 : 1)
            );
        }
    }

    @Hook(targetMethod = "doWork")
    @OnMethodCall(value = "getCog",shift = INSTEAD)
    public static ItemStack useFakeCog(TileMechanicalCompactingBinWorker tile, ItemStack cog) {
        return fakeCog.get();
    }

    @Hook
    @OnMethodCall("getStackInSlot")
    public static void update(TileCogWorkerBase tile) {
        if (TileLense.isPowered(tile)) {
            return;
        }

        IMechanicalPower capa = tile.getCapability(CapabilityMechanicalPower.MECHANICAL_POWER, null);

        if (capa == null) {
            return;
        }

        EnumFacing facing = getCogSide(tile);

        int mechanicalInput = capa.getMechanicalInput(facing);
        if (mechanicalInput < capa.getMinimumInput(facing)) {
            return;
        }

        CustomTickCounter tickCounter = updateTickCounter2.get(tile);

        boolean ready = tickCounter != null && tickCounter.increment(mechanicalInput);

        if (ready) {
            tickCounter.reset();
            int cogDamage = TileLense.doWork(tile, fakeCog.get());
            if (cogDamage >= 0) {
                triggered.get(tile).set(true);
                return;
            }
        }
        triggered.get(tile).set(false);
    }

    @FieldLens(createField = true)
    public static FieldAccessor<TileCogWorkerBase, CustomTickCounter> updateTickCounter2;

    @FieldLens
    public static FieldAccessor<TileCogWorkerBase, TileDataBoolean> triggered;

    private static EnumFacing getCogSide(TileCogWorkerBase tile) {
        World world = tile.getWorld();
        BlockPos pos = tile.getPos();
        EnumFacing tileFacing = tile.getTileFacing(world, pos, world.getBlockState(pos));

        if (tile instanceof TileMechanicalMulchSpreader) {
            return tileFacing.getOpposite();

        } else if (tile instanceof TileMechanicalBellowsTop) {
            return tileFacing.getOpposite();

        } else if (tile instanceof TileMechanicalCompactingBinWorker) {
            return tileFacing.rotateY();

        } else if (tile instanceof TileTripHammer) {
            return tileFacing.rotateYCCW();

        } else {
            return tileFacing;
        }
    }

    public static class MechanicalPowerProvider implements ICapabilityProvider, IMechanicalPower {

        private final TileCogWorkerBase tile;
        private final int checkDistance;

        public MechanicalPowerProvider(TileCogWorkerBase tile, int checkDistance) {
            this.tile = tile;
            this.checkDistance = checkDistance;
        }

        @Override
        public int getMechanicalOutput(EnumFacing facing) {
            return -1;
        }

        @Override
        public int getMechanicalInput(EnumFacing facing) {
            World world = tile.getWorld();
            BlockPos pos = tile.getPos();
            if (facing == getCogSide(tile)) {
                return BWMAPI.IMPLEMENTATION.getPowerOutput(world, pos.offset(facing, checkDistance), facing.getOpposite());
            }
            return 0;
        }

        @Override
        public int getMaximumInput(EnumFacing facing) {
            return 1;
        }

        @Override
        public int getMinimumInput(EnumFacing facing) {
            return 1;
        }

        @Override
        public Block getBlock() {
            return tile.getWorld().getBlockState(tile.getPos()).getBlock();
        }

        @Override
        public World getBlockWorld() {
            return tile.getWorld();
        }

        @Override
        public BlockPos getBlockPos() {
            return tile.getPos();
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityMechanicalPower.MECHANICAL_POWER;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (hasCapability(capability, facing)) {
                return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
            } else {
                return null;
            }
        }
    }

    public static class CustomTickCounter {

        private final int woodMax;
        private final int steelMax;
        private int count;

        public CustomTickCounter(int woodMax, int steelMax) {
            this.woodMax = woodMax;
            this.steelMax = steelMax;
        }

        public void reset() {
            this.count = 0;
        }

        public boolean increment(int power) {
            boolean isWoodPowered = !(power > 2);
            this.count += 1;
            if (this.count >= (isWoodPowered ? woodMax : steelMax)) {
                this.reset();
                return true;
            } else {
                return false;
            }
        }
    }
}
