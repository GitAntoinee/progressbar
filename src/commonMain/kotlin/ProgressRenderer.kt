package fr.pottime.progressbar

import com.soywiz.klock.ISO8601
import com.soywiz.klock.TimeSpan
import kotlin.math.floor

interface Renderable {
    val renderer: ProgressRenderer
}

interface ProgressRenderer {
    fun render(maxLength: Int): String
}

class DefaultProgressRenderer(
    private val progressable: Progressable,
    private val stylable: Stylable,
) : ProgressRenderer {
    private val percentage get() = "${progressable.state.percentage.round(2)}%".padEnd(7)
    private val ticks get() = (progressable.state.ticks / progressable.unit.size).round(2).padStart(maxTicks.length + 3)
    private val maxTicks get() = (progressable.state.maxTicks / progressable.unit.size).round(2)
    private val ticksPerSecond get() = (progressable.ticksPerSecond / progressable.unit.size).round(2, false)
    private val elapsed get() = ISO8601.TIME_LOCAL_COMPLETE.format(progressable.elapsedDate)
    private val remaining
        get() = ISO8601.TIME_LOCAL_COMPLETE.format(when (progressable.state.ticks) {
            0L -> TimeSpan.ZERO
            else -> {
                TimeSpan(progressable.elapsedDate.milliseconds / progressable.state.ticks * (progressable.state.maxTicks - progressable.state.ticks))
            }
        })

    private fun barBlock(barLength: Int): Int = (progressable.state.progress * barLength).toInt()
    private fun barFree(barLength: Int): Int = barLength - barBlock(barLength)
    private fun barFractionalBlock(barLength: Int): Char {
        val progression = progressable.state.progress * barLength
        val index = (progression - floor(progression)) * stylable.style.fractionChars.size
        return stylable.style.fractionChars[floor(index).toInt()]
    }

    override fun render(maxLength: Int): String {
        // TODO : Reduce buildString invokes

        val prefix = buildString {
            // `Task 99.99%` (with a progress of ~0.9)
            if (progressable.task != null) {
                append(progressable.task)
                append(' ')
            }

            if(progressable.options.showPercentage) {
                append(percentage)
                append(' ')
            }
        }

        val suffix = buildString {
            // `5/10 5/s (00:00:00)` (ticks is 5, max ticks is 10 and ticks per second is 5)

            if (progressable.options.showTicks) {
                append(' ')

                append(ticks)
                append('/')
                append(maxTicks)
                append(progressable.unit.name)
            }

            if (progressable.options.showSpeed) {
                append(' ')

                append(ticksPerSecond)
                append(progressable.unit.name)
                append("/s")
            }

            if (progressable.options.showTime) {
                append(' ')

                append('(')
                append(elapsed)
                append(" / ")
                append(remaining)
                append(')')
            }
        }

        val progressbar = buildString {
            // `[====>     ]` (with a length of 10)
            val barLength = maxLength - prefix.length - suffix.length - stylable.style.length
            append(stylable.style.begin)
            repeat(barBlock(barLength)) { append(stylable.style.block) }
            append(barFractionalBlock(barLength))
            repeat(barFree(barLength)) { append(stylable.style.free) }
            append(stylable.style.end)
        }

        return buildString(maxLength) {
            append(prefix)
            append(progressbar)
            append(suffix)
        }
    }
}
