package server

import devices.interfaces.Device
import java.util.*

internal class DeviceManager {

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