package fr.pottime.progressbar

data class ProgressBarOptions(
    val renderInterval: Long = 100L,
    val showSpeed: Boolean = true,
    val showTime: Boolean = true,
    val showTicks: Boolean = true,
    val showPercentage: Boolean = true,
)
