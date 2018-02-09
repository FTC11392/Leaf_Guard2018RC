package org.team11392.lib;

/*
Controller 2018

Controller 2018 is the Teleoperated (TeleOp) Op Mode designed to
operate when the teleoperated period of the match is active.

Controller 2018 is designed to automatically switch to endgame
after a specified amount of time.

Controller 2018 is developed by Brian Lu
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.team11392.lib.hardware.TeleHardware;

@TeleOp (name = "Controller", group = "Leaf Guard")
public class Controller extends OpMode {
    MrOutput out;
    TeleHardware robot;
    ElapsedTime et;
    TeleLibs tele;
    boolean started = false;
    @Override
    public void init() {
        out = new MrOutput(telemetry, 2);
        robot = new TeleHardware();
        et = new ElapsedTime();
        tele = new TeleLibs(hardwareMap, robot, out, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        if (!started) {
            tele.startInit();
            started = true;
        }
        if (et.seconds() > 120) {
            tele.endInit();
        }
        if (et.seconds() > 150) {
            tele.haltAll();
            stop();
        }
        tele.controlState();
    }
}
