package fr.pottime.progressbar

data class ProgressState(
    var ticks: Long,
    var maxTicks: Long,
)

val ProgressState.progress: Double
    get() = when {
        ticks == 0L -> 0.0
        ticks < 0L -> error("ticks is out of range")
        maxTicks < 0L -> error("maxTicks is out of range")
        ticks > maxTicks -> error("ticks cannot be higher than maxTicks")
        else -> ticks.toDouble() / maxTicks.toDouble()
    }

val ProgressState.percentage: Double get() = progress * 100
