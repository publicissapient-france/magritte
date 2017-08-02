package fr.xebia

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import fr.xebia.data.Placeholders
import fr.xebia.model.{Category, Model, ModelClass, Translation}
import fr.xebia.routes.VersionRoutes
import org.scalatest.{FunSpec, Matchers}
import spray.json.DefaultJsonProtocol

class RoutesTest extends FunSpec
  with Matchers
  with ScalatestRouteTest
  with SprayJsonSupport
  with DefaultJsonProtocol
  with Placeholders {



  describe("Routes") {
    val routes = new VersionRoutes("src/test/resources/models").baseRoute

    describe("GET on /versions") {
      it("should return list of models") {
        val version = 20170607
        Get("/versions") ~> routes ~> check {
          status should be(StatusCodes.OK)
          responseAs[List[Int]] shouldBe List(version)
        }
      }
    }

    describe("GET on /version/ID") {
      describe("With a valid model ID") {
        val version = 20170607

        it("should return HTTP 200") {
          Get(s"/versions/$version") ~> routes ~> check {
            status should be(StatusCodes.OK)
          }
        }
        it("should return a specific version") {
          Get(s"/versions/$version") ~> routes ~> check {
            responseAs[Model] shouldBe Model(
              version,
              "2017/02/20 15:47:10",
              List(
                Category(
                  "fruit",
                  urlPlaceholder,
                  List(
                    ModelClass("apple", urlPlaceholder, List(
                      Translation("fr", "pomme"),
                      Translation("en", "apple")
                    )),
                    ModelClass("banana", urlPlaceholder, List(
                      Translation("fr", "banane"),
                      Translation("en", "banana")
                    ))
                  )
                ),
                Category("vegetable", urlPlaceholder, List())
              )
            )
          }
        }
      }

      describe("With a model ID that does not correspond to any existing model") {
        it("should return 404") {
          Get("/versions/123456789") ~> routes ~> check {
            status should be(StatusCodes.NotFound)
          }
        }
      }

      describe("With a model ID that is not a number") {
        it("should return 400 BadRequest") {
          Get("/versions/invalid") ~> routes ~> check {
            status should be(StatusCodes.BadRequest)
          }
        }
      }
    }


    describe("GET on /version/ID/model") {
      describe("With a valid model ID") {
        it("should download the model file") {
          Get(s"/versions/20170607/model") ~> routes ~> check {
            status should be(StatusCodes.OK)
            responseEntity.contentType.mediaType.mainType shouldBe "application"
            responseEntity.contentType.mediaType.subType shouldBe "octet-stream"
            responseEntity.contentLengthOption shouldBe defined
            responseEntity.contentLengthOption.get shouldBe 21939501
            // header("Content-Disposition") shouldBe defined
            // header("Content-Disposition").get shouldBe "attachment; filename=\"model.pb\""
            // failTest("File name not tested")
          }
        }
      }

      describe("With a model ID that does not correspond to any existing model") {
        it("should return HTTP 404 NotFound") {
          Get(s"/versions/123456789/model") ~> routes ~> check {
            status should be(StatusCodes.NotFound)
          }
        }
      }

      describe("With a model ID that is not a number") {
        it("should return HTTP 400 BadRequest") {
          Get(s"/versions/bad_version/model") ~> routes ~> check {
            status should be(StatusCodes.BadRequest)
          }
        }
      }
    }

    describe("GET on /version/ID/categories") {
      describe("With a valid model ID") {
        it("should list all available categories on that model") {
          Get(s"/versions/20170607/categories") ~> routes ~> check {
            status should be(StatusCodes.OK)
            responseAs[List[Category]] should contain theSameElementsAs List(
              Category(
                "fruit",
                urlPlaceholder,
                List(
                  ModelClass("apple", urlPlaceholder, List(
                    Translation("fr", "pomme"),
                    Translation("en", "apple")
                  )),
                  ModelClass("banana", urlPlaceholder, List(
                    Translation("fr", "banane"),
                    Translation("en", "banana")
                  ))
                )
              ),
              Category("vegetable", urlPlaceholder, List())
            )
          }
        }
      }

      describe("With a model ID that does not correspond to any existing model") {
        it("should return HTTP 404 NotFound") {
          Get(s"/versions/123456789/categories") ~> routes ~> check {
            status should be(StatusCodes.NotFound)
          }
        }
      }

      describe("With a model ID that is not a number") {
        it("should return HTTP 400 BadRequest") {
          Get(s"/versions/bad_version/categories") ~> routes ~> check {
            status should be(StatusCodes.BadRequest)
          }
        }
      }
    }

    describe("GET on /version/ID/categories/CATEGORY_ID") {
      describe("With a valid model ID") {
        val version = "20170607"

        describe("With a valid CATEGORY_ID") {
          it("should return this category details") {
            Get(s"/versions/$version/categories/fruit") ~> routes ~> check {
              status should be(StatusCodes.OK)
              responseAs[Category] shouldBe Category(
                "fruit",
                urlPlaceholder,
                List(
                  ModelClass("apple", urlPlaceholder, List(
                    Translation("fr", "pomme"),
                    Translation("en", "apple")
                  )),
                  ModelClass("banana", urlPlaceholder, List(
                    Translation("fr", "banane"),
                    Translation("en", "banana")
                  ))
                )
              )
            }
          }
        }

        describe("With a CATEGORY_ID that does not correspond to any existing category") {
          it("should return HTTP 404") {
            Get(s"/versions/$version/categories/cars") ~> routes ~> check {
              status should be(StatusCodes.NotFound)
            }
          }
        }
      }

      describe("With a model ID that does not correspond to any existing model") {
        it("should return HTTP 404") {
          Get(s"/versions/123456789/categories/fruit") ~> routes ~> check {
            status should be(StatusCodes.NotFound)
          }
        }
      }

      describe("With a model ID that is not a number") {
        it("should return HTTP 500") {
          Get(s"/versions/bad_version/categories/fruit") ~> routes ~> check {
            status should be(StatusCodes.BadRequest)
          }
        }
      }
    }
  }

}
