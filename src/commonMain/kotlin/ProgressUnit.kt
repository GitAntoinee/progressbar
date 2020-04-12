package fr.pottime.progressbar

data class ProgressUnit(val name: String, val sourceUnit: String, val size: Double) {
    companion object {
        val DEFAULT: ProgressUnit = ProgressUnit("", "", 1.0)

        val KB_TO_MB: ProgressUnit = ProgressUnit("MB", "KB", 1000.0)
        val KiB_TO_MiB: ProgressUnit = ProgressUnit("MiB", "KiB", 1024.0)
        val MB_TO_KB: ProgressUnit = ProgressUnit("KB", "MB", 0.1000)
        val MiB_TO_KiB: ProgressUnit = ProgressUnit("KB", "MiB", 0.1024)
    }
}
