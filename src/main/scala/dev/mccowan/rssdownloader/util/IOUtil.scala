package dev.mccowan.rssdownloader.util

import java.nio.file.Path

/**
 * @author mccowan
 */
object IOUtil {
  /** Simple auto-closing block for resources with close() methods. */
  def automaticallyClosing[SOURCE_TYPE <: {def close()}, EXPR_TYPE](resource: SOURCE_TYPE)(block: SOURCE_TYPE => EXPR_TYPE) =
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }

  def readKeyValueMapFromEqualsSignDelimitedFile(file: Path): Map[String, String] = {
    slurpLines(file).map(_.split("=", 2))
      .map(tokens => (tokens(0), tokens(1)))
      .toMap
  }

  def slurpLines(file: Path) = {
    automaticallyClosing(scala.io.Source.fromFile(file.toFile)) {
      _.getLines().toList
    }
  }
  
  def assertExistsOrCreateFile(path: Path) = {
    
  }
}
