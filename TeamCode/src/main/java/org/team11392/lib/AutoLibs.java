package org.team11392.lib;

import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.*;

import org.firstinspires.ftc.teamcode.MecanumHardware;

public class AutoLibs {
    MecanumHardware robot;
    MrOutput out;
    private CryptoboxDetector cryptoboxDetector = null;

    public AutoLibs(MecanumHardware robot, MrOutput out) {
        this.robot = robot;
        this.out = out;
    }

    // This function is the entire process of detecting and moving a jewel
    public void jewelLoop(boolean redTeam) {
        double leftPosition = 0.2;
        double rightPosition = 0.8;
        out.println("jewel loop started");
        // Bring the base to the middle
        robot.hitTurn.setPosition(0.5);
        // Set the arm to an upright position
        robot.hitArm.setPosition(0.2);
        // Bring down the jewel arm slowly
        while (robot.hitArm.getPosition() > 0.03) {
            sleep(100);
            robot.hitArm.setPosition(robot.hitArm.getPosition() - 0.02);
        }
        // The following lines are for detecting the jewel color and moving the base
        robot.jewel.enableLed(true);
        int R, G, B;
        ElapsedTime et = new ElapsedTime();
        boolean jewelRed  = false;
        boolean jewelBlue = false;
        out.setStaticCaption(0,0);
        while ((!jewelRed || !jewelBlue) && et.seconds() < 5) {
            out.clearStaticLine(0);
            R = robot.jewel.red();
            G = robot.jewel.green();
            B = robot.jewel.blue();
            out.buildStaticLine(0,"red", out.toString(R));
            out.buildStaticLine(0,"green", out.toString(G));
            out.buildStaticLine(0,"blue", out.toString(B));
            // Detect jewel color, 1.5 is to make sure color is intense enough
            // If jewel is red
            if (R > G + 1.5 && R > B + 1.5) {
                jewelRed = true;
                // Move the base to the side
                out.println("jewel red");
            }
            //if jewel is blue
            if (B > G + 1.5 && B > R + 1.5) {
                jewelBlue = true;
                // Move the base to the side
                out.println("jewel blue");
            }
        }
        if (jewelRed && redTeam)
            robot.hitTurn.setPosition(leftPosition);
        if (jewelRed && !redTeam)
            robot.hitTurn.setPosition(rightPosition);
        if (jewelBlue && !redTeam)
            robot.hitTurn.setPosition(leftPosition);
        if (jewelBlue && redTeam)
            robot.hitTurn.setPosition(rightPosition);
        robot.hitTurn.setPosition(0.5);
        sleep(200);
        robot.hitArm.setPosition(0.7);
        robot.jewel.enableLed(false);
        out.println("jewel loop ended");
    }

    public void cryptoPosition() {

    }


    public void sleep(int ms) {
        ElapsedTime et = new ElapsedTime();
        while (et.milliseconds() < ms);
    }
}
