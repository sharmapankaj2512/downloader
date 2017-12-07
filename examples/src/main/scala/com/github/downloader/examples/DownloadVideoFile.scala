package com.github.downloader.examples

import com.github.downloader.{CommandLineProgressBar, DownloadManager, File, RemoteResource}

object DownloadVideoFile extends App {
  private val url = "http://mirrors.standaloneinstaller.com/video-sample/jellyfish-25-mbps-hd-hevc.3gp"

  private val file = File(url)
  private val totalSize = RemoteResource(url).size()
  private val progressBar = CommandLineProgressBar(totalSize)

  DownloadManager(progressBar, file).startDownload(url)
}
