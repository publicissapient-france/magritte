package fr.xebia.model

import fr.xebia.data.Placeholders
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Category(name: String,
                    thumbnailurl: String,
                    classes: Map[String, ModelClass])

object Category extends DefaultJsonProtocol with Placeholders {
  implicit val categoryFormat: RootJsonFormat[Category] = jsonFormat3(Category.apply)
}
