package dev.mccowan.rssdownloader.control

import java.net.URL
import java.nio.file.{Paths, StandardCopyOption, Files, Path}
import dev.mccowan.log.Loggable
import dev.mccowan.rssdownloader.util.IOUtil

/**
 * TODO$(user): Class description
 * @author mccowan
 */
object FileDownloader extends Loggable {
  def downloadWithOverwrite(url: URL, downloadDirectory: Path) {
    val downloadPath = downloadDirectory.resolve(Paths.get(url.getFile).getFileName)
    Log.info("Downloading from %s to %s ...", url, downloadPath)
    IOUtil.automaticallyClosing(url.openStream()) {
      Files.copy(_, downloadPath, StandardCopyOption.REPLACE_EXISTING)
    }
  }
}
