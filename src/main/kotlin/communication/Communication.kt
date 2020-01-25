package communication

import devices.interfaces.Device

/**
 * Represents the way a Device model has to communicate with the physical one
 */
interface Communication<T> {
    val device: Device

    /**
     * Obtains the Message sent to this device
     */
    fun extractMessage(received: T) : Message

    fun startServer(onReceive: (T) -> Unit)
    fun stopServer()

    /**
     * Sends a Message to the physical Device
     */
    fun send(message: Message)
}