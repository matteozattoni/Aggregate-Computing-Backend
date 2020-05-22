package server

import adapters.protelis.ServerUID
import communication.Message
import communication.MessageType
import devices.interfaces.Device
import devices.implementations.PeerDevice
import org.protelis.lang.datatype.DeviceUID
import server.interfaces.NetworkInformation
import server.interfaces.ServerFactory

internal object Support : PeerDevice(ServerUID(), "serverSupport") {

    private var serverFactory: ServerFactory = DefaultServerFactory()
    internal val deviceManager = DeviceManager()

    fun setServerFactory(serverFactory: ServerFactory){
        this.serverFactory = serverFactory
    }

    @Suppress("NAME_SHADOWING")
    override fun tell(message: Message) {
        when (message.type) {
            MessageType.Join -> {
                val networkInformation = message.content as NetworkInformation
                val message = networkInformation.getContent() as Message
                val content = message.content as DeviceUID?
                val device = serverFactory.createRemoteDevice(content ?: serverFactory.createNewID())
                deviceManager.addHostedDevice(device)
                networkInformation.setPhysicalDevice(device)
                device.tell(Message(this.id, MessageType.ID, device.id))

            }

            MessageType.OfferServer -> {
                val networkInformation = message.content as NetworkInformation
                val message = networkInformation.getContent() as Message
                val uidServer = message.content as DeviceUID
                val device = serverFactory.createRemoteDevice(uidServer)
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

    override fun execute() {
        deviceManager.getHostedDevices().forEach { synchronized(it) { it.execute() } }
    }

    fun replaceHosted(replace: Device, with: Device) {
        with.status = replace.status
        deviceManager.removeDevice(replace)
        deviceManager.addHostedDevice(with)
    }

    fun reset() {
        deviceManager.reset()
    }

    fun startServer() {
        physicalDevice?.startServer()
    }

    fun stopServer() {
        physicalDevice?.stopServer()
        physicalDevice = null
    }

}