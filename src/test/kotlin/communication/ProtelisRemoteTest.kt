package communication

import adapters.protelis.ProtelisAdapter
import adapters.protelis.ProtelisAdapterTest.HelloContext
import adapters.protelis.protelisModuleName
import devices.implementations.RemoteDevice
import devices.implementations.VirtualDevice
import org.junit.jupiter.api.Test
import server.*
import java.util.*
import kotlin.concurrent.schedule

class ProtelisRemoteTest {
    @Test
    fun test() {
        Execution.adapter = { ProtelisAdapter(it, protelisModuleName, ::HelloContext) }

        Support.devices.reset()

        Support.physicalDevice.startServer()

        val device = Support.devices.createAndAddDevice(::VirtualDevice) as VirtualDevice
        (device.adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)

        while (Support.devices.getDevices().none { it is RemoteDevice }) {
            //wait for a Client
        }

        Support.devices.createAndAddDevice(::VirtualDevice)

        Support.devices.finalize(Topology.Ring)

        Timer().schedule(0, 3000 ) {
            Support.execute()
            println("------------")
        }

        while (true) {
            //wait for completion
        }
    }
}