package controller

import communication.Message
import communication.interfaces.CommunicationController
import devices.interfaces.RemoteDevice
import server.Support
import java.util.*


open class NetworkController(supportEnabled: Boolean) {

    private val serverSupport: () -> Support? = { if (supportEnabled) Support else null}

    private val listOfController =
        Collections.synchronizedList(mutableListOf<CommunicationController>())

    fun startServer() {
        val support = serverSupport()
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
        serverSupport()?.stopServer()
    }

    fun addController(controllerInterface: CommunicationController) {
        // the support now is exposed
        controllerInterface.support = serverSupport()
        listOfController.add(controllerInterface)
    }


}