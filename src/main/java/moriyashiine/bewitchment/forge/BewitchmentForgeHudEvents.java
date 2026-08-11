/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.BloodComponent;
import moriyashiine.bewitchment.api.component.MagicComponent;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

/** Forge renders the food bar through overlays rather than InGameHud#renderStatusBars. */
final class BewitchmentForgeHudEvents {
	private static final Identifier GUI_ICONS_TEXTURE = Bewitchment.id("textures/gui/icons.png");

	private BewitchmentForgeHudEvents() {
	}

	static void hideVampireFood(RenderGuiOverlayEvent.Pre event) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (isOverlay(event, VanillaGuiOverlay.FOOD_LEVEL) && client.player != null && BewitchmentAPI.isVampire(client.player, true)) {
			event.setCanceled(true);
		}
	}

	static void renderStatusMeters(RenderGuiOverlayEvent.Post event) {
		// AIR_LEVEL is dispatched after health, armor, and food, including when the
		// player is not underwater, so it is the stable Forge equivalent of the
		// vanilla status-bar tail.
		if (!isOverlay(event, VanillaGuiOverlay.AIR_LEVEL)) {
			return;
		}
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null || client.options.hudHidden) {
			return;
		}
		DrawContext context = event.getGuiGraphics();
		if (BewitchmentAPI.isVampire(client.player, true)) {
			drawBlood(context, client.player, context.getScaledWindowWidth() / 2 + 82, context.getScaledWindowHeight() - 39, 10);
			if (client.player.isSneaking() && client.player.isPartOfGame() && client.targetedEntity instanceof LivingEntity living && living.getType().isIn(BWTags.HAS_BLOOD)) {
				drawBlood(context, living, context.getScaledWindowWidth() / 2 + 12, context.getScaledWindowHeight() / 2 + 9, 5);
			}
		}
		BWComponents.MAGIC_COMPONENT.maybeGet(client.player).ifPresent(magic -> drawMagic(context, magic));
	}

	private static boolean isOverlay(RenderGuiOverlayEvent event, VanillaGuiOverlay overlay) {
		return event.getOverlay().id().equals(overlay.id());
	}

	private static void drawMagic(DrawContext context, MagicComponent magic) {
		if (magic.getMagicTimer() <= 0) {
			return;
		}
		RenderSystem.setShaderColor(1, 1, 1, Math.min(1, magic.getMagicTimer() / 10f));
		context.drawTexture(GUI_ICONS_TEXTURE, 13, (context.getScaledWindowHeight() - 74) / 2, 25, 0, 7, 74);
		context.drawTexture(GUI_ICONS_TEXTURE, 13, (context.getScaledWindowHeight() - 74) / 2, 32, 0, 7, (int) (74 - magic.getMagic() * 74f / MagicComponent.MAX_MAGIC));
		context.drawTexture(GUI_ICONS_TEXTURE, 4, (context.getScaledWindowHeight() - 102) / 2, 0, 0, 25, 102);
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	private static void drawBlood(DrawContext context, LivingEntity living, int xPos, int yPos, int droplets) {
		int v = living.hasStatusEffect(StatusEffects.HUNGER) ? 9 : 0;
		float blood = (float) BWComponents.BLOOD_COMPONENT.get(living).getBlood() / BloodComponent.MAX_BLOOD * droplets;
		int full = (int) blood;
		for (int i = 0; i < full; i++) {
			context.drawTexture(GUI_ICONS_TEXTURE, xPos - i * 8, yPos, 39, v, 9, 9);
		}
		if (full < droplets) {
			float remaining = blood - full;
			context.drawTexture(GUI_ICONS_TEXTURE, xPos - full * 8, yPos, remaining > 5 / 6f ? 48 : remaining > 4 / 6f ? 57 : remaining > 3 / 6f ? 66 : remaining > 2 / 6f ? 75 : remaining > 1 / 6f ? 84 : remaining > 0 ? 93 : 102, v, 9, 9);
		}
		for (int i = full + 1; i < droplets; i++) {
			context.drawTexture(GUI_ICONS_TEXTURE, xPos - i * 8, yPos, 102, v, 9, 9);
		}
	}
}
