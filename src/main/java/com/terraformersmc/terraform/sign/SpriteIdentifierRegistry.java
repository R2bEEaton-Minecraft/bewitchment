package com.terraformersmc.terraform.sign;

import net.minecraft.client.util.SpriteIdentifier;

/** Forge stitches these sprites through the atlas event; this keeps the client call site loader-neutral. */
public final class SpriteIdentifierRegistry {
	public static final SpriteIdentifierRegistry INSTANCE = new SpriteIdentifierRegistry();

	private SpriteIdentifierRegistry() {
	}

	public void addIdentifier(SpriteIdentifier identifier) {
	}
}
