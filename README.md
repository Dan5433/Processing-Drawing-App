# Processing Drawing App

A drawing app made with the Processing 4 language

## Features

### Complete

* Simple Shape Tools (point, line, rect, ellipse, triangle)
* Polygon Tool (straight and curved)
* Image Tool
* Preset Colours (black, red, orange, yellow, green, cyan, blue, violet, magenta, white)
* Colour Swapping with Shift Key
* Undo and Redo (supports polygon vertices)
* Exporting as a Processing Sketch
* Exporting as an image (PNG or JPG)
* Saving and Loading Serialised JSON

### In development

* Custom Colours
* Same Width and Height Shapes with Ctrl Key

## How to Install
Windows has a native exe file. Other operating systems will need to install JRE and run the jar.
Steps:
* Go to [releases](https://github.com/Dan5433/Processing-Drawing-App/releases)
* Depending on OS:
  * Windows
    * Download the zip with the exe
    * Run the exe
  * Other
    * Download the jar file
    * [Download a JRE](https://www.java.com/en/download/), if you don't already have one
    * Open a terminal and navigate to the folder containing the downloaded jar file
    * Run: java -jar "Processing Drawing App.jar" 

## How to Use
* Right-click for cycling tools
* Click or drag left-click to draw
* Properties
  * 3 properties
    * Fill colour
    * Stroke colour
    * Stroke weight
  * Scroll wheel to edit property value
  * Middle-click (click scroll wheel) to change which property to edit
* Shortcuts
  * Ctrl-z: Undo
  * Ctrl-y: Redo
  * Ctrl-l: Load saved JSON
  * Ctrl-s: Save as JSON
  * Ctrl-e: Export as an image
  * Ctrl-alt-s: Export as Processing Sketch Code
