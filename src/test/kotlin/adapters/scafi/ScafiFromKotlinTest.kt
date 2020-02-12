package adapters.scafi;

import devices.implementations.VirtualDevice
import server.Execution
import server.Support
import server.Topology
import utils.FromKotlin.*

class Program : AbstractAggregateProgram() {
    //override fun main(): Any = this.mid()

    private fun isMe(): Boolean = nbr(def0 { mid() }) == mid()
    override fun main(): Any = this.foldhood(
        def0 { listOf<Int>() },
        def2 { l1, l2 -> l1 + l2 },
        def0 { mux(isMe(), listOf<Int>(), listOf(nbr(def0 { mid() as Int }))) }
    )
}

class ScafiFromKotlinTest {
    init {
        Execution.adapter = { ScafiAdapter(it, Program(), Support) }

        Support.devices.reset()

        repeat (3) {
            Support.devices.createAndAddDevice { id -> VirtualDevice(id, "Device $id") }
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