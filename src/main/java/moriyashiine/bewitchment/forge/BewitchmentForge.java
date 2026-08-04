package moriyashiine.bewitchment.forge;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

/** Forge entrypoint which delegates to the existing shared initializers. */
@Mod(Bewitchment.MOD_ID)
public final class BewitchmentForge {

    public BewitchmentForge() {
        new Bewitchment().onInitialize();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new BewitchmentClient().onInitializeClient());
    }
}
