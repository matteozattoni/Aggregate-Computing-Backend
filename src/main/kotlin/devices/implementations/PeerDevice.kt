package devices.implementations

import communication.Message
import communication.MessageType
import communication.interfaces.NetworkCommunication
import controller.NetworkController
import devices.interfaces.AbstractDevice
import devices.interfaces.RemoteDevice
import org.protelis.lang.datatype.DeviceUID


open class PeerDevice(
    override val id: DeviceUID,
    name: String = "",
    override var physicalDevice: NetworkCommunication? = null
) : AbstractDevice(id, name, ::println), RemoteDevice{

    override fun setPhysicalDevices(physicalDevice: NetworkCommunication) {
        if (this.physicalDevice != null)
            this.physicalDevice!!.addCommunication(physicalDevice)
        else
            this.physicalDevice = physicalDevice
    }

    override fun execute() {
        physicalDevice?.send(Message(id, MessageType.Execute))
    }

    override fun tell(message: Message) {
        super.tell(message)
        when (message.type) {
            MessageType.Execute -> {
            }
            MessageType.GoLightWeight -> {
                if (message.content as Boolean)
                    physicalDevice?.send(message)
                else
                    goLightWeight()
            }
            else -> physicalDevice?.send(message)
        }
    }

    private fun goLightWeight() {
        NetworkController.getNetworkController().support?.replaceHosted(
            this, LocalExecutionDevice(
                id, name, physicalDevice!!
            )
        )
        println("$id go lightweight")
    }

    fun isClient(): Boolean {
        return physicalDevice?.isConnectedToClient() ?: false
    }
}