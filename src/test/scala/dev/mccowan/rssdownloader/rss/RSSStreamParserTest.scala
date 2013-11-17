package dev.mccowan.rssdownloader.rss

import org.scalatest.FlatSpec
import java.io.FileInputStream
import dev.mccowan.rssdownloader.util.TestUtil.TestResourceable
import java.net.URL
import java.util.Date

/**
 * @author mccowan
 */
class RSSStreamParserTest extends FlatSpec with TestResourceable {
  "RSSReader" should "read all of the elements with the appropriate values an XML stream" in {
    val reader = new RSSStreamParser
    val items = reader.read(new FileInputStream(fetchPath("farf.xml").toFile))
    assert(items.toSet === Set(new RSSItem(
      title = "frob title",
      link = new URL("http://link.com"),
      description = "some test data",
      guid = "12345"
    ),new RSSItem(
      title = "",
      link = new URL("http://www.google.com"),
      description = "",
      guid = "a"
    ))) 
  }
}
