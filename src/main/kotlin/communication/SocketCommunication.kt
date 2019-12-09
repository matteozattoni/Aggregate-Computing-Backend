package communication

import backend.Backend
import devices.EmulatedDevice
import devices.PhysicalDevice
import devices.RemoteDevice
import incarnations.protelis.ProtelisIncarnation
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.Channels
import java.nio.channels.CompletionHandler
import kotlin.concurrent.thread

class SocketCommunication(override val device: PhysicalDevice): Communication {
    private var running = false

    override var received: MutableSet<Message> = mutableSetOf()

    init {
        start {
            when (it.type) {
                MessageType.Join -> {
                    if (device.address == Backend.address && device.port == Backend.port) {
                        val address = it.content as InetSocketAddress
                        println("$address wants to join")
                        Backend.subscribe(RemoteDevice(Backend.devices.size, address.port, address.address).apply {
                            communication = SocketCommunication(this)
                        })
                    }
                }
                else -> received.add(it)
            }

        }
    }

    override fun start(onReceive: (Message) -> Unit) {
        val server = AsynchronousServerSocketChannel.open()
        server.bind(device.getSocketAddress())
        println(device.id.toString() + " started at " + server.localAddress)
        running = true
        server.accept<Any>(null, object : CompletionHandler<AsynchronousSocketChannel, Any> {
            override fun completed(clientChannel: AsynchronousSocketChannel?, attachment: Any?) {
                if (clientChannel?.isOpen == true) {
                    try {
                        ObjectInputStream(Channels.newInputStream(clientChannel)).use {
                            val msg = it.readObject()
                            onReceive(msg as Message)
                        }
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

    override fun stop() {
        running = false
    }

    override fun send(message: Message, address: InetAddress, port: Int) {
        var client: AsynchronousSocketChannel? = null
        var stream: ObjectOutputStream? = null
        try {
            client = AsynchronousSocketChannel.open()
            val future = client!!.connect(InetSocketAddress(address, port))
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