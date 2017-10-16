
name := "magritte-api"
description := "Ceci n'est pas un pom(.xml)"

version := "1.3"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  val akkaGroupId = "com.typesafe.akka"
  val akkaV            = "2.5.1"
  val akkaHTTPV        = "10.0.6"
  val scalaMockV       = "3.6.0"
  val scalaTestV       = "3.0.3"
  val logbackV         = "1.2.3"

  val s3mockVersion    = "0.2.3"
  Seq(
    akkaGroupId %% "akka-slf4j"                                % akkaV,
    akkaGroupId %% "akka-stream"                               % akkaV,
    akkaGroupId %% "akka-http"                                 % akkaHTTPV,
    akkaGroupId %% "akka-http-core"                            % akkaHTTPV,
    akkaGroupId %% "akka-http-spray-json"                      % akkaHTTPV,
    "ch.qos.logback"    %  "logback-classic"                   % logbackV,
   "com.amazonaws" % "aws-java-sdk-s3" % "1.11.149",

    "org.scalatest"     %% "scalatest"                         % scalaTestV       % "test",
    "org.scalamock"     %% "scalamock-scalatest-support"       % scalaMockV       % "test",
    akkaGroupId %% "akka-http-testkit"                         % akkaHTTPV        % "test",
    "io.findify"        %% "s3mock"                            % s3mockVersion    % "test"
  )
}

enablePlugins(JavaAppPackaging)
dockerRepository := Some("slequeux")
