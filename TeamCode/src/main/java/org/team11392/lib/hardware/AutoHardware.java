package org.team11392.lib.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.team11392.lib.positron.Positron;

public class AutoHardware extends CommonHardware {
    public Servo jewelBase;
    public Servo jewelArm;
    public Positron pos;
    public void init(HardwareMap ahwMap) {
        initCommon(ahwMap);
        jewelBase  = hwMap.get(Servo.class, "jewelBase");
        jewelArm   = hwMap.get(Servo.class, "jewelArm");
        pos = new Positron();
    }
}
