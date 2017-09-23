package fr.xebia

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import fr.xebia.model.S3Model
import fr.xebia.routes.VersionRoutes

object MagritteAPI extends App {

  private val port = 8080
  private val listen = "0.0.0.0"

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val s3Client = AmazonS3ClientBuilder.standard()
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
      ???,
      ???)
    ))
    .withRegion(Regions.EU_WEST_1)
    .build()

  implicit val bucketName = "xebia-magritte"
  implicit val s3Model: S3Model = new S3Model(s3Client)

  val versionsRoutes = new VersionRoutes()
  val routes = versionsRoutes.baseRoute
  Http().bindAndHandle(routes, listen, port)

  Logger(getClass.getName).info(s"Listening on $listen:$port")
  Logger(getClass.getName).info(s"MODELS_PATH : ${versionsRoutes}")
}
