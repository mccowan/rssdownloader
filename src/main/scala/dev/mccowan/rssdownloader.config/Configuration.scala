package dev.mccowan.rssdownloader.config

import java.nio.file.{FileAlreadyExistsException, Files, Paths, Path}
import dev.mccowan.rssdownloader.util.IOUtil
import java.net.URL
import dev.mccowan.log.Loggable
import java.io.FileNotFoundException

/**
 * @author mccowan
 */


object Configuration extends Loggable {

  class ConfigurationException(msg: String) extends RuntimeException(msg)

  val ConfigurationDirectory = Paths.get(System.getProperty("user.home"), ".rssdownloader")
  val ConfigurationFile: Path = ConfigurationDirectory.resolve("general.config")

  object Defaults {
    val FeedFile: Path = ConfigurationDirectory.resolve("feeds.config")
    val DataDirectory = ConfigurationDirectory.resolve("downloaded_guids.data")
  }

  object Keys {
    val FeedFilePath = "path.file.feed"
    val DownloadedGUIDsPath = "path.file.guids"
    val DownloadDirectory = "download.directory"
  }

  /** Creates the expected configuration directory and configuration files that are to be read. */
  private def initializeConfigurationFileStructure() {
    assertConfigurationDirectoryExistsOrCreate(ConfigurationDirectory)
    assertConfigurationFileExistsOrCreateEmptyFile(ConfigurationFile)
  }

  private def assertConfigurationFileExistsOrCreateEmptyFile(file: Path) {
    try {
      Log.debug("Asserting configuration file %s exists, or creating an empty one...", file.toAbsolutePath)
      Files.createFile(file)
      Log.debug("Created directory %s.", file.toAbsolutePath)
    } catch {
      case e: FileAlreadyExistsException => Log.debug("Configuration file at %s already exists.", file.toAbsolutePath)
    }
  }

  private def assertConfigurationDirectoryExistsOrCreate(directory: Path) {
    try {
      Log.debug("Asserting configuration directory %s exists, or creating an empty one...", directory.toAbsolutePath)
      Files.createDirectory(directory)
      Log.debug("Created directory %s.", directory.toAbsolutePath)
    } catch {
      case e: FileAlreadyExistsException => Log.debug("Configuration directory at %s already exists.", directory.toAbsolutePath)
    }
  }
  
  /** The default configuration for this application. */
  lazy val DefaultConfiguration = new Configuration(ConfigurationFile)
}

class Configuration(val configurationFile: Path) {

  import Configuration._

  initializeConfigurationFileStructure()

  val (downloadDirectory, downloadedGUIDsFile, feedFile, feedURLs) = {
    val props = IOUtil.readKeyValueMapFromEqualsSignDelimitedFile(configurationFile)
    val downloadDirectory = props.get(Keys.DownloadDirectory).map(Paths.get(_)).getOrElse({
      val msg = "No %s property found in configuration file at %s.".format(Keys.DownloadDirectory, ConfigurationFile.toAbsolutePath)
      Log.error(msg)
      throw new ConfigurationException(msg)
    })
    assertConfigurationDirectoryExistsOrCreate(downloadDirectory)
    val downloadedGUIDsFile = props.get(Keys.DownloadedGUIDsPath).map(Paths.get(_)).getOrElse(Defaults.DataDirectory)
    val feedFile = props.get(Keys.FeedFilePath).map(Paths.get(_)).getOrElse(Defaults.FeedFile)
    List(feedFile, downloadedGUIDsFile) foreach assertConfigurationFileExistsOrCreateEmptyFile
    val feedURIs = try {
      IOUtil.slurpLines(feedFile).filterNot(_.isEmpty).map(new URL(_))
    } catch {
      case e: FileNotFoundException => throw new ConfigurationException("No feeds file found at %s.".format(feedFile.toAbsolutePath))
    }
    (downloadDirectory, downloadedGUIDsFile, feedFile, feedURIs)
  }
}



