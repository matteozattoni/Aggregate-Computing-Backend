package communication

import devices.Device
import devices.InternetDevice
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.Channels
import java.nio.channels.CompletionHandler

class SocketCommunication(override val device: InternetDevice): Communication<AsynchronousSocketChannel> {
    private val address = device.address
    private var running = false

    override fun extractMessage(received: AsynchronousSocketChannel) : Message =
        ObjectInputStream(Channels.newInputStream(received)).use {
            return it.readObject() as Message
        }

    override fun startServer(onReceive: (AsynchronousSocketChannel) -> Unit) {
        val server = AsynchronousServerSocketChannel.open()
        server.bind(address)
        println(device.id.toString() + " started at " + server.localAddress)
        running = true
        server.accept<Any>(null, object : CompletionHandler<AsynchronousSocketChannel, Any> {
            override fun completed(clientChannel: AsynchronousSocketChannel?, attachment: Any?) {
                if (clientChannel?.isOpen == true) {
                    try {
                        onReceive(clientChannel)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if (!running) {
                    server.close()
                } else if (server.isOpen) {
                    server.accept<Any>(null, this)
                }
            }
            override fun failed(exc: Throwable, attachment: Any?) {
                exc.printStackTrace()
            }
        })
    }

    override fun stopServer() {
        running = false
    }

    override fun send(message: Message) {
        var client: AsynchronousSocketChannel? = null
        var stream: ObjectOutputStream? = null
        try {
            client = AsynchronousSocketChannel.open()
            val future = client!!.connect(address)
            future.get()
            stream = ObjectOutputStream(Channels.newOutputStream(client))
            stream.writeObject(message)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                stream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                client?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}