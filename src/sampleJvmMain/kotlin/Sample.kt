package fr.pottime.progressbar.sample

import fr.pottime.progressbar.ProgressBar
import fr.pottime.progressbar.ProgressBarStyle
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
    val styleName by parser.option(ArgType.Choice(ProgressBarStyle.values().map(ProgressBarStyle::name)), "style")

    parser.parse(args)

    val style = styleName?.let { ProgressBarStyle.valueOf(it) } ?: ProgressBarStyle.UNICODE
    val unit = ProgressUnit.KB_TO_MB

    ProgressBar(ticks.toLong(), maxTicks.toLong(), style, task, unit).use { progressBar ->
        (progressBar.ticks..progressBar.maxTicks).forEach { ticks ->
            progressBar.tickTo(ticks)
            if(Random.nextBits(2) != 2) Thread.sleep(Random.nextLong(1, 10))
        }
    }
}
