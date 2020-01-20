package adapters.scafi;

import devices.VirtualDevice
import server.Support
import server.Topology

class AggregateProgramBuilderTest {
    init {
        Support.devices.reset()

        class Program : AbstractAggregateProgram() {
            //override fun main(): Any = this.mid()

            val empty: List<Int> = listOf()
            fun isMe(): Boolean = this.nbr<Int>(def0 { this.mid() as Int }) == this.mid()
            override fun main(): Any = this.foldhood<List<Int>>(
                def0 { empty },
                def2 { l1,l2 -> l1 + l2 },
                def0 { this.mux(isMe(), empty, listOf(this.nbr(def0 {this.mid() as Int }))) }
            )
        }

        repeat (3) {
            Support.devices.createAndAddDevice {id ->
                VirtualDevice(id).apply { adapter = ScafiAdapter(this, Program()) }
            }
        }

        Support.devices.finalize(Topology.Line)
    }

    @org.junit.jupiter.api.Test
    fun executeCycles() {
        repeat(5) {
            Support.execute()
        }
    }
}