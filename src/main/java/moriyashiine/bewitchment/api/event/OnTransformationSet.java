/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import moriyashiine.bewitchment.api.registry.Transformation;
import net.minecraft.entity.player.PlayerEntity;


public interface OnTransformationSet {
	BWEvent<OnTransformationSet> EVENT = BWEvent.create(OnTransformationSet.class, listeners -> (player, transformation) -> {
		for (OnTransformationSet listener : listeners) {
			listener.onTransformationSet(player, transformation);
		}
	});

	void onTransformationSet(PlayerEntity player, Transformation transformation);
}
