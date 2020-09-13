package ru.crawler.title

import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO}
import cats.implicits._
import ru.crawler.HttpClient
import ru.crawler.title.Dto.{Pair, TitleResponse}

import scala.concurrent.ExecutionContext

class Service() {

  private val ecIO = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
  private implicit val cs: ContextShift[IO] = IO.contextShift(ecIO)

  def run(urlList: List[String]): TitleResponse = {
    TitleResponse(urlList.map(requestTitle).parSequence.unsafeRunSync())
  }

  private def requestTitle(url: String): IO[Pair] = {
    HttpClient.client.use {
      _.expect[String](url).map { result =>

      val title = """<title>(.+)<\/title>""".r
        .findFirstMatchIn(result)
        .map(_.group(1))
        .getOrElse("Title not found")

      Pair(url, title)
    }}.handleError(_ => Pair(url, "Could not get title"))
  }
}

object Service {

  def apply(): Service = new Service()
}
