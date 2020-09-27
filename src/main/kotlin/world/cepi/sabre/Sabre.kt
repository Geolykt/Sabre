package world.cepi.sabre

import com.google.gson.Gson
import net.minestom.server.MinecraftServer
import java.io.FileReader

fun main(args: Array<String>) {
    val server = MinecraftServer.init()
    val config = Gson().fromJson(FileReader(Sabre.CONFIG_LOCATION), Config::class.java)

    // The IP and port are currently grabbed from the config file
    server.start(config.ip, config.port)
}

object Sabre {
    const val CONFIG_LOCATION = "./sabre-config.json"
}