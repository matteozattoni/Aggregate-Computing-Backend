package server

import devices.interfaces.Device
import java.util.*

/**
 * this class is used by SupportDevice, and it is the information expert of each Device that are connected to the
 * SupportDevice and that are Client (execute the program) or other SupportDevice. Each DeviceManager has local
 * information about the topology, this topology can be seen as a set of client those exchange information between
 * themselves thanks to the collaboration between SupportDevice. The neighbourhood relationship between two clients
 * is true if and only if: 
 * 1. If two clients are connected to the same SupportDevice then this relationship is true.
 * 2. If two clients are connected to two different SupportDevice and this two SupportDevice are connected themselves
 * then this relationship is true
 */
class DeviceManager {

    private val hostedDevices: MutableList<Device> = Collections.synchronizedList(mutableListOf())
    private val remoteDevices: MutableList<Device> = Collections.synchronizedList(mutableListOf())

    fun reset() {
        hostedDevices.clear()
        remoteDevices.clear()
    }

    fun getHostedDevices(): List<Device> = hostedDevices.toList()

    fun getRemoteDevices(): List<Device> = remoteDevices.toList()

    fun removeDevice(device: Device) {
        hostedDevices.removeIf { it.id == device.id }
        remoteDevices.removeIf { it.id == device.id }
    }

    fun addHostedDevice(device: Device) {
        hostedDevices += device
    }

    fun addRemoteDevice(device: Device) {
        remoteDevices += device
    }
}