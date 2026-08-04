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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolymorphStatusEffect extends StatusEffect {
	public static final Identifier IMPERSONATE_IDENTIFIER = Bewitchment.id("polymorph");

	private static final Logger LOGGER = LoggerFactory.getLogger("Bewitchment Polymorph");

	public PolymorphStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	/**
	 * On Fabric this hands the stored profile to Impersonate, which makes the
	 * player appear to everyone as the taglocked player.
	 *
	 * <p>Impersonate has no Forge counterpart, and doing it properly means
	 * rewriting player list packets and resolving the impersonated skin through
	 * the session service.  Until that is written the effect still brews, applies
	 * and expires, and the target is still recorded on the component — it simply
	 * does not change how the player looks.
	 */
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof ServerPlayerEntity player) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(player).ifPresent(polymorphComponent -> {
				if (polymorphComponent.getUuid() != null) {
					LOGGER.debug("Polymorph disguise is not implemented on Forge; {} keeps their own appearance", player.getGameProfile().getName());
				}
			});
		}
	}
}
