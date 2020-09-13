package ru.crawler.title

import cats.data.{NonEmptyChain, Validated}
import cats.implicits._
import ru.crawler.title.Dto.TitleRequest

object Validator {
  val maxUrlListLength = 10

  def validate(req: TitleRequest): Validated[NonEmptyChain[String], List[String]] = {
    validateLength(req.urlList.toList).andThen((urlList: List[String]) => urlList.map(validateUrl).sequence)
  }

  private def validateUrl(url: String): Validated[NonEmptyChain[String], String] =
    if (url.matches("^(?:http(s)?:\\/\\/)[\\w.-]+$")) url.validNec
    else s"Invalid url: '$url'".invalidNec

  private def validateLength(urlList: List[String]): Validated[NonEmptyChain[String], List[String]] =
    if (urlList.size <= maxUrlListLength) urlList.validNec
    else s"The length of 'urlList' should be less or equals $maxUrlListLength".invalidNec

  case class ValidationErrors(validationErrors: NonEmptyChain[String])
}
