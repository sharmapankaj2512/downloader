package com.github.downloader.examples

import com.github.downloader.{CommandLineProgressBar, Downloader, File, RemoteResource}

object DownloadVideoFile extends App {
  private val url = "http://mirrors.standaloneinstaller.com/video-sample/jellyfish-25-mbps-hd-hevc.3gp"

  private val file = File(url)
  private val totalSize = RemoteResource(url).size()
  private val downloadedSize = file.downloadedSize()
  private val progressBar = CommandLineProgressBar(totalSize)

  progressBar.tick(downloadedSize)
  Downloader(progressBar, file).startDownload(url, downloadedSize)
}
