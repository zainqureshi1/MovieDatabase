package com.test24i.moviedatabase.helpers

import android.content.Context
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.test24i.moviedatabase.utils.Consts
import com.test24i.moviedatabase.utils.SingletonHolder

class YoutubeUrlConverter private constructor (private val context: Context)
    : SingleConversionListener {

    companion object : SingletonHolder<YoutubeUrlConverter, Context>(::YoutubeUrlConverter)

    private var videoKeys: List<String>? = null
    private var index: Int = -1

    private var convertedUrls = ArrayList<String>()
    private var listener: UrlConverterListener? = null

    fun setListener(listener: UrlConverterListener) {
        this.listener = listener
    }

    fun convertKeysToUrls(videoKeys: List<String>) {
        this.videoKeys = videoKeys
        convertedUrls.clear()
        index = -1
        extractNext()
    }

    private fun extractNext() {
        val videoKeys = videoKeys ?: return
        index += 1
        if (index >= videoKeys.size) {
            listener?.onUrlsConverted(convertedUrls)
            return
        }
        val url = Consts.YOUTUBE_VIDEO_URL + videoKeys.get(index)
        Extractor(context, this).extract(url, true, true)
    }

    override fun onConverted(url: String?) {
        if (url != null) convertedUrls.add(url)
        extractNext()
    }

    private class Extractor(context: Context, val listener: SingleConversionListener): YouTubeExtractor(context) {
        override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
            if (ytFiles != null) {
                val itag = 22
                val downloadUrl = ytFiles.get(itag).url
                listener.onConverted(downloadUrl)
            } else {
                listener.onConverted(null)
            }
        }
    }

}

private interface SingleConversionListener {
    fun onConverted(url: String?)
}

interface UrlConverterListener {
    fun onUrlsConverted(urls: List<String>)
}