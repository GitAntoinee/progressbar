package fr.pottime.progressbar

enum class ProgressBarStyle(
    val begin: String,
    val end: String,
    val block: Char,
    val free: Char,
    val fractionChars: CharArray,
    val length: Int = begin.length + end.length,
) {
    COLORFUL_UNICODE("\u001b[33m│", "│\u001b[0m", '█', ' ', charArrayOf('▏', '▎', '▍', '▌', '▋', '▊', '▉'), 2),
    UNICODE("|", "|", '█', ' ', charArrayOf('▏', '▎', '▍', '▌', '▋', '▊', '▉')),
    ASCII("[", "]", '=', ' ', charArrayOf('>'));
}
