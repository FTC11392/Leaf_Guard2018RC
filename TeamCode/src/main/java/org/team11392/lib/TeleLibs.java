package org.team11392.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.team11392.lib.hardware.TeleHardware;

public class TeleLibs {
    private Config config;
    private HardwareMap hwMap;
    private TeleHardware robot;
    private MrOutput out;
    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private boolean clawADown = true;
    private ElapsedTime clawTurnTime;
    private boolean endInitExecuted = false;
    public TeleLibs(HardwareMap hwMap, TeleHardware robot, MrOutput out, Gamepad gamepad1, Gamepad gamepad2) {
        config = new Config();
        this.hwMap = hwMap;
        this.robot = robot;
        this.out = out;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        clawTurnTime = new ElapsedTime();
    }

    public void startInit() {
        out.println("Running match setup...");
        robot.init(hwMap);
        robot.relicWrist.setPosition(0.5);
        robot.relicHand.setPosition(0);
        robot.jewelBase.setPosition(0.5);
        robot.jewelArm.setPosition(0.2);
        out.println("done");
    }

    public void endInit() {
        if (!endInitExecuted) {
            robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void haltAll() {
        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);
    }

    public void controlState() {
        moveDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        moveGlyphLift();
        turnGlyphClaw();
        manipulateClaws();
        moveRelicArm();
        moveRelicManipulators();
    }

    private void moveDrive(double drive, double strafe, double turn) {
        robot.frontLeft.setPower(Range.clip(config.driveAdjuster * (drive - strafe  - turn), -1, 1));
        robot.frontRight.setPower(Range.clip(config.driveAdjuster * (-drive - strafe  - turn), -1, 1));
        robot.backLeft.setPower(Range.clip(config.driveAdjuster * (-drive - strafe  + turn), -1, 1));
        robot.backRight.setPower(Range.clip(config.driveAdjuster * (-drive - strafe  + turn), -1, 1));
    }

    private void moveGlyphLift() {
        robot.glyphLift.setPosition(Range.clip(-gamepad1.left_trigger + gamepad1.right_trigger + 0.5,0,1));
    }

    private void turnGlyphClaw() {
        if (clawTurnTime.milliseconds() > 500) {
            if (robot.clawTurner.getPosition() == 0)
                robot.clawTurner.setPosition(1);
            if (robot.clawTurner.getPosition() == 1)
                robot.clawTurner.setPosition(0);
            clawADown = !clawADown;
            clawTurnTime.reset();
        }
    }

    private void manipulateClaws() {
        if (gamepad1.a && clawADown)
            closeClawA();
        if (gamepad1.a && !clawADown)
            closeClawB();
        if (gamepad1.b && clawADown)
            closeClawB();
        if (gamepad1.b && !clawADown)
            closeClawA();
        if (gamepad1.dpad_up && clawADown)
            releaseClawB();
        if (gamepad1.dpad_up && !clawADown)
            releaseClawA();
        if (gamepad1.dpad_down && clawADown)
            releaseClawA();
        if (gamepad1.dpad_down && !clawADown)
            releaseClawB();
        if (gamepad1.dpad_left) {
            releaseClawA();
            releaseClawB();
        }
        if (gamepad1.dpad_right) {
            openClawA();
            openClawB();
        }
    }

    private void openClawA() {

    }

    private void releaseClawA() {

    }

    private void closeClawA() {

    }

    private void openClawB() {

    }

    private void releaseClawB() {

    }

    private void closeClawB() {

    }

    private void moveRelicArm() {
        if (gamepad2.right_trigger > 0.05) {
            robot.relicArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.relicArm.setPower(Range.clip(0.4, 0, 0.3));
            robot.relicArm.setTargetPosition(robot.relicArm.getCurrentPosition() + 40);
        }
        if (gamepad2.left_trigger > 0.05) {
            robot.relicArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.relicArm.setPower(Range.clip(-0.4, -0.3, 0));
            robot.relicArm.setTargetPosition(robot.relicArm.getCurrentPosition() - 40);
        }
    }

    private void moveRelicManipulators() {
        if (gamepad2.left_stick_y > 0.05)
            robot.relicWrist.setPosition(Range.clip(robot.relicWrist.getPosition() + 0.01,0,1));
        if (gamepad2.left_stick_y < -0.05)
            robot.relicWrist.setPosition(Range.clip(robot.relicWrist.getPosition() - 0.01,0,1));
        if (gamepad2.right_stick_y > 0.05)
            robot.relicHand.setPosition(Range.clip(robot.relicHand.getPosition() + 0.01,0,1));
        if (gamepad2.right_stick_y < -0.05)
            robot.relicHand.setPosition(Range.clip(robot.relicHand.getPosition() - 0.01,0,1));
    }
}
