package ru.crawler.title

object Dto {

  case class TitleRequest(urlList: Set[String])
  case class TitleResponse(list: List[Pair])
  case class Pair(url: String, title: String)
  case class ErrorResponse(error: String)
}
