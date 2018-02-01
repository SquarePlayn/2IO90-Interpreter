# Interpreter

## Installation instructions
These instructions assume that you use IntelliJ IDEA

1. Create a new empty java project
2. Clone this repository, the [test factory](https://github.com/SquarePlayn/2IO90-TestFactory) and the [algorithm](https://github.com/SquarePlayn/2IO90-Algorithm) in the project folder.
3. For each repository, do the following
   * In IntelliJ, select file > New > Module from existing sources
   * Select the repository
   * Import module from external modal > Maven
   * Next until finish
4. In the upper right corner, next to "run", select "Edit Configurations"
5. Press the green plus in the left upper corner > Maven
   * Set "Working directory" to the interpreter repository (click the yellow folder with blue square)
   * Set "Command line" to "clean install"
   * Apply and ok
6. Now "run" should build the entire project
