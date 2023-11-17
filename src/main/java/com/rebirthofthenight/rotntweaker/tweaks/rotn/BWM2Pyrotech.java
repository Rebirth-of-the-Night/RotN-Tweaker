package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import betterwithmods.api.BWMAPI;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataBoolean;
import com.codetaylor.mc.athenaeum.util.TickCounter;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine.Items;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalBellowsTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBinWorker;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalMulchSpreader;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileTripHammer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileLense;
import com.google.common.base.Suppliers;
import com.rebirthofthenight.rotntweaker.RotNTweaker;
import gloomyfolken.hooklib.api.FieldLens;
import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.OnMethodCall;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

        if (capa.getMechanicalInput(facing) < capa.getMinimumInput(facing)) {
            return;
        }

        TickCounter tickCounter = updateTickCounter(tile);
        boolean ready = tickCounter != null && tickCounter.increment();

        if (ready) {
            tickCounter.reset();
            int cogDamage = TileLense.doWork(tile, fakeCog.get());
            if (cogDamage >= 0) {
                triggered(tile).set(true);
                return;
            }
        }
        triggered(tile).set(false);
    }

    @FieldLens
    public static TickCounter updateTickCounter(TileCogWorkerBase tile) {
        return null;
    }

    @FieldLens
    public static TileDataBoolean triggered(TileCogWorkerBase tile) {
        return null;
    }

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
}
