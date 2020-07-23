package devices.implementations

import communication.Message
import communication.MessageType
import communication.interfaces.NetworkCommunication
import controller.NetworkController
import devices.interfaces.AbstractDevice
import devices.interfaces.RemoteDevice
import org.protelis.lang.datatype.DeviceUID

/**
 * Implements [RemoteDevice], is a Peer that can be reached through its [NetworkCommunication], dispatch the messages
 * received.
 */
open class PeerDevice(
    override val id: DeviceUID,
    name: String = "",
    override var physicalDevice: NetworkCommunication? = null
) : AbstractDevice(id, name, ::println), RemoteDevice{

    /**
     * Set the [physicalDevice], if already set, call [physicalDevice]'s addCommunication method because it has a Chain of
     * Responsibility pattern.
     */
    override fun setPhysicalDevices(physicalDevice: NetworkCommunication) {
        if (this.physicalDevice != null)
            this.physicalDevice!!.addCommunication(physicalDevice)
        else
            this.physicalDevice = physicalDevice
    }

    override fun execute() {
        physicalDevice?.send(Message(id, MessageType.Execute))
    }

    /**
     * Call the [AbstractDevice]'s tell method (that manage Status, Result, Show and Execute message types), and then
     * if the type is GoLightWeight and the content is false call goLightWeight otherwise forward to the physicalDevice
     * the message.
     * @param message the message, manage the Status, Result, Show and Execute types message or forward its
     */
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