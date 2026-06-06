package com.example.surveyshowcase

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {

    private var backsoundPlayer: MediaPlayer? = null
    private var clickSoundPlayer: MediaPlayer? = null
    private var restartSoundPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            backsoundPlayer = MediaPlayer.create(this, R.raw.bgm_apk).apply {
                isLooping = true
                setVolume(0.3f, 0.3f)
                start()
            }
        } catch (e: Exception) { e.printStackTrace() }

        try {
            clickSoundPlayer = MediaPlayer.create(this, R.raw.sound_click).apply {
                setVolume(1.0f, 1.0f)
            }
        } catch (e: Exception) { e.printStackTrace() }

        try {
            restartSoundPlayer = MediaPlayer.create(this, R.raw.sound_restart).apply {
                setVolume(1.0f, 1.0f)
            }
        } catch (e: Exception) { e.printStackTrace() }

        setContent {
            SurveyShowcaseApp(
                onPlaySoundPool = {
                    playCardClickSound()
                },
                onPlayRestartSound = {
                    playRestartSound()
                }
            )
        }
    }

    private fun playCardClickSound() {
        try {
            clickSoundPlayer?.let { player ->
                if (player.isPlaying) player.pause()
                player.seekTo(0)
                player.start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun playRestartSound() {
        try {
            restartSoundPlayer?.let { player ->
                if (player.isPlaying) player.pause()
                player.seekTo(0)
                player.start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    override fun onPause() {
        super.onPause()
        backsoundPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        backsoundPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        backsoundPlayer?.stop()
        backsoundPlayer?.release()

        clickSoundPlayer?.stop()
        clickSoundPlayer?.release()

        restartSoundPlayer?.stop()
        restartSoundPlayer?.release()
    }
}