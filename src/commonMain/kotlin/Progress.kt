package fr.pottime.progressbar

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan

interface Progressable {
    /**
     * The name of the task of the progress
     */
    val task: String?

    /**
     * The state of the progress
     */
    val state: ProgressState

    /**
     * The unit of the progress
     */
    val unit: ProgressUnit

    /**
     * The ticks per second
     */
    val ticksPerSecond: Long

    /**
     * The options of the progress bar
     */
    val options: ProgressBarOptions

    val startDate: DateTime
    val elapsedDate: TimeSpan get() = DateTime.now() - startDate

    /**
     * Convenience methods
     */
    interface Convenience {
        /**
         * Ticks of the progress
         *
         * @see [state]
         * @see [ProgressState.ticks]
         */
        var ticks: Long

        /**
         * The max ticks of the progress
         *
         * @see [state]
         * @see [ProgressState.maxTicks]
         */
        var maxTicks: Long

        /**
         * The remaining ticks
         *
         * @see [maxTicks]
         * @see [ticks]
         */
        val remainingTicks: Long get() = maxTicks - ticks

        /**
         * Increment [ticks] by 1 and set [Convenience.maxTicks] to [maxTicks] if it is not null
         */
        fun tick(maxTicks: Long? = null)

        /**
         * Increment [ticks] by [n] and set [Convenience.maxTicks] to [maxTicks] if it is not null
         */
        fun tickBy(n: Long, maxTicks: Long? = null)

        /**
         * Set [ticks] to [n] and set [Convenience.maxTicks] to [maxTicks] if it is not null
         */
        fun tickTo(n: Long, maxTicks: Long? = null)
    }
}

interface Stylable {
    val style: ProgressBarStyle
}

expect interface Closeable {
    fun close()
}

expect inline fun <T : Closeable?, R> T.use(block: (T) -> R): R