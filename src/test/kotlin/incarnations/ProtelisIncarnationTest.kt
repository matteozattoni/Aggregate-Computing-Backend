package incarnations

import backend.Backend
import devices.Device
import devices.EmulatedDevice
import devices.IncarnatedDevice
import incarnations.protelis.EmulatedNetworkManager
import incarnations.protelis.ProtelisContext
import incarnations.protelis.ProtelisIncarnation
import org.protelis.vm.NetworkManager

internal class ProtelisIncarnationTest {
    class HelloContext(private val device: Device, networkManager: NetworkManager) : ProtelisContext(device, networkManager) {
        override fun instance(): ProtelisContext = HelloContext(device, networkManager)

        fun announce(something: String) = println("${device.id} - $something")
    }

    private var devices: MutableList<Device> = mutableListOf()

    init {
        val protelisModuleName = "hello"
        val numDevices = 5

        repeat(numDevices) { index ->
            devices.add(EmulatedDevice(index) {
                ProtelisIncarnation(it, protelisModuleName, ::EmulatedNetworkManager, ::HelloContext)
            })
        }

        repeat(numDevices) {
            Backend.subscribe(devices[it], setOf(
                devices[(it + 1) % devices.size],
                devices[(it - 1 + devices.size) % devices.size]
            ))
        }

        ((devices.first() as IncarnatedDevice).incarnation as ProtelisIncarnation).context.executionEnvironment.put("leader", true)
    }

    @org.junit.jupiter.api.Test
    fun executeCycles() {
        repeat(5) {
            Backend.execute()
        }
    }
}