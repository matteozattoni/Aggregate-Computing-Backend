package communication.concrete.localipv

import communication.Message
import communication.interfaces.NetworkCommunication
import devices.interfaces.RemoteDevice
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

class LocalClientCommunication(device: RemoteDevice, private val address: InetSocketAddress) :
    NetworkCommunication(device) {

    private lateinit var connection: Socket
    private lateinit var inputStream: ObjectInputStream
    private lateinit var outputStream: ObjectOutputStream
    private var isClient = false
    private val semaphoreObjectStream: Semaphore = Semaphore(0)
    private val executors = Executors.newFixedThreadPool(3)

    lateinit var server: RemoteDevice

    // is called by the Network Information (the socket is already open)
    constructor(device: RemoteDevice, localNetworkInformation: LocalNetworkInformation) :
            this(
                device,
                InetSocketAddress(
                    localNetworkInformation.socket.inetAddress,
                    localNetworkInformation.socket.port
                )
            ) {
        this.connection = localNetworkInformation.socket
        this.inputStream = localNetworkInformation.inputStream
        this.outputStream = localNetworkInformation.outputStream
        semaphoreObjectStream.release()
        thread {
            try {
                while (connection.isConnected) {
                    val message = extractMessage(connection)
                    server.tell(message)
                }
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
    }

    override fun connect(onReceive: (Socket) -> Unit) {
        connection = Socket(address.address, address.port)
        thread {
            try {
                outputStream = ObjectOutputStream(connection.getOutputStream())
                semaphoreObjectStream.release()
                inputStream = ObjectInputStream(connection.getInputStream()) // can be blocking
                onReceive(connection)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
    }

    override fun isConnectedToClient(): Boolean {
        return isClient
    }

    override fun extractMessage(received: Socket): Message {
        return inputStream.readObject() as Message
    }

    override fun send(message: Message) {
        executors.submit {
            try {
                semaphoreObjectStream.acquire()
                outputStream.writeObject(message)
                //outputStream.flush()
                semaphoreObjectStream.release()
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
    }

    override fun serverCallback(connection: Socket) {
        while (connection.isConnected) {
            val message = extractMessage(connection)
            server.tell(message)
        }

    }

    override fun startServer(onReceive: (Socket) -> Unit) {
        throw Exception("A client must not start a server")
    }

    override fun stopServer() {
        throw Exception("A client must not stop a server")
    }
}