/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;


public final class BloodSuckEvents {
	public static final BWEvent<OnBloodSuck> ON_BLOOD_SUCK = BWEvent.create(OnBloodSuck.class, listeners -> (player, target, bloodToGive) -> {
		for (OnBloodSuck listener : listeners) {
			listener.onBloodSuck(player, target, bloodToGive);
		}
	});

	public static final BWEvent<SetBloodAmount> BLOOD_AMOUNT = BWEvent.create(SetBloodAmount.class, listeners -> (player, target, bloodToGive) -> {
		int result = bloodToGive;
		for (SetBloodAmount listener : listeners) {
			result = Math.max(result, listener.onBloodSuck(player, target, result));
		}
		return result;
	});

	public interface OnBloodSuck {
		void onBloodSuck(PlayerEntity player, LivingEntity target, int bloodToGive);
	}

	public interface SetBloodAmount {
		int onBloodSuck(PlayerEntity player, LivingEntity target, int currentBloodToGive);
	}
}
