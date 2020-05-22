package devices

import communication.Message
import communication.MessageType
import communication.implements.LocalNetworkController
import devices.implementations.*
import devices.interfaces.Device
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.protelis.lang.datatype.DeviceUID
import server.DefaultServerFactory
import server.Support
import java.net.InetAddress
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class LocalToRemoteTest {

    private val factory = DefaultServerFactory()
    private val localController = LocalNetworkController(Support, serverAddress = InetAddress.getLocalHost(), serverPort = 2001)

    private lateinit var server: Device
    private lateinit var device: Device

    @BeforeEach
    fun start(){
        localController.setCommunicationForServer(Support)
        Support.startServer()
    }

    @AfterEach
    fun stop(){
        Support.stopServer()
    }

    @Test
    fun test() {
        Support.reset()
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