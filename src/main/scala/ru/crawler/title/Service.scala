package ru.crawler.title

import cats.effect.IO
import cats.implicits._
import ru.crawler.HttpClient
import ru.crawler.title.Dto.{Pair, TitleResponse}

class Service() {

  def run(urlList: List[String]): TitleResponse = {
    TitleResponse(urlList.traverse(requestTitle).unsafeRunSync())
  }

  private def requestTitle(url: String): IO[Pair] = {
    HttpClient.client.use { _.expect[String](url).map { result =>
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
