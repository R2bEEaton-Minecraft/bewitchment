/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.BWBoatEntity;
import moriyashiine.bewitchment.common.entity.BWChestBoatEntity;
import moriyashiine.bewitchment.common.entity.DragonsBloodBroomEntity;
import moriyashiine.bewitchment.common.entity.ElderBroomEntity;
import moriyashiine.bewitchment.common.entity.JuniperBroomEntity;
import moriyashiine.bewitchment.common.entity.living.*;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWEntityTypes {
	private static final Map<EntityType<?>, Identifier> ENTITY_TYPES = new LinkedHashMap<>();

	public static final EntityType<JuniperBroomEntity> JUNIPER_BROOM = create("juniper_broom", builder(JuniperBroomEntity::new, SpawnGroup.MISC, EntityType.ARROW.getDimensions()).build(Bewitchment.id("juniper_broom").toString()));
	public static final EntityType<BroomEntity> CYPRESS_BROOM = create("cypress_broom", builder(BroomEntity::new, SpawnGroup.MISC, JUNIPER_BROOM.getDimensions()).build(Bewitchment.id("cypress_broom").toString()));
	public static final EntityType<ElderBroomEntity> ELDER_BROOM = create("elder_broom", builder(ElderBroomEntity::new, SpawnGroup.MISC, JUNIPER_BROOM.getDimensions()).build(Bewitchment.id("elder_broom").toString()));
	public static final EntityType<DragonsBloodBroomEntity> DRAGONS_BLOOD_BROOM = create("dragons_blood_broom", builder(DragonsBloodBroomEntity::new, SpawnGroup.MISC, JUNIPER_BROOM.getDimensions()).build(Bewitchment.id("dragons_blood_broom").toString()));

	public static final EntityType<BWBoatEntity> JUNIPER_BOAT = boat("juniper_boat");
	public static final EntityType<BWBoatEntity> CYPRESS_BOAT = boat("cypress_boat");
	public static final EntityType<BWBoatEntity> ELDER_BOAT = boat("elder_boat");
	public static final EntityType<BWBoatEntity> DRAGONS_BLOOD_BOAT = boat("dragons_blood_boat");

	public static final EntityType<BWChestBoatEntity> JUNIPER_CHEST_BOAT = chestBoat("juniper_chest_boat");
	public static final EntityType<BWChestBoatEntity> CYPRESS_CHEST_BOAT = chestBoat("cypress_chest_boat");
	public static final EntityType<BWChestBoatEntity> ELDER_CHEST_BOAT = chestBoat("elder_chest_boat");
	public static final EntityType<BWChestBoatEntity> DRAGONS_BLOOD_CHEST_BOAT = chestBoat("dragons_blood_chest_boat");

	public static final EntityType<SilverArrowEntity> SILVER_ARROW = create("silver_arrow", BWEntityTypes.<SilverArrowEntity>builder(SilverArrowEntity::new, SpawnGroup.MISC, EntityType.ARROW.getDimensions()).build(Bewitchment.id("silver_arrow").toString()));
	public static final EntityType<HornedSpearEntity> HORNED_SPEAR = create("horned_spear", BWEntityTypes.<HornedSpearEntity>builder(HornedSpearEntity::new, SpawnGroup.MISC, EntityType.TRIDENT.getDimensions()).build(Bewitchment.id("horned_spear").toString()));

	public static final EntityType<OwlEntity> OWL = create("owl", mob(OwlEntity::new, SpawnGroup.CREATURE, EntityDimensions.fixed(0.5f, 0.75f), false).build(Bewitchment.id("owl").toString()));
	public static final EntityType<RavenEntity> RAVEN = create("raven", mob(RavenEntity::new, SpawnGroup.CREATURE, EntityDimensions.fixed(0.4f, 0.4f), false).build(Bewitchment.id("raven").toString()));
	public static final EntityType<SnakeEntity> SNAKE = create("snake", mob(SnakeEntity::new, SpawnGroup.CREATURE, EntityDimensions.fixed(0.75f, 0.25f), false).build(Bewitchment.id("snake").toString()));
	public static final EntityType<ToadEntity> TOAD = create("toad", mob(ToadEntity::new, SpawnGroup.CREATURE, EntityDimensions.fixed(0.5f, 0.5f), false).build(Bewitchment.id("toad").toString()));

	public static final EntityType<GhostEntity> GHOST = create("ghost", mob(GhostEntity::new, SpawnGroup.MONSTER, EntityDimensions.fixed(0.6f, 1.8f), true).build(Bewitchment.id("ghost").toString()));
	public static final EntityType<VampireEntity> VAMPIRE = create("vampire", mob(VampireEntity::new, SpawnGroup.MONSTER, EntityType.PILLAGER.getDimensions(), false).build(Bewitchment.id("vampire").toString()));
	public static final EntityType<WerewolfEntity> WEREWOLF = create("werewolf", mob(WerewolfEntity::new, SpawnGroup.MONSTER, EntityDimensions.fixed(0.8f, 2.8f), false).build(Bewitchment.id("werewolf").toString()));
	public static final EntityType<HellhoundEntity> HELLHOUND = create("hellhound", mob(HellhoundEntity::new, SpawnGroup.MONSTER, EntityType.WOLF.getDimensions(), true).build(Bewitchment.id("hellhound").toString()));
	public static final EntityType<DemonEntity> DEMON = create("demon", mob(DemonEntity::new, SpawnGroup.MONSTER, EntityDimensions.fixed(0.8f, 2.4f), true).build(Bewitchment.id("demon").toString()));

	public static final EntityType<LeonardEntity> LEONARD = create("leonard", mob(LeonardEntity::new, SpawnGroup.MONSTER, EntityDimensions.fixed(0.8f, 2.8f), true).build(Bewitchment.id("leonard").toString()));
	public static final EntityType<BaphometEntity> BAPHOMET = create("baphomet", mob(BaphometEntity::new, SpawnGroup.MONSTER, EntityDimensions.fixed(0.8f, 2.8f), true).build(Bewitchment.id("baphomet").toString()));
	public static final EntityType<LilithEntity> LILITH = create("lilith", mob(LilithEntity::new, SpawnGroup.MONSTER, EntityDimensions.fixed(0.8f, 2.8f), true).build(Bewitchment.id("lilith").toString()));
	public static final EntityType<HerneEntity> HERNE = create("herne", mob(HerneEntity::new, SpawnGroup.MONSTER, EntityDimensions.fixed(0.8f, 2.8f), true).build(Bewitchment.id("herne").toString()));

	private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
		ENTITY_TYPES.put(type, Bewitchment.id(name));
		return type;
	}

	private static <T extends Entity> EntityType.Builder<T> builder(EntityType.EntityFactory<T> factory, SpawnGroup group, EntityDimensions dimensions) {
		return EntityType.Builder.create(factory, group).setDimensions(dimensions.width, dimensions.height);
	}

	private static EntityType<BWBoatEntity> boat(String name) {
		return create(name, EntityType.Builder.<BWBoatEntity>create(BWBoatEntity::new, SpawnGroup.MISC).setDimensions(1.375f, 0.5625f).maxTrackingRange(10).build(Bewitchment.id(name).toString()));
	}

	private static EntityType<BWChestBoatEntity> chestBoat(String name) {
		return create(name, EntityType.Builder.<BWChestBoatEntity>create(BWChestBoatEntity::new, SpawnGroup.MISC).setDimensions(1.375f, 0.5625f).maxTrackingRange(10).build(Bewitchment.id(name).toString()));
	}

	private static <T extends Entity> EntityType.Builder<T> mob(EntityType.EntityFactory<T> factory, SpawnGroup group, EntityDimensions dimensions, boolean fireImmune) {
		EntityType.Builder<T> builder = builder(factory, group, dimensions);
		return fireImmune ? builder.makeFireImmune() : builder;
	}

	public static void init() {
		ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registries.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
	}
}
