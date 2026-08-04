/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;


public final class ReviveEvents {
	public static final BWEvent<OnRevive> ON_REVIVE = BWEvent.create(OnRevive.class, listeners -> (player, source, poppet) -> {
		for (OnRevive listener : listeners) {
			listener.onRevive(player, source, poppet);
		}
	});

	public static final BWEvent<CancelRevive> CANCEL_REVIVE = BWEvent.create(CancelRevive.class, listeners -> (player, source, poppet) -> {
		for (CancelRevive listener : listeners) {
			if (listener.shouldCancel(player, source, poppet)) {
				return true;
			}
		}
		return false;
	});

	public interface OnRevive {
		void onRevive(PlayerEntity player, DamageSource source, ItemStack poppet);
	}

	public interface CancelRevive {
		boolean shouldCancel(PlayerEntity player, DamageSource source, ItemStack poppet);
	}
}
