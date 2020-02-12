package devices.interfaces

import communication.Message
import java.io.Serializable

/**
 * Basic model for all Devices
 */
interface Device {
    /**
     * The unique identifier of this Device
     */
    val id: Int

    /**
     * a readable name to better recognize this device
     */
    val name: String

    /**
     * Starts executing the program this Device knows
     */
    fun execute()

    /**
     * Messages this Device received
     */
    var status: MutableSet<Message>

    //fun selfStatus(): Set<Message> = status.filter { it.senderUid == id }.toSet()

    /**
     * Tell this Device something
     */
    fun tell(message: Message)

    /**
     * Show a result of computation
     */
    fun showResult(result: Serializable): Unit
}