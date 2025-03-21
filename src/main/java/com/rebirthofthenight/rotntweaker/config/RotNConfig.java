package com.rebirthofthenight.rotntweaker.config;

import com.rebirthofthenight.rotntweaker.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;

@Config(modid = RotNTweaker.MODID)
@Mod.EventBusSubscriber
public class RotNConfig {

    public static final ModuleConfig MODULES = new ModuleConfig();
    public static final TweakConfig TWEAKS = new TweakConfig();
    public static PotionConfig POTIONS = new PotionConfig();


    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(RotNTweaker.MODID)) {
            ConfigManager.sync(RotNTweaker.MODID, Config.Type.INSTANCE);

        }
    }


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

        @Config.Comment({
            "Intervals at which Pyrotech machines work (every x ticks) when powered by BWM/BWE axles",
            "These options override correpsonding options in the Pyrotech config, e.g. at",
            "config/pyrotech/module.tech.Machine.cfg#MECHANICAL_COMPACTING_BIN#WORK_INTERVAL_TICKS"
        })
        public PyrotechSpeedModifiersConfig pyrotech_speed_modifiers = new PyrotechSpeedModifiersConfig();

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

        public static final class PyrotechSpeedModifiersConfig {

            public WoodAxle wood_axle = new WoodAxle();
            public SteelAxle steel_axle = new SteelAxle();


            public static final class WoodAxle {

                public int bellows = 110;
                public int compactingBin = 40;
                public int mulchSpreader = 200;
                public int hopper = 40;
                public int hammer = 100;
            }

            public static final class SteelAxle {

                public int bellows = 55;
                public int compactingBin = 20;
                public int mulchSpreader = 100;
                public int hopper = 20;
                public int hammer = 50;
            }
        }
    }
    public static class PotionConfig {

        public Clean clean = new Clean();
        public Freezing freezing = new Freezing();
        public Sweltering sweltering = new Sweltering();
        public Scorching scorching = new Scorching();
        public Webbed webbed = new Webbed();
        public Cooling cooling = new Cooling();
        public Warming warming = new Warming();
        public WaterBottleTweak waterBottleTweak = new WaterBottleTweak();

        public static final class Clean {
            @Config.Comment({"List of Potions preemptively cured by Clean"})
            public String[] cleanRemovedPotion = new String[]{"minecraft:poison", "minecraft:nausea","rats:plague"};
        }

        public static final class Freezing {
            @Config.Comment({"List of Biomes where the player will lose temperature"})
            @Config.RequiresMcRestart
            public String[] playerFreezeBiomes = new String[]{"minecraft:taiga_cold", "minecraft:taiga_cold_hills"};
            @Config.Comment({"List of Blocks+Metadata where the player will lose temperature from standing on"})
            @Config.RequiresMcRestart
            public String[] playerFreezeBlocks = new String[]{"minecraft:ice","minecraft:snow"};
            public int freezeDamageCooldown = 40;
            public int playerFreezeUpdateInterval = 20;
            public int playerFreezeMaxCapacity = 400;
            public double playerFreezeSlowdown = 0.1;
            public float playerFreezeFadeIn = 3000.0f;
            public float playerFreezeFadeOut = 3000.0f;
        }

        public static final class Sweltering {
            @Config.Comment({"List of Biome+MinY+MaxY+Duration+Amp that trigger Sweltering"})
            @Config.RequiresMcRestart
            public String[] playerSwelteringBiomes = new String[]{"minecraft:mesa,1,100,200,0","minecraft:mesa_clear_rock,50,100,100,1"};
            public float swelteringFadeIn = 5000.0f;
            public float swelteringFadeOut = 2000.0f;
        }

        public static final class Scorching {
            public int scorchingInterval = 30;
            public int scorchingHealthDrainCyclesNeeded = 1;
            public float scorchingFadeIn = 2000.0f;
            public float scorchingFadeOut = 2000.0f;
        }

        public static final class Webbed {
            public int webbedStruggleCount = 25;
            public int webbedGraceTicks = 200;
            public double webbedMaxSlowdown=0.75;
            public double webbedSlowDownPerLevel=0.25;

            public boolean webbedBarEnabled = true;
            public int webbedBarXOffset = 0;
            public int webbedBarYOffset = 0;
            public boolean webbedTextEnabled = true;
            public int webbedTextXOffset = 0;
            public int webbedTextYOffset = 0;
            public boolean webbedWasdIconEnabled = true;
            public int webbedWasdIconXOffset = 0;
            public int webbedWasdIconYOffset = 0;
            public float webbedFadeIn = 90.0f;
            public float webbedFadeOut = 90.0f;
        }

        public static final class Cooling {
            public int coolingAmplifierMultiplier=20;

        }

        public static final class Warming {
            public int warmingAmplifierMultiplier=20;
        }

        public static final class WaterBottleTweak {
            @Config.RequiresMcRestart
            public String waterBottlePotion = "rotntweaker:sweltering";
            public int waterBottleDuration = 1200;
            public int waterBottleAmp = 1;
        }

        public boolean heartsRenderSingleRow = false;

    }

    public static HashSet<Potion> createPotionSet(String[] inputConfig) {
        HashSet<Potion> potionSet = new HashSet<>();
        for (String string : inputConfig) {
            Potion possiblePotion = Potion.getPotionFromResourceLocation(string);
            if (possiblePotion != null) {
                potionSet.add(possiblePotion);
            } else {
                System.out.println("INVALID POTION: " + string);
            }
        }
        return potionSet;
    }

    public static HashSet<Biome> createBiomeSet(String[] inputConfig) {
        HashSet<Biome> biomeSet = new HashSet<>();
        for (String string : inputConfig) {
            Biome possibleBiome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(string));
            if (possibleBiome != null) biomeSet.add(possibleBiome);
        }

        return biomeSet;
    }

    public static HashSet<IBlockState> createBlockstateMap(String[] inputConfig) {
        HashSet<IBlockState> blockStateMap = new HashSet<>();
        for (String string : inputConfig) {
            String[] splitStrings = string.split(",");
            if (splitStrings.length > 2) continue;

            Block block = Block.getBlockFromName(splitStrings[0]);
            if (splitStrings.length > 1) {
                IBlockState blockState = block.getStateFromMeta(Integer.parseInt(splitStrings[1]));
                blockStateMap.add(blockState);
            } else {
                blockStateMap.add(block.getDefaultState());
            }
        }

        return blockStateMap;
    }

    public static HashMap<Biome, List<Integer>> createBiomeToIntsMap(String[] inputConfig, int numInts) {
        HashMap<Biome, List<Integer>> swelteringMap = new HashMap<>();
        for (String string : inputConfig) {
            String[] splitStrings = string.split(",");
            if (splitStrings.length != numInts+1) continue;
            Biome possibleBiome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(splitStrings[0]));
            if (possibleBiome == null) {
                System.out.println("INVALID BIOME: " + splitStrings[0]);
                continue;
            }
            List<Integer> intList = new ArrayList<>();

            for (String moreStrings : Arrays.copyOfRange(splitStrings, 1, splitStrings.length)) {
                intList.add(Integer.parseInt(moreStrings.trim()));
            }
            swelteringMap.put(possibleBiome, intList);
        }
        return swelteringMap;
    }
}
