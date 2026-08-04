package moriyashiine.bewitchment.forge;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.*;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Forge entrypoint which delegates to the existing shared initializers. */
@Mod(Bewitchment.MOD_ID)
public final class BewitchmentForge {

    public BewitchmentForge() {
        // The legacy Fabric initializer is being migrated event-by-event. Do
        // not load it on Forge: its Fabric API linkage is intentionally
        // compile-only and must not be present in a native Forge installation.
    }
}

@Mod.EventBusSubscriber(modid = Bewitchment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
final class BewitchmentForgeModEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BWEntityTypes.OWL, OwlEntity.createAttributes().build());
        event.put(BWEntityTypes.RAVEN, RavenEntity.createAttributes().build());
        event.put(BWEntityTypes.SNAKE, SnakeEntity.createAttributes().build());
        event.put(BWEntityTypes.TOAD, ToadEntity.createAttributes().build());
        event.put(BWEntityTypes.GHOST, GhostEntity.createAttributes().build());
        event.put(BWEntityTypes.VAMPIRE, VampireEntity.createAttributes().build());
        event.put(BWEntityTypes.WEREWOLF, WerewolfEntity.createAttributes().build());
        event.put(BWEntityTypes.HELLHOUND, HellhoundEntity.createAttributes().build());
        event.put(BWEntityTypes.DEMON, DemonEntity.createAttributes().build());
        event.put(BWEntityTypes.LEONARD, LeonardEntity.createAttributes().build());
        event.put(BWEntityTypes.BAPHOMET, BaphometEntity.createAttributes().build());
        event.put(BWEntityTypes.LILITH, LilithEntity.createAttributes().build());
        event.put(BWEntityTypes.HERNE, HerneEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerFuelBurnTimes(FurnaceFuelBurnTimeEvent event) {
        int burnTime = BWObjects.getFuelBurnTime(event.getItemStack());
        if (burnTime > 0) event.setBurnTime(burnTime);
    }
}
