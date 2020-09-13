name := "crawler"

version := "0.1"

scalaVersion := "2.13.3"

val http4sVersion = "0.21.7"
val circeVersion = "0.12.3"
val http4sBackend = "2.2.8"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "com.softwaremill.sttp.client" %% "http4s-backend" % http4sBackend,
  "com.softwaremill.sttp.client" %% "async-http-client-backend-cats" % "2.2.8"
)

mainClass in (Compile, run) := Some("ru.crawler.Main")
