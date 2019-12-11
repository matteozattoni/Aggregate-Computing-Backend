package devices

import communication.Message

/**
 * Fake Device that does nothing
 * Used mainly for test purposes
 */
class DummyDevice(id: Int) : AbstractDevice(id) {
    override fun execute() {

    }

    override fun tell(message: Message) {

    }
}