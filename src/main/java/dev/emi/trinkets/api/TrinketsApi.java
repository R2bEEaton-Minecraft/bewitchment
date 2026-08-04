package dev.emi.trinkets.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
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

        public List<Pair<SlotReference, ItemStack>> getEquipped(Item item) {
            return handler.findCurios(item).stream()
                    .map(result -> new Pair<>(new SlotReference(result.slotContext()), result.stack()))
                    .toList();
        }
    }
}
