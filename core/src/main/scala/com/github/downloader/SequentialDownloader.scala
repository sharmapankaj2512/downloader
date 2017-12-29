package com.github.downloader

import akka.NotUsed
import akka.actor.{ActorSystem, Terminated}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Broadcast, Sink}
import com.github.subscriber.DownloadSubscriber

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class SequentialDownloader(subscribers: List[DownloadSubscriber]) extends Downloader {
  implicit val system: ActorSystem = ActorSystem("downloader")
  implicit val materialize: ActorMaterializer = ActorMaterializer()

  def download(remoteResource: RemoteResource, offset: Long): Unit = {
    val sinks = subscribers.map(subscriber => Sink.foreach[PartialResponse](pr => subscriber.notify(pr)))
    val combined: Sink[PartialResponse, NotUsed] = Sink.combine(sinks.head, sinks.tail.head)(Broadcast(_))

    remoteResource.asStream(offset)
      .map(stream => combined.runWith(stream))
      .getOrElse(Future.unit)
      .onComplete(_ => {
        subscribers.foreach(subscriber => subscriber.completed())
        system.terminate()
      })
  }
}
