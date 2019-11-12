package backend

import devices.Device
import devices.RemoteDevice
import devices.LocalExecutionDevice

class Backend {
    /**
     * Set of devices with their neighbours
     */
    private var members: MutableMap<Device, Set<Device>> = mutableMapOf()

    private val devices: Set<Device>
    get() = members.keys

    fun Device.neigbours(): Set<Device> = members.getOrDefault(this, emptySet())

    fun subscribe(device: Device, neighbours: Set<Device> = emptySet()) = members.putIfAbsent(device, neighbours)

    fun goLightWeight(device: RemoteDevice) {
        if (devices.contains(device)) {
            val neighbours = device.neigbours()
            members.remove(device)
            members[LocalExecutionDevice.createFromRemote(device)] = neighbours
        }
    }

    fun executeCycle() = devices.forEach { it.execute() }
}