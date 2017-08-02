package fr.xebia

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import fr.xebia.routes.VersionRoutes

object MagritteAPI extends App {

  private val port = 8080
  private val listen = "0.0.0.0"

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val versionsRoutes = new VersionRoutes(sys.env.getOrElse("MODELS_PATH", ""))
  val routes = versionsRoutes.baseRoute
  Http().bindAndHandle(routes, listen, port)

  Logger(getClass.getName).info(s"Listening on $listen:$port")
  Logger(getClass.getName).info(s"MODELS_PATH : ${versionsRoutes.modelsPath}")
}
