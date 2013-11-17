package dev.mccowan.rssdownloader.control

import dev.mccowan.rssdownloader.config.Configuration
import dev.mccowan.rssdownloader.rss.{RSSItem, RSSStreamParser}
import dev.mccowan.log.Loggable
import java.io.IOException

/**
 * TODO$(user): Class description
 * @author mccowan
 */
object RSSDownloaderController extends Loggable

class RSSDownloaderController(configuration: Configuration) {

  import RSSDownloaderController._

  val downloadTransactionManager = new DownloadTransactionManager(configuration.downloadedGUIDsFile)
  val reader = new RSSStreamParser

  def fetchNewFilesFromFeedsAndDownload() = {
    Log.info("Fetching RSS elements to download.")
    for (
      feedURL <- configuration.feedURLs;
      itemToDownloadCandidate <- reader.read(feedURL)
    ) {
      if (downloadTransactionManager.getHasItemBeenDownloaded(itemToDownloadCandidate)) {
        Log.debug("Skipping download because it was downloaded previously: %s.", itemToDownloadCandidate)
      } else {
        try {
          FileDownloader.downloadWithOverwrite(itemToDownloadCandidate.link, configuration.downloadDirectory)
          downloadTransactionManager.registerDownloadedItem(itemToDownloadCandidate)
        } catch {
          case e: IOException => Log.error(e, "Problem downloading file for %s.", itemToDownloadCandidate)
        }
      }
    }
  }
}
