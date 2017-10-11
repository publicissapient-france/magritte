package fr.xebia.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Label(index: Int, value: String)

object Label extends DefaultJsonProtocol {
  implicit val labelFormat: RootJsonFormat[Label] = jsonFormat2(Label.apply)
}