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

const val SUPPORT_ID = -1

/**
 * The server basis.
 * The only place where the net topology is known.
 */
object Support : AbstractDevice(SUPPORT_ID, "Support"), InternetDevice {
    private const val port: Int = 20000
    override val address: SocketAddress = InetSocketAddress(InetAddress.getLocalHost(), port)
    override var physicalDevice = SocketCommunication(this)

    val devices: DeviceManager = DeviceManager();

    override fun execute() {
        devices.getDevices().forEach { it.tell(Message(id, MessageType.Execute))}
    }

    override fun tell(message: Message) {
        when (message.type) {
            MessageType.SendToNeighbours -> devices.getNeighbours(message.senderUid).forEach {
                it.tell(message.content as Message)
            }
            else -> { }
        }
    }

    override fun showResult(result: String) {
        //unused
    }

    @JvmStatic
    fun main(args: Array<String>) {
        //used for test purposes, mainly to check the interactions between server and clients

        physicalDevice.startServer(SocketCommunication.serverCallback)
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