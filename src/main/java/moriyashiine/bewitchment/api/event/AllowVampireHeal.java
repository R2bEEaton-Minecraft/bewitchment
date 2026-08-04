/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.entity.player.PlayerEntity;


public interface AllowVampireHeal {
	BWEvent<AllowVampireHeal> EVENT = BWEvent.create(AllowVampireHeal.class, listeners -> (player, isPledgedToLilith) -> {
		for (AllowVampireHeal listener : listeners) {
			if (!listener.allowHeal(player, isPledgedToLilith)) {
				return false;
			}
		}
		return true;
	});

	boolean allowHeal(PlayerEntity player, boolean isPledgedToLilith);
}
