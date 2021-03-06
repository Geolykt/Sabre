package world.cepi.sabre.loaders

import net.minestom.server.MinecraftServer

/** Loader interface for making it easier to reference the load function*/
interface Loader {

    /** Load function that calls in sequential order in the [Loaders] enum. */
    fun load()
}

/** Enum representation of all loaders, act independently from eachother.*/
enum class Loaders(val loader: Loader) {
    STORAGE(StorageLoader),
    MOJANG_AUTH(MojangAuthenticationLoader),
    UUID(UUIDLoader),
    INSTANCES(InstanceLoader),
    COMMANDS(CommandLoader),
    SAFE_SHUTDOWN(SafeShutdownLoader),
    VELOCITY(VelocityLoader),
    BUNGEE(BungeeLoader),
    OPTIFINE(OptifineLoader),
    BLOCKRULES(BlockPlacementLoader)
}


/** Loads all the loaders from the loader package. */
fun load() {
    Loaders.values().forEach {
        try {
            it.loader.load()
        } catch (e: Exception) {
            e.printStackTrace()
            MinecraftServer.LOGGER.warn("Logger ${it.name} failed to load.")
        }
    }
}