package adapters.protelis

import adapters.protelis.ProtelisAdapter
import adapters.protelis.ProtelisContext
import communication.implements.LocalNetworkController
import devices.interfaces.Device
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.protelis.vm.NetworkManager
import server.DefaultServerFactory
import server.Execution
import server.Support
import java.io.File
import java.net.InetAddress

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProtelisLocalTest {

    private lateinit var localNetworkController: LocalNetworkController
    private val inetAddress = InetAddress.getLoopbackAddress()
    private val port = 2001

    @BeforeAll
    fun connectSupportToLocalNetwork(){
        localNetworkController = LocalNetworkController(Support, serverAddress = inetAddress, serverPort = port)
        localNetworkController.setCommunicationForServer(Support)
        Support.startServer()
    }

    @AfterAll
    fun stopServer(){
        Support.stopServer()
    }

    @Test
    fun test(){
        val adapterTest = ProtelisAdapterTest()
        executeProgram(4)
    }

    internal class ProtelisAdapterTest {

        class HelloContext(private val device: Device, networkManager: NetworkManager) : ProtelisContext(device, networkManager) {
            override fun instance(): ProtelisContext =
                HelloContext(device, networkManager)

            fun announce(something: String) = device.showResult("$device - $something")
            fun getName() = device.toString()
            fun test() = device.showResult("HSHR")
            fun test2(): String = "rest"
        }

        private fun readFromRaw() : String {
            val bufferedReader = File("D:\\Matteo\\Documenti\\GitHub repo\\Aggregate-Computing-Client\\app\\src\\main\\res\\raw\\hello.pt").bufferedReader()
            return bufferedReader.readText()
        }


        init {

            Execution.adapter = { ProtelisAdapter(it, readFromRaw() , ::HelloContext,
                Support
            ) }
            val factory = DefaultServerFactory()
            val numDevice = 2

            val names = listOf("Mario", "Luca")

            repeat(numDevice) { n ->
                val device = factory.createEmulatedDevice(factory.createNewID(), names[n])
                Support.deviceManager.addHostedDevice(device)
                if (n == 0)
                    (device.adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
                if (n==1)
                    (device.adapter as ProtelisAdapter).context.executionEnvironment.put("luc", true)
            }
        }
    }

    private fun executeProgram(times: Int) {
        repeat(times) {
            Support.execute()
            Thread.sleep(5000)
        }
    }


}