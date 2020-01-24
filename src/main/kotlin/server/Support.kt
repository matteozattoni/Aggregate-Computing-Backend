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
 *
 */
object Support : AbstractDevice(SUPPORT_ID, "Support"), InternetDevice {
    private const val port: Int = 20000
    override val address: SocketAddress = InetSocketAddress(InetAddress.getLocalHost(), port)
    override var physicalDevice = SocketCommunication(this)

    val devices: DeviceManager = DeviceManager();

    override fun execute() {
        //devices.finalizeIfNecessary()
        devices.getDevices().forEach { it.tell(Message(id, MessageType.Execute))}
    }

    override fun tell(message: Message) {
        //unused
    }

    override fun showResult(result: String) {
        //unused
    }

    @JvmStatic
    fun main(args: Array<String>) {
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