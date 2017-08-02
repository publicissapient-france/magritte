package fr.xebia.model

import fr.xebia.data.Placeholders
import spray.json.DefaultJsonProtocol

case class Category(name: String,
                    thumbnailurl: String,
                    classes: List[ModelClass])

object Category extends DefaultJsonProtocol with Placeholders {
  implicit val categoryFormat = jsonFormat3(Category.apply)
}
