package fr.xebia.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import fr.xebia.model.{_}
import spray.json.DefaultJsonProtocol

/**
  * /versions                                 => list all versions's ID
  * /versions/ID                              => json doc
  *     {
  *       version: ID,
  *       model: link to /versions/ID/model,
  *       created_at: timestamp,
  *       categories: ["fruits", "vegetables", ...]
  *     }
  * /versions/ID/model                        => download pb file
  * /versions/ID/categories                   => list all available categories
  * /versions/ID/categories/CATEGORY_ID       => json doc
  *     {
  *       name: "fruit",
  *       thumbnail: "http://...",
  *       classes: [
  *         {
  *           label: "apple",
  *           image: "http://...",
  *           translations: {
  *             "fr": "pomme",
  *             "english": "apple",
  *             ...
  *           }
  *         },
  *         ...
  *       ]
  *     }
  */
object Routes extends SprayJsonSupport with DefaultJsonProtocol {

  val modelRoute: Route = complete("toti")
    //path("versions" / Segment / "model") { version =>
    //  failWith(???)
    //}

}
