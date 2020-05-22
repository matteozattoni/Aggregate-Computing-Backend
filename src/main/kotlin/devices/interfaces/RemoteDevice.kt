package devices.interfaces

import communication.interfaces.NetworkCommunication


interface RemoteDevice: Device {

    var physicalDevice: NetworkCommunication?

    fun setPhysicalDevices(physicalDevice: NetworkCommunication)
}