package org.team11392.lib;

import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.*;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.MecanumHardware;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;

public class AutoLibs {
    MecanumHardware robot;
    MrOutput out;
    private CryptoboxDetector cryptoboxDetector;
    char column = 'c';
    int columnOffest = 0;
    int pixelAccuracy = 80;
    double centerPixel = 500;
    double shiftSpeed = 0.2;
    double vuMarkPos = 0.3;
    public AutoLibs(MecanumHardware robot, MrOutput out) {
        this.robot = robot;
        this.out = out;
    }

    // This function is the entire process of detecting and moving a jewel
    public void jewelLoop(boolean redTeam) {
        robot.handsClose(1);
        robot.handsClose(3);
        sleep(300);
        robot.liftUp(0.2, 0);
        sleep(1000);
        robot.liftHold();
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
                out.println("jewel red");
            }
            //if jewel is blue
            if (B > G + 1.5 && B > R + 1.5) {
                jewelBlue = true;
                out.println("jewel blue");
            }
        }
        // Move the base to the side, depending on the team
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

    public void cryptoPosition(boolean rotateBot, boolean farTurn) {
        //turn the phone servo to face VuMark
        robot.phoneServo.setPosition(vuMarkPos);
        //wait until detected
        robot.detectPic();
        //Convert enum to char
        switch (robot.vuMark) {
            case LEFT:
                column = 'l';
                break;
            case CENTER:
                column = 'c';
                break;
            case RIGHT:
                column = 'r';
                break;
            default:
                out.println(1,"wait wut, bad vu reading");
                break;
        }
        //remove vuforia from camera for DogeCV
        robot.vuforia.close();
        //revert phone position back to side
        robot.phoneServo.setPosition(0.5);
        // DogeCV functions
        //moving with auto should be rigid
        robot.zeroBrake();
        //dogecv declarations
        cryptoboxDetector = new CryptoboxDetector();
        cryptoboxDetector.init(robot.hwMap.appContext, CameraViewDisplay.getInstance());
        cryptoboxDetector.rotateMat = true;
        cryptoboxDetector.enable();
        //the middle X pixel of the camera
        centerPixel = cryptoboxDetector.getFrameSize().width/2;
        if (!farTurn) {
            while (!cryptoboxDetector.isCryptoBoxDetected()) {
                shiftCameraLeft(rotateBot);
            }
        } else {
            if (!rotateBot) {
                robot.moveByTime(shiftSpeed,0,0,2000);
                robot.imuTurnLeft(-90);
                while (!cryptoboxDetector.isCryptoBoxDetected()) {
                    shiftCameraLeft(rotateBot);
                }
            } else {
                robot.moveByTime(-shiftSpeed,0,0,2000);
                robot.imuTurnRight(90);
                while (!cryptoboxDetector.isCryptoBoxDetected()) {
                    shiftCameraRight(rotateBot);
                }
            }
        }
        robot.setPowerZero();
        setCryptoPerpendicular(rotateBot, farTurn);
        while (Math.abs(centerPixel-cryptoboxDetector.getCryptoBoxCenterPosition()) > pixelAccuracy) {
            if (centerPixel + columnOffest > cryptoboxDetector.getCryptoBoxCenterPosition())
                shiftCameraRight(rotateBot);
            if (centerPixel + columnOffest < cryptoboxDetector.getCryptoBoxCenterPosition())
                shiftCameraLeft(rotateBot);
        }
        setCryptoPerpendicular(rotateBot, farTurn);
        switch (column) {
            case 'l':
                while (Math.abs(centerPixel-cryptoboxDetector.getCryptoBoxLeftPosition()) > pixelAccuracy) {
                    if (centerPixel + columnOffest > cryptoboxDetector.getCryptoBoxLeftPosition())
                        shiftCameraRight(rotateBot);
                    if (centerPixel + columnOffest < cryptoboxDetector.getCryptoBoxLeftPosition())
                        shiftCameraLeft(rotateBot);
                }
                break;
            case 'c':
                // probs already centered
                break;
            case 'r':
                while (Math.abs(centerPixel-cryptoboxDetector.getCryptoBoxRightPosition()) > pixelAccuracy) {
                    if (centerPixel + columnOffest > cryptoboxDetector.getCryptoBoxRightPosition())
                        shiftCameraRight(rotateBot);
                    if (centerPixel + columnOffest < cryptoboxDetector.getCryptoBoxRightPosition())
                        shiftCameraLeft(rotateBot);
                }
                break;
            default:
                out.println(1,"wait wut, improper column variable");
                break;
        }
        robot.imuTurnRight(robot.getIMUHeading() + 90);
        robot.moveByTime(shiftSpeed,0,0, 8000);
        robot.handsOpen(1);
        robot.handsOpen(3);
        robot.moveByTime(shiftSpeed,0,0, 1000);
        robot.setPowerZero();
    }

    private void setCryptoPerpendicular(boolean rotateBot, boolean farTurn) {
        if (!rotateBot && !farTurn)
            robot.imuSet(0);
        if (!rotateBot && farTurn)
            robot.imuSet(-90);
        if (rotateBot && !farTurn)
            robot.imuSet(0);
        if (rotateBot && farTurn)
            robot.imuSet(90);
    }

    private void shiftCameraLeft(boolean rotateBot) {
        if (!rotateBot)
            robot.move(shiftSpeed,0,0);
        if (rotateBot)
            robot.move(-shiftSpeed,0,0);
    }
    private void shiftCameraRight(boolean rotateBot) {
        if (!rotateBot)
            robot.move(-shiftSpeed,0,0);
        if (rotateBot)
            robot.move(shiftSpeed,0,0);
    }

    public void runPile() {
        //TODO Glyph pile scavenge
    }

    public void navToCryptobox() {
        //TODO navigate back to cryptobox from pile
    }

    public void sleep(int ms) {
        ElapsedTime et = new ElapsedTime();
        while (et.milliseconds() < ms);
    }
}
