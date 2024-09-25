# Three-letter-word-game-in-Java
The program simulates a three-letter word game for two players sitting at the console, taking turns by sharing a keyboard.

The purpose of the project is to:
  * Write a simple Java program using a procedural approach.
  * Make the program run robustly.
  * Write modular, readable, and maintainable code.
  * Make the program delicately handle user input.
  * Carefully follow the given instructions.

Game description
The letters of the alphabet are assigned numerical values according to their position, from A=1 to Z=26.
The first player enters a three-letter word whose combined letter value totals 20 or less – for example

A C E = 9 (1 + 3 + 5)

The program ensures that the word entered by the player is valid when checked against the word list
provided by the datafile.txt resource (it is recommended that the list is first loaded into a program data
structure rather than accessing the external text file each time). If the word is not a valid word, the player
should be prompted to enter a valid three-letter word until a valid word is input. If the three-letter word
entered has a calculated value that is greater than 20, then the first player should be prompted until they
have entered a word that has a value of 20 or less (this rule only applies for the first word at the start of the
game).
