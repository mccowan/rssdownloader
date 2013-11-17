package dev.mccowan.rssdownloader.util

import java.nio.file.Paths

/**
 * @author mccowan
 */
object TestUtil {
  /** Implemented as a trait for [[dev.mccowan.rssdownloader.util.TestUtil.getClass]]. */
  trait TestResourceable {
    def fetchPath(path: String) = Paths.get(getClass.getResource(path).toURI)
  }
}
