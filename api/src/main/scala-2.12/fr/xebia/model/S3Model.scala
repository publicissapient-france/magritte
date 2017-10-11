package fr.xebia.model

import java.io.ByteArrayOutputStream

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{ObjectListing, S3ObjectSummary}

import scala.collection.JavaConverters._

class S3Model(s3client: AmazonS3)(implicit bucketName: String) {

  def checkBucketExist(): Boolean = ???

  def listModelVersion(): List[String] = {
    val list: ObjectListing = s3client.listObjects(bucketName, "models")
    list.getObjectSummaries
      .asScala
      .filter(_.getKey.startsWith("models/"))
      .map(summary => (summary.getKey.substring(7), summary))
      .filter(_._1.indexOf("/") > 0 - 1)
      .map {
        case (key, summary) => (key.substring(0, key.indexOf("/")), summary)
      }
      .groupBy(_._1)
      .keys
      .toList
  }

  def listLabels(modelVerison: String, categoryName: String): Option[List[String]] = {
    listObjectForModel(modelVerison)
      .find(_.getKey.endsWith(s"labels_$categoryName.txt"))
      .map(getS3ObjectContentAsString)
      .map(_.split("\n"))
      .map(_.toList)
  }

  def listObjectForModel(modelVerison: String): List[S3ObjectSummary] = {
    val list: ObjectListing = s3client.listObjects(bucketName, s"models/$modelVerison")
    list.getObjectSummaries
      .asScala
      .filter(_.getKey.startsWith(s"models/$modelVerison/"))
      .toList
  }

  def getS3ObjectContent(s3ObjectSummary: S3ObjectSummary): Array[Byte] = {
    val s3Object = s3client.getObject(bucketName, s3ObjectSummary.getKey)
    val os = new ByteArrayOutputStream()
    val jsonContent = s3Object.getObjectContent
    Iterator
      .continually(jsonContent.read)
      .takeWhile(-1 !=)
      .foreach(os.write)
    os.toByteArray
  }

  def getS3ObjectContentAsString(s3ObjectSummary: S3ObjectSummary): String = {
    val s3Object = s3client.getObject(bucketName, s3ObjectSummary.getKey)
    val os = new ByteArrayOutputStream()
    val jsonContent = s3Object.getObjectContent
    Iterator
      .continually(jsonContent.read)
      .takeWhile(-1 !=)
      .foreach(os.write)
    os.toString()
  }

}
