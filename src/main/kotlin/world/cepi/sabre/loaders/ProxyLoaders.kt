package world.cepi.sabre.loaders

import net.minestom.server.extras.bungee.BungeeCordProxy
import net.minestom.server.extras.velocity.VelocityProxy
import world.cepi.sabre.Forwarder
import world.cepi.sabre.config

object VelocityLoader : Loader {
    override fun load() {
        if (config().proxy == Forwarder.VELOCITY) VelocityProxy.enable(config().velocitySecret)
    }
}

object BungeeLoader : Loader {
    override fun load() {
        if (config().proxy == Forwarder.BUNGEE) BungeeCordProxy.enable()
    }
}