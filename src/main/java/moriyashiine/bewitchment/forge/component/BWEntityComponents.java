/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.forge.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Backs Bewitchment's components with a Forge capability.
 *
 * <p>Cardinal Components gives every component storage, NBT persistence, a tick
 * and automatic synchronization; Forge provides none of that, so this attaches
 * one capability to every entity holding the components that apply to it, and
 * the surrounding event handlers drive the rest of the lifecycle.
 */
public final class BWEntityComponents {
	public static final Identifier SYNC_PACKET_ID = Bewitchment.id("sync_component");

	public static final Capability<Holder> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	private BWEntityComponents() {
	}

	public static void registerCapability(RegisterCapabilitiesEvent event) {
		event.register(Holder.class);
	}

	public static void attach(AttachCapabilitiesEvent<Entity> event) {
		Holder holder = new Holder(event.getObject());
		if (holder.isEmpty()) {
			return;
		}
		event.addCapability(Bewitchment.id("components"), new Provider(holder));
	}

	@Nullable
	public static <T extends Component> T get(Entity entity, ComponentKey<T> key) {
		Holder holder = holder(entity);
		return holder == null ? null : holder.get(key);
	}

	@Nullable
	private static Holder holder(Entity entity) {
		return entity.getCapability(CAPABILITY).resolve().orElse(null);
	}

	/** Runs every applicable component's tick, mirroring Cardinal Components. */
	public static void tick(Entity entity) {
		Holder holder = holder(entity);
		if (holder == null) {
			return;
		}
		boolean client = entity.getWorld().isClient();
		for (Component component : holder.components.values()) {
			if (component instanceof CommonTickingComponent common) {
				common.tick();
			}
			if (!client && component instanceof ServerTickingComponent server) {
				server.serverTick();
			}
		}
	}

	/** Sends one component to every client tracking the entity. */
	public static void sync(Entity entity, ComponentKey<?> key) {
		if (entity.getWorld().isClient()) {
			return;
		}
		Component component = get(entity, key);
		if (!(component instanceof AutoSyncedComponent)) {
			return;
		}
		// Each send consumes its buffer, so every recipient needs its own.
		for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
			ServerPlayNetworking.send(player, SYNC_PACKET_ID, packet(entity, key, component));
		}
		if (entity instanceof ServerPlayerEntity self) {
			ServerPlayNetworking.send(self, SYNC_PACKET_ID, packet(entity, key, component));
		}
	}

	/** Sends every synced component of an entity to one player. */
	public static void syncAll(Entity entity, ServerPlayerEntity target) {
		Holder holder = holder(entity);
		if (holder == null) {
			return;
		}
		holder.components.forEach((key, component) -> {
			if (component instanceof AutoSyncedComponent) {
				ServerPlayNetworking.send(target, SYNC_PACKET_ID, packet(entity, key, component));
			}
		});
	}

	private static PacketByteBuf packet(Entity entity, ComponentKey<?> key, Component component) {
		NbtCompound nbt = new NbtCompound();
		component.writeToNbt(nbt);
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(entity.getId());
		buf.writeIdentifier(key.getId());
		buf.writeNbt(nbt);
		return buf;
	}

	/** Holds one entity's components. */
	public static final class Holder implements INBTSerializable<NbtCompound> {
		private final Map<ComponentKey<?>, Component> components = new IdentityHashMap<>();

		Holder(Entity entity) {
			for (ComponentKey<?> key : ComponentKey.all()) {
				if (key.appliesTo(entity)) {
					components.put(key, key.create(entity));
				}
			}
		}

		boolean isEmpty() {
			return components.isEmpty();
		}

		@SuppressWarnings("unchecked")
		@Nullable
		<T extends Component> T get(ComponentKey<T> key) {
			return (T) components.get(key);
		}

		@Override
		public NbtCompound serializeNBT() {
			NbtCompound nbt = new NbtCompound();
			components.forEach((key, component) -> {
				NbtCompound entry = new NbtCompound();
				component.writeToNbt(entry);
				nbt.put(key.getId().toString(), entry);
			});
			return nbt;
		}

		@Override
		public void deserializeNBT(NbtCompound nbt) {
			components.forEach((key, component) -> {
				String name = key.getId().toString();
				if (nbt.contains(name)) {
					component.readFromNbt(nbt.getCompound(name));
				}
			});
		}
	}

	private static final class Provider implements ICapabilitySerializable<NbtCompound> {
		private final Holder holder;
		private final LazyOptional<Holder> optional;

		Provider(Holder holder) {
			this.holder = holder;
			this.optional = LazyOptional.of(() -> holder);
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
			return CAPABILITY.orEmpty(capability, optional);
		}

		@Override
		public NbtCompound serializeNBT() {
			return holder.serializeNBT();
		}

		@Override
		public void deserializeNBT(NbtCompound nbt) {
			holder.deserializeNBT(nbt);
		}
	}
}
