/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.entity.player.PlayerEntity;

public interface AllowVampireBurn {
	BWEvent<AllowVampireBurn> EVENT = BWEvent.create(AllowVampireBurn.class, listeners -> player -> {
		for (AllowVampireBurn listener : listeners) {
			if (!listener.allowBurn(player)) {
				return false;
			}
		}
		return true;
	});

	boolean allowBurn(PlayerEntity player);
}
