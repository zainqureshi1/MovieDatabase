package com.test24i.moviedatabase.helpers

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.test24i.moviedatabase.R
import com.test24i.moviedatabase.utils.SingletonHolder
import timber.log.Timber
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource

class VideoPlayer private constructor (context: Context) : Player.EventListener {

    companion object : SingletonHolder<VideoPlayer, Context>(::VideoPlayer)

    private val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)
    private val dataSourceFactory: DataSource.Factory

    init {
        player.addListener(this)

        val appName = context.getString(R.string.app_name)
        dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, appName))
    }

    fun setPlayerView(playerView: PlayerView) {
        player.stop(true)
        playerView.player = player
    }

    fun setPlaylist(videoUrls: List<String>) {
        val concatenatedSource = ConcatenatingMediaSource()
        videoUrls.forEach {
            val uri = Uri.parse(it)
            val mediaSource: MediaSource
            if (it.toUpperCase().contains("M3U8")) {
                mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            } else {
                mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            }
            concatenatedSource.addMediaSource(mediaSource)
        }
        player.prepare(concatenatedSource)
        player.playWhenReady = true
    }

    fun release() = player.release()

    override fun onPlayerError(error: ExoPlaybackException?) {
        super.onPlayerError(error)
        Timber.e(error)
    }

}