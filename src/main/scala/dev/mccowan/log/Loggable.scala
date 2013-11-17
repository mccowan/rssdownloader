package dev.mccowan.log

/**
 * Convenience mix-in for enabling logging.  Mix into companion objects or static elements, not individual instances.
 * @author mccowan
 */
trait Loggable {
  val Log = dev.mccowan.log.Log(getClass)
}
