package fr.pottime.progressbar

import org.jline.terminal.TerminalBuilder

actual object Terminal {
    private val terminal = TerminalBuilder.builder()!!.jansi(true).build()!!

    actual val width: Int get() = terminal.width - 1
}
