/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The identity a polymorphed player is wearing.
 *
 * <p>Impersonate synchronizes the swapped profile itself on Fabric.  There is no
 * Forge equivalent, so the disguise is applied on the client instead and this
 * component carries the profile there.
 */
public class PolymorphComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final Entity obj;
	private UUID uuid;
	private String name;

	public PolymorphComponent(Entity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		// A sync packet without a profile is how the server removes a disguise.
		// Clear both fields rather than leaving the old client-side profile active.
		uuid = tag.containsUuid("UUID") ? tag.getUuid("UUID") : null;
		name = tag.contains("Name") ? tag.getString("Name") : null;
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (getUuid() != null) {
			tag.putUuid("UUID", uuid);
			// UUID and name are assigned in separate calls by potion processing.
			// The first synchronization must remain valid while the name is pending.
			tag.putString("Name", name == null ? "" : name);
		}
	}

	@Override
	public void serverTick() {
		if (obj instanceof PlayerEntity player && getUuid() != null && !player.hasStatusEffect(BWStatusEffects.POLYMORPH)) {
			setUuid(null);
			setName(null);
		}
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
		BWComponents.POLYMORPH_COMPONENT.sync(obj);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		BWComponents.POLYMORPH_COMPONENT.sync(obj);
	}
}
