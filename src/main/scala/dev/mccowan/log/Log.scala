package dev.mccowan.log

import Log._
import java.io.{PrintWriter, Writer, OutputStreamWriter}
import java.util.Date

/**
 * Probably hugely non-performant, but simple, logging utility.
 * @author mccowan
 */
object Log {
  lazy val DefaultWriter = new PrintWriter(System.err, true)

  object Level extends Enumeration {
    val Error, Warning, Information, Debug = Value
  }

  def apply[T](clazz: Class[T]) = new Log(clazz, DefaultWriter)
}

class Log[T](clazz: Class[T], outputWriter: PrintWriter) {
  private def emit(level: Level.Value, exception: Option[Throwable], format: String, formatArguments: Any*) = synchronized {
    val logStatement = s"${new Date} [$level] ${clazz.getSimpleName} - %s".format(format.format(formatArguments: _*))
    outputWriter.println(logStatement)
    exception foreach {
      _.printStackTrace(outputWriter)
    }
  }

  def error(exception: Throwable, format: String, formatArguments: Any*) = emit(Level.Error, Option(exception), format, formatArguments: _*)

  def error(format: String, formatArguments: Any*) = emit(Level.Error, None, format, formatArguments: _*)

  def warn(format: String, formatArguments: Any*) = emit(Level.Warning, None, format, formatArguments: _*)

  def info(format: String, formatArguments: Any*) = emit(Level.Information, None, format, formatArguments: _*)

  def debug(format: String, formatArguments: Any*) = emit(Level.Debug, None, format, formatArguments: _*)
}
