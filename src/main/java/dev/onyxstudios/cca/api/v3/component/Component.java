package dev.onyxstudios.cca.api.v3.component;

import net.minecraft.nbt.NbtCompound;

/**
 * Forge-port compatibility contract.  Storage and lifecycle dispatch are
 * provided by {@link ComponentKey}; it deliberately mirrors the CCA methods
 * used by Bewitchment without requiring Fabric's Cardinal Components runtime.
 */
public interface Component {
	void readFromNbt(NbtCompound tag);

	void writeToNbt(NbtCompound tag);
}
