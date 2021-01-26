package com.rebirthofthenight.rotntweaker.config;

import com.rebirthofthenight.rotntweaker.RotNTweaker;

import net.minecraftforge.common.config.Config;

@Config(modid = RotNTweaker.MODID)
public class RotNConfig {
    
    public static final ModuleConfig MODULES = new ModuleConfig();
    public static final TweakConfig TWEAKS = new TweakConfig();

    public static final class ModuleConfig {
        // @Config.Name("BetterNether Tweaked")
        // @Config.RequiresMcRestart
        // @Config.Comment({"Whether to enable the modified version of BetterNether."})
        // public boolean betterNether = true;
    }

    public static final class TweakConfig {
        @Config.Name("Curse of the Final Stand")
        @Config.Comment({"Whether to enable the modifications to the Curse of the Final Stand"})
        public boolean curseFinalStand = true;
    }
}