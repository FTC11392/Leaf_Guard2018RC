package org.firstinspires.ftc.teamcode.lib.positron;

/*
IMU component of Positron 2018

Positron is Defenestration's implementation for robot positioning.
It has functions that allow it to position and align a robot with
a combination of IMUs, encoders, and computer vision.

IMU turns, when passing their desired end position, will NOT
compensate. The robot will continue turning until the desired
position is measured.

Positron 2018 is developed by Brian Lu
 */

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.lib.MrOutput;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class IMU implements Runnable{
    private int updateInterval = 80;

    private boolean fatalError = false;
    private boolean running = true;
    private BNO055IMU imu;
    private MrOutput out;

    private double xPosition = 0;
    private double yPosition = 0;

    private double heading = 0;

    public IMU(HardwareMap hwMap, MrOutput out) {
        this.out = out;
        out.println("Starting up IMU...");
        BNO055IMU.Parameters imuparams = new BNO055IMU.Parameters();
        try {
            out.println("Loading IMU parameters...");
            imuparams.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            imuparams.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            imuparams.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            imuparams.loggingEnabled = true;
            imuparams.loggingTag = "IMU";
            imuparams.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        } catch (Exception e) {
            out.println(2, "Fatal failure on loading IMU parameters");
            fatalError = true;
        }
        try {
            out.println("Loading IMU...");
            imu = hwMap.get(BNO055IMU.class, "imu");
            imu.initialize(imuparams);
            imu.startAccelerationIntegration(new Position(), new Velocity(), updateInterval);
        } catch (Exception e) {
            out.println(2, "Fatal failure to initialize IMU");
            fatalError = true;
        }
        if (fatalError) {
            out.println(2, "Fatal error, IMU functions will not run.");
        } else {
            out.println("IMU loaded with no fatal errors.");
        }
    }

    public Double getHeading() {
        if (!fatalError) {
          return heading;
        }
        return null;
    }

    public Double getXPosition() {
        if (!fatalError) {
            return xPosition;
        }
        return null;
    }

    public Double getYPosition() {
        if (!fatalError) {
            return yPosition;
        }
        return null;
    }

    @Override
    public void run() {
        while(running) {
            if (!fatalError) {
                updateOrientation();
                updatePosition();
                try {
                    sleep(updateInterval);
                } catch (InterruptedException e) {
                    out.println(2, "IMU thread could not sleep");
                    fatalError = true;
                    out.println(2, "Fatal error, IMU functions will not run.");
                }
            }
        }
    }

    private void updateOrientation() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        heading  = Double.parseDouble(formatAngle(angles.angleUnit, angles.firstAngle));
    }

    private String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    private String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    private void updatePosition() {
        Acceleration linearAccel = imu.getLinearAcceleration();
        Acceleration overallAccel = imu.getOverallAcceleration();
        xPosition = (updateInterval / 1000) * linearAccel.xAccel + xPosition;
        yPosition = (updateInterval / 1000) * linearAccel.yAccel + yPosition;

    }

    public void stopIMU() {
        if (!fatalError) {
            out.println("Stopping IMU logging...");
            imu.stopAccelerationIntegration();
            imu.close();
        }
        out.println("IMU logging stop");
        running = false;
    }
}
