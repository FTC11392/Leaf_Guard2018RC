package org.team11392.lib.hardware;

/*
CommonHardware 2018

Defenestration puts all CommonHardware declarations into a single
class named CommonHardware. CommonHardware 2018 is this class for the
2017-2018 FTC season.

The configuration of CommonHardware can be customized in the
variables below, so that missing components will not throw
fatal errors.

This year's configuration is designed for a Mecanum/
Holonomic drivetrain.

CommonHardware 2018 is developed by Brian Lu
 */

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class CommonHardware {
    // Hardware objects will be defined with HardwareMap
     public HardwareMap hwMap;
    // Hardware objects for mecanum drivetrain
    // Names are defined with the glyph lift as the front
    public DcMotor  frontLeft;
    public DcMotor  frontRight;
    public DcMotor  backLeft;
    public DcMotor  backRight;
    // Linear lift for glyphs
    public Servo    glyphLift;
    // Servo that turns the claw mechanism
    public Servo    clawTurner;
    // First claw servos
    public Servo    clawALeft;
    public Servo    clawARight;
    // Second claw servos
    public Servo    clawBLeft;
    public Servo    clawBRight;


    public DcMotor relicArm;
    public Servo relicWrist;
    public Servo   relicHand;
    public Servo jewelBase;
    public Servo jewelArm;
    public ColorSensor jewelEye;


    public void initCommon(HardwareMap ahwMap) {
        // Save reference to hardware map
        hwMap = ahwMap;
        // Initialize the hardware objects with names in the configuration
        frontLeft  = hwMap.get(DcMotor.class, "frontLeft");
        frontRight = hwMap.get(DcMotor.class, "frontRight");
        backLeft   = hwMap.get(DcMotor.class, "backLeft");
        backRight  = hwMap.get(DcMotor.class, "backRight");
        glyphLift  = hwMap.get(Servo.class, "glyphLift");
        clawTurner = hwMap.get(Servo.class, "clawTurner");
        clawALeft  = hwMap.get(Servo.class, "clawALeft");
        clawARight = hwMap.get(Servo.class, "clawARight");
        clawBLeft  = hwMap.get(Servo.class, "clawBLeft");
        clawBRight = hwMap.get(Servo.class, "clawBRight");


        relicArm   = hwMap.get(DcMotor.class, "relicArm");
        relicWrist = hwMap.get(Servo.class, "relicWrist");
        relicHand  = hwMap.get(Servo.class, "relicHand");
        relicArm.setDirection(DcMotor.Direction.FORWARD);
        relicArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jewelBase  = hwMap.get(Servo.class, "jewelBase");
        jewelArm   = hwMap.get(Servo.class, "jewelArm");
        jewelEye   = hwMap.get(ColorSensor.class, "jewelEye");
        jewelEye.enableLed(false);


        // Set motor direction for mecanum
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        // No encoders wire attached, so no encoders
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
