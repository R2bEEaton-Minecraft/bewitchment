/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.entity.LivingEntity;


public final class BloodSetEvents {
	public static final BWEvent<OnFillBlood> ON_BLOOD_FILL = BWEvent.create(OnFillBlood.class, listeners -> (entity, amount, simulate) -> {
		for (OnFillBlood listener : listeners) {
			listener.onFillBlood(entity, amount, simulate);
		}
	});

	public static final BWEvent<OnDrainBlood> ON_BLOOD_DRAIN = BWEvent.create(OnDrainBlood.class, listeners -> (entity, amount, simulate) -> {
		for (OnDrainBlood listener : listeners) {
			listener.onDrainBlood(entity, amount, simulate);
		}
	});

	public static final BWEvent<OnSetBlood> ON_BLOOD_SET = BWEvent.create(OnSetBlood.class, listeners -> (entity, amount) -> {
		for (OnSetBlood listener : listeners) {
			listener.onSetBlood(entity, amount);
		}
	});

	public interface OnFillBlood {
		void onFillBlood(LivingEntity entity, int amount, boolean simulate);
	}

	public interface OnDrainBlood {
		void onDrainBlood(LivingEntity entity, int amount, boolean simulate);
	}

	public interface OnSetBlood {
		void onSetBlood(LivingEntity entity, int amount);
	}
}
