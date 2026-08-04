/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import moriyashiine.bewitchment.forge.component.BWEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/** Applies a component the server pushed to this client. */
public class SyncComponentPacket {
	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftClient client, net.minecraft.client.network.ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			int entityId = buf.readVarInt();
			Identifier componentId = buf.readIdentifier();
			NbtCompound nbt = buf.readNbt();
			client.execute(() -> {
				if (client.world == null || nbt == null) {
					return;
				}
				Entity entity = client.world.getEntityById(entityId);
				if (entity == null) {
					return;
				}
				for (ComponentKey<?> key : ComponentKey.all()) {
					if (key.getId().equals(componentId)) {
						Component component = BWEntityComponents.get(entity, key);
						if (component != null) {
							component.readFromNbt(nbt);
						}
						return;
					}
				}
			});
		}
	}
}
