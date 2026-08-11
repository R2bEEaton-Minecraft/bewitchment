/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client.integration.mca;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "forge.net.mca.client.render.layer.HairLayer")
public class HairLayerMixin {
	private static final Identifier FALLBACK_HAIR = new Identifier("mca", "missing");

	@Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
	private void bewitchment$useFallbackHair(LivingEntity villager, CallbackInfoReturnable<Identifier> callbackInfo) {
		NbtCompound nbt = villager.writeNbt(new NbtCompound());
		if (nbt.getString("hair").isEmpty()) {
			callbackInfo.setReturnValue(FALLBACK_HAIR);
		}
	}
}
