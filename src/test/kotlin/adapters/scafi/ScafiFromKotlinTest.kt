package adapters.scafi;

import devices.VirtualDevice
import server.Support
import server.Topology
import utils.FromKotlin.*

class ScafiFromKotlinTest {
    init {
        Support.devices.reset()

        class Program : AbstractAggregateProgram() {
            //override fun main(): Any = this.mid()

            fun isMe(): Boolean = nbr(def0 { mid() }) == mid()
            override fun main(): Any = this.foldhood(
                def0 { listOf<Int>() },
                def2 { l1, l2 -> l1 + l2 },
                def0 { mux(isMe(), listOf<Int>(), listOf(nbr(def0 { mid() as Int }))) }
            )
        }

        repeat (3) {
            Support.devices.createAndAddDevice {id ->
                VirtualDevice(id, "Device $id") { ScafiAdapter(it, Program(), false, null) }
            }
        }

        Support.devices.finalize(Topology.Line)
    }

    @org.junit.jupiter.api.Test
    fun executeCycles() {
        repeat(5) {
            Support.execute()
            println("------------")
        }
    }
}