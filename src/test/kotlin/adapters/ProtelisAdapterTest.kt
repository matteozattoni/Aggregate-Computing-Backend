package adapters

import server.Support
import devices.Device
import devices.VirtualDevice
import devices.EmulatedDevice
import adapters.protelis.ProtelisNetworkManager
import adapters.protelis.ProtelisContext
import adapters.protelis.ProtelisAdapter
import org.protelis.vm.NetworkManager

internal class ProtelisAdapterTest {
    class HelloContext(private val device: Device, networkManager: NetworkManager) : ProtelisContext(device, networkManager) {
        override fun instance(): ProtelisContext = HelloContext(device, networkManager)

        fun announce(something: String) = println("${device.id} - $something")
    }

    private var devices: MutableList<Device> = mutableListOf()

    init {
        val protelisModuleName = "hello"
        val numDevices = 5

        repeat(numDevices) { index ->
            devices.add(VirtualDevice(index) {
                ProtelisAdapter(it, protelisModuleName, ::HelloContext)
            })
        }

        repeat(numDevices) {
            Support.subscribe(devices[it], setOf(
                devices[(it + 1) % devices.size],
                devices[(it - 1 + devices.size) % devices.size]
            ))
        }

        ((devices.first() as EmulatedDevice).adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
    }

    @org.junit.jupiter.api.Test
    fun executeCycles() {
        repeat(5) {
            Support.execute()
        }
    }
}