package fr.xebia.config

import fr.xebia.model.{Category, ModelClass}

case class ModelClassJsonConfig(name: String, translations: List[TranslationJsonConfig]) {
  def toModelClass: ModelClass = ModelClass(
    name,
    Category.urlPlaceholder,
    translations.map(_.toTranslation)
  )
}