
package ca.concordia.encs.comp354.controller;

/**
*
* @author Christopher Idah
* 
* This is the package level documentation for the {@link ca.concordia.encs.comp354.controller} package.
* 
* It is part of the MVC design pattern used to implement the Codenames board game.
* 
* This is done by incorporating the data classes from the {@link ca.concordia.encs.comp354.model} package
* and the {@link ca.concordia.encs.comp354.view} package that handles the visualization of data through the game.
* 
* The function of the classes in this package is to control the data flow into the model package and update the 
* view package as changes are made to the model. This allows for a higher degree of encapsulation as it keeps the model 
* and view packages separate from each other.
* 
* This package revolves around the {@link GameController} class, which receives input events from the view, coordinates 
* instances of {@link Player} subclasses, and generates {@link GameAction}s to drive the game logic.
*/