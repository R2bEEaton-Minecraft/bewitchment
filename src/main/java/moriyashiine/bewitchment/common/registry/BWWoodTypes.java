/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;

/**
 * Bewitchment's wood types.  Vanilla derives a sign's atlas sprite, its entity
 * model layer and the hanging sign editor background from the wood type name,
 * so these are registered under the mod namespace:
 * {@code bewitchment:juniper} resolves to
 * {@code bewitchment:textures/entity/signs/juniper.png} and friends.
 */
public final class BWWoodTypes {
	public static final WoodType JUNIPER = register("juniper");
	public static final WoodType CYPRESS = register("cypress");
	public static final WoodType ELDER = register("elder");
	public static final WoodType DRAGONS_BLOOD = register("dragons_blood");

	public static final WoodType[] VALUES = {JUNIPER, CYPRESS, ELDER, DRAGONS_BLOOD};

	private BWWoodTypes() {
	}

	private static WoodType register(String name) {
		return WoodType.register(new WoodType(Bewitchment.id(name).toString(), BlockSetType.OAK));
	}
}
