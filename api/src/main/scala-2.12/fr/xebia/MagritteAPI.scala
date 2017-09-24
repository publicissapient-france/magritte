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

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  Logger(getClass.getName).info("Loading S3 keys from environment variables")
  val accessKey = sys.env("S3_ACCESS_KEY")
  val secretKey = sys.env("S3_SECRET_KEY")

  val s3Client = AmazonS3ClientBuilder.standard()
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
    .withRegion(Regions.EU_WEST_1)
    .build()

  implicit val bucketName: String = "xebia-magritte"
  implicit val s3Model: S3Model = new S3Model(s3Client)

  val versionsRoutes = new VersionRoutes()
  Http().bindAndHandle(versionsRoutes.baseRoute, listen, port)

  Logger(getClass.getName).info(s"Listening on $listen:$port")
}
