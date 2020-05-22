package communication

import adapters.Adapter
import adapters.protelis.ProtelisAdapter
import adapters.protelis.ProtelisContext
import adapters.protelis.ServerUID
import communication.implements.LocalNetworkController
import devices.implementations.PeerDevice
import devices.interfaces.Device
import devices.interfaces.EmulatedDevice

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.NetworkManager
import server.DefaultServerFactory
import server.DeviceManager
import server.interfaces.NetworkInformation
import server.Support
import server.interfaces.ServerFactory

import java.io.File

import java.net.InetAddress
import java.net.InetSocketAddress



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProtelisRemoteTest {

    private lateinit var localNetworkController: LocalNetworkController
    private val address = InetAddress.getLoopbackAddress()
    private lateinit var adapterTest: ProtelisAdapterTest
    private lateinit var serverDummy1: ServerDummyDevice
    private lateinit var serverDummy2: ServerDummyDevice
    private lateinit var serverDummy1Controller: LocalNetworkController
    private lateinit var serverDummy2Controller: LocalNetworkController

    private val serverDummy1Address = InetSocketAddress(address, 2004)
    private val serverDummy2Address = InetSocketAddress(address, 2006)

    @BeforeEach
    fun startServer(){
        serverDummy1 = ServerDummyDevice("ServerDummy1")
        serverDummy2 = ServerDummyDevice("ServerDummy2")
        adapterTest = ProtelisAdapterTest(serverDummy1, serverDummy2)
        localNetworkController = LocalNetworkController(Support, serverAddress = address, serverPort =  2001)
        serverDummy1Controller = LocalNetworkController(serverDummy1, serverAddress = address, serverPort =  2004)
        serverDummy2Controller = LocalNetworkController(serverDummy2, serverAddress = address, serverPort = 2006)

        // set Server Communication
        localNetworkController.setCommunicationForServer(Support)
        serverDummy1Controller.setCommunicationForServer(serverDummy1)
        serverDummy2Controller.setCommunicationForServer(serverDummy2)

        // start server
        Support.startServer()
        serverDummy1.startServer()
        serverDummy2.startServer()
    }

    @AfterEach
    fun stopServer(){
        Support.stopServer()
        serverDummy1.stopServer()
        serverDummy2.stopServer()
        Support.reset()
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
                Support.execute()
                serverDummy1.execute()
                serverDummy2.execute()
                Thread.sleep(5000)
            }
        })
        executeThread.start()
        executeThread.join()
    }

    private class ProtelisAdapterTest(private val serverDummy1: ServerDummyDevice, private val serverDummy2: ServerDummyDevice) {

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
                ProtelisAdapterTest::HelloContext
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
                Support.deviceManager.addHostedDevice(device)
                 if (n == 0)
                    (device.adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
            }

            repeat(namesDummy1.size) { n ->
                val device = factory.createEmulatedDevice(ServerUID(), namesDummy1[n], adapterDummy1)
                serverDummy1.deviceManager.addHostedDevice(device)
                //if (n==0)
                    //(device.adapter as ProtelisAdapter).context.executionEnvironment.put("leader", true)
            }

            repeat(namesDummy2.size) { n ->
                val device = factory.createEmulatedDevice(ServerUID(), namesDummy2[n], adapterDummy2)
                serverDummy2.deviceManager.addHostedDevice(device)
            }
        }
    }

    internal class ServerDummyDevice(name: String): PeerDevice(ServerUID(), name) {
        private var serverFactory: ServerFactory = DefaultServerFactory()
        internal val deviceManager = DeviceManager()

        fun setServerFactory(serverFactory: ServerFactory) {
            this.serverFactory = serverFactory
        }

        @Suppress("NAME_SHADOWING")
        override fun tell(message: Message) {
            when (message.type) {
                MessageType.Join -> {
                    val networkInformation = message.content as NetworkInformation
                    val message = networkInformation.getContent() as Message
                    val content = message.content as DeviceUID?
                    val device = serverFactory.createRemoteDevice(content ?: serverFactory.createNewID())
                    deviceManager.addHostedDevice(device)
                    networkInformation.setPhysicalDevice(device)
                    device.tell(Message(this.id, MessageType.ID, device.id))

                }

                MessageType.OfferServer -> {
                    val networkInformation = message.content as NetworkInformation
                    val message = networkInformation.getContent() as Message
                    val uidServer = message.content as DeviceUID
                    val device = serverFactory.createRemoteDevice(uidServer)
                    deviceManager.addRemoteDevice(device)
                    networkInformation.setPhysicalDevice(device)
                    // reply nothing
                }

                MessageType.SendToNeighbours -> {
                    deviceManager.getHostedDevices().forEach {
                        if (it.id != message.senderUid)
                            synchronized(it) { it.tell(message.content as Message) }
                    }

                    val device = deviceManager.getRemoteDevices().find { it.id == message.senderUid }
                    if (device == null) {
                        //the message came from a hosted client (can't be both)
                        deviceManager.getRemoteDevices().forEach {
                            it.tell(Message(id, MessageType.SendToNeighbours, message.content))
                        }
                    }
                }
                MessageType.GoLightWeight,
                MessageType.LeaveLightWeight -> {
                    deviceManager.getHostedDevices().single { it.id == message.senderUid }
                        .tell(Message(message.senderUid, message.type, false))
                }
                else -> {
                }
            }

        }

        override fun execute() {
            deviceManager.getHostedDevices().forEach { synchronized(it) { it.execute() } }
        }

        fun replaceHosted(replace: Device, with: Device) {
            with.status = replace.status
            deviceManager.removeDevice(replace)
            deviceManager.addHostedDevice(with)
        }

        fun reset() {
            deviceManager.reset()
        }

        fun startServer() {
            physicalDevice?.startServer()
        }

        fun stopServer() {
            physicalDevice?.stopServer()
            physicalDevice = null
        }
    }
}