package devices

import incarnations.Incarnation

/**
 * Device that does everything remotely
 */
class RemoteDevice(val id: Int, val incarnation: Incarnation) : IncarnatedDevice(id, incarnation) {

    override fun execute() {

    }
}