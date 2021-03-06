package devices

import communication.Message
import communication.MessageType
import communication.concrete.localipv.LocalNetworkController
import devices.implementations.*
import devices.interfaces.Device
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.protelis.lang.datatype.DeviceUID
import server.DefaultServerFactory
import devices.implementations.SupportDevice
import java.net.InetAddress
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

internal class LocalToRemoteTest {

    private val factory = DefaultServerFactory()
    private val supportServer = SupportDevice(
        factory.createNewID(),
        "support server",
        factory = factory
    )
    private val localController = LocalNetworkController(
        supportServer,
        serverAddress = InetAddress.getLocalHost(),
        serverPort = 2001
    )

    private lateinit var server: Device
    private lateinit var device: Device

    @BeforeEach
    fun start(){
        localController.setCommunicationForServer(supportServer)
        supportServer.startServer()
    }

    @AfterEach
    fun stop(){
        supportServer.stopServer()
    }

    @Test
    fun test() {
        supportServer.reset()
        val waitServerAndID = Semaphore(-1)
        val resultArrived = Semaphore(0)
        localController.getRemoteServer({
            server = it
            it.tell(Message(factory.createNewID(), MessageType.Join))
            waitServerAndID.release()
        }){
            if (it.type == MessageType.ID){
                device = VirtualDevice(it.content as DeviceUID, "Dummy")
                waitServerAndID.release()
            }
            if (it.type == MessageType.Result){
                assertTrue(it.content as String == "des")
                resultArrived.release()
            }

        }

        assertTrue(waitServerAndID.tryAcquire(8, TimeUnit.SECONDS))
        server.tell(Message(device.id, MessageType.GoLightWeight, true))
        Thread.sleep(2000)
        server.tell(Message(factory.createNewID(), MessageType.SendToNeighbours,
            Message(factory.createNewID(), MessageType.Result, "des")))

        assertTrue(resultArrived.tryAcquire(8, TimeUnit.SECONDS))
    }
}