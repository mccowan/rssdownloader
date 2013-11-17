package dev.mccowan.rssdownloader.control

import dev.mccowan.rssdownloader.util.IOUtil
import java.io._
import dev.mccowan.rssdownloader.rss.RSSItem
import dev.mccowan.log.Loggable
import java.nio.file.Path

/**
 * Simple file-based DAO for querying items that have been downloaded and registering items as downloaded.
 * @author mccowan
 */
object DownloadTransactionManager extends Loggable

class DownloadTransactionManager(private val downloadedGUIDsFile: Path) {
  import DownloadTransactionManager._
  private var downloadedItemGUIDs = IOUtil.slurpLines(downloadedGUIDsFile)
  private lazy val downloadedItemWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(downloadedGUIDsFile.toFile, true))) 
    
  def getHasItemBeenDownloaded(rssItem: RSSItem) = {
    val result = downloadedItemGUIDs.contains(rssItem.guid)
    Log.debug("Checking if %s has been downloaded; result: %s.", rssItem, result)
    result
  }
  
  def registerDownloadedItem(rssItem: RSSItem) = {
    Log.debug("Registering RSS item as downloaded: %s.", rssItem)
    downloadedItemGUIDs ::= rssItem.guid
    downloadedItemWriter.write(rssItem.guid)
    downloadedItemWriter.newLine()
    downloadedItemWriter.flush()
  }
}
