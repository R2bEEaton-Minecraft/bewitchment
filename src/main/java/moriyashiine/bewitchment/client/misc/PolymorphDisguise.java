/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.misc;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Resolves who a polymorphed player is pretending to be.
 *
 * <p>Impersonate swaps the player's profile server-side on Fabric.  Forge has no
 * counterpart, so the polymorph component is synced to clients and the disguise
 * is worn here: a player's skin, cape and body model all resolve through their
 * player list entry, so answering with the impersonated player's entry changes
 * all three at once.
 */
public final class PolymorphDisguise {
	private PolymorphDisguise() {
	}

	/**
	 * {@return the impersonated player's list entry, or {@code null} when this
	 * player is not disguised}
	 *
	 * <p>The disguise needs the impersonated player's textures to be known to
	 * this client, which they are while that player is on the server.  When they
	 * are not, this returns {@code null} and the player keeps their own
	 * appearance rather than rendering a broken skin.
	 */
	@Nullable
	public static PlayerListEntry getEntry(PlayerEntity player) {
		UUID uuid = BWComponents.POLYMORPH_COMPONENT.maybeGet(player).map(component -> component.getUuid()).orElse(null);
		if (uuid == null) {
			return null;
		}
		ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
		return handler == null ? null : handler.getPlayerListEntry(uuid);
	}
}
