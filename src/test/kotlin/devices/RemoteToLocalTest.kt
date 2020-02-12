package devices

import adapters.DummyAdapter
import communication.Message
import communication.MessageType
import devices.implementations.DummyDevice
import devices.implementations.LocalExecutionDevice
import devices.implementations.RemoteDevice
import devices.interfaces.Device
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import server.Support
import server.Topology
import java.net.InetSocketAddress

class RemoteToLocalTest {
    private fun createRemoteDevice(id: Int) =
        RemoteDevice(id, InetSocketAddress("localhost",2000), "Devv")

    @Test
    fun test() {
        Support.devices.reset()

        Support.devices.createAndAddDevice(::DummyDevice)
        val second = Support.devices.createAndAddDevice(::DummyDevice)
        val remote = Support.devices.createAndAddDevice(::createRemoteDevice)

        Support.devices.finalize(Topology.Ring)

        val oldNeigh = Support.devices.getNeighbours(remote)

        Assertions.assertTrue(Support.devices.getNeighbours(second).contains(remote))

        remote.status.add(Message(-1, MessageType.Result, 3))
        remote.tell(Message(-1, MessageType.GoLightWeight, false))

        val local = Support.devices.getDevices().last()

        Assertions.assertTrue(local is LocalExecutionDevice)
        Assertions.assertEquals(remote.id, local.id)
        Assertions.assertEquals(remote.name, local.name)
        Assertions.assertEquals(remote.status, local.status)
        Assertions.assertTrue(Support.devices.getDevices().contains(local))
        Assertions.assertFalse(Support.devices.getDevices().contains(remote))
        Assertions.assertEquals(Support.devices.getNeighbours(local), oldNeigh)
        Assertions.assertEquals(Support.devices.getNeighbours(remote), setOf<Device>())
        Assertions.assertTrue(Support.devices.getNeighbours(second).contains(local))
        Assertions.assertFalse(Support.devices.getNeighbours(second).contains(remote))
    }
}