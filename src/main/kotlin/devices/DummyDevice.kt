package devices

import communication.Message

/**
 * Fake Device that does nothing
 * Used mainly for test purposes
 */
class DummyDevice(id: Int, name: String) : AbstractDevice(id, name) {
    constructor(id: Int): this(id, "")

    override fun execute() {

    }

    override fun showResult(result: String) {
        println(result)
    }

    override fun tell(message: Message) {

    }
}