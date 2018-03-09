package fr.xebia.magritte

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.module() {
    install(io.ktor.features.DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        get("/") {
            call.respondText("Magritte API", ContentType.Text.Html)
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080, watchPaths = listOf("MagritteAPI"), module = Application::module).start(wait = true)
}