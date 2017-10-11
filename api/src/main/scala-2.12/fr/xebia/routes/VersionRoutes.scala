package fr.xebia.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentType, MediaType, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, _}
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.settings.RoutingSettings
import fr.xebia.model.{Category, Model, S3Model}
import spray.json.DefaultJsonProtocol

class VersionRoutes(implicit val s3Client: S3Model, val routingSettings: RoutingSettings)
  extends SprayJsonSupport with DefaultJsonProtocol {

  val notFound: Route = complete(StatusCodes.NotFound)
  val badRequest: Route = complete(StatusCodes.BadRequest)

  def myUserPassAuthenticator(credentials: Credentials): Option[String] =
    credentials match {
      case p@Credentials.Provided(id) if p.verify("p4ssw0rd") => Some(id)
      case _ => None
    }

  val securedRoute =
    Route.seal {
      authenticateBasic(realm = "secure site", myUserPassAuthenticator) { userName =>
        baseRoute
      }
    }

  val baseRoute: Route =
    path("versions") {
      get {
        complete(Model.listVersions())
      }
    } ~
      path("versions" / IntNumber / "data") { modelVersion =>
        get {
          findModel(modelVersion).map(model => {
            println(model)
            complete(model)
          }).getOrElse(notFound)
        }
      } ~
      path("versions" / IntNumber / "labels") { modelVersion =>
        parameters("category") { category =>
          get {
            findModel(modelVersion)
              .flatMap(Model.listLabels(_, category))
              .map(complete(_))
              .getOrElse(notFound)
          }
        }
      } ~
      path("versions" / IntNumber / "categories") { modelVersion =>
        get {
          findModel(modelVersion).map(model => {
            complete(model.categories)
          }).getOrElse(notFound)
        }
      } ~
      path("versions" / IntNumber / "categories" / Segment) {
        (modelVersion, categoryId) => {
          get {
            findModel(modelVersion)
              .flatMap(model => {
                findCategory(model, categoryId)
              })
              .map(category => {
                complete(category)
              })
              .getOrElse(notFound)
          }
        }
      } ~
      path("versions" / IntNumber / "model") { modelVersion =>
        get {
          findModel(modelVersion)
            .map((model: Model) => {
              getFromFile(
                Model.toFile(model),
                ContentType(
                  MediaType.applicationBinary("octet-stream", MediaType.NotCompressible)
                )
              )
            }).getOrElse(notFound)
        }
      } ~
      get {
        badRequest
      }

  def findModel(modelVersion: Int): Option[Model] = {
    Model(modelVersion.toString)
  }

  def findCategory(model: Model, categoryId: String): Option[Category] = {
    model.categories.find(_.name == categoryId)
  }

}
