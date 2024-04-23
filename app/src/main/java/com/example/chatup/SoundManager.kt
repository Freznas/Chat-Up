package com.example.chatup

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundManager(context: Context) {
    var soundPool: SoundPool

    private var messageSound: Int = 0
    private var chatUp: Int = 0

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        messageSound = soundPool.load(context,R.raw.recievedmessage, 1)
        chatUp = soundPool.load(context,R.raw.chatup, 1)
    }

    fun release(){
        soundPool.release()
    }

    fun messageSound(){
        soundPool.play(messageSound,1.0f,1.0f,1,0,1.0f)

    }
    fun chatUpSound(){
        soundPool.play(chatUp,0.1f,0.1f,1,0,1.0f)

    }
}