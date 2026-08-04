/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.TransformationComponent;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.forge.BewitchmentForgeTransformationEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TransformationAbilityPacket {
	public static final Identifier ID = Bewitchment.id("transformation_ability");

	public static void send() {
		ClientPlayNetworking.send(ID, new PacketByteBuf(Unpooled.buffer()));
	}

	private static boolean canUseAbility(PlayerEntity player) {
		TransformationComponent transformationComponent = BWComponents.TRANSFORMATION_COMPONENT.get(player);
		if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE) {
			return true;
		}
		if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF) {
			return !BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).isForcedTransformation();
		}
		return false;
	}

	public static void useAbility(PlayerEntity player, boolean forced) {
		BWComponents.TRANSFORMATION_COMPONENT.maybeGet(player).ifPresent(transformationComponent -> {
			World world = player.getWorld();
			boolean isAlternateForm = transformationComponent.isAlternateForm();
			if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE && (forced || (BewitchmentAPI.isPledged(player, BWPledges.LILITH) && BWComponents.BLOOD_COMPONENT.get(player).getBlood() > 0))) {
				PlayerLookup.tracking(player).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, player));
				SpawnSmokeParticlesPacket.send((ServerPlayerEntity) player, player);
				world.playSound(null, player.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, player.getSoundCategory(), 1, 1);
				transformationComponent.setAlternateForm(!isAlternateForm);
				BewitchmentForgeTransformationEvents.setVampireFlight(player, !isAlternateForm);
				player.calculateDimensions();
			} else if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF && (forced || BewitchmentAPI.isPledged(player, BWPledges.HERNE))) {
				PlayerLookup.tracking(player).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, player));
				SpawnSmokeParticlesPacket.send((ServerPlayerEntity) player, player);
				world.playSound(null, player.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, player.getSoundCategory(), 1, 1);
				transformationComponent.setAlternateForm(!isAlternateForm);
				player.calculateDimensions();
				if (isAlternateForm && player.hasStatusEffect(StatusEffects.NIGHT_VISION) && player.getStatusEffect(StatusEffects.NIGHT_VISION).isAmbient()) {
					player.removeStatusEffect(StatusEffects.NIGHT_VISION);
				}
			}
		});
	}

	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			server.execute(() -> {
				if (canUseAbility(player)) {
					useAbility(player, false);
				}
			});
		}
	}
}
