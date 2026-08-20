/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.forge;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * The parts of transforming that Bewitchment gets from Fabric-only libraries.
 *
 * <p>Body size comes from Pehkui on Fabric and from Forge's size event here;
 * vampire flight comes from PlayerAbilityLib there and from the player's own
 * ability flags here.
 */
public final class BewitchmentForgeTransformationEvents {
	private static final float VANILLA_STANDING_HEIGHT = EntityType.PLAYER.getDimensions().height;

	private BewitchmentForgeTransformationEvents() {
	}

	/** PlayerEntity's own pose eye heights, free of any modded scaling. */
	private static float vanillaEyeHeight(EntityPose pose) {
		return switch (pose) {
			case SWIMMING, FALL_FLYING, SPIN_ATTACK -> 0.4f;
			case CROUCHING -> 1.27f;
			default -> 1.62f;
		};
	}

	/** Shrinks a bat-form vampire and enlarges a beast-form werewolf. */
	public static void resizeTransformedPlayers(EntityEvent.Size event) {
		if (!(event.getEntity() instanceof PlayerEntity player)) {
			return;
		}
		BWComponents.TRANSFORMATION_COMPONENT.maybeGet(player).ifPresent(transformation -> {
			EntityDimensions baseDimensions = player.getDimensions(event.getPose());
			float baseEyeHeight = player.getEyeHeightAccess(event.getPose(), baseDimensions);
			if (!transformation.isAlternateForm()) {
				event.setNewSize(baseDimensions);
				event.setNewEyeHeight(baseEyeHeight);
				return;
			}
			EntityDimensions dimensions = null;
			if (BewitchmentAPI.isVampire(player, false)) {
				dimensions = EntityType.BAT.getDimensions();
			} else if (BewitchmentAPI.isWerewolf(player, false)) {
				dimensions = BWEntityTypes.WEREWOLF.getDimensions();
			}
			if (dimensions != null) {
				event.setNewSize(dimensions);
				// PlayerEntity's eye-height method ignores the supplied dimensions, so
				// scale a pose-specific eye height to the target form explicitly.  The
				// unmodified vanilla heights are used rather than the player's own: an
				// alternate form wears a bat or werewolf body, so a mod scaling the
				// human body (Minecraft Comes Alive does, and clamps that scaling
				// against the player's current height) must not be folded in on top of
				// the form's proportions.
				event.setNewEyeHeight(vanillaEyeHeight(event.getPose()) * dimensions.height / VANILLA_STANDING_HEIGHT);
			}
		});
	}

	/**
	 * Grants or revokes the flight a vampire has while in bat form.  Creative and
	 * spectator flight is left alone, since it is not ours to take away.
	 */
	public static void setVampireFlight(PlayerEntity player, boolean allow) {
		PlayerAbilities abilities = player.getAbilities();
		if (abilities.creativeMode || player.isSpectator()) {
			return;
		}
		abilities.allowFlying = allow;
		if (!allow) {
			abilities.flying = false;
		}
		if (player instanceof ServerPlayerEntity serverPlayer) {
			serverPlayer.sendAbilitiesUpdate();
		}
	}
}
