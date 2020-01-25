package devices.implementations

import communication.Message
import devices.interfaces.AbstractDevice

/**
 * Fake Device that does nothing
 * Used mainly for test purposes
 */
class DummyDevice(id: Int, name: String) : AbstractDevice(id, name, ::println) {
    constructor(id: Int): this(id, "")

    override fun execute() {

    }

    override fun tell(message: Message) {

    }
}