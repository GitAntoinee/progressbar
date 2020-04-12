package fr.pottime.progressbar

actual fun Double.round(digits: Int): String = "%.${digits}f".format(this)
