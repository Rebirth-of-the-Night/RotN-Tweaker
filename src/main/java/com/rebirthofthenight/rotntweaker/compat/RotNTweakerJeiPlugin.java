package com.rebirthofthenight.rotntweaker.compat;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class RotNTweakerJeiPlugin implements IModPlugin {
    @Override
    public void registerItemSubtypes(final ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(RotNTweaker.Items.smeltingVessel);
    }
}
