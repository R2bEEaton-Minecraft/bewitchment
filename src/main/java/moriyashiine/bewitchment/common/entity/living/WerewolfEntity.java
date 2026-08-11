/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class WerewolfEntity extends BWHostileEntity {
	private static final String MCA_FALLBACK_HAIR = "mca:missing";
	private static final String MCA_FALLBACK_CLOTHES = "mca:skins/clothing/normal/neutral/none/3.png";

	public NbtCompound storedVillager;

	public WerewolfEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15).add(EntityAttributes.GENERIC_ARMOR, 20).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 8).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4).add(ForgeMod.ENTITY_REACH.get(), 1).add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1);
	}

	public static boolean canSpawn(EntityType<WerewolfEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return world.toServerWorld().isNight() && BewitchmentAPI.getMoonPhase(world.toServerWorld().toServerWorld()) == 0 && HostileEntity.canSpawnInDark(type, world, spawnReason, pos, random);
	}

	@Override
	public void tick() {
		super.tick();
		if (!getWorld().isClient) {
			getArmorItems().forEach(stack -> dropStack(stack.split(1)));
			if (BWUtil.isTool(getMainHandStack())) {
				dropStack(getMainHandStack().split(1));
			}
			if (BWUtil.isTool(getOffHandStack())) {
				dropStack(getOffHandStack().split(1));
			}
			if (storedVillager != null && age % 20 == 0 && (getWorld().isDay() || BewitchmentAPI.getMoonPhase(getWorld()) != 0)) {
				VillagerEntity entity = createVillager(getWorld(), storedVillager);
				if (entity != null) {
					// MCA deliberately preserves its existing appearance for CONVERSION
					// spawns.  This entity is newly created, though, so use an event spawn
					// to initialize its genetics, skin, clothing, relationships, and AI
					// before we restore the saved vanilla villager data below.
					entity.initialize((ServerWorldAccess) getWorld(), getWorld().getLocalDifficulty(getBlockPos()), SpawnReason.EVENT, null, null);
					PlayerLookup.tracking(this).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, this));
					getWorld().playSound(null, getX(), getY(), getZ(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, getSoundCategory(), getSoundVolume(), getSoundPitch());
					if (!isMcaVillager(entity)) {
						entity.readNbt(storedVillager);
					}
					entity.updatePositionAndAngles(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
					entity.setHealth(entity.getMaxHealth() * (getHealth() / getMaxHealth()));
					entity.setFireTicks(getFireTicks());
					entity.clearStatusEffects();
					getStatusEffects().forEach(entity::addStatusEffect);
					BWComponents.CURSES_COMPONENT.maybeGet(entity).ifPresent(entityCursesComponent -> BWComponents.CURSES_COMPONENT.maybeGet(this).ifPresent(thisCursesComponent -> {
						entityCursesComponent.getCurses().clear();
						thisCursesComponent.getCurses().forEach(entityCursesComponent::addCurse);
					}));
					BWComponents.WEREWOLF_VILLAGER_COMPONENT.get(entity).setStoredWerewolf(writeNbt(new NbtCompound()));
					getWorld().spawnEntity(entity);
					// MCA stores its appearance in tracked data.  Apply a fallback only
					// after spawning, so the correction is delivered to clients as an
					// entity-data update instead of being missed by the spawn packet.
					if (isMcaVillager(entity)) {
						ensureMcaAppearance(entity);
						syncTrackedData(entity);
					}
					remove(RemovalReason.DISCARDED);
				}
			}
		}
	}

	/**
	 * MCA replaces a vanilla villager one tick after it spawns, but that queue
	 * does not copy Forge capabilities.  Spawn MCA's entity directly and retain
	 * its MCA-owned skin/genetics NBT while copying vanilla villager state.
	 */
	private VillagerEntity createVillager(World world, NbtCompound storedVillager) {
		VillagerEntity previousVillager = EntityType.VILLAGER.create(world);
		if (previousVillager == null) {
			return null;
		}
		previousVillager.readNbt(storedVillager);
		Identifier id = new Identifier("mca", random.nextBoolean() ? "male_villager" : "female_villager");
		// MCA registers its entity types through Forge's registry.  The shared
		// vanilla registry does not include those entries on this port.
		EntityType<?> mcaType = ForgeRegistries.ENTITY_TYPES.getValue(id);
		Entity mcaVillager = mcaType == null ? null : mcaType.create(world);
		if (mcaVillager instanceof VillagerEntity villager) {
			villager.setVillagerData(previousVillager.getVillagerData());
			villager.setBreedingAge(previousVillager.getBreedingAge());
			Text customName = previousVillager.getCustomName();
			if (customName != null) {
				villager.setCustomName(customName);
			}
			return villager;
		}
		return previousVillager;
	}

	private static boolean isMcaVillager(VillagerEntity villager) {
		String className = villager.getClass().getName();
		// Architectury retains the platform prefix for MCA's Forge classes.
		return className.startsWith("net.mca.") || className.startsWith("forge.net.mca.");
	}

	/**
	 * MCA can occasionally select an empty appearance entry for a newly spawned
	 * baby.  Its renderer has no fallback for empty strings, so retain every
	 * valid randomized value and replace only missing layer identifiers.
	 */
	private static void ensureMcaAppearance(VillagerEntity villager) {
		NbtCompound nbt = villager.writeNbt(new NbtCompound());
		boolean changed = false;
		if (nbt.getString("hair").isEmpty()) {
			nbt.putString("hair", MCA_FALLBACK_HAIR);
			changed = true;
		}
		if (nbt.getString("clothes").isEmpty()) {
			nbt.putString("clothes", MCA_FALLBACK_CLOTHES);
			changed = true;
		}
		if (changed) {
			villager.readNbt(nbt);
		}
	}

	/**
	 * The replacement is created during the old entity's tick.  MCA's skin data
	 * lives in tracked values, which can otherwise miss the first client update
	 * in that handoff; a relog works only because it sends that data again.
	 */
	private void syncTrackedData(VillagerEntity villager) {
		List<DataTracker.SerializedEntry<?>> trackedData = villager.getDataTracker().getDirtyEntries();
		if (trackedData != null) {
			EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket(villager.getId(), trackedData);
			PlayerLookup.tracking(this).forEach(player -> player.networkHandler.sendPacket(packet));
		}
	}

	@Override
	protected boolean hasShiny() {
		return true;
	}

	@Override
	public int getVariants() {
		return getVariantsStatic();
	}

	@Override
	public EntityGroup getGroup() {
		return BewitchmentAPI.DEMON;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_WEREWOLF_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_WEREWOLF_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_WEREWOLF_DEATH;
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if (dataTracker.get(VARIANT) != 0) {
			RegistryEntry<Biome> biome = world.getBiome(getBlockPos());
			if (biome.isIn(ConventionalBiomeTags.FOREST)) {
				dataTracker.set(VARIANT, random.nextBoolean() ? 1 : 2);
			} else if (biome.isIn(ConventionalBiomeTags.TAIGA)) {
				dataTracker.set(VARIANT, random.nextBoolean() ? 3 : 4);
			}
			if (biome.isIn(ConventionalBiomeTags.ICY)) {
				dataTracker.set(VARIANT, random.nextBoolean() ? 5 : 6);
			} else {
				dataTracker.set(VARIANT, random.nextInt(getVariants() - 1) + 1);
			}
		}
		if (spawnReason == SpawnReason.NATURAL || spawnReason == SpawnReason.SPAWN_EGG) {
			storedVillager = EntityType.VILLAGER.create((World) world).writeNbt(new NbtCompound());
		}
		return data;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("StoredVillager")) {
			storedVillager = nbt.getCompound("StoredVillager");
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (storedVillager != null) {
			nbt.put("StoredVillager", storedVillager);
		}
	}

	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity instanceof PlayerEntity || entity instanceof SheepEntity || entity instanceof MerchantEntity || entity.getGroup() == EntityGroup.ILLAGER));
	}

	public static int getVariantsStatic() {
		return 7;
	}
}
