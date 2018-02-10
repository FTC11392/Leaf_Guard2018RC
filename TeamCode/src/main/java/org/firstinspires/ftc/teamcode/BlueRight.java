package org.firstinspires.ftc.teamcode;

/*
BlueRight 2018

BlueRight 2018 is the Autonomous Op Mode designed to operate when
the robot is located on the blue alliance, and on the right
balancing board from the drive team's perspective.

BlueRight 2018 is developed by Brian Lu
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.team11392.lib.AutoLibs;
import org.team11392.lib.MrOutput;
import org.team11392.lib.positron.Positron;

@Autonomous (name="BlueRight", group="Leaf Guard")
public class BlueRight extends OpMode{
    boolean redTeam = false;
    boolean rotateBot = !redTeam;
    boolean farTurn = false;

    private MrOutput out;
    private AutoLibs auto;
    private MecanumHardware robot;
    private ElapsedTime et;
    private Positron pos;
    @Override
    public void init() {
        out = new MrOutput(telemetry, 2);
        robot = new MecanumHardware();
        robot.init(hardwareMap);
        et = new ElapsedTime();
        auto = new AutoLibs(robot, out);
        pos = new Positron();
        out.println("Initialized!");
    }

    @Override
    public void init_loop() {
        //auto.jewelLoop(redTeam);
        auto.cryptoPosition(rotateBot, farTurn);
    }

    @Override
    public void loop() {
        if (et.seconds() > 25) {
            auto.navToCryptobox();
            stop();
        }
        auto.runPile();
    }
}
