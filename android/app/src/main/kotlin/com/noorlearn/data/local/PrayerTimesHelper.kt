package com.noorlearn.data.local

import java.util.Calendar
import kotlin.math.sin

data class PrayerTime(
    val name: String,
    val time: String, // e.g. "04:32 AM"
    val icon: String,
    val hour: Int,
    val minute: Int
)

object PrayerTimesHelper {

    fun getPrayerTimes(calendar: Calendar): List<PrayerTime> {
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        // Seasonal variation simulation
        val angle = Math.toRadians((dayOfYear * 360.0 / 365.0))
        val seasonalOffsetMinutes = (45 * sin(angle)).toInt()

        val fajrMin = 270 - seasonalOffsetMinutes // base 04:30
        val dhuhrMin = 745 + (seasonalOffsetMinutes / 10) // base 12:25
        val asrMin = 945 + (seasonalOffsetMinutes / 2) // base 15:45
        val maghribMin = 1145 + seasonalOffsetMinutes // base 19:05
        val ishaMin = 1235 + seasonalOffsetMinutes // base 20:35

        return listOf(
            createPrayerTime("Fajr", fajrMin, "🌅"),
            createPrayerTime("Dhuhr", dhuhrMin, "☀️"),
            createPrayerTime("Asr", asrMin, "⛅"),
            createPrayerTime("Maghrib", maghribMin, "🌇"),
            createPrayerTime("Isha", ishaMin, "🌃")
        )
    }

    private fun createPrayerTime(name: String, minutesSinceMidnight: Int, icon: String): PrayerTime {
        val hour = (minutesSinceMidnight / 60) % 24
        val minute = minutesSinceMidnight % 60
        val amPm = if (hour >= 12) "PM" else "AM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val timeString = "%02d:%02d %s".format(displayHour, minute, amPm)
        return PrayerTime(name, timeString, icon, hour, minute)
    }

    fun getNextPrayer(calendar: Calendar, times: List<PrayerTime>): Pair<PrayerTime, String> {
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentMinutes = currentHour * 60 + currentMinute

        for (time in times) {
            val prayerMinutes = time.hour * 60 + time.minute
            if (prayerMinutes > currentMinutes) {
                val diff = prayerMinutes - currentMinutes
                val hours = diff / 60
                val mins = diff % 60
                val countdown = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
                return Pair(time, countdown)
            }
        }

        // If none found today, the next one is Fajr tomorrow
        val firstPrayer = times.first()
        val diff = (24 * 60 - currentMinutes) + (firstPrayer.hour * 60 + firstPrayer.minute)
        val hours = diff / 60
        val mins = diff % 60
        val countdown = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
        return Pair(firstPrayer, countdown)
    }
}
