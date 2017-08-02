package fr.xebia.routes

import akka.event.slf4j.Logger
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentType, MediaType, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, _}
import akka.http.scaladsl.server.Route
import fr.xebia.model.{Model, Models}
import spray.json.DefaultJsonProtocol

/**
  *
  * /versions                                 => list all versions's ID
  * /versions/ID                              => json doc from model version
  *
  */
class VersionRoutes(val modelsPath: String) extends SprayJsonSupport with DefaultJsonProtocol {

  private val models = new Models(modelsPath)

  val baseRoute: Route =
    pathPrefix("versions") {
      pathEnd {
        get {
          complete(models.listAll().map(_.version))
        }
      } ~
        pathPrefix(IntNumber) { modelVersion =>
          modelRoute(modelVersion)
        } ~
        complete(StatusCodes.BadRequest, s"Version should be a number")
    }

  def modelRoute(modelVersion: Int): Route = {
    models.listAll().find(_.version == modelVersion) match {
      case None =>
        get {
          complete(StatusCodes.NotFound)
        }
      case Some(model) =>
        (pathEnd & get) {
          complete(model)
        } ~
          (path("model") & get) {
            // FIXME add filename
            getFromFile(
              model.toFile,
              ContentType(
                MediaType.applicationBinary("octet-stream", MediaType.NotCompressible)
              )
            )
          } ~
          pathPrefix("categories") {
            categoriesRoute(model)
          }
    }
  }

  def categoriesRoute(model: Model): Route = {
    (pathEnd & get) {
      complete(model.categories)
    } ~
      pathPrefix(Segment) { categoryId =>
        categoryRoute(model, categoryId)
      }
  }

  def categoryRoute(model: Model, categoryId: String): Route = {
    model.categories.find(_.name == categoryId) match {
      case None =>
        get {
          complete(StatusCodes.NotFound)
        }
      case Some(category) =>
        (pathEnd & get) {
          complete(category)
        }
    }
  }

}
