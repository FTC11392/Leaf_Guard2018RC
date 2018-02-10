package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.team11392.lib.AutoLibs;
import org.team11392.lib.MrOutput;

/**
 * Created by lu_ha on 2/9/2018.
 */

@Autonomous(name="EncoderTest", group="Leaf Guard")
public class EncoderTest extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        MrOutput out = new MrOutput(telemetry, 2);
        MecanumHardware robot = new MecanumHardware();
        robot.init(hardwareMap);
        AutoLibs auto = new AutoLibs(robot, out);
        waitForStart();
        robot.moveByInches(12, 0.3);
        sleep(20000);
        robot.imuSet(-90);
        sleep(20000);
        robot.moveByInches(6, 0.3);
        sleep(10);
    }
}
