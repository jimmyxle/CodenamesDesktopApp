# COMP354_CODENAMES
Winter 2019 project for COMP 354
Taught by Gregory Butler

## Getting Started
This project requires JDK 8+ and Maven 3.5+. If running from Eclipse, ensure that the bundled version of Maven is sufficiently recent (Eclipse Oxygen or newer should be fine).

If you've already cloned the repository, importing the project into Eclipse is simple. Click `File -> Import... -> Maven -> Existing Maven Projects`. Set the root directory to the project root folder. In the "Projects" list, you should now see a `pom.xml` for `354_codenames`. Make sure the checkbox next to this item is selected, and click "Finish." 

To run the game from Eclipse, right-click `ca.concordia.encs.comp354.Codenames` in the Package Explorer, and click `Run As -> Java Application`. To run unit tests, right-click the `tests` folder, then click `Run As -> JUnit Test`.

For a more detailed guide to downloading and building the project with Eclipse, see https://github.com/jimmyxle/COMP354_CODENAMES/wiki/Setup%3A-For-Eclipse-Users.

## Technology used/Built with 
Language used: Java
Libraries used: JavaFX 8, JUnit 4
IDE: Eclipse, IntelliJ IDEA

## About the game
This iteration simulates four simple AI players engaged in a game of Codenames. User input is limited to advancing the simulation, as well as stepping backward and forward through it.

## Rules/How to play
Use the "Advance" button in the bottom-right to step to the next event in the game. The "Show Keycard" checkbox in the upper-right will toggle the keycard overlay (normally only visible to spymasters). The "Undo" and "Redo" buttons will step backward and forward through the game history.

## Authors
COMP 354 group PK-A
* Alexandre	Abrams
* Alexandre	Briere
* Yaacov Cohen
* Zachary Hynes
* Christopher Idah
* Alexandre-Molyvan	Kang
* Elie Khoury
* Jimmy Le
* Mykyta Leonidov
* Vickel Leung
* Hoai An Luu
