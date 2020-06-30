# Prova Finale di Ingegneria del Software

<img src="https://cf.geekdo-images.com/imagepagezoom/img/pWzdo3Bkfko9ruepJTjchCa4Z0A=/fit-in/1200x900/filters:no_upscale()/pic3283110.png" alt="santorini_board_game" width="500"/>


## Componenti gruppo (PSP43)

Leonardo Gargani - leonardo.gargani@mail.polimi.it
Riccardo Gibello - riccardo.gibello@mail.polimi.it
Roberto Macaccaro - roberto.macaccaro@mail.polimi.it


## Funzionalità implementate

Sono stati implementati tutti i requisiti di progetto e 2 funzionalità avanzate [FA]:
- Regole Complete
- CLI
- GUI
- Socket
- Partite Multiple [FA]
- Divinità Avanzate [FA]

È stata inoltre implementata (sebbene non richiesta) una funzione che permette al player di lasciare la partita in qualunque momento, tornando alla schermata principale, e permettendogli di inserirsi in una nuova partita sullo stesso server.


## Istruzioni per avviare server e client

Utilizzando i profiles di Maven abbiano generato due jar distinti: uno per client e uno per server. 

#### Client

Per quanto riguarda il client, è possibile avviarlo sia in modalità CLI che GUI:
- modalità GUI: basta semplicemente fare doppio click sull'icona;
- modalità CLI: è necessario digitare nel terminale `java -jar Client.jar -cli`.

#### Server

È possibile avviare il server con doppio click sull'icona o tramite terminale digitando `java -jar Server.jar`.


## UML

Gli UML del progetto sono disponibili ai seguenti link:
- [UML completo]() (link a github da inserire)
- [UML dettagliati]() (link a github da inserire)


## Note aggiuntive

Per quanto riguarda il testing abbiamo fatto uso di *Mockito* (importandone quindi le librerie).
In questo modo abbiamo potuto testare agevolmente l'interazione con il client senza dover implementare classi aggiuntive.

