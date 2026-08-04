package dev.emi.trinkets.api.client;

import net.minecraft.item.Item;

/**
 * Renderer registration facade. Curios owns the slot inventory; model-layer
 * rendering is registered here until each legacy Trinkets renderer is moved to
 * Curios' renderer API.
 */
public final class TrinketRendererRegistry {
    private TrinketRendererRegistry() {
    }

    public static void registerRenderer(Item item, TrinketRenderer renderer) {
    }
}
