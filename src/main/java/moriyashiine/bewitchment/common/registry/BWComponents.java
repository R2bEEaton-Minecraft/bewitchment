/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.RespawnCopyStrategy;
import moriyashiine.bewitchment.api.component.*;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.component.entity.*;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;

import java.util.function.Function;

import static dev.onyxstudios.cca.api.v3.component.RespawnCopyStrategy.ALWAYS_COPY;
import static dev.onyxstudios.cca.api.v3.component.RespawnCopyStrategy.LOSSLESS_ONLY;
import static dev.onyxstudios.cca.api.v3.component.RespawnCopyStrategy.NEVER_COPY;

public final class BWComponents {
	public static final ComponentKey<ContractsComponent> CONTRACTS_COMPONENT = register("contracts", ALWAYS_COPY, entity -> new ContractsComponent((PlayerEntity) entity), PlayerEntity.class);
	public static final ComponentKey<FortuneComponent> FORTUNE_COMPONENT = register("fortune", ALWAYS_COPY, entity -> new FortuneComponent((PlayerEntity) entity), PlayerEntity.class);
	public static final ComponentKey<MagicComponent> MAGIC_COMPONENT = register("magic", LOSSLESS_ONLY, entity -> new MagicComponent((PlayerEntity) entity), PlayerEntity.class);
	public static final ComponentKey<PledgeComponent> PLEDGE_COMPONENT = register("pledge", ALWAYS_COPY, entity -> new PledgeComponent((PlayerEntity) entity), PlayerEntity.class);
	public static final ComponentKey<TransformationComponent> TRANSFORMATION_COMPONENT = register("transformation", ALWAYS_COPY, entity -> new TransformationComponent((PlayerEntity) entity), PlayerEntity.class);
	public static final ComponentKey<BloodComponent> BLOOD_COMPONENT = register("blood", LOSSLESS_ONLY, entity -> new BloodComponent((LivingEntity) entity), LivingEntity.class);
	public static final ComponentKey<CursesComponent> CURSES_COMPONENT = register("curses", ALWAYS_COPY, entity -> new CursesComponent((LivingEntity) entity), LivingEntity.class);

	public static final ComponentKey<AdditionalWerewolfDataComponent> ADDITIONAL_WEREWOLF_DATA_COMPONENT = register("additional_werewolf_data", ALWAYS_COPY, entity -> new AdditionalWerewolfDataComponent((PlayerEntity) entity), PlayerEntity.class);
	public static final ComponentKey<RespawnTimerComponent> RESPAWN_TIMER_COMPONENT = register("respawn_timer", LOSSLESS_ONLY, entity -> new RespawnTimerComponent(), PlayerEntity.class);
	public static final ComponentKey<TeleportTimerComponent> TELEPORT_TIMER_COMPONENT = register("teleport_timer", LOSSLESS_ONLY, entity -> new TeleportTimerComponent(), PlayerEntity.class);
	public static final ComponentKey<FullInvisibilityComponent> FULL_INVISIBILITY_COMPONENT = register("full_invisibility", LOSSLESS_ONLY, entity -> new FullInvisibilityComponent((PlayerEntity) entity), PlayerEntity.class);
	public static final ComponentKey<BroomUserComponent> BROOM_USER_COMPONENT = register("broom_user", NEVER_COPY, entity -> new BroomUserComponent((PlayerEntity) entity), PlayerEntity.class);
	public static final ComponentKey<PolymorphComponent> POLYMORPH_COMPONENT = register("polymorph", LOSSLESS_ONLY, PolymorphComponent::new, PlayerEntity.class, ArrowEntity.class, AreaEffectCloudEntity.class);
	public static final ComponentKey<AdditionalWaterDataComponent> ADDITIONAL_WATER_DATA_COMPONENT = register("additional_water_data", LOSSLESS_ONLY, AdditionalWaterDataComponent::new, Entity.class);
	public static final ComponentKey<FamiliarComponent> FAMILIAR_COMPONENT = register("familiar", ALWAYS_COPY, entity -> new FamiliarComponent((LivingEntity) entity), LivingEntity.class);
	public static final ComponentKey<MinionComponent> MINION_COMPONENT = register("minion", ALWAYS_COPY, entity -> new MinionComponent((MobEntity) entity), MobEntity.class);
	public static final ComponentKey<FakeMobComponent> FAKE_MOB_COMPONENT = register("fake_mob", ALWAYS_COPY, entity -> new FakeMobComponent((MobEntity) entity), MobEntity.class);
	public static final ComponentKey<WerewolfVillagerComponent> WEREWOLF_VILLAGER_COMPONENT = register("werewolf_villager", ALWAYS_COPY, entity -> new WerewolfVillagerComponent((VillagerEntity) entity), VillagerEntity.class);
	public static final ComponentKey<CaduceusFireballComponent> CADUCEUS_FIREBALL_COMPONENT = register("caduceus_fireball", ALWAYS_COPY, entity -> new CaduceusFireballComponent(), FireballEntity.class);

	private BWComponents() {
	}

	@SafeVarargs
	private static <T extends Component> ComponentKey<T> register(String name, RespawnCopyStrategy respawnCopyStrategy, Function<Entity, T> factory, Class<? extends Entity>... targets) {
		return ComponentKey.register(Bewitchment.id(name), respawnCopyStrategy, factory, targets);
	}

	/** Forces class initialization so every key is registered before entities are created. */
	public static void init() {
	}

	/** Keeps component implementation types out of mixin signatures. */
	public static boolean isFullyInvisible(PlayerEntity player) {
		return FULL_INVISIBILITY_COMPONENT.get(player).isFullInvisible();
	}
}
