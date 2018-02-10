package org.firstinspires.ftc.teamcode;

/*
BlueLeft 2018

BlueLeft 2018 is the Autonomous Op Mode designed to operate when
the robot is located on the blue alliance, and on the left
balancing board from the drive team's perspective.

BlueLeft 2018 is developed by Brian Lu
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.team11392.lib.AutoLibs;
import org.team11392.lib.MrOutput;
import org.team11392.lib.positron.Positron;

@Autonomous (name="BlueLeft", group="Leaf Guard")
public class BlueLeft extends LinearOpMode {
    boolean redTeam = false;
    boolean rotateBot = !redTeam;
    boolean farTurn = true;

    private MrOutput out;
    private AutoLibs auto;
    private MecanumHardware robot;
    private ElapsedTime et;
    private Positron pos;

    @Override
    public void runOpMode() throws InterruptedException {
        out = new MrOutput(telemetry, 2);
        robot = new MecanumHardware();
        robot.init(hardwareMap);
        et = new ElapsedTime();
        auto = new AutoLibs(robot, out);
        pos = new Positron();
        out.println("Initialized!");
        waitForStart();
        auto.jewelLoop(redTeam);
        auto.encodedPark(rotateBot, farTurn);
        sleep(30000);
    }
}
