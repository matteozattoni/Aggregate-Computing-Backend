package devices

import communication.Message
import adapters.Adapter
import communication.MessageType

/**
 * Fully emulated Device
 */
class VirtualDevice(id: Int) : EmulatedDevice(id) {
    override fun execute() = adapter!!.execute()
    override fun showResult(result: String) {
        println(result)
    }
}