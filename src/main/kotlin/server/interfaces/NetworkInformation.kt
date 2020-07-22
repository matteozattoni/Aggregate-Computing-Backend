package server.interfaces

import devices.interfaces.RemoteDevice
import java.io.Serializable

/**
 * This interface is used when Join or OfferServer Message are received, hide information used to assign the right
 * NetworkCommunication to the new Device that has been created. This interface is needed because the SupportDevice is
 * the creator of devices, but the ServerSupport doesn’t know anything about which NetworkCommunication is used to get
 * message and to reach this specific device, therefore this interfaces is needed to assign the network communication
 * to the device, moreover each network stack can manage this assignment, and the information that are needed
 * (and hided)
 */
interface NetworkInformation : Serializable {
    fun getContent(): Serializable?
    fun setPhysicalDevice(peer: RemoteDevice)
}