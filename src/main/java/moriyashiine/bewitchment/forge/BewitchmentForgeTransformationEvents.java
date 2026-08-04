/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.forge;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.entity.EntityDimensions;
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
	private BewitchmentForgeTransformationEvents() {
	}

	/** Shrinks a bat-form vampire and enlarges a beast-form werewolf. */
	public static void resizeTransformedPlayers(EntityEvent.Size event) {
		if (!(event.getEntity() instanceof PlayerEntity player)) {
			return;
		}
		BWComponents.TRANSFORMATION_COMPONENT.maybeGet(player).ifPresent(transformation -> {
			if (!transformation.isAlternateForm()) {
				return;
			}
			EntityDimensions dimensions = null;
			if (BewitchmentAPI.isVampire(player, false)) {
				dimensions = EntityType.BAT.getDimensions();
			} else if (BewitchmentAPI.isWerewolf(player, false)) {
				dimensions = BWEntityTypes.WEREWOLF.getDimensions();
			}
			if (dimensions != null) {
				float scale = dimensions.height / event.getOldSize().height;
				event.setNewSize(dimensions);
				event.setNewEyeHeight(event.getOldEyeHeight() * scale);
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
