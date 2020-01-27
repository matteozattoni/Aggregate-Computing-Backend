package server

import adapters.scafi.AbstractAggregateProgram
import adapters.scafi.ScafiAdapter
import communication.SocketCommunication
import devices.implementations.RemoteDevice
import devices.implementations.VirtualDevice
import org.junit.jupiter.api.Test

class RemoteTest {

    init {
        Support.devices.reset()

        val program = object : AbstractAggregateProgram() {
            override fun main(): Any = mid()
        }

        Support.devices.createAndAddDevice { id -> VirtualDevice(id, "", { ScafiAdapter(it, program, null) }) }
        //Support.devices.createAndAddDevice { id -> VirtualDevice(id, "", { ScafiAdapter(it, program, null) }) }
        //Support.devices.createAndAddDevice { id -> VirtualDevice(id, "", { ScafiAdapter(it, program, null) }) }
    }

    @Test
    fun test() {
        Support.physicalDevice.startServer(SocketCommunication.serverCallback)

        while (Support.devices.getDevices().none { it is RemoteDevice }) {
            //wait for a Client
        }

        Support.devices.finalize(Topology.Ring)

        println("finalized")

        Support.execute()

        while (true) {
            //wait for completion
        }
    }
}