package devices.implementations

import communication.Message
import communication.MessageType
import communication.interfaces.NetworkCommunication
import devices.interfaces.Device
import org.protelis.lang.datatype.DeviceUID
import server.DeviceManager
import server.interfaces.NetworkInformation
import server.interfaces.ServerFactory

/**
 * Extends [PeerDevice], it’s the server class thanks to other [SupportDevice] provide the communication between client
 * on entire network
 */
open class SupportDevice(override val id: DeviceUID, name: String = "Support",
                         override var physicalDevice: NetworkCommunication? = null,
                         private val factory: ServerFactory
): PeerDevice(id, name, physicalDevice) {

    protected val deviceManager = DeviceManager()

    override fun setPhysicalDevices(physicalDevice: NetworkCommunication) {
        if (this.physicalDevice != null)
            this.physicalDevice!!.addCommunication(physicalDevice)
        else
            this.physicalDevice = physicalDevice
    }

    override fun execute() {
        deviceManager.getHostedDevices().forEach { synchronized(it) { it.execute() } }
    }

    /**
     * When a client want join the network send a Join message to its SupportDevice, if the message is OfferServer
     * then means another SupportDevice offer his service to this SupportDevice, if a device want send a message to its
     * neighbour then send a SendToNeighbour to its SupportDevice, if want change its mode the device send a
     * GoLightWeight or LeaveLightWeight to its SupportDevice.
     */
    override fun tell(message: Message) {
        when (message.type) {
            MessageType.Join -> {
                val networkInformation = message.content as NetworkInformation
                val messageContent = networkInformation.getContent() as Message
                val content = messageContent.content as DeviceUID?
                val device = factory.createRemoteDevice(content ?: factory.createNewID())
                deviceManager.addHostedDevice(device)
                networkInformation.setPhysicalDevice(device)
                device.tell(Message(this.id, MessageType.ID, device.id))

            }

            MessageType.OfferServer -> {
                val networkInformation = message.content as NetworkInformation
                val messageContent = networkInformation.getContent() as Message
                val uidServer = messageContent.content as DeviceUID
                val device = factory.createRemoteDevice(uidServer)
                deviceManager.addRemoteDevice(device)
                networkInformation.setPhysicalDevice(device)
                // reply nothing
            }

            MessageType.SendToNeighbours -> {
                deviceManager.getHostedDevices().forEach {
                    if (it.id != message.senderUid)
                        synchronized(it) { it.tell(message.content as Message) }
                }

                val device = deviceManager.getRemoteDevices().find { it.id == message.senderUid }
                if (device == null) {
                    //the message came from a hosted client (can't be both)
                    deviceManager.getRemoteDevices().forEach {
                        synchronized(it){it.tell(Message(id, MessageType.SendToNeighbours, message.content))}
                    }
                }
            }
            MessageType.GoLightWeight,
            MessageType.LeaveLightWeight -> {
                deviceManager.getHostedDevices().single { it.id == message.senderUid }
                    .tell(Message(message.senderUid, message.type, false))
            }
            else -> {
            }
        }
    }

    /**
     * Used when a Device want go or leave the lightweight mode
     */
    open fun replaceHosted(replace: Device, with: Device) {
        with.status = replace.status
        deviceManager.removeDevice(replace)
        deviceManager.addHostedDevice(with)
    }

    open fun reset() {
        deviceManager.reset()
    }

    open fun startServer() {
        physicalDevice?.startServer()
    }

    open fun stopServer() {
        physicalDevice?.stopServer()
        physicalDevice = null
    }
}