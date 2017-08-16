package fr.xebia.model

import java.io.File
import java.nio.file.Files

import fr.xebia.config.ModelJsonFormats
import spray.json.{DefaultJsonProtocol, RootJsonFormat, _}

import scala.collection.JavaConversions._

case class Model(modelDir: String, version: Int, createdAt: String, categories: List[Category]) {
  def toFile: File = new File(new File(modelDir), "model.pb")
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
          Model("", version.convertTo[Int], createdAt.convertTo[String], categories.convertTo[List[Category]])
        case other ⇒ deserializationError("Cannot deserialize ProductItem: invalid input. Raw input: " + other)
      }
    }
  }

  def apply(modelDir: File): Model = {
    Files.newDirectoryStream(modelDir.toPath).find(_.toFile.getName == "model.json") match {
      case None => throw new Exception(s"No model.json file inside modelDir $modelDir")
      case Some(jsonFile) =>
        val fileContent = new String(Files.readAllBytes(jsonFile))
        modelJsonFormat.read(fileContent.parseJson).toModel(modelDir)
    }
  }
}