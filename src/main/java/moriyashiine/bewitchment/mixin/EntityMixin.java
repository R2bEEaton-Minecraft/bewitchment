/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.forge.component.BWEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract UUID getUuid();

	@Shadow
	private World world;

	/**
	 * Cardinal Components ticks an entity's components with the entity itself.
	 * Forge has no event that covers every entity type, so drive it from here.
	 */
	@Inject(method = "tick", at = @At("TAIL"))
	private void tickComponents(CallbackInfo callbackInfo) {
		BWEntityComponents.tick((Entity) (Object) this);
	}

	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && !world.isClient && (Object) this instanceof MobEntity mob) {
			if (source.getAttacker() instanceof LivingEntity living) {
				if (BWComponents.MINION_COMPONENT.maybeGet(mob).map(component -> living.getUuid().equals(component.getMaster())).orElse(false)) {
					callbackInfo.setReturnValue(true);
				} else if (living instanceof MobEntity mobAttacker && BWComponents.MINION_COMPONENT.maybeGet(mobAttacker).map(component -> getUuid().equals(component.getMaster())).orElse(false)) {
					callbackInfo.setReturnValue(true);
				}
			}
		}
	}
}
