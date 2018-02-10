package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.team11392.lib.Config;
import org.team11392.lib.MrOutput;

@TeleOp (name="Mecanum Bot Linear", group="Def Bot")

public class MecanumBot extends LinearOpMode{
    boolean claw1Bottom = false;
    boolean claw1closed = false;
    boolean claw3closed = false;
    // Declare OpMode members.
    MecanumHardware robot       = new MecanumHardware();
    double clawSecs = 0;
    private ElapsedTime runtime = new ElapsedTime();
    double time;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode() {
        double driveY = 0.0, driveX = 0.0, driveTurn = 0.0;
        double up = 0.5, down = 0.5;
        double adjuster = 1.0;
        robot.init(hardwareMap);
        boolean clawTurning = false;
        telemetry.addData("Status", "Initialized");
        MrOutput out = new MrOutput(telemetry, 2);
        out.println("initialized!");
        waitForStart();
        out.println("starting class decs");
        ElapsedTime clawTime = new ElapsedTime();
        runtime.reset(); 
        robot.elbow.setPosition(0);
        robot.relicHand.setPosition(0);
        out.println("starting loop");
        robot.climbAssist.setPower(1);
        robot.hands.setPosition(0);
        robot.handsOpen(1);
        robot.handsOpen(3);
        runtime.reset();

        while (opModeIsActive()) {
            if (runtime.seconds() > 120)
                robot.zeroBrake();
            if (clawTime.milliseconds() == 500)
                clawTurning = false;

            // Show the elapsed game time and wheel power.
            //telemetry.addData("Positions", "Y (%.2f)", "X (%.2f)", "Turn (%.2f)", driveY, driveX, driveTurn);
            
            driveY = -adjuster*gamepad1.left_stick_y;
            driveX = -adjuster*gamepad1.left_stick_x;
            driveTurn = -adjuster*gamepad1.right_stick_x;
            robot.move(driveY, driveX, driveTurn);
            
            if (gamepad1.right_trigger > 0.05) { 
                robot.liftUp(gamepad1.right_trigger, 0); } 
            else if (gamepad1.left_trigger  > 0.05 ) { 
                robot.liftDown(gamepad1.left_trigger); } 

            else {
                robot.liftHold();
            }
            
            if (gamepad2.right_trigger > 0.05) {
                robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.arm.setPower(Range.clip(0.4, 0, 0.3));
                robot.arm.setTargetPosition(robot.arm.getCurrentPosition() + 80);
            }
            if (gamepad2.left_trigger > 0.05) {
                robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.arm.setPower(Range.clip(-0.4, -0.3, 0));
                robot.arm.setTargetPosition(robot.arm.getCurrentPosition() - 80);
            }
            
            if (gamepad2.left_stick_y > 0.05) {
                out.setStaticLine(0, "left stick up, curr pos:" + robot.elbow.getPosition() + " step pos:" + Range.clip(robot.elbow.getPosition() + 0.01,0,1));
                robot.elbow.setPosition(Range.clip(robot.elbow.getPosition() + 0.01,0,1));
            }
            if (gamepad2.left_stick_y < -0.05) {
                out.setStaticLine(0, "left stick down, curr pos:" + robot.elbow.getPosition() + " step pos:" + Range.clip(robot.elbow.getPosition() - 0.01,0,1));
                robot.elbow.setPosition(Range.clip(robot.elbow.getPosition() - 0.01,0,1));
            }
            
            if (gamepad2.right_stick_y > 0.05) {
                out.setStaticLine(0, "right stick up, curr pos:" + robot.relicHand.getPosition() + " step pos:" + Range.clip(robot.relicHand.getPosition() + 0.01,0,1));
                robot.relicHand.setPosition(Range.clip(robot.relicHand.getPosition() + 0.01,0,1));
            }
            if (gamepad2.right_stick_y < -0.05) {
                out.setStaticLine(0, "right stick down, curr pos:" + robot.relicHand.getPosition() + " step pos:" + Range.clip(robot.relicHand.getPosition() - 0.01,0,1));
                robot.relicHand.setPosition(Range.clip(robot.relicHand.getPosition() - 0.01,0,1));
            }

            if (gamepad1.left_bumper) {
                robot.upDown();
                claw1Bottom = !claw1Bottom;
            }

            if (gamepad1.right_bumper ) { //release
                robot.handsRelease(1);
                robot.handsRelease(3);
            }
            if (gamepad1.x) { //open wide
                robot.handsOpen(1);
            }
            if (gamepad1.y) { //close in half
                robot.handsClose(1);
            }
            if (gamepad1.a) {
                robot.handsOpen(3);
            }
            if (gamepad1.b) {
                robot.handsClose(3);
            }
            if (gamepad1.dpad_left) {
                if ((runtime.seconds() - clawSecs) > 0.2) {
                robot.handsOpen(1);
                robot.handsOpen(3);
                    clawSecs = runtime.seconds();
                }
            }
            if (gamepad1.dpad_right) {
                if ((runtime.seconds() - clawSecs) > 0.2) {
                robot.handsClose(1);
                robot.handsClose(3);
                clawSecs = runtime.seconds();
            }

            }
            //relic 
            /*
            telemetry.addData("runtime ", "%.2f", runtime.seconds());
            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", 
                    robot.sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.update();
            */
        }
        robot.setPowerZero();
        stop();
    }
}


