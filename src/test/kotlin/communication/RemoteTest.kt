package communication

import adapters.scafi.AbstractAggregateProgram
import adapters.scafi.ScafiAdapter
import devices.implementations.RemoteDevice
import devices.implementations.VirtualDevice
import org.junit.jupiter.api.Test
import server.*
import utils.FromKotlin.*
import java.util.*
import kotlin.concurrent.schedule

class RemoteTest {
    class Program : AbstractAggregateProgram() {
        //override fun main(): Any = this.mid()

        private fun isMe(): Boolean = nbr(def0 { mid() }) == mid()
        override fun main(): Any = foldhood(
            def0 { listOf<Int>() },
            def2 { l1, l2 -> l1 + l2 },
            def0 { mux(isMe(), listOf<Int>(), listOf(nbr(def0 { mid() as Int }))) }
        )
    }

    @Test
    fun test() {
        Execution.adapter = { ScafiAdapter(it, Program(), Support) }

        Support.devices.reset()

        Support.physicalDevice.startServer()

        Support.devices.createAndAddDevice(::VirtualDevice)

        while (Support.devices.getDevices().none { it is RemoteDevice }) {
            //wait for a Client
        }

        Support.devices.createAndAddDevice(::VirtualDevice)

        Support.devices.finalize(Topology.Ring)

        Timer().schedule(0, 3000 ) {
            Support.execute()
            println("------------")
        }

        while (true) {
            //wait for completion
        }
    }
}