package backend

import communication.Communication
import communication.SocketCommunication
import devices.Device
import devices.RemoteDevice
import devices.LocalExecutionDevice
import devices.PhysicalDevice
import incarnations.protelis.IntUID
import java.net.InetAddress

object Backend : PhysicalDevice {
    override val id: Int = -1
    override val address: InetAddress = InetAddress.getLocalHost()
    override val port: Int = 20000
    override var communication: Communication = SocketCommunication(this)

    /**
     * Set of devices with their neighbours
     */
    private var members: MutableMap<Device, Set<Device>> = mutableMapOf()

    val devices: Set<Device>
    get() = members.keys

    private fun Device.neighbours(): Set<Device> = members.getOrDefault(this, emptySet())
    fun getNeighbours(device: Device) = device.neighbours()
    fun getNeighbours(id: Int) = getNeighbours(devices.single { it.id == id })

    fun subscribe(device: Device, neighbours: Set<Device> = emptySet()) {
        members.putIfAbsent(device, neighbours)
    }

    override fun execute() {
        devices.forEach(Device::execute)
    }
}