/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.client.misc.PolymorphDisguise;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Wears the polymorph disguise.
 *
 * <p>Skin, cape and body model all resolve through the player list entry, so
 * answering with the impersonated player's entry disguises all three at once.
 */
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
	@Inject(method = "getPlayerListEntry", at = @At("HEAD"), cancellable = true)
	private void bewitchment$polymorphSkin(CallbackInfoReturnable<PlayerListEntry> callbackInfo) {
		PlayerListEntry entry = PolymorphDisguise.getEntry((PlayerEntity) (Object) this);
		if (entry != null) {
			callbackInfo.setReturnValue(entry);
		}
	}
}
