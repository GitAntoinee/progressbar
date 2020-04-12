package fr.pottime.progressbar

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
    private val percentage get() = "${progressable.state.percentage.round(2)}%"

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
            // `99.99%` (with a progress of ~0.9)
            append(percentage)
            append(' ')
        }

        val suffix = buildString {
            // `ticks/maxTicks`
            append(' ')
            append((progressable.state.ticks / progressable.unit.size).round(2))
            append('/')
            append((progressable.state.maxTicks / progressable.unit.size).round(2))
            append(progressable.unit.name)
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
