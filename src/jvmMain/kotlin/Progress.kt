package fr.pottime.progressbar

import kotlin.io.use

actual typealias Closeable = java.io.Closeable

actual inline fun <T : Closeable?, R> T.use(block: (T) -> R): R = use(block)
