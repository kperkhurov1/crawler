package ru.crawler

import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO, Resource}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, duration}

object HttpClient {

  private val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
  private implicit val cs: ContextShift[IO] = IO.contextShift(global)

  val client: Resource[IO, Client[IO]] = BlazeClientBuilder[IO](ec)
    .withConnectTimeout(duration.FiniteDuration(200, duration.MILLISECONDS))
    .withRequestTimeout(duration.FiniteDuration(500, duration.MILLISECONDS))
    .resource
}
