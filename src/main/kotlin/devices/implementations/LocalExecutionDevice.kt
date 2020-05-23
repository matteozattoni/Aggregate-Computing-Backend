package devices.implementations

import adapters.Adapter
import communication.Message
import communication.MessageType
import communication.interfaces.NetworkCommunication
import controller.NetworkController
import devices.interfaces.EmulatedDevice
import org.protelis.lang.datatype.DeviceUID
import server.Execution
import java.io.Serializable

/**
 * Device model that executes locally but shows the results on the physical device
 */
internal class LocalExecutionDevice(id: DeviceUID, name: String, private val physicalDevice: NetworkCommunication?,
                           adapterBuilder: (EmulatedDevice) -> Adapter = Execution.adapter) :
        EmulatedDevice(id, name, adapterBuilder, ::println) {

    override fun showResult(result: Serializable) {
        physicalDevice?.send(Message(id, MessageType.Show, result))
    }

    override fun tell(message: Message) {
        super.tell(message)
        when (message.type) {
            MessageType.Result, MessageType.Show -> physicalDevice?.send(message)
            MessageType.LeaveLightWeight -> goFullWeight()
            else -> { }
        }
    }

    private fun goFullWeight() {
        NetworkController.getNetworkController().support?.replaceHosted(
            this, PeerDevice(id, name, physicalDevice))
        println("$id left lightweight")
    }
}