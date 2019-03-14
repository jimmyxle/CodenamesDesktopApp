package ca.concordia.encs.comp354.model;

/**
*
* @author Christopher Idah
* 
* This is the package level documentation for the {@link ca.concordia.encs.comp354.model} package.
* 
* It is part of the MVC design pattern used to implement the Codenames board game.
* 
* This is done by incorporating the logic and control flow classes from the {@link ca.concordia.encs.comp354.controller} 
* package and the {@link ca.concordia.encs.comp354.view} package that handles the visualization of data through the 
* game.
* 
* The function of the classes in this package is to maintain the integrity and fundamental attributes of game objects. 
* 
* These classes will have some logic to update the controller package classes if their data changes.
* 
* This allows for a higher degree of encapsulation as it keeps the model and view packages separate from each other.
* 
* The model is implemented in the {@link GameState} class. The controller interacts with the <tt>GameState</tt> 
* directly, and is allowed to modify its state freely. The view, on the other hand, receives the model as a 
* <tt>ReadOnlyGameState</tt>, which offers an immutable interface to the model. This allows view elements to watch for
* changes in the model, minimizing redundant code and maintaining encapsulation, as the view still has to interact
* with the controller to instigate changes in the model.
* 
* The {@link GameState} also serves as the command manager.
* 
*/