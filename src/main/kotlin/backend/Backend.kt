package backend

import communication.Communication
import communication.SocketCommunication
import devices.Device
import devices.RemoteDevice
import devices.LocalExecutionDevice
import incarnations.protelis.IntUID
import java.net.InetAddress

object Backend {
    /**
     * Set of devices with their neighbours
     */
    private var members: MutableMap<Device, Set<Device>> = mutableMapOf()

    val devices: Set<Device>
    get() = members.keys

    var communication: Communication = SocketCommunication(InetAddress.getLocalHost(), 20000)

    private fun Device.neighbours(): Set<Device> = members.getOrDefault(this, emptySet())
    fun getNeighbours(device: Device) = device.neighbours()
    fun getNeighbours(id: Int) = getNeighbours(devices.single { it.id == id })

    fun subscribe(device: Device, neighbours: Set<Device> = emptySet()) {
        members.putIfAbsent(device, neighbours)
    }

    /*fun goLightWeight(device: RemoteDevice) {
        if (devices.contains(device)) {
            val neighbours = device.neighbours()
            members.remove(device)
            members[LocalExecutionDevice.createFromRemote(device)] = neighbours
        }
    }*/

    fun executeCycle() = devices.forEach { it.execute() }
}