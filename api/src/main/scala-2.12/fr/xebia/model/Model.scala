package fr.xebia.model

import java.io.{File, FileOutputStream}
import java.nio.file.Files

import com.amazonaws.services.s3.model.S3ObjectSummary
import spray.json.{DefaultJsonProtocol, RootJsonFormat, _}

case class Model(version: Int, createdAt: String, categories: List[Category], params: Map[String, Any] = Map())

object Model extends DefaultJsonProtocol with CustomJsonFormats {

  implicit object ModelFormat extends RootJsonFormat[Model] {
    override def write(model: Model): JsValue = JsObject(Map(
      "version" -> model.version.toJson,
      "params" -> model.params.toJson,
      "created_at" -> model.createdAt.toJson,
      "categories" -> model.categories.toJson
    ))

    override def read(json: JsValue): Model = {
      val result = json.asJsObject.getFields("version", "created_at", "categories") match {
        case Seq(version, createdAt, categories) =>
          Model(version.convertTo[Int], createdAt.convertTo[String], categories.convertTo[List[Category]])
        case other => deserializationError("Cannot deserialize ProductItem: invalid input. Raw input: " + other)
      }
      val params = json.asJsObject
        .fields.get("params")
        .map(_.convertTo[Map[String, Any]])
        .getOrElse(Map())
      result.copy(params = params)
    }
  }

  def apply(version: String)(implicit s3client: S3Model): Option[Model] =
    apply(s3client.listObjectForModel(version))

  def apply(modelFiles: List[S3ObjectSummary])(implicit s3Client: S3Model): Option[Model] = {
    modelFiles
      .find(_.getKey.contains("model.json"))
      .map(modelDescriptorS3ObjectSummary => {
        val modelDescriptor = s3Client.getS3ObjectContentAsString(modelDescriptorS3ObjectSummary)
        ModelFormat.read(modelDescriptor.parseJson)
      })
  }

  def toFile(model: Model)(implicit s3client: S3Model): File = {
    val file = Files.createTempFile(s"model_${model.version}", "pb").toFile
    val os = new FileOutputStream(file)
    val descriptor = s3client.listObjectForModel(model.version.toString).filter(_.getKey.endsWith("model.pb")).head
    os.write(s3client.getS3ObjectContent(descriptor))
    os.close()
    file
  }

  def listVersions()(implicit s3client: S3Model): List[String] = {
    s3client.listModelVersion()
  }

  def listLabels(model: Model, category: String)(implicit s3client: S3Model): Option[List[Label]] = {
    s3client
      .listLabels(model.version.toString, category)
      .map(list => {
        list.zipWithIndex.map((tuple: (String, Int)) => Label(tuple._2, tuple._1))
      })
  }

  def labelFile(model: Model, category: String)(implicit s3client: S3Model): Option[File] = {
    val file = Files.createTempFile(s"labels_$category", "txt").toFile
    val os = new FileOutputStream(file)
    s3client
      .listObjectForModel(model.version.toString)
      .find(_.getKey.endsWith(s"labels_$category.txt"))
      .map(objectSummary => {
        os.write(s3client.getS3ObjectContent(objectSummary))
        os.close()
        file
      })
  }
}