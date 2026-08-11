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
@Mixin(targets = "forge.net.mca.client.render.layer.ClothingLayer")
public class ClothingLayerMixin {
	private static final Identifier FALLBACK_CLOTHES = new Identifier("mca", "skins/clothing/normal/neutral/none/3.png");

	@Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
	private void bewitchment$useFallbackClothes(LivingEntity villager, CallbackInfoReturnable<Identifier> callbackInfo) {
		NbtCompound nbt = villager.writeNbt(new NbtCompound());
		if (nbt.getString("clothes").isEmpty()) {
			callbackInfo.setReturnValue(FALLBACK_CLOTHES);
		}
	}
}
