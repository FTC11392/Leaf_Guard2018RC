package org.firstinspires.ftc.teamcode;



/*
Vision 2018

Vision 2018 is Defenestration's class for all functions
related to vision. It utilises both Vuforia and DogeCV, a
computer vision library designed for the FTC 2017-2018
season based on OpenCV.

Vision 2018 requires a 3rd party class by team 5484
EnderBots called ClosableVuforiaLocalizer.java.
Vision 2018 will not compile if not present

Vision 2018 is developed by Brian Lu
 */

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class Vision {
    String vuforiaKey = BuildConfig.VUFORIA_KEY;
    public ClosableVuforiaLocalizer vuforia;
    public VuforiaTrackables relicTrackables;
    public VuforiaTrackable relicTemplate;
}
