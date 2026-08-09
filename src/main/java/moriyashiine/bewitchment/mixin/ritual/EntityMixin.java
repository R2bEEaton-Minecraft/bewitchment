/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.ritual;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(method = "isWet", at = @At("RETURN"), cancellable = true)
	private void isWet(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.maybeGet((Entity) (Object) this).map(component -> component.getWetTimer() > 0).orElse(false)) {
			callbackInfo.setReturnValue(true);
		}
	}

	@Inject(method = "isTouchingWaterOrRain", at = @At("RETURN"), cancellable = true)
	private void isTouchingWaterOrRain(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.maybeGet((Entity) (Object) this).map(component -> component.getWetTimer() > 0).orElse(false)) {
			callbackInfo.setReturnValue(true);
		}
	}
}
