package com.test24i.moviedatabase.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.test24i.moviedatabase.R
import com.test24i.moviedatabase.databinding.ActivityVideoBinding
import com.test24i.moviedatabase.helpers.UrlConverterListener
import com.test24i.moviedatabase.helpers.VideoPlayer
import com.test24i.moviedatabase.helpers.YoutubeUrlConverter
import com.test24i.moviedatabase.models.VideosResult
import com.test24i.moviedatabase.utils.Consts
import kotlinx.android.synthetic.main.activity_video.*


class VideoActivity : AppCompatActivity(), UrlConverterListener {

    private lateinit var binding: ActivityVideoBinding

    private var videosResult: VideosResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video)
        binding.lifecycleOwner = this

        videosResult = intent.extras?.getSerializable(Consts.EXTRA_VIDEOS_RESULT) as? VideosResult
        setupViews()
    }

    private fun setupViews() {
        val videosResult = this.videosResult ?: return
        if (videosResult.isEmpty()) {
            return
        }
        VideoPlayer.getInstance(this).setPlayerView(playerView)

        val urlConverter = YoutubeUrlConverter.getInstance(this)
        urlConverter.setListener(this)
        urlConverter.convertKeysToUrls(videosResult.keys(true))
    }

    override fun onUrlsConverted(urls: List<String>) {
        VideoPlayer.getInstance(this).setPlaylist(urls)
    }

}