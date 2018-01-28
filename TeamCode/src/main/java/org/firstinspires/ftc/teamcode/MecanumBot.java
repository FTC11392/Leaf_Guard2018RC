package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp (name="Mecanum Bot Linear", group="Def Bot")

public class MecanumBot extends LinearOpMode{

    // Declare OpMode members.
    MecanumHardware robot       = new MecanumHardware();
    
    private ElapsedTime runtime = new ElapsedTime();
    double time;
    
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode() {
        double driveY = 0.0, driveX = 0.0, driveTurn = 0.0;
        double up = 0.5, down = 0.5;
        double adjuster = 0.7;
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
    
        waitForStart();
    
        runtime.reset(); 
        
        while (opModeIsActive()) { 
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
            
            if (gamepad1.dpad_left) { 
                robot.handsUp(0.1, 0); } 
            else if (gamepad1.dpad_right ) { 
                robot.handsDown(0.1); } 

            else {
                robot.handsHold();
            }
            
            if (gamepad1.right_bumper ) { //release
                robot.handsRelease(1);
            }
            if (gamepad1.x) { //open wide
                robot.handsOpen(1);
            }
            if (gamepad1.y) { //close in half 
                robot.handsClose(1);
            }
            if (gamepad1.a) {
                //robot.zeroBreak();
                robot.handsOpen(3);
                //robot.armMotor.setPower(0.2);
                //robot.wait();
            }
            if (gamepad1.b) {
                //robot.zeroFloat();
                robot.handsClose(3);
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


