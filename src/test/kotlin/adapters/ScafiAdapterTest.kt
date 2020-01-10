package adapters

import adapters.scafi.ScafiAdapter
import server.Support
import devices.Device
import devices.VirtualDevice

internal class ScafiAdapterTest {
    private var devices: MutableList<Device> = mutableListOf()

    init {
        val numDevices = 5

        repeat(numDevices) { index ->
            devices.add(VirtualDevice(index) {
                ScafiAdapter()
            })
        }
    }

    @org.junit.jupiter.api.Test
    fun executeCycles() {
        repeat(5) {
            Support.execute()
        }
    }
}