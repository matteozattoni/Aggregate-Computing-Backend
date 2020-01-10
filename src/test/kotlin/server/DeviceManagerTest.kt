package server

import devices.Device
import devices.DummyDevice
import org.junit.jupiter.api.Assertions.*

internal class DeviceManagerTest {
    private val deviceManager: DeviceManager = DeviceManager()

    @org.junit.jupiter.api.Test
    fun generateID() {
        assertEquals(deviceManager.createDevice(::DummyDevice).id, 0 )

        deviceManager.createAndAddDevice(::DummyDevice)
        assertEquals(deviceManager.createDevice(::DummyDevice).id, 1 )

        deviceManager += DummyDevice(3)
        assertEquals(deviceManager.createDevice(::DummyDevice).id, 2 )

        deviceManager += DummyDevice(2)
        assertEquals(deviceManager.createDevice(::DummyDevice).id, 4 )
    }

    private fun createDummy(n: Int, topology: Topology) {
        repeat(n) {
            deviceManager.createAndAddDevice(::DummyDevice)
        }
        deviceManager.finalize(topology)
    }

    @org.junit.jupiter.api.Test
    fun lineTopology() {
        createDummy(3, Topology.Line)

        assertEquals(deviceManager.getNeighbours(0), setOf(DummyDevice(1)))
        assertEquals(deviceManager.getNeighbours(1), setOf(DummyDevice(0), DummyDevice(2)))
        assertEquals(deviceManager.getNeighbours(2), setOf(DummyDevice(1)))
        assertEquals(deviceManager.getNeighbours(3), emptySet<Device>())
    }

    @org.junit.jupiter.api.Test
    fun ringTopology() {
        createDummy(3, Topology.Ring)

        assertEquals(deviceManager.getNeighbours(0), setOf(DummyDevice(1), DummyDevice(2)))
        assertEquals(deviceManager.getNeighbours(1), setOf(DummyDevice(0), DummyDevice(2)))
        assertEquals(deviceManager.getNeighbours(2), setOf(DummyDevice(1), DummyDevice(0)))
        assertEquals(deviceManager.getNeighbours(3), emptySet<Device>())
    }

    @org.junit.jupiter.api.Test
    fun ringTopologyMin() {
        createDummy(2, Topology.Ring)

        assertEquals(deviceManager.getNeighbours(0), setOf(DummyDevice(1)))
        assertEquals(deviceManager.getNeighbours(2), emptySet<Device>())
    }
}