package fr.xebia

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.amazonaws.auth.{AWSStaticCredentialsProvider, AnonymousAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import fr.xebia.data.Placeholders
import fr.xebia.model._
import fr.xebia.routes.VersionRoutes
import io.findify.s3mock.S3Mock
import org.scalatest.{BeforeAndAfterAll, FunSpec, Matchers}
import spray.json.DefaultJsonProtocol

class RoutesTest extends FunSpec
  with Matchers
  with ScalatestRouteTest
  with SprayJsonSupport
  with DefaultJsonProtocol
  with BeforeAndAfterAll
  with Placeholders {

  val api = S3Mock(port = 8001, dir = "src/test/")
  api.start
  val endpoint = new EndpointConfiguration("http://localhost:8001", "us-west-2")
  val s3Client: AmazonS3 = AmazonS3ClientBuilder
    .standard
    .withPathStyleAccessEnabled(true)
    .withEndpointConfiguration(endpoint)
    .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
    .build
  implicit val bucketName = "resources"
  implicit val s3Model = new S3Model(s3Client)

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
                      "fruit",
                      "http://placeholder.xebia.fr",
                      Map(
                        "apple" -> ModelClass(
                          "http://placeholder.xebia.fr",
                          List(
                            Translation("fr", "pomme"),
                            Translation("en", "apple")
                          )),
                        "banana" -> ModelClass(
                          "http://placeholder.xebia.fr",
                          List(
                            Translation("fr", "banane"),
                            Translation("en", "banana")
                          ))
                      )
                    ),
                    Category("vegetable", "http://placeholder.xebia.fr", Map())
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

          describe("With a model ID that is not a number") {
            it("should return 400 BadRequest") {
              Get("/versions/invalid/data") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.BadRequest)
              }
            }
          }
        }

        describe("GET on /version/ID/labels?category=fruit") {
          describe("With a valid model id") {
            val version = 20170607

            describe("With a known category") {
              it("should retun category details") {
                val category = "fruit"
                Get(s"/versions/$version/labels?category=$category") ~> addCredentials(validCredentials) ~> routes ~> check {
                  responseAs[List[Label]] shouldBe List(
                    Label(0, "kiwi"),
                    Label(1, "grape"),
                    Label(2, "apple"),
                    Label(3, "plum"),
                    Label(4, "strawberry"),
                    Label(5, "mango"),
                    Label(6, "pineapple"),
                    Label(7, "orange"),
                    Label(8, "raspberry"),
                    Label(9, "banana")
                  )
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

          describe("With a model ID that is not a number") {
            val category = "fruit"
            it("should return 400 BadRequest") {
              Get(s"/versions/invalid/labels?category=$category") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.BadRequest)
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
                // header("Content-Disposition") shouldBe defined
                // header("Content-Disposition").get shouldBe "attachment; filename=\"model.pb\""
                // failTest("File name not tested")
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

          describe("With a model ID that is not a number") {
            it("should return HTTP 400 BadRequest") {
              Get(s"/versions/bad_version/model") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.BadRequest)
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
                    "http://placeholder.xebia.fr",
                    Map(
                      "apple" -> ModelClass(
                        "http://placeholder.xebia.fr",
                        List(
                          Translation("fr", "pomme"),
                          Translation("en", "apple")
                        )),
                      "banana" -> ModelClass(
                        "http://placeholder.xebia.fr",
                        List(
                          Translation("fr", "banane"),
                          Translation("en", "banana")
                        ))
                    )
                  ),
                  Category("vegetable", "http://placeholder.xebia.fr", Map())
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

          describe("With a model ID that is not a number") {
            it("should return HTTP 400 BadRequest") {
              Get(s"/versions/bad_version/categories") ~> addCredentials(validCredentials) ~> routes ~> check {
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
                Get(s"/versions/$version/categories/fruit") ~> addCredentials(validCredentials) ~> routes ~> check {
                  status should be(StatusCodes.OK)
                  responseAs[Category] shouldBe Category(
                    "fruit",
                    "http://placeholder.xebia.fr",
                    Map(
                      "apple" -> ModelClass(
                        "http://placeholder.xebia.fr",
                        List(
                          Translation("fr", "pomme"),
                          Translation("en", "apple")
                        )),
                      "banana" -> ModelClass(
                        "http://placeholder.xebia.fr",
                        List(
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

          describe("With a model ID that is not a number") {
            it("should return HTTP 400 BadRequest") {
              Get(s"/versions/bad_version/categories/fruit") ~> addCredentials(validCredentials) ~> routes ~> check {
                status should be(StatusCodes.BadRequest)
              }
            }
          }
        }
      }
    }
  }
}
