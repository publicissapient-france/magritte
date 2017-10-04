package fr.xebia.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class ModelClass(thumbnailurl: String,
                      translations: List[Translation])

case class Translation(lang: String, value: String)

object ModelClass extends DefaultJsonProtocol {
  implicit val translationFormat: RootJsonFormat[Translation] = jsonFormat2(Translation.apply)
  implicit val modelClassFormat: RootJsonFormat[ModelClass] = jsonFormat2(ModelClass.apply)
}