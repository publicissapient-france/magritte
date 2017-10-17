package fr.xebia.routes

import akka.http.javadsl.model.headers.ContentDisposition
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.ContentDispositionTypes.attachment
import akka.http.scaladsl.model.headers.`Content-Disposition`
import akka.http.scaladsl.model.{ContentType, HttpHeader, MediaType, StatusCodes}
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
            complete(model)
          }).getOrElse(notFound)
        }
      } ~
      path("versions" / IntNumber / "labels") { modelVersion =>
        jsonCategory(modelVersion)
      } ~
      path("versions" / IntNumber / "labels" / "json") { modelVersion =>
        jsonCategory(modelVersion)
      } ~
      path("versions" / IntNumber / "labels" / "file") { modelVersion =>
        parameters("category") { category =>
          get {
            findModel(modelVersion)
              .flatMap((model: Model) => Model.labelFile(model, category))
              .map(file => {
                respondWithHeader(`Content-Disposition`(attachment, Map("filename" -> s"labels_$category.txt"))) {
                  getFromFile(
                    file,
                    ContentType(
                      MediaType.applicationBinary("octet-stream", MediaType.NotCompressible)
                    )
                  )
                }
              }).getOrElse(notFound)
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
              val modelFile = Model.toFile(model)
              respondWithHeader(`Content-Disposition`(attachment, Map("filename" -> "model.pb"))) {
                getFromFile(
                  modelFile,
                  ContentType(
                    MediaType.applicationBinary("octet-stream", MediaType.NotCompressible)
                  )
                )
              }
            }).getOrElse(notFound)
        }
      } ~
      get {
        badRequest
      }

  private def jsonCategory(modelVersion: Int) = {
    parameters("category") { category =>
      get {
        findModel(modelVersion)
          .flatMap(Model.listLabels(_, category))
          .map(complete(_))
          .getOrElse(notFound)
      }
    }
  }

  def findModel(modelVersion: Int): Option[Model] = {
    Model(modelVersion.toString)
  }

  def findCategory(model: Model, categoryId: String): Option[Category] = {
    model.categories.find(_.name == categoryId)
  }

}
