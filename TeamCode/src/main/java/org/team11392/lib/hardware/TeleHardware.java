package org.team11392.lib.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TeleHardware extends CommonHardware {
    /*
    public DcMotor relicArm;
    public Servo relicWrist;
    public Servo   relicHand;
    */
    public void init(HardwareMap ahwMap) {
        initCommon(ahwMap);
        /*
        relicArm   = hwMap.get(DcMotor.class, "relicArm");
        relicWrist = hwMap.get(Servo.class, "relicWrist");
        relicHand  = hwMap.get(Servo.class, "relicHand");
        relicArm.setDirection(DcMotor.Direction.FORWARD);
        relicArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        */
    }
}
