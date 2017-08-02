package fr.xebia.config

import fr.xebia.model._

case class ModelJsonConfig(version: Int, created_at: String, categories: List[CategoryJsonConfig]) {
  def toModel: Model = {
    Model(
      version,
      created_at,
      categories.map(_.toCategory)
    )
  }
}