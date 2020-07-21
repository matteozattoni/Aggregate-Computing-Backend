# **Aggregate Computing Backend**

The goal of this project is the development of a backend for Aggregate Computing, using a Peer to Peer architecture.

## **Requirements**

* 3 basic functional mode:

  * The backend itself executes the whole program
  * The backend exchanges information to other backend
  * The distributed devices execute the whole program, using the backend to send messages to other members


* Hybrid mode

  * The device can decide to enter the lightweight mode, telling the backend that they won’t execute anything. In that case the backend must execute their part in their place.


* Protelis compatibility

  * Basic functionalities must be platform independent
  * Can be added ScaFi comparability if the device and message class use the DeviceUID interface instead of Integer to represent the Device’s identity, this is for not inject structural dependencies.

## **Architecture**

The backend part is where all client can synchronised with other distributed device without concern about the management of topology and communication. Therefor the client after established the communication with the server it has only the responsibility to execute the aggregate program.

The device provide operation to share their internal state, to execute program cycle, ...

The execution is totally delegated to device:
  * Emulated
  * Local execution
  * Totally remote


It is possible to use these types of device together to create a mixed situation where some devices execute on some others don’t.

## **Package**

* #### *Adapter*
  * **ProtelisAdapter ProtelisContext ProtelisNetworkManager SimpleProtelisContext**: they are user to executing Protelis program, the device has an id field of type DeviceUID (isn’t integer anymore)
  * **ServerUID**: is an implementation of DeviceUID where the class UUID represent the identity of the device


* #### *Communication*

  * ###### *interfaces/*
    * **Communication**: interface that represent how a device can communicate with the physical device (some implementation of device represent a remote device)
    * **NetworkCommunication**:  extends Communication (see above), here the T parameter is a Socket, moreover is using a Chain of Responsibility pattern where more NetworkCommunication can add Network Protocol to reach the device
    * **CommunicationController**: this interfaces represent the responsibility that have each controller, this controller can be added to NetworkController

  * ###### *implements/*
    * Here I’ve implemented some interfaces above, in particular I’ve used ipv4/6 address and Socket for communication between a client and server.

* #### *controller*
  * **NetworkController**: has a list of CommunicationController (interface that represent generic network controller) in fact, each controller specified a network technology this platform can use. Moreover, is the information expert about the singleton SupportDevice (if not supported can find it on the network), this controller is the entry point where the client can add functionality and connect to the backend.


* #### *devices*
  * ###### *interfaces/*
    * **Device**: represent a generic device, the field ID uses the DeviceUID abstraction
    * **RemoteDevice**: represent a remote device, the field NetworkCommunication tell how to reach the physical device
    * **AbstractDevice/Emulated Device**: abstraction of Device

  * ###### *implements/*

    * **PeerDevice**: implements RemoteDevice, is a Peer that can be reached through its NetworkCommunication, dispatch the message that received.
    * **LocalExecutionDevice**: represent the local platform execution, only the result of computation are sent to physical device
    * **SupportDevice**: extends PeerDevice, it’s the server class thanks to other SupportDevice provide the communication between client on entire network
* #### *server*
  * ###### *interfaces/*
    * **NetworkInformation**: this interface is used when Join or OfferServer Message are received, hide information used to assign the right NetworkCommunication to the new Device that has been created. This interface is needed because the SupportDevice is the creator of devices, but the ServerSupport doesn’t know anything about which NetworkCommunication is used to get message and to reach this specific device, therefore this interfaces is needed to assign the network communication to the device, moreover each network stack can manage this assignment, and the information that are needed (and hided)
    * **ServerFactory**: specified an Abstract Factory pattern. each one who uses this platform can give their own implementation of Remote Device or Emulator, DeviceUID and even a Server Support, this open the possibility to extend almost the whole platform.


 * **DefaultServerFactory**: the default concrete ServerFactory

 * **DeviceManager**: this class is used by SupportDevice, and it is the information expert of each Device that are connected to the ServerSupport and that are Client (execute the program) or other SupportDevice. Each DeviceManager has a local information about the topology, this topology can be seen as a set of client those exchange information between themselves thanks to the collaboration between SupportDevice. The neighbourhood relationship between two clients is true if and only if: 
    1. If two clients are connected to the same SupportDevice then this relationship is true.

    1. If two clients are connected  to two different SupportDevice and this two SupportDevice are connected themselves then this relationship is true
