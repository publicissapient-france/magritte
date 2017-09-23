package fr.xebia.config

import java.io.File

import fr.xebia.model._

case class ModelJsonConfig(version: Int, created_at: String, categories: List[CategoryJsonConfig]) {
  def toModel(modelDir: File): Model = {
    new Model(
      version,
      created_at,
      categories.map(_.toCategory)
    )
  }
}