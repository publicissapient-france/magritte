package fr.xebia

import com.amazonaws.auth.{AWSStaticCredentialsProvider, AnonymousAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import fr.xebia.model.S3Model
import io.findify.s3mock.S3Mock

trait ModelMock {
  private val apiPort = 8001
  val api = S3Mock(port = apiPort, dir = "src/test/")
  api.start
  val endpoint = new EndpointConfiguration("http://localhost:8001", "us-west-2")
  val s3Client: AmazonS3 = AmazonS3ClientBuilder
    .standard
    .withPathStyleAccessEnabled(true)
    .withEndpointConfiguration(endpoint)
    .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
    .build
  implicit val bucketName = "resources"
  implicit val s3Model = new S3Model(s3Client)
}
