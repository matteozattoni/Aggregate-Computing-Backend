package controller

import communication.Message
import communication.interfaces.CommunicationController
import devices.interfaces.RemoteDevice
import server.DefaultServerFactory
import server.interfaces.ServerFactory
import devices.implementations.SupportDevice
import java.util.*


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

    fun startServer() {
        if (support != null) {
            listOfController.iterator().forEach {
                it.setCommunicationForServer(support)
            }
            support.startServer()
        }
    }

    fun getMainServer(serverReady: (RemoteDevice) -> Unit, onMessage: (Message) -> Unit) {
        listOfController.iterator().forEach {
            it.getRemoteServer(serverReady, onMessage)
        }
    }


    fun startOfferServer() {
        listOfController.iterator().forEach {
            it.startOfferServer()
        }
    }

    fun stopOfferServer() {
        listOfController.iterator().forEach {
            it.stopOfferServer()
        }
    }

    fun stopAllService() {
        stopOfferServer()
        support?.stopServer()
    }

    fun addController(controllerInterface: CommunicationController) {
        controllerInterface.support = support
        listOfController.add(controllerInterface)
    }


}