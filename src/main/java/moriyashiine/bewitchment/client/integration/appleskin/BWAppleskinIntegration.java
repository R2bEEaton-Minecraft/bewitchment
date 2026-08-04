/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.appleskin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.client.MinecraftClient;
import squeek.appleskin.api.event.HUDOverlayEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BWAppleskinIntegration {
	@SubscribeEvent
	public static void onSaturation(HUDOverlayEvent.Saturation saturation) {
		if (BewitchmentAPI.isVampire(MinecraftClient.getInstance().player, true)) {
			saturation.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onExhaustion(HUDOverlayEvent.Exhaustion exhaustion) {
		if (BewitchmentAPI.isVampire(MinecraftClient.getInstance().player, true)) {
			exhaustion.setCanceled(true);
		}
	}
}
