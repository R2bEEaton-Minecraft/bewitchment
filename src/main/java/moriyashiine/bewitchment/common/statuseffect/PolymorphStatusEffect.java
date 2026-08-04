/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PolymorphStatusEffect extends StatusEffect {
	public static final Identifier IMPERSONATE_IDENTIFIER = Bewitchment.id("polymorph");

	public PolymorphStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	/**
	 * Puts the disguise on.
	 *
	 * <p>Fabric hands the stored profile to Impersonate, which swaps the player's
	 * profile server-side.  Forge has no counterpart, so the component is synced
	 * to clients instead and worn there by {@code AbstractClientPlayerEntityMixin};
	 * pushing it here covers players already watching when the effect lands.
	 */
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof ServerPlayerEntity player) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(player).ifPresent(polymorphComponent -> {
				if (polymorphComponent.getUuid() != null) {
					BWComponents.POLYMORPH_COMPONENT.sync(player);
				}
			});
		}
	}
}
