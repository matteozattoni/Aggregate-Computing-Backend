package server

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import devices.*
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.AsynchronousSocketChannel
import java.util.*
import kotlin.concurrent.thread

/**
 *
 */
object Support : AbstractDevice(-1), InternetDevice {
    private const val port: Int = 20000
    override val address: SocketAddress = InetSocketAddress(InetAddress.getLocalHost(), port)
    override var physicalDevice = SocketCommunication(this)

    val devices: DeviceManager = DeviceManager();

    override fun execute() {
        //devices.finalizeIfNecessary()
        devices.getDevices().forEach { it.tell(Message(id, MessageType.Execute))}
    }

    override fun tell(message: Message) {
    }

    override fun showResult(result: String) {
    }

    private val defaultSocketCallback: (AsynchronousSocketChannel) -> Unit = {
        val address = it.remoteAddress
        val message = physicalDevice.extractMessage(it)
        when (message.type) {
            MessageType.Join -> {
                val ip = address.toString().trim('/').split(':').first()
                val port = message.content.toString().toInt()
                val joining = devices.createAndAddDevice { id ->
                    LocalExecutionDevice(id, InetSocketAddress(InetAddress.getByName(ip), port))
                }
                joining.tell(Message(id, MessageType.ID, joining.id))

                println("$ip:$port joins")
            }
            MessageType.SendToNeighbours -> devices.getNeighbours(message.senderUid).forEach { n ->
                n.tell(message.content as Message)
            }
            else -> { }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        physicalDevice.startServer(defaultSocketCallback)
        thread {
            val timer = Timer()
            val task = object: TimerTask() {
                var run = 0
                override fun run() {
                    devices.getDevices().filterIsInstance<LocalExecutionDevice>().forEach {
                        it.showResult(run.toString())
                    }
                    run++
                }
            }

            timer.schedule(task, 0, 2000)
        }

    }
}