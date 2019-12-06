package devices

import java.net.InetAddress

/**
 * Fully emulated Device
 */
class EmulatedDevice(id: Int, override val port: Int) : IncarnatedDevice(id), PhysicalDevice {
    override val address: InetAddress = InetAddress.getLocalHost()

    override fun getSensor(sensorName: String): Any = incarnation.readSensor(sensorName)

    override fun execute() = incarnation.execute()
}