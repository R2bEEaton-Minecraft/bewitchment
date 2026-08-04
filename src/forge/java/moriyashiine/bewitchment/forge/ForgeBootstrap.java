package moriyashiine.bewitchment.forge;

import net.minecraftforge.fml.common.Mod;

/**
 * Native Forge bootstrap used while the original Fabric/Yarn implementation is
 * migrated in slices. Keeping this class independent of Fabric makes the
 * ForgeGradle client launch and the user's Forge profile testable throughout
 * the port.
 */
@Mod("bewitchment")
public final class ForgeBootstrap {
    public ForgeBootstrap() {
    }
}
