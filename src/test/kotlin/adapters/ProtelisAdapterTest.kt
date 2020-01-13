package adapters

import server.Support
import devices.Device
import devices.VirtualDevice
import devices.EmulatedDevice
import adapters.protelis.ProtelisNetworkManager
import adapters.protelis.ProtelisContext
import adapters.protelis.ProtelisAdapter
import org.protelis.vm.NetworkManager
import server.Topology

internal class ProtelisAdapterTest {
    class HelloContext(private val device: Device, networkManager: NetworkManager) : ProtelisContext(device, networkManager) {
        override fun instance(): ProtelisContext = HelloContext(device, networkManager)

        fun announce(something: String) = println("${device.id} - $something")
    }

    init {
        //useful to not make adapter tests interact with each other
        Support.devices.reset()

        val protelisModuleName = "hello"
        val numDevices = 5

        repeat(numDevices) {
            Support.devices.createAndAddDevice { id ->
                VirtualDevice(id).apply { adapter = ProtelisAdapter(this, protelisModuleName, ::HelloContext) }
            }
        }

        Support.devices.finalize(Topology.Ring)

        ((Support.devices.getDevices().first() as EmulatedDevice).adapter as ProtelisAdapter)
            .context.executionEnvironment.put("leader", true)
    }

    @org.junit.jupiter.api.Test
    fun executeCycles() {
        repeat(5) {
            Support.execute()
        }
    }
}