package dev.mccowan.rssdownloader.rss

import java.net.URL
import java.util.Date

/**
 * Basic embodiments of an item from an RSS feed.
 * @author mccowan
 */
case class RSSItem(
  title: String,
  link: URL,
  description: String,
  guid: String)
