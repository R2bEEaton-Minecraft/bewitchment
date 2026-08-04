/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.CoffinBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallHangingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
	/**
	 * Vanilla fixes each block entity type's block list at construction, so
	 * blocks added by mods are rejected — which strips their block entities from
	 * chunks and skips their renderers.  Coffins reuse the bed block entity, and
	 * Bewitchment's signs reuse the vanilla sign block entities.
	 */
	@Inject(method = "supports", at = @At("HEAD"), cancellable = true)
	private void supports(BlockState state, CallbackInfoReturnable<Boolean> callbackInfo) {
		Object type = this;
		if (type == BlockEntityType.BED && state.getBlock() instanceof CoffinBlock) {
			callbackInfo.setReturnValue(true);
		} else if (type == BlockEntityType.SIGN && (state.getBlock() instanceof SignBlock || state.getBlock() instanceof WallSignBlock)) {
			callbackInfo.setReturnValue(true);
		} else if (type == BlockEntityType.HANGING_SIGN && (state.getBlock() instanceof HangingSignBlock || state.getBlock() instanceof WallHangingSignBlock)) {
			callbackInfo.setReturnValue(true);
		}
	}
}
