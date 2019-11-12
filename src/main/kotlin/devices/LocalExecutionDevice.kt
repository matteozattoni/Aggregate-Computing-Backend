package devices

import incarnations.Incarnation

/**
 * Device that executes locally but reads/writes sensors/actuators on the remotely
 */
class LocalExecutionDevice(id: Int, incarnation: Incarnation) : IncarnatedDevice(id, incarnation) {

    override fun execute() {

    }

    companion object {
        fun createFromRemote(remote: RemoteDevice): LocalExecutionDevice =
            LocalExecutionDevice(remote.id, remote.incarnation)
    }
}