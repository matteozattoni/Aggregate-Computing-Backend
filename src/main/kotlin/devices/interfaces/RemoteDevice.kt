package devices.interfaces

import communication.interfaces.NetworkCommunication

/**
 * A device model that needs to communicate with a physical counterpart through a [NetworkCommunication]
 */
interface RemoteDevice: Device {

    var physicalDevice: NetworkCommunication?

    fun setPhysicalDevices(physicalDevice: NetworkCommunication)
}