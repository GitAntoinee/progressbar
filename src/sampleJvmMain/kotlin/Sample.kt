package fr.pottime.progressbar.sample

import fr.pottime.progressbar.ProgressBar
import fr.pottime.progressbar.ProgressBarStyle
import fr.pottime.progressbar.ProgressState
import fr.pottime.progressbar.ProgressUnit
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlin.random.Random

fun main(args: Array<String>) {
    val parser = ArgParser("progressbar-sample")

    val ticks by parser.option(ArgType.Int).default(0)
    val maxTicks by parser.option(ArgType.Int).default(25000)
    val task by parser.option(ArgType.String)
    val style by parser.option(ArgType.Choice(ProgressBarStyle.values().map(ProgressBarStyle::name)))

    val showSpeed by parser.option(ArgType.Boolean).default(false)
    val showTicks by parser.option(ArgType.Boolean).default(false)
    val showTime by parser.option(ArgType.Boolean).default(false)
    val showPercentage by parser.option(ArgType.Boolean).default(false)

    parser.parse(args)

    ProgressBar(
        ProgressState(ticks.toLong(), maxTicks.toLong()),
        ProgressBarStyle.valueOf(style ?: "ASCII"),
        task,
        ProgressUnit.KB_TO_MB,
        ProgressBarOptions(
            showSpeed = showSpeed,
            showTicks = showTicks,
            showTime = showTime,
            showPercentage = showPercentage,
        )
    ).use { progressBar ->
        (progressBar.ticks..progressBar.maxTicks).forEach { ticks ->
            progressBar.tickTo(ticks)
            if (Random.nextBits(2) != 2) Thread.sleep(Random.nextLong(1, 10))
        }
    }
}
