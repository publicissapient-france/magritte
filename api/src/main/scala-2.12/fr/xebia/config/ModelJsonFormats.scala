package fr.xebia.config

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait ModelJsonFormats extends DefaultJsonProtocol {
  implicit val translationJsonConfig: RootJsonFormat[TranslationJsonConfig] = jsonFormat2(TranslationJsonConfig.apply)
  implicit val modelClassJsonConfig: RootJsonFormat[ModelClassJsonConfig] = jsonFormat2(ModelClassJsonConfig.apply)
  implicit val categoryJsonFormat: RootJsonFormat[CategoryJsonConfig] = jsonFormat2(CategoryJsonConfig.apply)
  implicit val modelJsonFormat: RootJsonFormat[ModelJsonConfig] = jsonFormat3(ModelJsonConfig.apply)
}