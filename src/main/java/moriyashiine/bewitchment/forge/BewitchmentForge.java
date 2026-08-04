package moriyashiine.bewitchment.forge;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.api.client.model.BroomEntityModel;
import moriyashiine.bewitchment.client.model.ContributorHornsModel;
import moriyashiine.bewitchment.client.model.entity.living.*;
import moriyashiine.bewitchment.client.model.equipment.armor.WitchArmorModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.*;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.*;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.forge.component.BWComponentEvents;
import moriyashiine.bewitchment.forge.component.BWEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.ChestBoatEntityModel;
import net.minecraft.registry.RegistryKeys;

/** Forge entrypoint which delegates to the existing shared initializers. */
@Mod(Bewitchment.MOD_ID)
public final class BewitchmentForge {

    public BewitchmentForge() {
        // Component keys must all exist before the first entity is constructed,
        // because attaching the capability enumerates them.
        BWComponents.init();
        new Bewitchment().onInitialize();
        // These are Bewitchment's own Fabric-backed registries, not Forge
        // registry events.  Their entries must exist before item-group search
        // indexes request tooltips for the sigil, contract, and curse items.
        BWRitualFunctions.init();
        BWFortunes.init();
        BWSigils.init();
        BWTransformations.init();
        BWContracts.init();
        BWCurses.init();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(BewitchmentForgeModEvents::commonSetup);
        modBus.addListener(BewitchmentForgeModEvents::registerContent);
        modBus.addListener(BewitchmentForgeModEvents::registerEntityAttributes);
        modBus.addListener(BWEntityComponents::registerCapability);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, BWEntityComponents::attach);
        MinecraftForge.EVENT_BUS.addListener(BWComponentEvents::copyOnRespawn);
        MinecraftForge.EVENT_BUS.addListener(BWComponentEvents::syncOnStartTracking);
        MinecraftForge.EVENT_BUS.addListener(BWComponentEvents::syncOnJoin);
        MinecraftForge.EVENT_BUS.addListener(BewitchmentForgeModEvents::registerFuelBurnTimes);
        MinecraftForge.EVENT_BUS.addListener(BewitchmentForgeGameplayEvents::applyVoodooDrowning);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modBus.addListener(BewitchmentForgeClientModEvents::clientSetup);
            modBus.addListener(BewitchmentForgeClientModEvents::registerLayerDefinitions);
        });
    }
}

final class BewitchmentForgeModEvents {

	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(Bewitchment::registerAltarMapEntries);
	}

	public static void registerContent(RegisterEvent event) {
		if (event.getRegistryKey().equals(RegistryKeys.BLOCK)) {
			BWObjects.registerBlocks(event);
		} else if (event.getRegistryKey().equals(RegistryKeys.ITEM)) {
			BWObjects.registerItems(event);
		} else if (event.getRegistryKey().equals(RegistryKeys.ITEM_GROUP)) {
			BWObjects.registerItemGroup(event);
		} else if (event.getRegistryKey().equals(RegistryKeys.BLOCK_ENTITY_TYPE)) {
			moriyashiine.bewitchment.common.registry.BWBlockEntityTypes.init();
		} else if (event.getRegistryKey().equals(RegistryKeys.ENTITY_TYPE)) {
			BWEntityTypes.init();
		} else if (event.getRegistryKey().equals(RegistryKeys.STATUS_EFFECT)) {
			moriyashiine.bewitchment.common.registry.BWStatusEffects.init();
		} else if (event.getRegistryKey().equals(RegistryKeys.SOUND_EVENT)) {
			moriyashiine.bewitchment.common.registry.BWSoundEvents.init();
		} else if (event.getRegistryKey().equals(RegistryKeys.PARTICLE_TYPE)) {
			moriyashiine.bewitchment.common.registry.BWParticleTypes.init();
		} else if (event.getRegistryKey().equals(RegistryKeys.RECIPE_SERIALIZER)) {
			moriyashiine.bewitchment.common.registry.BWRecipeTypes.registerSerializers();
		} else if (event.getRegistryKey().equals(RegistryKeys.RECIPE_TYPE)) {
			moriyashiine.bewitchment.common.registry.BWRecipeTypes.registerTypes();
		} else if (event.getRegistryKey().equals(RegistryKeys.SCREEN_HANDLER)) {
			moriyashiine.bewitchment.common.registry.BWScreenHandlerTypes.init();
		}
	}

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BWEntityTypes.OWL, OwlEntity.createAttributes().build());
        event.put(BWEntityTypes.RAVEN, RavenEntity.createAttributes().build());
        event.put(BWEntityTypes.SNAKE, SnakeEntity.createAttributes().build());
        event.put(BWEntityTypes.TOAD, ToadEntity.createAttributes().build());
        event.put(BWEntityTypes.GHOST, GhostEntity.createAttributes().build());
        event.put(BWEntityTypes.VAMPIRE, VampireEntity.createAttributes().build());
        event.put(BWEntityTypes.WEREWOLF, WerewolfEntity.createAttributes().build());
        event.put(BWEntityTypes.HELLHOUND, HellhoundEntity.createAttributes().build());
        event.put(BWEntityTypes.DEMON, DemonEntity.createAttributes().build());
        event.put(BWEntityTypes.LEONARD, LeonardEntity.createAttributes().build());
        event.put(BWEntityTypes.BAPHOMET, BaphometEntity.createAttributes().build());
        event.put(BWEntityTypes.LILITH, LilithEntity.createAttributes().build());
        event.put(BWEntityTypes.HERNE, HerneEntity.createAttributes().build());
    }

	public static void registerFuelBurnTimes(FurnaceFuelBurnTimeEvent event) {
        int burnTime = BWObjects.getFuelBurnTime(event.getItemStack());
        if (burnTime > 0) event.setBurnTime(burnTime);
	}

}

final class BewitchmentForgeGameplayEvents {

	public static void applyVoodooDrowning(LivingBreatheEvent event) {
		if (BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(event.getEntity()).isSubmerged()) {
			event.setCanBreathe(false);
		}
	}
}

/** Client model definitions must be registered on Forge's model-layer event;
 * the Fabric compatibility callback is dispatched too late by Forge's loader. */
final class BewitchmentForgeClientModEvents {
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> new BewitchmentClient().onInitializeClient());
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		// Sign layers need no registration here: vanilla builds them for every
		// registered WoodType, which by now includes Bewitchment's.  Boat layers
		// are keyed off the closed BoatEntity.Type enum, so they do.
		for (String wood : BewitchmentClient.WOODS) {
			event.registerLayerDefinition(BewitchmentClient.boatModelLayer(wood, false), BoatEntityModel::getTexturedModelData);
			event.registerLayerDefinition(BewitchmentClient.boatModelLayer(wood, true), ChestBoatEntityModel::getTexturedModelData);
		}
		event.registerLayerDefinition(BewitchmentClient.CONTRIBUTOR_HORNS_MODEL_LAYER, ContributorHornsModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER, WitchArmorModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.SPECTER_BANGLE_MODEL_LAYER, SpecterBangleModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.PRICKLY_BELT_MODEL_LAYER, PricklyBeltModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.DRUID_BAND_MODEL_LAYER, DruidBandModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.ZEPHYR_HARNESS_MODEL_LAYER, ZephyrHarnessModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.BROOM_MODEL_LAYER, BroomEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.OWL_MODEL_LAYER, OwlEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.RAVEN_MODEL_LAYER, RavenEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.SNAKE_MODEL_LAYER, SnakeEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.TOAD_MODEL_LAYER, ToadEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.GHOST_MODEL_LAYER, GhostEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.VAMPIRE_MODEL_LAYER, VampireEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.WEREWOLF_MODEL_LAYER, WerewolfEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.HELLHOUND_MODEL_LAYER, HellhoundEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.MALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataMale);
		event.registerLayerDefinition(BewitchmentClient.FEMALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataFemale);
		event.registerLayerDefinition(BewitchmentClient.LEONARD_MODEL_LAYER, LeonardEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.BAPHOMET_MODEL_LAYER, BaphometEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.LILITH_MODEL_LAYER, LilithEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BewitchmentClient.HERNE_MODEL_LAYER, HerneEntityModel::getTexturedModelData);
	}
}
