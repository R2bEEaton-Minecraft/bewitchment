/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.appleskin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.client.MinecraftClient;
import squeek.appleskin.api.event.HUDOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

public class BWAppleskinIntegration {
	/** Registers AppleSkin hooks only after Forge has confirmed AppleSkin is present. */
	public static void init() {
		MinecraftForge.EVENT_BUS.addListener(BWAppleskinIntegration::onSaturation);
		MinecraftForge.EVENT_BUS.addListener(BWAppleskinIntegration::onExhaustion);
	}

	public static void onSaturation(HUDOverlayEvent.Saturation saturation) {
		if (BewitchmentAPI.isVampire(MinecraftClient.getInstance().player, true)) {
			saturation.setCanceled(true);
		}
	}

	public static void onExhaustion(HUDOverlayEvent.Exhaustion exhaustion) {
		if (BewitchmentAPI.isVampire(MinecraftClient.getInstance().player, true)) {
			exhaustion.setCanceled(true);
		}
	}
}
