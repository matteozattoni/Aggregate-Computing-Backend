package communication.concrete.localipv

import communication.Message
import communication.MessageType
import communication.concrete.localipv.LocalNetworkInformation
import communication.interfaces.NetworkCommunication
import devices.interfaces.RemoteDevice
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

// used by support
class LocalServerCommunication(
    override val device: RemoteDevice,
    private val address: InetSocketAddress
) : NetworkCommunication(device) {

    private lateinit var serverSocket: ServerSocket

    override fun connect(onReceive: (Socket) -> Unit) {
        throw Exception("Server")
    }

    override fun isConnectedToClient(): Boolean {
        return false
    }

    override fun extractMessage(received: Socket): Message {
        throw Exception("ServerCommunication can't extract Message")
    }

    override fun send(message: Message) {
        throw Exception("ServerCommunication can't send Message")
    }

    override fun serverCallback(connection: Socket) {
        val objectOutputStream = ObjectOutputStream(connection.getOutputStream())
        val objectInputStream = ObjectInputStream(connection.getInputStream()) // can be blocking

        // waiting for join
        val joinMessage = objectInputStream.readObject() as Message
        val messageType = joinMessage.type
        if (messageType != MessageType.Join && messageType != MessageType.OfferServer) {
            connection.close()
        } else {
            val networkInformation = LocalNetworkInformation(
                connection,
                objectInputStream,
                objectOutputStream,
                joinMessage
            )
            networkInformation.server = this.device
            val newMessage = Message(joinMessage.senderUid, joinMessage.type, networkInformation)
            device.tell(newMessage)
        }

    }

    override fun startServer(onReceive: (Socket) -> Unit) {
        serverSocket = ServerSocket(address.port, 5, address.address)
        thread {
            try {
                while (!serverSocket.isClosed) {
                    val clientSocket = serverSocket.accept()
                    thread {
                        onReceive(clientSocket)
                    }
                }
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
    }

    override fun stopServer() {
        serverSocket.close()
    }
}