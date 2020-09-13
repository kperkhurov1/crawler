package ru.crawler

import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO, Resource}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.{ExecutionContext, duration}

object HttpClient {

  private val ecHttp = ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())
  private val ecIO = ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())
  private implicit val cs: ContextShift[IO] = IO.contextShift(ecIO)

  val client: Resource[IO, Client[IO]] = BlazeClientBuilder[IO](ecHttp)
    .withConnectTimeout(duration.FiniteDuration(200, duration.MILLISECONDS))
    .withRequestTimeout(duration.FiniteDuration(500, duration.MILLISECONDS))
    .resource
}
