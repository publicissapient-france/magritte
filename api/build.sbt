
name := "magritte-api"
description := "Ceci n'est pas un pom(.xml)"

version := "1.1"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  val akkaV            = "2.5.1"
  val akkaHTTPV        = "10.0.6"
  val scalaMockV       = "3.6.0"
  val scalaTestV       = "3.0.3"
  val logbackV         = "1.2.3"
  val s3mockVersion    = "0.2.3"

  Seq(
    "com.typesafe.akka" %% "akka-slf4j"                        % akkaV,
    "com.typesafe.akka" %% "akka-stream"                       % akkaV,
    "com.typesafe.akka" %% "akka-http"                         % akkaHTTPV,
    "com.typesafe.akka" %% "akka-http-core"                    % akkaHTTPV,
    "com.typesafe.akka" %% "akka-http-spray-json"              % akkaHTTPV,
    "ch.qos.logback"    %  "logback-classic"                   % logbackV,
    //"jp.co.bizreach" %% "aws-s3-scala" % "0.0.11",
   "com.amazonaws" % "aws-java-sdk-s3" % "1.11.149",

    "org.scalatest"     %% "scalatest"                         % scalaTestV       % "test",
    "org.scalamock"     %% "scalamock-scalatest-support"       % scalaMockV       % "test",
    "com.typesafe.akka" %% "akka-http-testkit"                 % akkaHTTPV        % "test",
    "io.findify"        %% "s3mock"                            % s3mockVersion    % "test"
  )
}

enablePlugins(JavaAppPackaging)
dockerRepository := Some("slequeux")
