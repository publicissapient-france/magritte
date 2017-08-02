package fr.xebia.config

import fr.xebia.model.Translation

case class TranslationJsonConfig(lang: String, value: String) {
  def toTranslation: Translation = Translation(
    lang,
    value
  )
}