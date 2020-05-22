package communication.implements

import communication.Message
import communication.MessageType
import communication.interfaces.CommunicationController
import devices.interfaces.RemoteDevice
import server.DefaultServerFactory
import server.interfaces.ServerFactory
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.concurrent.thread



class LocalNetworkController(
    override var support: RemoteDevice? = null,
    private var factory: ServerFactory = DefaultServerFactory(),
    private val serverAddress: InetAddress,
    private val serverPort: Int
) : CommunicationController {


    // connect to lookup server socket, instance the serverPeer, subscribe to server
    // and call the server Ready, this is called by the client
    override fun getRemoteServer(
        onServerReady: (RemoteDevice) -> Unit,
        onMessage: (Message) -> Unit
    ) {
        val serverPeer = factory.createRemoteDevice(factory.createNewID())
        val localCommunication = LocalClientCommunication(
            serverPeer, InetSocketAddress(
                serverAddress, serverPort
            )
        )
        serverPeer.setPhysicalDevices(localCommunication)
        try {
            localCommunication.connect(onReceive = {
                while (it.isConnected) {
                    val message = localCommunication.extractMessage(it)
                    onMessage(message)
                }
            }) // can trow exception
            onServerReady(serverPeer)
        } catch (error: Exception) {
            // cant connect to server, try again or abort
            error.printStackTrace()
        }

    }

    override fun startOfferServer() {
        //need to implement a protocol for discovering server in the local network, when found the
        //address calls serverFoundAt(address)
        TODO("Not yet implemented")
    }

    override fun stopOfferServer() {
        // stop the protocol above
        TODO("Not yet implemented")
    }

    override fun setCommunicationForServer(server: RemoteDevice) {
        server.setPhysicalDevices(
            LocalServerCommunication(
                server,
                InetSocketAddress(serverAddress, serverPort)
            )
        )
    }

    // When the network found a neighbour server to send a Join and wait for message
    // the support doesn't add the peer
    fun serverFoundAt(address: InetAddress, port: Int) {
        if (support != null) {
            thread {
                try {
                    val id = support!!.id
                    val connection = Socket(address, port)
                    val offerSendMessage = Message(id, MessageType.OfferServer, id)
                    val outputStream = ObjectOutputStream(connection.getOutputStream())
                    outputStream.writeObject(offerSendMessage)
                    val objectInput = ObjectInputStream(connection.getInputStream())
                    while (connection.isConnected) {
                        val receiveMessage = objectInput.readObject() as Message
                        support!!.tell(receiveMessage)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}