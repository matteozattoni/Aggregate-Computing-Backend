package adapters.protelis

import server.Support
import devices.interfaces.Device
import devices.implementations.VirtualDevice
import devices.interfaces.EmulatedDevice
import org.protelis.vm.NetworkManager
import server.Execution
import server.Topology

internal class ProtelisAdapterTest {
    class HelloContext(private val device: Device, networkManager: NetworkManager) : ProtelisContext(device, networkManager) {
        override fun instance(): ProtelisContext =
            HelloContext(device, networkManager)

        fun announce(something: String) = device.showResult("$device - $something")
        fun getName() = device.toString()
    }

    init {
        val protelisModuleName = "hello"

        Execution.adapter = { ProtelisAdapter(it, protelisModuleName, ::HelloContext) }

        //useful to not make adapter tests interact with each other
        Support.devices.reset()

        val numDevices = 5

        val names = listOf("Mario", "", "Marta", "Maurizio", "Michele")

        repeat(numDevices) {n ->
            val device = Support.devices.createAndAddDevice { id -> VirtualDevice(id, names[n]) } as VirtualDevice
            if (n == 0)
                (device.adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
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