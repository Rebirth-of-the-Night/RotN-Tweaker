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

        // @Config.Name("Curse of the Final Stand")
        // @Config.Comment({"Whether to enable the modifications to the Curse of the Final Stand"})
        public boolean curseFinalStand = false;

        @Config.Name("Remove Nameplates")
        @Config.Comment({"Whether to remove Nameplates"})
        public boolean noNameplates = true;

        @Config.Comment({"if true, torch flame particles will be replaced from (0,24) to (16,24) texture coords"})
        public TorchParticleConfig torchParticles = new TorchParticleConfig();

        public static final class TorchParticleConfig {

            @Config.Comment({"if true, vanilla torch flame particles will be replaced"})
            public boolean vanilla = true;

            @Config.Comment({"if true, GlareTorch torch flame particles will be replaced"})
            public boolean glareTorch = true;

            @Config.Comment({"if true, Quark candle flame particles will be replaced"})
            public boolean quark = true;

            @Config.Comment({"if true, Rustic candle flame particles will be replaced"})
            public boolean rustic = true;
        }

    }
}
