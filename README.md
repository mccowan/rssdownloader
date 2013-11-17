#### Overview

rssdownloader is a simple tool for downloading items from RSS feeds (it'll do its best to download from the URL in the `<link/>` element).  You configure it with a list of RSS feed URLs, and when you run it, it will download any items from them that it hasn't seen and downloaded before.

For most use cases, you'll want to set it up to run periodically - it doesn't know how to do this on its own.

#### Building

`git clone git@github.com:mccowan/rssdownloader.git && cd rssdownloader && sbt assembly`

#### Configuring

###### Set it up with your RSS URLs and the directory to which it will download stuff

```
mkdir ~/.rssdownloader
echo download.directory=/dir/path/to/where/i/want/stuff/downloaded > ~/.rssdownloader/general.config
echo http://path.to/my/rss?feed >> ~/.rssdownloader/feeds.config
```

###### Run it every so often

(crontab) ```0,30 * * * * java -jar ~/bin/rssdownloader-assembly-0.1.jar```
