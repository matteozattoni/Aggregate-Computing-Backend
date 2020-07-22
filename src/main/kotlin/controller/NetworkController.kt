package controller

import communication.Message
import communication.interfaces.CommunicationController
import devices.interfaces.RemoteDevice
import server.DefaultServerFactory
import server.interfaces.ServerFactory
import devices.implementations.SupportDevice
import java.util.*

/**
 * This class is the entry point to using this library, it's used for adding communication technology to whole platform.
 *
 * Has a list of [CommunicationController] (interface that represent generic network controller) in fact, each controller
 * specified a network technology this platform can use. Moreover, is the information expert about the singleton
 * [SupportDevice] (if not supported can find it on the network), this controller is the entry point where the client can
 * add functionality and connect to the backend.
 */
class NetworkController private constructor(serverFactory: ServerFactory) {

    companion object {
        @Volatile
        private lateinit var networkController: NetworkController

        fun createNetworkController(serverFactory: ServerFactory = DefaultServerFactory()): NetworkController{
            if (::networkController.isInitialized)
                return networkController

            synchronized(this) {
                if (::networkController.isInitialized)
                    return networkController
                val controller = NetworkController(serverFactory)
                networkController = controller
                return controller
            }
        }

        @Throws(UninitializedPropertyAccessException::class)
        fun getNetworkController(): NetworkController{
            if (!::networkController.isInitialized){
                throw UninitializedPropertyAccessException("property: networkController is not initialized yet.")
            }
            return networkController
        }
    }

    val support: SupportDevice? = serverFactory.createSupport()

    private val listOfController =
        Collections.synchronizedList(mutableListOf<CommunicationController>())

    /**
     * Start the server, set all the Communication to the server
     */
    fun startServer() {
        if (support != null) {
            listOfController.iterator().forEach {
                it.setCommunicationForServer(support)
            }
            support.startServer()
        }
    }

    /**
     * This is called by the Client, the NetworkController send the reference's callback to each CommunicationController
     * @param serverReady this callback is called when the SupportDevice is found and a join has been sent to it
     * @param onMessage this callback is called when messages have been received from SupportDevice
     */
    fun getMainServer(serverReady: (RemoteDevice) -> Unit, onMessage: (Message) -> Unit) {
        listOfController.iterator().forEach {
            it.getRemoteServer(serverReady, onMessage)
        }
    }

    /**
     * Start the offering server calling the method on each CommunicationController
     */
    fun startOfferServer() {
        listOfController.iterator().forEach {
            it.startOfferServer()
        }
    }


    /**
     * Stop the offering server calling the method on each CommunicationController
     */
    fun stopOfferServer() {
        listOfController.iterator().forEach {
            it.stopOfferServer()
        }
    }

    /**
     * Call stopServer on each physical device attached to the Support
     */
    fun stopAllService() {
        stopOfferServer()
        support?.stopServer()
    }

    /**
     * Add a [controllerInterface] to this NetworkController
     */
    fun addController(controllerInterface: CommunicationController) {
        controllerInterface.support = support
        listOfController.add(controllerInterface)
    }


}