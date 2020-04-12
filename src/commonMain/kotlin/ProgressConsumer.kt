package fr.pottime.progressbar

interface Consumable {
    val consumer: ProgressConsumer
}

interface ProgressConsumer {
    fun consume(text: String)
}

object TerminalProgressConsumer : ProgressConsumer {
    private const val carriageReturn: Char = '\r'

    override fun consume(text: String) {
        print(carriageReturn)
        print(text)
    }
}
