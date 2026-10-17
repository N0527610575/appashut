package com.example.data

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

class SoundManager {
  private val sampleRate = 44100
  private val scope = CoroutineScope(Dispatchers.Default)

  var isEnabled: Boolean = true

  private fun playPcm(generator: () -> ShortArray) {
    if (!isEnabled) return
    scope.launch {
      try {
        val samples = generator()
        val bufferSize = samples.size * 2
        val audioTrack = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_GAME)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(bufferSize)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()

        audioTrack.write(samples, 0, samples.size)
        audioTrack.play()
        // Wait till finished then release
        val durationMs = (samples.size * 1000L) / sampleRate
        kotlinx.coroutines.delay(durationMs + 50)
        audioTrack.release()
      } catch (_: Exception) {}
    }
  }

  fun playCardFlip() {
    playPcm {
      val durationMs = 120
      val count = (sampleRate * durationMs) / 1000
      val buffer = ShortArray(count)
      for (i in 0 until count) {
        val progress = i.toFloat() / count
        // Bandpass-shaped noise burst simulating paper sliding
        val envelope = (sin(progress * Math.PI) * (1f - progress)).toFloat()
        val noise = (Random.nextFloat() * 2f - 1f)
        val sample = (noise * envelope * 9000).toInt().coerceIn(-32767, 32767)
        buffer[i] = sample.toShort()
      }
      buffer
    }
  }

  fun playCardSlide() {
    playPcm {
      val durationMs = 160
      val count = (sampleRate * durationMs) / 1000
      val buffer = ShortArray(count)
      for (i in 0 until count) {
        val progress = i.toFloat() / count
        val envelope = (sin(progress * Math.PI) * (1f - progress * 0.7f)).toFloat()
        val noise = (Random.nextFloat() * 2f - 1f)
        val sample = (noise * envelope * 8000).toInt().coerceIn(-32767, 32767)
        buffer[i] = sample.toShort()
      }
      buffer
    }
  }

  fun playGlassStoneClink(doubleClink: Boolean = false) {
    playPcm {
      val durationMs = if (doubleClink) 260 else 180
      val count = (sampleRate * durationMs) / 1000
      val buffer = ShortArray(count)

      fun addPing(offsetMs: Int, baseFreq: Double) {
        val startIndex = (sampleRate * offsetMs) / 1000
        val pingDuration = (sampleRate * 0.16).toInt()
        val endIndex = (startIndex + pingDuration).coerceAtMost(count)

        for (i in startIndex until endIndex) {
          val t = (i - startIndex).toDouble() / sampleRate
          val decay = Math.exp(-t * 30.0)
          val s1 = sin(2.0 * Math.PI * baseFreq * t)
          val s2 = sin(2.0 * Math.PI * (baseFreq * 1.48) * t) * 0.55
          val s3 = sin(2.0 * Math.PI * (baseFreq * 2.15) * t) * 0.25
          val value = ((s1 + s2 + s3) * decay * 14000.0).toInt().coerceIn(-32767, 32767)
          buffer[i] = (buffer[i] + value).coerceIn(-32767, 32767).toShort()
        }
      }

      addPing(0, 2450.0)
      if (doubleClink) {
        addPing(85, 2780.0)
      }
      buffer
    }
  }

  fun playDiceRoll() {
    playPcm {
      val durationMs = 420
      val count = (sampleRate * durationMs) / 1000
      val buffer = ShortArray(count)
      val taps = listOf(15, 65, 130, 210, 290, 360)

      taps.forEach { tapMs ->
        val startIndex = (sampleRate * tapMs) / 1000
        val tapDuration = (sampleRate * 0.035).toInt()
        val endIndex = (startIndex + tapDuration).coerceAtMost(count)
        val freq = 550.0 + Random.nextInt(-60, 140)

        for (i in startIndex until endIndex) {
          val t = (i - startIndex).toDouble() / sampleRate
          val decay = Math.exp(-t * 85.0)
          val s = sin(2.0 * Math.PI * freq * t) * decay * 12000.0
          buffer[i] = (buffer[i] + s.toInt()).coerceIn(-32767, 32767).toShort()
        }
      }
      buffer
    }
  }

  fun playVictory() {
    playPcm {
      val durationMs = 700
      val count = (sampleRate * durationMs) / 1000
      val buffer = ShortArray(count)
      // Joyful ascending crystal arpeggio: C5, E5, G5, C6
      val notes = listOf(523.25, 659.25, 783.99, 1046.50)

      notes.forEachIndexed { index, freq ->
        val startMs = index * 120
        val startIndex = (sampleRate * startMs) / 1000
        val noteLength = (sampleRate * 0.32).toInt()
        val endIndex = (startIndex + noteLength).coerceAtMost(count)

        for (i in startIndex until endIndex) {
          val t = (i - startIndex).toDouble() / sampleRate
          val decay = Math.exp(-t * 9.0)
          val s = (sin(2.0 * Math.PI * freq * t) + sin(2.0 * Math.PI * (freq * 2) * t) * 0.3) * decay * 9500.0
          buffer[i] = (buffer[i] + s.toInt()).coerceIn(-32767, 32767).toShort()
        }
      }
      buffer
    }
  }
}
