package fr.pottime.progressbar

data class ProgressUnit(val name: String, val size: Double) {
    companion object {
        val DEFAULT: ProgressUnit = ProgressUnit("", 1.0)
    }
}
