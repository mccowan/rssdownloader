package dev.mccowan.rssdownloader

import dev.mccowan.rssdownloader.control.RSSDownloaderController
import dev.mccowan.rssdownloader.config.Configuration

/**
 * TODO$(user): Class description
 * @author mccowan
 */
object Main extends App {
  val control = new RSSDownloaderController(Configuration.DefaultConfiguration)
  control.fetchNewFilesFromFeedsAndDownload()
}
