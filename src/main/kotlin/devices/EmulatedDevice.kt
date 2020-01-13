package devices

import adapters.Adapter
import communication.Message

/**
 * Emulated Device model, it needs an Adapter to be able to execute
 */
abstract class EmulatedDevice(id: Int): AbstractDevice(id) {
    var adapter: Adapter? = null
}