/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.forge.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Drives the parts of a component's life that Cardinal Components handles on
 * Fabric: carrying data through a respawn, and getting it onto clients.
 *
 * <p>Ticking is driven from {@code EntityMixin} instead, because Forge has no
 * event covering every entity type.
 */
public final class BWComponentEvents {
	private BWComponentEvents() {
	}

	/** Carries components onto the new player entity after death or a dimension change. */
	public static void copyOnRespawn(PlayerEvent.Clone event) {
		PlayerEntity original = event.getOriginal();
		PlayerEntity clone = event.getEntity();
		// The original is detached from the world by this point; its capabilities
		// are only readable while they are explicitly revived.
		original.reviveCaps();
		try {
			for (ComponentKey<?> key : ComponentKey.all()) {
				if (key.appliesTo(clone) && key.getRespawnCopyStrategy().shouldCopy(event.isWasDeath())) {
					key.copy(original, clone);
				}
			}
			clone.calculateDimensions();
		} finally {
			original.invalidateCaps();
		}
	}

	/** Sends an entity's components to a player who just started seeing it. */
	public static void syncOnStartTracking(PlayerEvent.StartTracking event) {
		if (event.getEntity() instanceof ServerPlayerEntity player) {
			BWEntityComponents.syncAll(event.getTarget(), player);
		}
	}

	/** Sends players their own components when they enter a world. */
	public static void syncOnJoin(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if (!event.getLevel().isClient() && entity instanceof ServerPlayerEntity player) {
			BWEntityComponents.syncAll(player, player);
		}
	}
}
