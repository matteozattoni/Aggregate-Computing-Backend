package server

import devices.Device
import devices.DummyDevice
import org.junit.jupiter.api.Assertions.assertEquals

internal class SupportTest {
    private val devices = listOf<Device>(
        DummyDevice(0),
        DummyDevice(1),
        DummyDevice(2)
    )

    init {
        Support.subscribe(devices[0])
        Support.subscribe(devices[1], setOf(devices[0]))
        Support.subscribe(devices[2], setOf(devices[0], devices[1]))
    }

    @org.junit.jupiter.api.Test
    fun getNeighbours() {
        assertEquals(Support.getNeighbours(devices[1]), setOf(devices[0]))
        Support.subscribe(devices[1], setOf(devices[0], devices[2]))
        assertEquals(Support.getNeighbours(devices[1]), setOf(devices[0]))

        repeat(devices.size) {
            assertEquals(Support.getNeighbours(devices[it]), Support.getNeighbours(devices[it].id))
        }
    }
}