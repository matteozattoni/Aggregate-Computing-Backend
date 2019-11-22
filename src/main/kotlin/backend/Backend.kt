package backend

import devices.Device
import devices.RemoteDevice
import devices.LocalExecutionDevice

object Backend {
    /**
     * Set of devices with their neighbours
     */
    private var members: MutableMap<Device, Set<Device>> = mutableMapOf()

    private val devices: Set<Device>
    get() = members.keys

    private fun Device.neighbours(): Set<Device> = members.getOrDefault(this, emptySet())
    fun getNeighbours(device: Device) = device.neighbours()

    fun subscribe(device: Device, neighbours: Set<Device> = emptySet()) {
        members.putIfAbsent(device, neighbours)
    }

    fun goLightWeight(device: RemoteDevice) {
        if (devices.contains(device)) {
            val neighbours = device.neighbours()
            members.remove(device)
            members[LocalExecutionDevice.createFromRemote(device)] = neighbours
        }
    }

    fun executeCycle() = devices.forEach { it.execute() }
}