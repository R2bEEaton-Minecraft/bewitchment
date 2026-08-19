/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTransformations;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Keeps first-person view in lockstep with a transformation size change. */
@Mixin(Camera.class)
public abstract class CameraMixin {
	@Shadow
	private Entity focusedEntity;

	@Shadow
	private float cameraY;

	@Shadow
	private float lastCameraY;

	@Inject(method = "updateEyeHeight", at = @At("TAIL"))
	private void bewitchment$syncTransformationEyeHeight(CallbackInfo ci) {
		if (focusedEntity instanceof PlayerEntity player) {
			BWComponents.TRANSFORMATION_COMPONENT.maybeGet(player).ifPresent(transformation -> {
				if (transformation.getTransformation() == BWTransformations.VAMPIRE || transformation.getTransformation() == BWTransformations.WEREWOLF) {
					cameraY = player.getStandingEyeHeight();
					lastCameraY = cameraY;
				}
			});
		}
	}
}
