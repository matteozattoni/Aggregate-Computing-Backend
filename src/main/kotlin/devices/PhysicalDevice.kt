package devices

import java.net.InetAddress

/**
 * A device model that needs to communicate with a physical counterpart
 */
interface PhysicalDevice : Device {
    val address: InetAddress
    val port: Int
}