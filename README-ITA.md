# Aggregate Computing Backend

Il goal del progetto è quello di sviluppare una Architettura backend per Aggregate Computing, nello specifico utilizzando un modello Peer to Peer

## **Requisiti**

* 3 modalità di funzione:

  * il server esegue il programma
  * il server scambia informazioni con altri server
  * I device eseguono il programma, usando il server per inviare messaggi agli altri membri


* Modalità ibrida

  * I device possono decidere di entrare in modalità Lightweight, dicendo al server che loro non eseguiranno nulla. In questo caso sarà il server ad eseguire il programma per loro inviando al device interessato il risultato


* Compatibilità Protelis

  * Funzionalità base devono essere piattaforma indipendente
  * In futuro potrà essere aggiunta la compatibilità con ScaFi, bisogna adattare i cambiamenti effettuati, nello specifico le classi Device e Message non utilizzano più Interi per rappresentare gli identificativi, ma utilizzano l'interfaccia DeviceUID per non creare dipendenze strutturali e lasciare il software estendibile a futuri requisiti.

## **Architettura**

La parte server è quella parte che permette a singoli client di sincronizzarsi con vari dispositivi distribuiti senza doversi preoccuparsi di come la rete e comunicazioni vengano gestite, in pratica il client dopo essersi collegato al Server ha solo il compito di eseguire il programma aggregato.

I Device offrono operazioni per condividere il loro stato interno, eseguire cicli di programma, ...

L'esecuzione è totalmente responsabilità dei devices; differenti modalità di esecuzione sono rappresentate da differenti devices:
  * Emulated
  * Local execution
  * Totally remote

È possibile utilizzare questi tipi di device insieme per creare situazioni dove alcuni device eseguono ed altri no


## **Package**

* #### *Adapter*
  * **ProtelisAdapter ProtelisContext ProtelisNetworkManager SimpleProtelisContext**: sono utilizzate per eseguire programmi Protelis, il device possiede un campo id del tipo DeviceUID (non più Int)
  * **ServerUID**: è una implementazione di DeviceUID dove si utilizza la classe UUID per rappresentare gli identificativi


* #### *Communication*

  * ###### *interfaces/*
    * **Communication**: Interfaccia generale che specifica come un Device può comunicare con il Device fisico (ricordando che alcune istanze di Device specificano device remoti)
    * **NetworkCommunication**: Implementa Communication, come parametro T utilizza Socket; in più specifica un Pattern of Responsibility dove più NetworkCommunication possono essere riferite ad un unico Device, il Device deve ereditare l'interfaccia RemoteDevice
    * **CommunicationController**: Questa interfaccia specifica quali responsabilità devono avere i controller che possono essere aggiunti al NetworkController per aggiungere Protocolli di Network al server

  * ###### *implements/*
    * Qui ho implementato le classi sopracitate, vengono utilizzati indirizzi locali utilizzati per raggiungere Server Support e Client, con la differenza che l’indirizzo a cui si vuole connettere deve essere dato esplicitamente (non c’è un protocollo di ricerca attivo che autonomamente ricerca se esistono server nella rete, ma può essere aggiunto in qualsiasi momento)
* #### *controller*
  * **NetworkController**: offre servizi applicativi come il ServerSupport (lo cerca se non è supportato), possiede una lista di CommunicationControllerInterface che sono Interfacce Controller di vari Livelli Network che possono essere inseriti per aumentare in maniera indipendente funzionalità (come BLE)


* #### *devices*
  * ###### *interfaces/*
    * **Device**: Interfaccia che rappresenta un generico device, come ID viene utilizzato DeviceUID (non un intero)
    * **RemoteDevice**: Rappresenta un device Remoto che possiede un campo NetworkCommunication che specifica come raggiungere il device fisico
    * **AbstractDevice/Emulated Device**: astrazioni dell'interfaccia Device
    * **InternetDevice**: Rappresenta un Device che utilizza esclusivamente un SocketAddress per raggiungere il device fisico (non viene utilizzata)
  * ###### *implements/*

    * **PeerDevice**: implementa RemoteDevice, rappresenta un Peer che può essere raggiunto esattamente come specificato dall'interfaccia, quindi inoltra tutti i messaggi al device fisico.
    * **LocalExecutionDevice**: rappresenta la piattaforma di esecuzione in modalità locale, vengono inviati al device fisico solo i risultati delle iterazioni
    * **SupportDevice**: eredita PeerDevice, rappresenta la classe Server, grazie alla collaborazione di altri SupportDevice presenti in altri dispositivi, consente la comunicazione tra i vari dispositivi della rete
* #### *server*
  * ###### *interfaces/*
    * **NetworkInformation**: interfaccia che viene utilizzata quando si ricevono messaggi Join o OfferServer, incapsula informazioni utilizzate per assegnare il physicalDevice al PeerDevice e viene restituita al ServerSupport per ricevere il contenuto del messaggio (che è stato incapsulato) e per assegnare il physicalDevice con il giusto NetworkCommunication). Questa interfaccia nasce dal bisogno di non cambiare le responsabilità già assegnate nell’architettura Client/Server, ovvero il DeviceManager continua ad essere l'Information Expert del Device ed il Support continua ad essere il Creator di tale, però il Support non ha dipendenze sulle implementazioni di Communication, tuttavia finché si usavano socket asincroni dove bastava un indirizzo per avere tutte le informazioni necessarie, questo non vale per altri parametri T(come i socket classici) e quindi il Server Support non possiede le informazioni necessarie per assegnare la giusta implementazione della comunicazione al Device, il device è creato dopo che il socket è già stato aperto, quindi si utilizza questa interfaccia che continua a nascondere le implementazioni della Communication dal Support lasciando libertà a chi la implementa di avere le informazioni necessarie e la loro gestione.
    Ho utilizzato Socket come parametro T perché la maggior parte dei protocolli di rete li utilizza e lasciare l’implementazione più generale possibile (ad esempio se si decidesse di utilizzare WiFi Aware)
    * **ServerFactory**: Questa interfaccia specifica un pattern Abstract Factory dove chi utilizza la libreria può specificare una propria factory (quindi una propria implementazione) dei Device Remoti o Emulati, DeviceUID e persino un proprio Support, questo apre la possibilità di personalizzare tutta l'architettura con l'eccezione della classe Message

 * **DefaultServerFactory**: implementazione che verrà utilizzata se non vengono specificate altre ServerFactory
 * **DeviceManager**: classe utilizzata dal SupportDevice a cui viene assegnata la responsabilità di avere un riferimento di tutti i Device che si connettono al SupportDevice, in più distingue quali device si connettono in veste di Client (ovvero di voler eseguire il programma) oppure in veste di altri SupportDevice. Ogni DeviceManager quindi possiede una informazione locale sulla rete, la rete può essere vista come un insieme di Client che comunicano tra di loro grazie alla collaborazione dei SupportDevice. La relazione di vicinanza tra due client è vera se e solo se:
    1. se due client sono connessi allo stesso SupportDevice allora la relazione di vicinanza tra questi due client è vera
    1. se due client sono connessi a due SupportDevice distinti e questi due SupportDevice sono connessi direttamente allora la relazione di vicinanza tra questi due client è vera
