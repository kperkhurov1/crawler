package ru.crawler

import cats.data.Validated._
import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._
import ru.crawler.title.Dto.{ErrorResponse, TitleRequest}
import ru.crawler.title.Validator.ValidationErrors
import ru.crawler.title.{Service, Validator}

import scala.concurrent.ExecutionContext.Implicits.global

case class HealthCheckResponse(status: String)

object Main extends IOApp {

  private implicit val urlListDecoder = jsonOf[IO, TitleRequest]

  private val routes = HttpRoutes.of[IO] {
    case GET -> Root / "healthCheck" => Ok(HealthCheckResponse("alive").asJson)
    case req @ POST -> Root / "titles" => req.as[TitleRequest].flatMap {
      Validator.validate(_) match {
        case Valid(urlList) => Service().run(urlList).flatMap(response => Ok(response.asJson))
        case Invalid(e) => BadRequest(ValidationErrors(e).asJson)
      }
    }.handleErrorWith {
      e: Throwable => InternalServerError(ErrorResponse(e.getMessage).asJson)
    }
  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder.apply[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(routes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
