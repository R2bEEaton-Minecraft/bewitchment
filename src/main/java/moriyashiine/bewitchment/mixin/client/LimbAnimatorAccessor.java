/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import net.minecraft.entity.LimbAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LimbAnimator.class)
public interface LimbAnimatorAccessor {
	@Accessor("prevSpeed")
	void bewitchment$setPrevSpeed(float prevSpeed);

	@Accessor("speed")
	void bewitchment$setSpeed(float speed);

	@Accessor("pos")
	void bewitchment$setPos(float pos);
}
