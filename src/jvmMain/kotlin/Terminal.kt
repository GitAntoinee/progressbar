package fr.pottime.progressbar

import org.jline.terminal.TerminalBuilder

private val terminal = TerminalBuilder.builder()!!.jansi(true).build()!!

actual val terminalWidth: Int get() = terminal.width - 1
