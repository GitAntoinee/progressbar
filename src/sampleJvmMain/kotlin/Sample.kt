package fr.pottime.progressbar.sample

import fr.pottime.progressbar.ProgressBar
import fr.pottime.progressbar.ProgressBarStyle
import kotlin.random.Random

fun main() {
    ProgressBar(0, 25000, ProgressBarStyle.COLORFUL_UNICODE).use { progressBar ->
        (progressBar.ticks..progressBar.maxTicks).forEach { ticks ->
            progressBar.tickTo(ticks)
            Thread.sleep(Random.nextLong(1, 10))
        }
    }
    println()
}
