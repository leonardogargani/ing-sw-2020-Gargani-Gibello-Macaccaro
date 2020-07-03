# Software Engineering Final Project 2019/2020

<img src="https://cf.geekdo-images.com/imagepagezoom/img/pWzdo3Bkfko9ruepJTjchCa4Z0A=/fit-in/1200x900/filters:no_upscale()/pic3283110.png" alt="santorini_board_game" width="500"/>
<br>
<br>

Read this in: [English](https://github.com/leonardogargani/ing-sw-2020-Gargani-Gibello-Macaccaro/blob/master/README.md), [Italian](https://github.com/leonardogargani/ing-sw-2020-Gargani-Gibello-Macaccaro/blob/master/README.it.md).


## Group members (PSP43)

Leonardo Gargani - leonardo.gargani@mail.polimi.it

Riccardo Gibello - riccardo.gibello@mail.polimi.it

Roberto Macaccaro - roberto.macaccaro@mail.polimi.it


## Implemented features

We have implemented all the project requirements and 2 advanced features [FA]:
- Full Rules
- CLI
- GUI
- Socket
- Multiple Games [FA]
- Advanced Gods [FA]

In addition, we have implemented another (unrequired) feature that allows a player to leave the game at any time, returning to the main screen, and giving the possibility to enter a new game hosted on the same server.


## Instructions to launch the server and the client

We have generated two separate jars using Maven profiles: one for the client and the other one for the server.


#### Client

The client can be launched in two different ways:
- GUI mode: simply double-click on the icon of the jar or run the command `java -jar Client.jar` in the directory containing it;
- CLI mode: you have to run the command `java -jar Client.jar -cli` in the directory containing the jar.


#### Server

It is possible to launch the server double-clicking on the icon of the jar or running the command `java -jar Server.jar`.


## UML

The UMLs of the project are available at the following links:
- [Full UML](https://github.com/leonardogargani/ing-sw-2020-Gargani-Gibello-Macaccaro/blob/master/deliveries/final/uml/uml_complete.pdf)
- [Detailed UMLs](https://github.com/leonardogargani/ing-sw-2020-Gargani-Gibello-Macaccaro/tree/master/deliveries/final/uml/uml_detailed)


## Additional remarks

As regards testing we have used *Mockito* (thus importing its library).
This way we have been able to easily test the interaction with the client without having to implement additional classes.
