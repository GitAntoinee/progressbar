package fr.pottime.progressbar

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProgressBar(
    override val state: ProgressState,
    override val style: ProgressBarStyle,
    override val unit: ProgressUnit = ProgressUnit.DEFAULT,
) : Progressable, Progressable.Convenience, Stylable, Consumable, Renderable, Closeable {
    constructor(
        ticks: Long,
        maxTicks: Long = ticks + 100,
        style: ProgressBarStyle = ProgressBarStyle.ASCII,
        unit: ProgressUnit = ProgressUnit.DEFAULT
    ) : this(ProgressState(ticks, maxTicks), style, unit)

    override val renderer: ProgressRenderer = DefaultProgressRenderer(this, this)
    override val consumer: ProgressConsumer = TerminalProgressConsumer

    private var closed: Boolean = false
    private val rendererJob: Job = GlobalScope.launch {
        while (!closed) {
            consumer.consume(renderer.render(terminalWidth - 1))
            delay(100)
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
        rendererJob.cancel()

        // Final
        consumer.consume(renderer.render(terminalWidth - 1))
    }
}
