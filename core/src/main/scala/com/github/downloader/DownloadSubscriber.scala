package com.github.downloader

trait DownloadSubscriber {
  def notify(partialResponse: PartialResponse)
  def end(): Unit
}