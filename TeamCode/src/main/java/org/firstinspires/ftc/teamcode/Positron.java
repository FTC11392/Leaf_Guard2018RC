package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/*
Positron 2018

Positron is Defenestration's implementation for robot positioning.
It has functions that allow it to position and align a robot with
a combination of IMUs, encoders, and computer vision.

Computer vision requires Vision.java.
Encoders are not implemented yet.

IMU turns, when passing their desired end position, will NOT
compensate. The robot will continue turning until the desired
position is measured.

Positron 2018 is developed by Brian Lu
 */
public class Positron {
    public BNO055IMU imu;
    Orientation angles;
    public Positron() {

    }
}
