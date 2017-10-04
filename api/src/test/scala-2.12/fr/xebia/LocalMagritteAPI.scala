package fr.xebia

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.amazonaws.auth.{AWSStaticCredentialsProvider, AnonymousAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import fr.xebia.model.S3Model
import fr.xebia.routes.VersionRoutes
import io.findify.s3mock.S3Mock

object LocalMagritteAPI extends App {

  private val port = 8080
  private val listen = "0.0.0.0"

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val s3LocalDir = sys.env("S3_LOCAL")
  implicit val bucketName: String = sys.env("S3_LOCAL_BUCKET")

  val api = S3Mock(port = 8001, dir = s3LocalDir)
  api.start
  val endpoint = new EndpointConfiguration("http://localhost:8001", "us-west-2")

  val s3Client: AmazonS3 = AmazonS3ClientBuilder
    .standard
    .withPathStyleAccessEnabled(true)
    .withEndpointConfiguration(endpoint)
    .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
    .build
  implicit val s3Model: S3Model = new S3Model(s3Client)

  val versionsRoutes = new VersionRoutes()
  Http().bindAndHandle(versionsRoutes.baseRoute, listen, port)

  Logger(getClass.getName).info(s"Listening on $listen:$port")
}
