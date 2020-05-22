package devices.implementations

import communication.Message
import devices.interfaces.AbstractDevice
import org.protelis.lang.datatype.DeviceUID

/**
 * Fake Device that does nothing
 * Used mainly for test purposes
 */
class DummyDevice(id: DeviceUID, name: String) : AbstractDevice(id, name, ::println) {
    /**
     * Useful to speed up the creation of devices using the :: notation
     */
    constructor(id: DeviceUID): this(id, "")

    override fun execute() {

    }

    override fun tell(message: Message) {

    }
}