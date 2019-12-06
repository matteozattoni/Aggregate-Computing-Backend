package devices

import java.net.InetAddress
import java.net.InetSocketAddress

/**
 * A device model that needs to communicate with a physical counterpart
 */
interface PhysicalDevice : Device {
    val address: InetAddress
    val port: Int

    fun getSocketAddress(): InetSocketAddress = InetSocketAddress(address, port)
}