package adapters.protelis

import communication.implements.LocalNetworkController
import devices.interfaces.Device
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.NetworkManager
import server.DefaultServerFactory
import server.Execution
import server.interfaces.ServerFactory
import devices.implementations.SupportDevice
import java.io.File
import java.net.InetAddress

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProtelisLocalTest {

    private lateinit var localNetworkController: LocalNetworkController
    private lateinit var serverSupport: SupportDeviceTest
    private val inetAddress = InetAddress.getLoopbackAddress()
    private val port = 2001

    @BeforeAll
    fun connectSupportToLocalNetwork(){
        val factory = DefaultServerFactory()
        serverSupport = SupportDeviceTest(factory.createNewID(), "Server Support", factory = factory)
        localNetworkController = LocalNetworkController(serverSupport, serverAddress = inetAddress, serverPort = port)
        localNetworkController.setCommunicationForServer(serverSupport)
        serverSupport.startServer()
    }

    @AfterAll
    fun stopServer(){
        serverSupport.stopServer()
    }

    @Test
    fun test(){
        val adapterTest = ProtelisAdapterTest(serverSupport)
        executeProgram(4)
    }

    internal class ProtelisAdapterTest(private val serverSupport: SupportDeviceTest) {

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
                serverSupport
            ) }
            val factory = DefaultServerFactory()
            val numDevice = 2

            val names = listOf("Mario", "Luca")

            repeat(numDevice) { n ->
                val device = factory.createEmulatedDevice(factory.createNewID(), names[n])
                serverSupport.deviceManagerPublic.addHostedDevice(device)
                if (n == 0)
                    (device.adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
                if (n==1)
                    (device.adapter as ProtelisAdapter).context.executionEnvironment.put("luc", true)
            }
        }
    }

    private fun executeProgram(times: Int) {
        repeat(times) {
            serverSupport.execute()
            Thread.sleep(5000)
        }
    }

    internal class SupportDeviceTest(id: DeviceUID, name: String, factory: ServerFactory): SupportDevice(id, name, factory = factory){
        val deviceManagerPublic = super.deviceManager
    }


}