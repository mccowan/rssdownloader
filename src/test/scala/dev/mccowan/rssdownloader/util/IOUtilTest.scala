package dev.mccowan.rssdownloader.util

import org.scalatest.FlatSpec
import dev.mccowan.rssdownloader.util.TestUtil.TestResourceable
import org.scalatest.matchers.ClassicMatchers
import java.io.{FileInputStream, FileReader, BufferedReader}
import scala.io.BufferedSource

/**
 * @author mccowan
 */
class IOUtilTest extends FlatSpec with ClassicMatchers with TestResourceable {
  "IOUtil" should "read equals-sign-delimited key-value from each line" in {
    val expected = Map("key one" -> "value one", "key2" -> "value 2")
    val actual = IOUtil.readKeyValueMapFromEqualsSignDelimitedFile(fetchPath("test.file"))
    assert(actual === expected)
  }
  
  it should "read all the lines from a file like a BufferedSource" in {
    val actual = IOUtil.slurpLines(fetchPath("lines.file"))
    val expected = new BufferedSource(new FileInputStream(fetchPath("lines.file").toFile)).getLines().toList
    assert(actual === expected)
  }
}
