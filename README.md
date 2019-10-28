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