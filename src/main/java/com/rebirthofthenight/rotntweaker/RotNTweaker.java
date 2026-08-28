    package com.rebirthofthenight.rotntweaker;

    import com.rebirthofthenight.rotntweaker.config.RotNConfig;
    import com.rebirthofthenight.rotntweaker.content.entity.EntitySmeltingVesselProjectile;
    import com.rebirthofthenight.rotntweaker.content.items.ItemSmeltingVessel;
    import com.rebirthofthenight.rotntweaker.content.player.PlayerTicksFrozen;
    import com.rebirthofthenight.rotntweaker.content.player.PlayerTicksSweltering;
    import com.rebirthofthenight.rotntweaker.content.potions.*;
    import com.rebirthofthenight.rotntweaker.content.potions.compat.HealingDenial;
    import com.rebirthofthenight.rotntweaker.network.KeyPressMessage;
    import com.rebirthofthenight.rotntweaker.network.KeyPressMessageHandler;
    import com.rebirthofthenight.rotntweaker.network.TicksFrozenMessage;
    import com.rebirthofthenight.rotntweaker.network.TicksFrozenMessageHandler;
    import com.rebirthofthenight.rotntweaker.tweaks.rotn.*;
    import com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles.*;
    import net.minecraft.block.state.IBlockState;
    import net.minecraft.item.Item;
    import net.minecraft.potion.Potion;
    import net.minecraft.util.*;
    import net.minecraft.world.biome.Biome;
    import net.minecraftforge.common.*;
    import net.minecraftforge.common.config.Config;
    import net.minecraftforge.common.config.ConfigManager;
    import net.minecraftforge.common.util.*;
    import net.minecraftforge.event.RegistryEvent;
    import net.minecraftforge.fml.common.*;
    import net.minecraftforge.fml.common.Mod.*;
    import net.minecraftforge.fml.common.event.*;
    import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
    import net.minecraftforge.fml.common.network.NetworkRegistry;
    import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
    import net.minecraftforge.fml.common.registry.EntityEntry;
    import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
    import net.minecraftforge.fml.common.registry.ForgeRegistries;
    import net.minecraftforge.fml.common.registry.GameRegistry;
    import net.minecraftforge.fml.relauncher.*;
    import org.apache.logging.log4j.*;

    import java.util.HashMap;
    import java.util.HashSet;
    import java.util.List;

    @Mod.EventBusSubscriber
    @Mod(modid = RotNTweaker.MODID, name = RotNTweaker.NAME, version = RotNTweaker.VERSION, useMetadata = true)
    public class RotNTweaker {

        // -Dlegacy.debugClassLoading=true -Dlegacy.debugClassLoadingSave=true
        public static final String MODID = "rotntweaker";
        public static final String NAME = "RotN Tweaker";
        public static final String VERSION = RotNTweakerConsts.VERSION;

        public static Logger logger;
        public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

        @SidedProxy(
                clientSide = "com.rebirthofthenight.rotntweaker.ClientProxy",
                serverSide = "com.rebirthofthenight.rotntweaker.CommonProxy"
        )
        public static CommonProxy sidedProxy;

        public static final Potion POTION_SOULSTREAM = new PotionSoulStream().setRegistryName(MODID, "soul_stream");
        public static final Potion POTION_CLEAN = new PotionClean().setRegistryName(MODID, "clean");
        public static final Potion POTION_WEBBED = new PotionWebbed().setRegistryName(MODID, "webbed");
        public static final Potion POTION_CARDIAC_ARREST = new PotionCardiacArrest().setRegistryName(MODID, "cardiac_arrest");
        public static final Potion POTION_STRONG_HEART = new PotionStrongHeart().setRegistryName(MODID, "strong_heart");
        public static final Potion POTION_SWELTERING = new PotionSweltering().setRegistryName(MODID, "sweltering");
        public static final Potion POTION_SCORCHING = new PotionScorching().setRegistryName(MODID, "scorching");
        public static final Potion POTION_GALE_BARRIER = new PotionGaleBarrier().setRegistryName(MODID, "gale_barrier");
        public static final Potion POTION_ACTIVATE_COOLING = new PotionActivateCooling().setRegistryName(MODID, "activate_cooling");
        public static final Potion POTION_ACTIVATE_WARMING = new PotionActivateWarming().setRegistryName(MODID, "activate_warming");
        public static final Potion POTION_FREEZING = new PotionFreezing().setRegistryName(MODID, "freezing");

        public static HashSet<Potion> CLEAN_REMOVED_POTIONS = new HashSet<>();
        public static HashSet<Biome> FREEZING_BIOMES = new HashSet<>();
        public static HashSet<IBlockState> FREEZING_BLOCKS = new HashSet<>();
        //Biome -> miny, maxy, duration, amplifier
        public static HashMap<Biome, List<Integer>> SWELTERING_BIOME_DATA = RotNConfig.createBiomeToIntsMap(RotNConfig.POTIONS.sweltering.playerSwelteringBiomes, 4);


        @EventHandler
        public void preInit(FMLPreInitializationEvent event) {
            MinecraftForge.EVENT_BUS.register(sidedProxy);
            MinecraftForge.EVENT_BUS.register(PotionClean.class);
            MinecraftForge.EVENT_BUS.register(PotionWebbed.class);
            MinecraftForge.EVENT_BUS.register(PotionCardiacArrest.class);
            MinecraftForge.EVENT_BUS.register(PotionSweltering.class);
            MinecraftForge.EVENT_BUS.register(PotionScorching.class);
            MinecraftForge.EVENT_BUS.register(PotionFreezing.class);
            MinecraftForge.EVENT_BUS.register(PlayerTicksFrozen.class);
            MinecraftForge.EVENT_BUS.register(PlayerTicksSweltering.class);
            MinecraftForge.EVENT_BUS.register(WaterBottleEffect.class);

            MinecraftForge.EVENT_BUS.register(new PotionSoulStream());
            if (Loader.isModLoaded("applecore")) {
                MinecraftForge.EVENT_BUS.register(new HealingDenial());
            }

            logger = event.getModLog();
            ConfigManager.sync(RotNTweaker.MODID, Config.Type.INSTANCE);
            sidedProxy.onPreInit(event);
        }

        @EventHandler
        public void init(FMLInitializationEvent event) {
            sidedProxy.onInit(event);

            NETWORK.registerMessage(KeyPressMessageHandler.class, KeyPressMessage.class, 0, Side.SERVER);

            NETWORK.registerMessage(TicksFrozenMessageHandler.class, TicksFrozenMessage.class, 1, Side.CLIENT);
        }

        @EventHandler
        public void postInit(FMLPostInitializationEvent event) {
            sidedProxy.onPostInit(event);
            //System.out.println(Arrays.toString(RotNConfig.POTIONS.cleanRemovedPotion));
            CLEAN_REMOVED_POTIONS = RotNConfig.createPotionSet(RotNConfig.POTIONS.clean.cleanRemovedPotion);
            FREEZING_BIOMES = RotNConfig.createBiomeSet(RotNConfig.POTIONS.freezing.playerFreezeBiomes);
            FREEZING_BLOCKS = RotNConfig.createBlockstateMap(RotNConfig.POTIONS.freezing.playerFreezeBlocks);
            SWELTERING_BIOME_DATA = RotNConfig.createBiomeToIntsMap(RotNConfig.POTIONS.sweltering.playerSwelteringBiomes, 4);

            if (Loader.isModLoaded("pyrotech") && Loader.isModLoaded("betterwithmods")) {
                MinecraftForge.EVENT_BUS.register(new BWM2Pyrotech());
            }
        }

        @SubscribeEvent
        public static void registerPotion(RegistryEvent.Register<Potion> event)
        {
            ForgeRegistries.POTIONS.register(POTION_SOULSTREAM);
            ForgeRegistries.POTIONS.register(POTION_CLEAN);
            ForgeRegistries.POTIONS.register(POTION_WEBBED);
            ForgeRegistries.POTIONS.register(POTION_CARDIAC_ARREST);
            ForgeRegistries.POTIONS.register(POTION_STRONG_HEART);
            ForgeRegistries.POTIONS.register(POTION_SWELTERING);
            ForgeRegistries.POTIONS.register(POTION_SCORCHING);
            ForgeRegistries.POTIONS.register(POTION_GALE_BARRIER);
            ForgeRegistries.POTIONS.register(POTION_ACTIVATE_COOLING);
            ForgeRegistries.POTIONS.register(POTION_ACTIVATE_WARMING);
            ForgeRegistries.POTIONS.register(POTION_FREEZING);
        }

        @GameRegistry.ObjectHolder(MODID)
        public static class Items {
            public static final Item smeltingVessel = new ItemSmeltingVessel().setRegistryName(MODID, "smelting_vessel").setUnlocalizedName(MODID + ".smelting_vessel");
        }

        @Mod.EventBusSubscriber
        public static class ObjectRegistryHandler {
            @SubscribeEvent
            public static void addItems(RegistryEvent.Register<Item> event) {

                event.getRegistry().register(Items.smeltingVessel);
            }
        }


        @SubscribeEvent
        public static void onRegisterEntities(RegistryEvent.Register<EntityEntry> event) {
            event.getRegistry().register(
                    EntityEntryBuilder.create()
                            .entity(EntitySmeltingVesselProjectile.class)
                            .id(new ResourceLocation(MODID), 0)
                            .name("smelting_vessel_thrown")
                            .tracker(64, 1, true)
                            .build()
            );
        }
    }
