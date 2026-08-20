/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render;

import moriyashiine.bewitchment.mixin.client.LimbAnimatorAccessor;
import net.minecraft.entity.LimbAnimator;

public final class TransformedPlayerRenderState {
	private TransformedPlayerRenderState() {
	}

	public static void copyLimbAnimation(LimbAnimator source, LimbAnimator target) {
		copyLimbAnimation(source, (LimbAnimatorAccessor) (Object) target);
	}

	static void copyLimbAnimation(LimbAnimator source, LimbAnimatorAccessor target) {
		target.bewitchment$setPrevSpeed(source.getSpeed(0));
		target.bewitchment$setSpeed(source.getSpeed());
		target.bewitchment$setPos(source.getPos());
	}
}
