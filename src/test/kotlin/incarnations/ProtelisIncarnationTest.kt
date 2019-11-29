package incarnations

import backend.Backend
import devices.Device
import devices.EmulatedDevice
import devices.IncarnatedDevice
import incarnations.protelis.ProtelisIncarnation

internal class ProtelisIncarnationTest {

    private var devices: MutableList<Device> = mutableListOf()

    init {
        val protelisModuleName = "hello"

        repeat(5) {
            devices.add(EmulatedDevice(it, ProtelisIncarnation(it, protelisModuleName)))
        }

        repeat(5) {
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
            Backend.executeCycle()
        }
    }
}