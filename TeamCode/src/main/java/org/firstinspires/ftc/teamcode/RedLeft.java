package org.firstinspires.ftc.teamcode;

/*
RedLeft 2018

RedLeft 2018 is the Autonomous Op Mode designed to operate when
the robot is located on the red alliance, and on the left
balancing board from the drive team's perspective.

RedLeft 2018 is developed by Brian Lu
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.team11392.lib.AutoLibs;
import org.team11392.lib.MrOutput;
import org.team11392.lib.hardware.AutoHardware;
import org.team11392.lib.positron.Positron;

@Autonomous (name="RedLeft", group="Leaf Guard")
public class RedLeft extends OpMode{
    private MrOutput out;
    private AutoLibs auto;
    private AutoHardware robot;
    private ElapsedTime et;
    private Positron pos;

    private boolean jewelRun = false;
    @Override
    public void init() {
        out = new MrOutput(telemetry, 2);
        robot.init(hardwareMap);
        et = new ElapsedTime();
        auto = new AutoLibs(robot, out);
        pos = new Positron();
        out.println("Initialized!");
    }
    @Override
    public void loop() {
        if (!jewelRun) {
            out.println("jewelLoop has not been run. running.");
            auto.jewelLoop(true);
            jewelRun = true;
        }
        if (et.seconds() > 25) {
            out.println("OOF");
        }
    }
}
