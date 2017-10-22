package fr.xebia

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.model.{ContentTypes, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import fr.xebia.data.Placeholders
import fr.xebia.model._
import fr.xebia.routes.VersionRoutes
import org.scalatest.{BeforeAndAfterAll, FunSpec, Matchers}
import spray.json.DefaultJsonProtocol

class RoutesTest extends FunSpec
  with Matchers
  with ScalatestRouteTest
  with SprayJsonSupport
  with DefaultJsonProtocol
  with BeforeAndAfterAll
  with Placeholders
  with ModelMock {

  val apple = "apple"
  val banana = "banana"
  val localeFr = "fr"
  val localeEn = "en"

  describe("Routes") {
    val routes = new VersionRoutes().securedRoute

    describe("On any path") {
      describe("With no credentials") {
        it("should return a 'Unauthorized' response") {
          Get("/versions") ~> routes ~> check {
            status should be(StatusCodes.Unauthorized)
          }
        }
      }

      describe("With invalid credentials") {
        val invalidCredentials = BasicHttpCredentials("Peter", "pan")
        it("should return a 'Unauthorized' response") {
          Get("/versions") ~> addCredentials(invalidCredentials) ~> routes ~> check {
            status should be(StatusCodes.Unauthorized)
          }
        }
      }

      describe("With valid credentials") {
        val validCredentials = BasicHttpCredentials("toto", "p4ssw0rd")

        describe("Any request") {
          it("should return a 'OK' response") {
            Get("/versions") ~> addCredentials(validCredentials) ~> routes ~> check {
              status should be(StatusCodes.OK)
            }
          }
        }

        describe("GET on /versions") {
          it("should return list of models") {
            val version = "20170607"
            Get("/versions") ~> addCredentials(validCredentials) ~> routes ~> check {
              status should be(StatusCodes.OK)
              responseAs[List[String]] shouldBe List(version)
            }
          }
        }

        describe("GET on /version/ID/data") {
          describe("With a valid model ID") {
            val version = 20170607

            it("should return HTTP 200") {
              Get(s"/versions/$version/data") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.OK)
              }
            }
            it("should return a specific version") {
              Get(s"/versions/$version/data") ~> addCredentials(validCredentials) ~> routes ~> check {
                responseAs[Model] shouldBe new Model(
                  version,
                  "2017/02/20 15:47:10",
                  List(
                    Category(
                      localeFr + "uit",
                      urlPlaceholder,
                      Map(
                        apple -> ModelClass(
                          urlPlaceholder,
                          List(
                            Translation(localeFr, "pomme"),
                            Translation(localeEn, apple)
                          )),
                        banana -> ModelClass(
                          urlPlaceholder,
                          List(
                            Translation(localeFr, "banane"),
                            Translation(localeEn, banana)
                          ))
                      )
                    ),
                    Category("vegetable", urlPlaceholder, Map())
                  ),
                  Map(
                    "INPUT_SIZE" -> 224,
                    "IMAGE_MEAN" -> 224,
                    "IMAGE_STD" -> 224,
                    "INPUT_NAME" -> "input"
                  )
                )
              }
            }
          }

          describe("With a model ID that does not correspond to any existing model") {
            it("should return 404") {
              Get("/versions/123456789/data") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.NotFound)
              }
            }
          }
        }

        describe("GET on /version/ID/labels?category=fruit") {
          describe("With a valid model id") {
            val version = 20170607

            describe("With a known category") {
              val category = "fruit"
              it("should return category details as JSON") {

                Get(s"/versions/$version/labels?category=$category") ~>
                  addHeader("accept", MediaTypes.`application/json`.value) ~>
                  addCredentials(validCredentials) ~>
                  routes ~> check {

                  status should be(StatusCodes.OK)
                  val fruits = List("kiwi", "grape", apple, "plum", "strawberry", "mango", "pineapple", "orange", "raspberry", banana)
                  responseAs[List[Label]] shouldBe (0 to 9).zip(fruits).map {
                    case (idx, label) => Label(idx, label)
                  }
                }
              }
              it("should return category details as File") {

                Get(s"/versions/$version/labels?category=$category") ~>
                  addHeader("accept", MediaTypes.`application/octet-stream`.value) ~>
                  addCredentials(validCredentials) ~>
                  routes ~> check {

                  status should be(StatusCodes.OK)
                  responseEntity.contentType shouldBe ContentTypes.`application/octet-stream`
                  responseEntity.contentLengthOption shouldBe defined
                  responseEntity.contentLengthOption.get shouldBe 73
                  header("Content-Disposition") shouldBe defined
                  header("Content-Disposition").get.value() shouldBe "attachment; filename=\"labels_fruit.txt\""
                }
              }
            }

            describe("With an unknown category") {
              val category = "something"
              it("should return HTTP 404") {
                Get(s"/versions/$version/labels?category=$category") ~> addCredentials(validCredentials) ~> routes ~> check {
                  status should be(StatusCodes.NotFound)
                }
              }
            }
          }

          describe("With a model ID that does not correspond to any existing model") {
            val category = "fruit"
            it("should return 404") {
              Get(s"/versions/123456789/labels?category=$category") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.NotFound)
              }
            }
          }
        }

        describe("GET on /version/ID/model") {
          describe("With a valid model ID") {
            it("should download the model file") {
              Get(s"/versions/20170607/model") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.OK)
                responseEntity.contentType.mediaType.mainType shouldBe "application"
                responseEntity.contentType.mediaType.subType shouldBe "octet-stream"
                responseEntity.contentLengthOption shouldBe defined
                responseEntity.contentLengthOption.get shouldBe 21939501
                header("Content-Disposition") shouldBe defined
                header("Content-Disposition").get.value() shouldBe "attachment; filename=\"model.pb\""
              }
            }
          }

          describe("With a model ID that does not correspond to any existing model") {
            it("should return HTTP 404 NotFound") {
              Get(s"/versions/123456789/model") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.NotFound)
              }
            }
          }
        }

        describe("GET on /version/ID/categories") {
          describe("With a valid model ID") {
            it("should list all available categories on that model") {
              Get(s"/versions/20170607/categories") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.OK)
                responseAs[List[Category]] should contain theSameElementsAs List(
                  Category(
                    "fruit",
                    urlPlaceholder,
                    Map(
                      apple -> ModelClass(
                        urlPlaceholder,
                        List(
                          Translation("fr", "pomme"),
                          Translation("en", apple)
                        )),
                      banana -> ModelClass(
                        urlPlaceholder,
                        List(
                          Translation("fr", "banane"),
                          Translation("en", banana)
                        ))
                    )
                  ),
                  Category("vegetable", urlPlaceholder, Map())
                )
              }
            }
          }

          describe("With a model ID that does not correspond to any existing model") {
            it("should return HTTP 404 NotFound") {
              Get(s"/versions/123456789/categories") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.NotFound)
              }
            }
          }
        }

        describe("GET on /version/ID/categories/CATEGORY_ID") {
          describe("With a valid model ID") {
            val version = "20170607"

            describe("With a valid CATEGORY_ID") {
              it("should return this category details") {
                Get(s"/versions/$version/categories/fruit") ~> addCredentials(validCredentials) ~> routes ~> check {
                  status should be(StatusCodes.OK)
                  responseAs[Category] shouldBe Category(
                    "fruit",
                    urlPlaceholder,
                    Map(
                      apple -> ModelClass(
                        urlPlaceholder,
                        List(
                          Translation("fr", "pomme"),
                          Translation("en", apple)
                        )),
                      banana -> ModelClass(
                        urlPlaceholder,
                        List(
                          Translation("fr", "banane"),
                          Translation("en", banana)
                        ))
                    )
                  )
                }
              }
            }

            describe("With a CATEGORY_ID that does not correspond to any existing category") {
              it("should return HTTP 404") {
                Get(s"/versions/$version/categories/cars") ~> addCredentials(validCredentials) ~> routes ~> check {
                  status should be(StatusCodes.NotFound)
                }
              }
            }
          }

          describe("With a model ID that does not correspond to any existing model") {
            it("should return HTTP 404") {
              Get(s"/versions/123456789/categories/fruit") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.NotFound)
              }
            }
          }
        }
      }
    }
  }
}
