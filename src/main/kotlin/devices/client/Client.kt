package devices.client

import adapters.Adapter
import communication.SocketCommunication
import devices.EmulatedDevice
import devices.InternetDevice
import java.net.SocketAddress

/**
 * Client model
 * will be used in physical devices
 */
class Client(override val id: Int, override val address: SocketAddress,
             adapterBuilder: (EmulatedDevice) -> Adapter, private val onResult: (String) -> Unit) :
    EmulatedDevice(id, "Client $id", adapterBuilder), InternetDevice {

    override val physicalDevice = SocketCommunication(this)

    override fun execute() = adapter.execute()

    override fun showResult(result: String) = onResult(result)
}