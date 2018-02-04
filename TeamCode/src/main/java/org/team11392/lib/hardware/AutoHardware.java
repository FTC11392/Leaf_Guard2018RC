package org.team11392.lib.hardware;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class AutoHardware extends CommonHardware {
    public Servo jewelBase;
    public Servo jewelArm;
    public ColorSensor jewelEye;
    public void init(HardwareMap ahwMap) {
        initCommon(ahwMap);
        jewelBase  = hwMap.get(Servo.class, "jewelBase");
        jewelArm   = hwMap.get(Servo.class, "jewelArm");
        jewelEye   = hwMap.get(ColorSensor.class, "jewelEye");
        jewelEye.enableLed(false);
    }
}
