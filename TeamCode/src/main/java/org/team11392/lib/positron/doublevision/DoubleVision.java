package org.team11392.lib.positron.doublevision;

/*
DoubleVision component of Positron 2018

Positron is Defenestration's implementation for robot positioning.
It has functions that allow it to position and align a robot with
a combination of IMUs, encoders, and computer vision.

IMU turns, when passing their desired end position, will NOT
compensate. The robot will continue turning until the desired
position is measured.

Positron 2018 is developed by Brian Lu
 */

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.BuildConfig;

public class DoubleVision {
    String vuforiaKey = BuildConfig.VUFORIA_KEY;
    public ClosableVuforiaLocalizer vuforia;
    public VuforiaTrackables relicTrackables;
    public VuforiaTrackable relicTemplate;
}
