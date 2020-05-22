package server.interfaces

import devices.interfaces.RemoteDevice
import java.io.Serializable

interface NetworkInformation : Serializable {
    fun getContent(): Serializable?
    fun setPhysicalDevice(peer: RemoteDevice)
}