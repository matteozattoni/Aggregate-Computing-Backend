package devices

import communication.Message
import adapters.Adapter

/**
 * Fully emulated Device
 */
class VirtualDevice(id: Int, adapterBuilder: (VirtualDevice) -> Adapter) : EmulatedDevice(id) {
    override val adapter: Adapter = adapterBuilder(this)

    override fun getSensor(sensorName: String): Any = adapter.readSensor(sensorName)

    override fun execute() = adapter.execute()

    override fun tell(message: Message) {
        receivedMessages.add(message)
    }
}