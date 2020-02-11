package adapters

import devices.interfaces.EmulatedDevice

/**
 * Fake adapter used for test purposes
 */
class DummyAdapter(override val device: EmulatedDevice) : Adapter {

    override fun execute() {

    }
}