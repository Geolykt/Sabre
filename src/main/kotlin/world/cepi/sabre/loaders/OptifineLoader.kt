package world.cepi.sabre.loaders

import net.minestom.server.extras.optifine.OptifineSupport
import world.cepi.sabre.config

object OptifineLoader : Loader {
    override fun load() {
        if (config().optifineSupport)
            OptifineSupport.enable()
    }
}