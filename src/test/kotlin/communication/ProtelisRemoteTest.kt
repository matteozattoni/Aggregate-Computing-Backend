package communication

import adapters.Adapter
import adapters.protelis.ProtelisAdapter
import adapters.protelis.ProtelisContext
import adapters.protelis.ServerUID
import communication.implements.LocalNetworkController
import devices.interfaces.Device
import devices.interfaces.EmulatedDevice

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.NetworkManager
import server.DefaultServerFactory
import devices.implementations.SupportDevice

import java.io.File

import java.net.InetAddress
import java.net.InetSocketAddress



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProtelisRemoteTest {

    private lateinit var localNetworkController: LocalNetworkController
    private val address = InetAddress.getLoopbackAddress()
    private lateinit var adapterTest: ProtelisAdapterTest
    private lateinit var serverDummy1: ProtelisAdapterTest.SupportDeviceTest
    private lateinit var serverDummy2: ProtelisAdapterTest.SupportDeviceTest
    private lateinit var serverDummy1Controller: LocalNetworkController
    private lateinit var serverDummy2Controller: LocalNetworkController

    private val factory = DefaultServerFactory()
    private val supportServer = ProtelisAdapterTest.SupportDeviceTest(factory.createNewID(), "server Dummy 2", factory = factory)

    private val serverDummy1Address = InetSocketAddress(address, 2004)
    private val serverDummy2Address = InetSocketAddress(address, 2006)

    @BeforeEach
    fun startServer(){
        serverDummy1 = ProtelisAdapterTest.SupportDeviceTest(factory.createNewID(), "server Dummy 1", factory = factory)
        serverDummy2 = ProtelisAdapterTest.SupportDeviceTest(factory.createNewID(), "server Dummy 2", factory = factory)
        adapterTest = ProtelisAdapterTest(serverDummy1, serverDummy2, supportServer)
        localNetworkController = LocalNetworkController(
            supportServer,
            serverAddress = address,
            serverPort = 2001
        )
        serverDummy1Controller = LocalNetworkController(
            serverDummy1,
            serverAddress = address,
            serverPort = 2004
        )
        serverDummy2Controller = LocalNetworkController(
            serverDummy2,
            serverAddress = address,
            serverPort = 2006
        )

        // set Server Communication
        localNetworkController.setCommunicationForServer(supportServer)
        serverDummy1Controller.setCommunicationForServer(serverDummy1)
        serverDummy2Controller.setCommunicationForServer(serverDummy2)

        // start server
        supportServer.startServer()
        serverDummy1.startServer()
        serverDummy2.startServer()
    }

    @AfterEach
    fun stopServer(){
        supportServer.stopServer()
        serverDummy1.stopServer()
        serverDummy2.stopServer()
        supportServer.reset()
    }

    @Test
    fun testOneDummy1(){
        connectMainToDummy1()
        Thread.sleep(3000)
        executeProgram(4)
    }



    @Test
    fun testBothDummy(){
        connectEveryOne()
        Thread.sleep(3000)
        executeProgram(4)
    }


    @Test
    fun testTriangleDummy(){
        connectMainToDummy1()
        connectDummy1ToDummy2()
        Thread.sleep(3000)
        executeProgram(4)
    }

    // connect Main with Dummy1
    private fun connectMainToDummy1(){
        localNetworkController.serverFoundAt(serverDummy1Address.address, serverDummy1Address.port)
        serverDummy1Controller.serverFoundAt(address, 2001)
    }

    private fun connectDummy1ToDummy2(){
        serverDummy2Controller.serverFoundAt(address, serverDummy1Address.port)
        serverDummy1Controller.serverFoundAt(address, serverDummy2Address.port)
    }

    private fun connectEveryOne(){
        connectMainToDummy1()
        connectDummy1ToDummy2()

        //connect Main with Dummy2
        localNetworkController.serverFoundAt(address, serverDummy2Address.port)
        serverDummy2Controller.serverFoundAt(address, 2001)
    }

    private fun executeProgram(times: Int) {
        val executeThread = Thread(Runnable {
            repeat(times) {
                supportServer.execute()
                serverDummy1.execute()
                serverDummy2.execute()
                Thread.sleep(5000)
            }
        })
        executeThread.start()
        executeThread.join()
    }

    private class ProtelisAdapterTest(private val serverDummy1: SupportDeviceTest,
                                      private val serverDummy2: SupportDeviceTest,
                                      private val supportServer: SupportDeviceTest) {

        class HelloContext(private val device: Device, networkManager: NetworkManager) : ProtelisContext(device, networkManager) {
            override fun instance(): ProtelisContext =
                HelloContext(device, networkManager)

            fun announce(something: String) = device.showResult("$device - $something")
            fun getName() = device.toString()
        }

        fun readFromRaw() : String {
            val bufferedReader = File("D:\\Matteo\\Documenti\\GitHub repo\\Aggregate-Computing-Client\\app\\src\\main\\res\\raw\\hello.pt").bufferedReader()
            return bufferedReader.readText()
        }


        init {
            val adapterSupport: (EmulatedDevice) -> Adapter = { ProtelisAdapter(it, readFromRaw(),
                ProtelisAdapterTest::HelloContext, supportServer
            )}
            val adapterDummy1: (EmulatedDevice) -> Adapter = { ProtelisAdapter(it, readFromRaw(),
                ProtelisAdapterTest::HelloContext, serverDummy1)}
            val adapterDummy2: (EmulatedDevice) -> Adapter = { ProtelisAdapter(it, readFromRaw(),
                ProtelisAdapterTest::HelloContext, serverDummy2)}

            val namesSupport = listOf("Mario")
            val namesDummy1 = listOf("Dummy1")
            val namesDummy2 = listOf("Dummy2")

            val factory = DefaultServerFactory()

            repeat(namesSupport.size) { n ->
                val device = factory.createEmulatedDevice(ServerUID(), namesSupport[n], adapterSupport)
                supportServer.deviceManagerPublic.addHostedDevice(device)
                 if (n == 0)
                    (device.adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
            }

            repeat(namesDummy1.size) { n ->
                val device = factory.createEmulatedDevice(ServerUID(), namesDummy1[n], adapterDummy1)
                serverDummy1.deviceManagerPublic.addHostedDevice(device)
                //if (n==0)
                    //(device.adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
            }

            repeat(namesDummy2.size) { n ->
                val device = factory.createEmulatedDevice(ServerUID(), namesDummy2[n], adapterDummy2)
                serverDummy2.deviceManagerPublic.addHostedDevice(device)
            }
        }

        internal class SupportDeviceTest(id: DeviceUID, name: String, factory: DefaultServerFactory = DefaultServerFactory()): SupportDevice(id,name,factory= factory){
            val deviceManagerPublic = super.deviceManager
        }
    }

}