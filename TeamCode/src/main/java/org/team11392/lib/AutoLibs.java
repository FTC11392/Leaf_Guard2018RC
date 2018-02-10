package org.team11392.lib;

import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.*;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.MecanumHardware;

public class AutoLibs {
    MecanumHardware robot;
    MrOutput out;
    Config config = new Config();
    private CryptoboxDetector cryptoboxDetector;
    char column = 'c';
    int columnOffest = 0;
    int pixelAccuracy = 80;
    double centerPixel = 500;
    double shiftSpeed = 0.2;
    public AutoLibs(MecanumHardware robot, MrOutput out) {
        this.robot = robot;
        this.out = out;
    }

    // This function is the entire process of detecting and moving a jewel
    public void jewelLoop(boolean redTeam) {
        robot.phoneServo.setPosition(1);
        robot.handsClose(1);
        robot.handsClose(3);
        sleep(300);
        robot.liftUp(0.2, 0);
        sleep(2000);
        robot.liftHold();
        double leftPosition = 0.2;
        double rightPosition = 0.8;
        out.println("jewel loop started");
        // Bring the base to the middle
        robot.hitTurn.setPosition(0.5);
        // Set the arm to an upright position
        robot.hitArm.setPosition(1);
        //Move phone to Vuforia Position
        robot.phoneServo.setPosition(0);
        // Bring down the jewel arm slowly
        while (robot.hitArm.getPosition() > 0.025) {
            sleep(100);
            robot.hitArm.setPosition(robot.hitArm.getPosition() - 0.02);
        }
        // The following lines are for detecting the jewel color and m.0oving the base
        robot.jewel.enableLed(true);
        int R, G, B;
        ElapsedTime et = new ElapsedTime();
        boolean jewelRed  = false;
        boolean jewelBlue = false;
        out.setStaticCaption(0,0);
        while ((!jewelRed && !jewelBlue) && et.seconds() < 1) {
            out.clearStaticLine(0);
            R = robot.jewel.red();
            G = robot.jewel.green();
            B = robot.jewel.blue();
            out.buildStaticLine(0,"red", out.toString(R));
            out.buildStaticLine(0,"green", out.toString(G));
            out.buildStaticLine(0,"blue", out.toString(B));
            // Detect jewel color, 1.5 is to make sure color is intense enough
            // If jewel is red
            if ((R > G + 1.5) && (R > B + 1.5)) {
                jewelRed = true;
                out.println("jewel red");
            }
            //if jewel is blue
            if ((B > G + 1.5) && (B > R + 1.5)) {
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
        sleep(2000);
        robot.hitArm.setPosition(0.7);
        sleep(200);
        robot.hitTurn.setPosition(0.5);
        robot.jewel.enableLed(false);
        out.println("jewel loop ended");
    }

    public void cryptoPosition(boolean rotateBot, boolean farTurn) {
        //turn the phone servo to face VuMark
        robot.phoneServo.setPosition(1);
        //wait until detected
        robot.detectPic();
        //Convert enum to char
        switch (robot.vuMark) {
            case LEFT:
                out.println("read left");
                column = 'l';
                break;
            case CENTER:
                out.println("read center");
                column = 'c';
                break;
            case RIGHT:
                out.println("read right");
                column = 'r';
                break;
            default:
                out.println(1,"wait wut, bad vu reading");
                break;
        }
        //remove vuforia from camera for DogeCV
        robot.vuforia.close();
        //revert phone position back to side
        robot.phoneServo.setPosition(0);
        // DogeCV functions
        //moving with auto should be rigid
        robot.zeroBrake();
        //dogecv declarations
        if (config.enableDogeCV)
            dogeCVPark(rotateBot, farTurn);
        else
            encodedPark(rotateBot, farTurn);
        robot.setPowerZero();
        robot.imuTurnRight(robot.getIMUHeading() + 90);
        robot.moveByTime(shiftSpeed,0,0, 8000);
        robot.handsOpen(1);
        robot.handsOpen(3);
        robot.moveByTime(shiftSpeed,0,0, 1000);
        robot.setPowerZero();
   }

    public void encodedPark(boolean rotateBot, boolean farTurn) {
        robot.zeroBrake();
        robot.detectPic();
        out.println(out.toString(robot.vuMark));
        robot.phoneServo.setPosition(1);
        if (!farTurn) {
            if (!rotateBot) {
                robot.moveByTime(0.5, 0, 0, 1000);
                setCryptoPerpendicular(rotateBot, farTurn);
                robot.moveByTime(-0.2, 0, 0, 1500);
                setCryptoPerpendicular(rotateBot, farTurn);
                robot.moveByInches(16,0.8);
                robot.imuSet(90);
                robot.handsOpen(3);
                robot.handsOpen(1);
                robot.hands.setPosition(0);
                robot.moveByInches(-5, 0.8);
                robot.liftDown(0.2);
                sleep(1500);
                robot.liftHold();
                robot.moveByTime(0.2,0,0, 2000);
                robot.moveByTime(-1,0,0,200);
            } else {
                robot.moveByTime(-0.5, 0, 0, 2000);
                setCryptoPerpendicular(rotateBot, farTurn);
                robot.moveByTime(-0.2, 0, 0, 3000);
            }
        }
    }

    private void dogeCVPark(boolean rotateBot, boolean farTurn) {
        cryptoboxDetector = new CryptoboxDetector();
        cryptoboxDetector.init(robot.hwMap.appContext, CameraViewDisplay.getInstance());
        if (!rotateBot)
            cryptoboxDetector.detectionMode = CryptoboxDetector.CryptoboxDetectionMode.RED;
        if (rotateBot)
            cryptoboxDetector.detectionMode = CryptoboxDetector.CryptoboxDetectionMode.BLUE;
        cryptoboxDetector.rotateMat = true;
        cryptoboxDetector.enable();
        //the middle X pixel of the camera
        centerPixel = cryptoboxDetector.getFrameSize().width / 2;
        if (!farTurn) {
            while (!cryptoboxDetector.isCryptoBoxDetected()) {
                shiftCameraLeft(rotateBot);
            }
        } else {
            if (!rotateBot) {
                robot.moveByTime(shiftSpeed, 0, 0, 2000);
                robot.imuTurnLeft(-90);
                while (!cryptoboxDetector.isCryptoBoxDetected()) {
                    shiftCameraLeft(rotateBot);
                }
            } else {
                robot.moveByTime(-shiftSpeed, 0, 0, 2000);
                robot.imuTurnRight(90);
                while (!cryptoboxDetector.isCryptoBoxDetected()) {
                    shiftCameraRight(rotateBot);
                }
            }
        }
        robot.setPowerZero();
        setCryptoPerpendicular(rotateBot, farTurn);
        while (Math.abs(centerPixel - cryptoboxDetector.getCryptoBoxCenterPosition()) > pixelAccuracy) {
            if (centerPixel + columnOffest > cryptoboxDetector.getCryptoBoxCenterPosition())
                shiftCameraRight(rotateBot);
            if (centerPixel + columnOffest < cryptoboxDetector.getCryptoBoxCenterPosition())
                shiftCameraLeft(rotateBot);
        }
        setCryptoPerpendicular(rotateBot, farTurn);
        switch (column) {
            case 'l':
                while (Math.abs(centerPixel - cryptoboxDetector.getCryptoBoxLeftPosition()) > pixelAccuracy) {
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
                while (Math.abs(centerPixel - cryptoboxDetector.getCryptoBoxRightPosition()) > pixelAccuracy) {
                    if (centerPixel + columnOffest > cryptoboxDetector.getCryptoBoxRightPosition())
                        shiftCameraRight(rotateBot);
                    if (centerPixel + columnOffest < cryptoboxDetector.getCryptoBoxRightPosition())
                        shiftCameraLeft(rotateBot);
                }
                break;
            default:
                out.println(1, "wait wut, improper column variable");
                break;
        }
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
        //TODO navigate back to cryptobox from pile & place glyph
    }

    public void sleep(int ms) {
        ElapsedTime et = new ElapsedTime();
        while (et.milliseconds() < ms);
    }
}
