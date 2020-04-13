package fr.pottime.progressbar

import com.soywiz.klock.DateTime
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProgressBar(
    override val state: ProgressState,
    override val style: ProgressBarStyle,
    override val task: String? = null,
    override val unit: ProgressUnit = ProgressUnit.DEFAULT,
    override val options: ProgressBarOptions = ProgressBarOptions(),
) : Progressable, Progressable.Convenience, Stylable, Consumable, Renderable, Closeable {
    constructor(
        ticks: Long,
        maxTicks: Long = ticks + 100,
        style: ProgressBarStyle = ProgressBarStyle.ASCII,
        task: String? = null,
        unit: ProgressUnit = ProgressUnit.DEFAULT,
    ) : this(ProgressState(ticks, maxTicks), style, task, unit)

    override val startDate: DateTime = DateTime.now()

    override val renderer: ProgressRenderer = DefaultProgressRenderer(this, this)
    override val consumer: ProgressConsumer = TerminalProgressConsumer

    private var backingTicksPerSecond: Long = 0
    override val ticksPerSecond: Long get() = backingTicksPerSecond

    private var closed: Boolean = false

    private val ticksPerSecondJob: Job = GlobalScope.launch {
        while (!closed) {
            val before = ticks
            delay(1000)

            backingTicksPerSecond = ticks - before
        }
    }

    private val rendererJob: Job = GlobalScope.launch {
        while (!closed) {
            consumer.consume(renderer.render(terminalWidth - 1))
            delay(options.renderInterval)
        }
    }

    override var ticks: Long
        get() = state.ticks
        set(value) {
            state.ticks = value
        }

    override var maxTicks: Long
        get() = state.maxTicks
        set(value) {
            state.maxTicks = value
        }

    override fun tick(maxTicks: Long?) {
        ticks++
        maxTicks?.let { this.maxTicks = it }
    }

    override fun tickBy(n: Long, maxTicks: Long?) {
        ticks += n
        maxTicks?.let { this.maxTicks = it }
    }

    override fun tickTo(n: Long, maxTicks: Long?) {
        ticks = n
        maxTicks?.let { this.maxTicks = it }
    }

    override fun close() {
        closed = true
        ticksPerSecondJob.cancel()
        rendererJob.cancel()

        // Final
        consumer.consume(renderer.render(terminalWidth - 1))
    }
}
