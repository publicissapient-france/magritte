package fr.xebia.model

import java.io.File
import java.nio.file.Files

import fr.xebia.config.ModelJsonFormats
import spray.json.{DefaultJsonProtocol, RootJsonFormat, _}

import scala.collection.JavaConversions._

case class Model(version: Int, createdAt: String, categories: List[Category]) {
  def toFile: File = new File(s"src/test/resources/models/$version/model.pb")
}

object Model extends DefaultJsonProtocol with ModelJsonFormats {
  implicit val modelFormat: RootJsonFormat[Model] = jsonFormat3(Model.apply)

  def apply(modelDir: File): Model = {
    Files.newDirectoryStream(modelDir.toPath).find(_.toFile.getName == "model.json") match {
      case None => throw new Exception(s"No model.json file inside modelDir $modelDir")
      case Some(jsonFile) =>
        val fileContent = new String(Files.readAllBytes(jsonFile))
        modelJsonFormat.read(fileContent.parseJson).toModel
    }
  }
}