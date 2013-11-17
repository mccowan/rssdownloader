package dev.mccowan.rssdownloader.rss

import java.io.InputStream
import dev.mccowan.log.Loggable
import java.net.URL
import dev.mccowan.rssdownloader.util.IOUtil

/**
 * Parser generating [[dev.mccowan.rssdownloader.rss.RSSItem]] from streams.
 * @author mccowan
 */
object RSSStreamParser extends Loggable {
  object Label {
    // Root elements
    val RSS = "rss"
    val Channel = "channel"
    val Item = "item"

    // Item elements
    val Title = "title"
    val Link = "link"
    val GUID = "guid"
    val Description = "description"
    val Enclosure = "enclosure"
  }
}

class RSSStreamParser {
  import dev.mccowan.rssdownloader.rss.RSSStreamParser._
  
  /** Convenience method for [[dev.mccowan.rssdownloader.rss.RSSStreamParser.read()]]. */ 
  def read(url: URL): Traversable[RSSItem] = {
    Log.info("Reading RSS items from %s ...", url)
    IOUtil.automaticallyClosing(url.openStream)(read)
  }
  
  /** Attempst to read RSS elements from the provided stream, which is expected to start at the root of the rss tree. */
  def read(rssXmlStream: InputStream): Traversable[RSSItem] = {
    val xmlData = xml.XML.load(rssXmlStream) 
    val items = xmlData \ Label.Channel \ Label.Item
    val parsedItems = items flatMap {
      itemElement =>
        try {
          val item = RSSItem(
            title = (itemElement \ Label.Title).text,
            link = new URL(itemElement \ Label.Link text),
            guid = (itemElement \ Label.GUID).text,
            description = (itemElement \ Label.Description).text
          )
          Log.info("Successfully read item %s.", item.guid)
          Option(item)
        } catch {
          case e: Exception => 
            Log.error(e, "Problem parsing XML; skipping. Problem element starts with: %s", itemElement.mkString.take(100))
            None
        }
    }
    Log.info("Found %s items in the provided stream and parsed %s.", items.size, parsedItems.size)
    parsedItems
  }
}
