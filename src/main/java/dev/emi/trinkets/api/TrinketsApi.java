package dev.emi.trinkets.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

/** Curios implementation of the Trinkets lookup used by Bewitchment. */
public final class TrinketsApi {
    private TrinketsApi() {
    }

    public static Optional<Component> getTrinketComponent(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity).resolve().map(Component::new);
    }

    public record Component(top.theillusivec4.curios.api.type.capability.ICuriosItemHandler handler) {
        public boolean isEquipped(Item item) {
            return handler.isEquipped(item);
        }
    }
}
