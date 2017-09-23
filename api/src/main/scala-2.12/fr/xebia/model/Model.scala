package fr.xebia.model

import java.io.{File, FileOutputStream}
import java.nio.file.Files

import com.amazonaws.services.s3.model.S3ObjectSummary
import fr.xebia.config.ModelJsonFormats
import spray.json.{DefaultJsonProtocol, RootJsonFormat, _}

case class Model(version: Int, createdAt: String, categories: List[Category]) {
}

object Model extends DefaultJsonProtocol with ModelJsonFormats {

  implicit object ModelFormat extends RootJsonFormat[Model] {
    override def write(model: Model): JsValue = JsObject(Map(
      "version" -> model.version.toJson,
      "created_at" -> model.createdAt.toJson,
      "categories" -> model.categories.toJson
    ))

    override def read(json: JsValue): Model = {
      json.asJsObject.getFields("version", "created_at", "categories") match {
        case Seq(version, createdAt, categories) =>
          Model(version.convertTo[Int], createdAt.convertTo[String], categories.convertTo[List[Category]])
        case other ⇒ deserializationError("Cannot deserialize ProductItem: invalid input. Raw input: " + other)
      }
    }
  }

  def apply(version: String, modelFiles: List[S3ObjectSummary])(implicit s3Client: S3Model): Option[Model] = {
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
}