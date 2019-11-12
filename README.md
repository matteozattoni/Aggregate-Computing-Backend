# Aggregate Computing Backend

The goal of this project is the development of a backend for Aggregate Computing.

...

**Requirements**

*	2 basic functioning modes:
	*	The backend itself executes the whole program.
	*	The distributed devices execute the program, using the backend to send messages to the other members.
	
*	Ibrid mode
	*	Devices can decide to enter in lightweight mode, telling the backend that they won't execute anything.
		In that case, the backend must execute their parts in their place.
		
*	Protelis and Scafi compatibility.
	*	Basic functionalities must be platform-independant.
	
**Architecture**

The Backend part is where all the members, as well as their topology, are stored.

Before starting the program execution, all the Devices need to subscribe into the Backend in order to be part of the net.

Devices provide operations to share their internal state, to execute program cycles, ...

The execution is totally delegated to Devices; 
different execution modes are represented by different Devices:
*   Emulated
*   Local execution
*   Totally remote

It is possible to use these types of Devices together to create mixed situations where some devices execute on some others don't.

Scafi and Protelis compatibility is granted by passing to Devices an Incarnation object that lets them to use platform-specific features.
