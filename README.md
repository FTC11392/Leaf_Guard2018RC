## Leaf Guard 2018 Robot COntroller
Leaf Guard is FTC Team 11392 Defenestration's robot for the 2017-2018 FIRST Tech Challenge Relic Recovery Season. The rest is self-explanatory.

## Notable Components
Our codebase has several notable components that might be useful to other teams.

### Positron
Positron (short for Position-tronic) is the positioning system used on our robot. It uses a combonation of gyroscopes, accelerometers, and computer vision to (hopefully accurately) approximate the position of the robot on the field.

AKA it knows where your robot is and moves to wher you want it.

### DoubleVision
DoubleVision is the vision component of Positron. It combines both Vuforia and DogeCV's implementations of the 2017-2018 field and recieves the best of both world by loading the libraries on different threads.

### Mr. Output
Annoyed by how the FTC SDK's telemetry system worked, Defenestration's lead programmer decided to make a more convenient way to output text. Mr. Output is able to both output telemetry-like "static" (stationary) lines and scrolling "output" lines at the same time. It still uses telemetry after it organizes the lines, but its easy to use.
