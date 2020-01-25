package adapters.protelis

import server.Support
import devices.Device
import devices.server.VirtualDevice
import devices.EmulatedDevice
import org.protelis.vm.NetworkManager
import server.Topology

internal class ProtelisAdapterTest {
    class HelloContext(private val device: Device, networkManager: NetworkManager) : ProtelisContext(device, networkManager) {
        override fun instance(): ProtelisContext =
            HelloContext(device, networkManager)

        fun announce(something: String) = device.showResult("$device - $something")
        fun getName() = device.toString()
    }

    init {
        //useful to not make adapter tests interact with each other
        Support.devices.reset()

        val protelisModuleName = "hello"
        val numDevices = 5

        val names = listOf("Mario", "", "Marta", "Maurizio", "Michele")

        repeat(numDevices) {n ->
            val device = Support.devices.createAndAddDevice { id ->
                VirtualDevice(id, names[n]) {
                    ProtelisAdapter(
                        it, protelisModuleName,
                        ProtelisAdapterTest::HelloContext
                    )
                }
            }
            if (n == 0)
                ((device as EmulatedDevice).adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
        }

        Support.devices.finalize(Topology.Ring)

        println("------------")
        Support.devices.printNeighbours()
        println("------------")
    }

    @org.junit.jupiter.api.Test
    fun executeCycles() {
        repeat(5) {
            Support.execute()
            println("------------")
        }
    }
}