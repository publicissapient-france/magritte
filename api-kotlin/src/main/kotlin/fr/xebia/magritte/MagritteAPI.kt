package fr.xebia.magritte

import fr.xebia.magritte.factory.MagritteLabelFactory
import fr.xebia.magritte.factory.MagritteModelFactory
import fr.xebia.magritte.model.MagritteVersion
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.static
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.module() {
    install(io.ktor.features.DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Routing) {
        get("/version") {
            call.respond(MagritteVersion("v1"))
        }
        get("/labels") {
            call.respond(MagritteLabelFactory().getLabels())
        }
        get("/models") {
            call.respond(MagritteModelFactory().getModels())
        }
        static("/") {
            // TODO
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080, watchPaths = listOf("MagritteAPI"), module = Application::module).start(wait = true)
}