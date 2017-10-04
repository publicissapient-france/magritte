package fr.xebia.config

import fr.xebia.model.Category

case class CategoryJsonConfig(name: String, classes: List[ModelClassJsonConfig]) {
  def toCategory: Category = Category(
    name,
    Category.urlPlaceholder,
    classes.map(clazz => (clazz.name, clazz.toModelClass)).toMap
  )
}