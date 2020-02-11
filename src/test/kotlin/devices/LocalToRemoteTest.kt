package devices

import adapters.DummyAdapter
import communication.Message
import communication.MessageType
import devices.implementations.DummyDevice
import devices.implementations.LocalExecutionDevice
import devices.implementations.RemoteDevice
import devices.interfaces.Device
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import server.Support
import server.Topology
import java.net.InetSocketAddress

class LocalToRemoteTest {

    private fun createLocalDevice(id: Int) =
        LocalExecutionDevice(id, InetSocketAddress("localhost",2000), "Devv", ::DummyAdapter)

    @Test
    fun test() {
        Support.devices.reset()

        Support.devices.createAndAddDevice(::DummyDevice)
        val second = Support.devices.createAndAddDevice(::DummyDevice)
        val local = Support.devices.createAndAddDevice(::createLocalDevice)

        Support.devices.finalize(Topology.Ring)

        val oldNeigh = Support.devices.getNeighbours(local)

        assertTrue(Support.devices.getNeighbours(second).contains(local))

        local.status.add(Message(-1, MessageType.Result, 3))
        local.tell(Message(-1, MessageType.LeaveLightWeight))

        val remote = Support.devices.getDevices().last()

        assertTrue(remote is RemoteDevice)
        assertEquals(remote.id, local.id)
        assertEquals(remote.name, local.name)
        assertEquals(remote.status, local.status)
        assertTrue(Support.devices.getDevices().contains(remote))
        assertFalse(Support.devices.getDevices().contains(local))
        assertEquals(Support.devices.getNeighbours(remote), oldNeigh)
        assertEquals(Support.devices.getNeighbours(local), setOf<Device>())
        assertTrue(Support.devices.getNeighbours(second).contains(remote))
        assertFalse(Support.devices.getNeighbours(second).contains(local))
    }
}