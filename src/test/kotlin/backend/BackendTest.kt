package backend

import devices.Device
import devices.DummyDevice
import org.junit.jupiter.api.Assertions.*

internal class BackendTest {
    private val devices = listOf<Device>(
        DummyDevice(0),
        DummyDevice(1),
        DummyDevice(2)
    )

    init {
        Backend.subscribe(devices[0])
        Backend.subscribe(devices[1], setOf(devices[0]))
        Backend.subscribe(devices[2], setOf(devices[0], devices[1]))
    }

    @org.junit.jupiter.api.Test
    fun getNeighbours() {
        assertEquals(Backend.getNeighbours(devices[1]), setOf(devices[0]))
        Backend.subscribe(devices[1], setOf(devices[0], devices[2]))
        assertEquals(Backend.getNeighbours(devices[1]), setOf(devices[0]))

        repeat(devices.size) {
            assertEquals(Backend.getNeighbours(devices[it]), Backend.getNeighbours(devices[it].getID()))
        }
    }
}