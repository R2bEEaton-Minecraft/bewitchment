package moriyashiine.bewitchment.forge;

import moriyashiine.bewitchment.api.client.model.BroomEntityModel;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.ContributorHornsModel;
import moriyashiine.bewitchment.client.model.entity.living.*;
import moriyashiine.bewitchment.client.model.equipment.armor.WitchArmorModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Registers model layers during Forge's required pre-renderer phase. */
@Mod.EventBusSubscriber(modid = "bewitchment", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class BewitchmentForgeClientEvents {
	private BewitchmentForgeClientEvents() {}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
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
