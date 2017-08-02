
name := "magritte-api"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  val akkaV            = "2.5.1"
  val akkaHTTPV        = "10.0.6"
  val scalaMockV       = "3.6.0"
  val scalaTestV       = "3.0.3"
  val logbackV         = "1.2.3"

  Seq(
    "com.typesafe.akka" %% "akka-slf4j"                        % akkaV,
    "com.typesafe.akka" %% "akka-stream"                       % akkaV,
    "com.typesafe.akka" %% "akka-http"                         % akkaHTTPV,
    "com.typesafe.akka" %% "akka-http-core"                    % akkaHTTPV,
    "com.typesafe.akka" %% "akka-http-spray-json"              % akkaHTTPV,
    "ch.qos.logback"    %  "logback-classic"                   % logbackV,

    "org.scalatest"     %% "scalatest"                         % scalaTestV       % "test",
    "org.scalamock"     %% "scalamock-scalatest-support"       % scalaMockV       % "test",
    "com.typesafe.akka" %% "akka-http-testkit"                 % akkaHTTPV        % "test"
  )
}

enablePlugins(JavaAppPackaging)
dockerRepository := Some("slequeux")
