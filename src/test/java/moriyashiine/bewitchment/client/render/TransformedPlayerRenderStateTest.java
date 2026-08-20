/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render;

import moriyashiine.bewitchment.mixin.client.LimbAnimatorAccessor;
import net.minecraft.entity.LimbAnimator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransformedPlayerRenderStateTest {
	@Test
	void copiesAnimationValuesWithoutReplacingTheTargetAnimator() {
		LimbAnimator source = new LimbAnimator();
		source.setSpeed(0.25F);
		source.updateLimbs(0.75F, 0.5F);
		source.updateLimbs(0.9F, 0.5F);
		RecordingAnimator target = new RecordingAnimator();

		TransformedPlayerRenderState.copyLimbAnimation(source, target);

		assertEquals(0.5F, target.prevSpeed);
		assertEquals(0.7F, target.speed);
		assertEquals(1.2F, target.pos);
	}

	private static final class RecordingAnimator implements LimbAnimatorAccessor {
		private float prevSpeed;
		private float speed;
		private float pos;

		@Override
		public void bewitchment$setPrevSpeed(float prevSpeed) {
			this.prevSpeed = prevSpeed;
		}

		@Override
		public void bewitchment$setSpeed(float speed) {
			this.speed = speed;
		}

		@Override
		public void bewitchment$setPos(float pos) {
			this.pos = pos;
		}
	}
}
