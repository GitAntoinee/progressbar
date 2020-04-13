package fr.pottime.progressbar.sample

import fr.pottime.progressbar.ProgressBar
import fr.pottime.progressbar.ProgressBarStyle
import fr.pottime.progressbar.ProgressUnit
import kotlin.random.Random

fun main() {
    ProgressBar(0, 25000, ProgressBarStyle.COLORFUL_UNICODE, "Sample", ProgressUnit.KB_TO_MB).use { progressBar ->
        (progressBar.ticks..progressBar.maxTicks).forEach { ticks ->
            progressBar.tickTo(ticks)
            if(Random.nextBoolean()) Thread.sleep(Random.nextLong(1, 10))
        }
    }
    println()
}
