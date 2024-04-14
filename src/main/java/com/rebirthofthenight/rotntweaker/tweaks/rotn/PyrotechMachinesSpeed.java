package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.*;
import com.rebirthofthenight.rotntweaker.config.*;
import com.rebirthofthenight.rotntweaker.config.RotNConfig.TweakConfig.PyrotechSpeedModifiersConfig.*;
import com.rebirthofthenight.rotntweaker.tweaks.rotn.BWM2Pyrotech.*;
import gloomyfolken.hooklib.api.*;

@HookContainer
public class PyrotechMachinesSpeed {

    private static SteelAxle steelAxleConfig() {
        return RotNConfig.TWEAKS.pyrotech_speed_modifiers.steel_axle;
    }

    private static WoodAxle woodAxleConfig() {
        return RotNConfig.TWEAKS.pyrotech_speed_modifiers.wood_axle;
    }

    @Hook(targetMethod = Constants.CONSTRUCTOR_NAME)
    @OnReturn
    public static void initTickCounter(TileMechanicalBellowsTop tile) {
        BWM2Pyrotech.updateTickCounter2.set(tile, new CustomTickCounter(woodAxleConfig().bellows, steelAxleConfig().bellows));
    }

    @Hook(targetMethod = Constants.CONSTRUCTOR_NAME)
    @OnReturn
    public static void initTickCounter(TileMechanicalCompactingBinWorker tile) {
        BWM2Pyrotech.updateTickCounter2.set(tile, new CustomTickCounter(woodAxleConfig().compactingBin, steelAxleConfig().compactingBin));
    }

    @Hook(targetMethod = Constants.CONSTRUCTOR_NAME)
    @OnReturn
    public static void initTickCounter(TileMechanicalMulchSpreader tile) {
        BWM2Pyrotech.updateTickCounter2.set(tile, new CustomTickCounter(woodAxleConfig().mulchSpreader, steelAxleConfig().mulchSpreader));
    }

    @Hook(targetMethod = Constants.CONSTRUCTOR_NAME)
    @OnReturn
    public static void initTickCounter(TileStoneHopper tile) {
        BWM2Pyrotech.updateTickCounter2.set(tile, new CustomTickCounter(woodAxleConfig().hopper, steelAxleConfig().hopper));
    }

    @Hook(targetMethod = Constants.CONSTRUCTOR_NAME)
    @OnReturn
    public static void initTickCounter(TileTripHammer tile) {
        BWM2Pyrotech.updateTickCounter2.set(tile, new CustomTickCounter(woodAxleConfig().hammer, steelAxleConfig().hammer));
    }

}
