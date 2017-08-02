package fr.xebia.model

import spray.json.DefaultJsonProtocol

case class ModelClass(label: String,
                      imageUrl: String,
                      translations: List[Translation])

case class Translation(lang: String, value: String)

object ModelClass extends DefaultJsonProtocol {
  implicit val translationFormat = jsonFormat2(Translation.apply)
  implicit val modelClassFormat = jsonFormat3(ModelClass.apply)
}