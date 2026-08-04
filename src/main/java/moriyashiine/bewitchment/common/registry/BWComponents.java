/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import moriyashiine.bewitchment.api.component.*;
import moriyashiine.bewitchment.common.component.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

public final class BWComponents {
	public static final ComponentKey<ContractsComponent> CONTRACTS_COMPONENT = new ComponentKey<>(entity -> new ContractsComponent((PlayerEntity) entity));
	public static final ComponentKey<FortuneComponent> FORTUNE_COMPONENT = new ComponentKey<>(entity -> new FortuneComponent((PlayerEntity) entity));
	public static final ComponentKey<MagicComponent> MAGIC_COMPONENT = new ComponentKey<>(entity -> new MagicComponent((PlayerEntity) entity));
	public static final ComponentKey<PledgeComponent> PLEDGE_COMPONENT = new ComponentKey<>(entity -> new PledgeComponent((PlayerEntity) entity));
	public static final ComponentKey<TransformationComponent> TRANSFORMATION_COMPONENT = new ComponentKey<>(entity -> new TransformationComponent((PlayerEntity) entity));
	public static final ComponentKey<BloodComponent> BLOOD_COMPONENT = new ComponentKey<>(entity -> new BloodComponent((LivingEntity) entity));
	public static final ComponentKey<CursesComponent> CURSES_COMPONENT = new ComponentKey<>(entity -> new CursesComponent((LivingEntity) entity));
	public static final ComponentKey<AdditionalWerewolfDataComponent> ADDITIONAL_WEREWOLF_DATA_COMPONENT = new ComponentKey<>(entity -> new AdditionalWerewolfDataComponent((PlayerEntity) entity));
	public static final ComponentKey<RespawnTimerComponent> RESPAWN_TIMER_COMPONENT = new ComponentKey<>(entity -> new RespawnTimerComponent());
	public static final ComponentKey<TeleportTimerComponent> TELEPORT_TIMER_COMPONENT = new ComponentKey<>(entity -> new TeleportTimerComponent());
	public static final ComponentKey<FullInvisibilityComponent> FULL_INVISIBILITY_COMPONENT = new ComponentKey<>(entity -> new FullInvisibilityComponent((PlayerEntity) entity));
	public static final ComponentKey<BroomUserComponent> BROOM_USER_COMPONENT = new ComponentKey<>(entity -> new BroomUserComponent((PlayerEntity) entity));
	public static final ComponentKey<PolymorphComponent> POLYMORPH_COMPONENT = new ComponentKey<>(PolymorphComponent::new);
	public static final ComponentKey<AdditionalWaterDataComponent> ADDITIONAL_WATER_DATA_COMPONENT = new ComponentKey<>(AdditionalWaterDataComponent::new);
	public static final ComponentKey<FamiliarComponent> FAMILIAR_COMPONENT = new ComponentKey<>(entity -> new FamiliarComponent((LivingEntity) entity));
	public static final ComponentKey<MinionComponent> MINION_COMPONENT = new ComponentKey<>(entity -> new MinionComponent((MobEntity) entity));
	public static final ComponentKey<FakeMobComponent> FAKE_MOB_COMPONENT = new ComponentKey<>(entity -> new FakeMobComponent((MobEntity) entity));
	public static final ComponentKey<WerewolfVillagerComponent> WEREWOLF_VILLAGER_COMPONENT = new ComponentKey<>(entity -> new WerewolfVillagerComponent((VillagerEntity) entity));
	public static final ComponentKey<CaduceusFireballComponent> CADUCEUS_FIREBALL_COMPONENT = new ComponentKey<>(entity -> new CaduceusFireballComponent());

	/** Keeps component implementation types out of mixin signatures. */
	public static boolean isFullyInvisible(PlayerEntity player) {
		return FULL_INVISIBILITY_COMPONENT.get(player).isFullInvisible();
	}
}
