package fr.pottime.progressbar

actual fun Double.round(digits: Int, trimZero: Boolean): String {
    val formatted = "%.${digits}f".format(this)
    return if (trimZero) formatted.trimEnd('0').trimEnd(',') else formatted
}
