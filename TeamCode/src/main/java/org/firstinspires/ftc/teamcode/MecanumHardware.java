package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import java.util.Locale;

public class MecanumHardware {

    /* Public OpMode members. */
    public DcMotor  leftFront   = null;
    public DcMotor  rightFront  = null;
    public DcMotor  leftBack    = null;
    public DcMotor  rightBack   = null;

    public Servo    leftHand    = null;
    public Servo    rightHand   = null;
    public Servo    leftHand2    = null;
    public Servo    rightHand2   = null;
    public Servo    hands      = null; //392 to turn hands 
    public Servo    relicHand   = null; //
    public Servo    lift        = null; //392 to lift hands
    public Servo    arm         = null; //392 for relic arm to grab the relic
    public Servo    hitArm      = null; //for hitting the jewel

    public ColorSensor jewel    = null;
    public DistanceSensor sensorDistance = null;
    public boolean jewelRedColor = false;
    public boolean jewelBlueColor = false;
    public double red = 0, blue = 0, green = 0;
    
    public OpenGLMatrix lastLocation   = null;
    public VuforiaLocalizer vuforia;
    public VuforiaTrackables relicTrackables;
    public VuforiaTrackable relicTemplate;
    public RelicRecoveryVuMark vuMark;
    //IMU jazz
    /*
    public BNO055IMU imu;
    Orientation angles;
    */
    /* local OpMode members. */
    HardwareMap hwMap           =  null;

    /* Constructor */
    public MecanumHardware (){
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        //imu jazz
        /*
        BNO055IMU.Parameters imuparams = new BNO055IMU.Parameters();
        imuparams.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuparams.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparams.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        imuparams.loggingEnabled      = true;
        imuparams.loggingTag          = "IMU";
        imuparams.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        */
        leftFront  = hwMap.get(DcMotor.class, "leftFront");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        leftBack   = hwMap.get(DcMotor.class, "leftBack");
        rightBack  = hwMap.get(DcMotor.class, "rightBack");
        //imu needs hardware port channel to work
        /*
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuparams);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        */
        leftFront.setDirection(DcMotor.Direction.REVERSE); 
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.FORWARD);   //Andy Mark
        //leftBack.setDirection(DcMotor.Direction.REVERSE); //Tetrix
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        
        // Set all motors to zero power
        setPowerZero();

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        zeroFloat();

        // Define and initialize ALL installed servos.
        leftHand  = hwMap.get(Servo.class, "left_hand");
        rightHand = hwMap.get(Servo.class, "right_hand");
        leftHand2  = hwMap.get(Servo.class, "left_hand2");
        rightHand2 = hwMap.get(Servo.class, "right_hand2");
        //relicHand = hwMap.get(Servo.class, "relic_hand");
        //hitArm    = hwMap.get(Servo.class, "hit_arm");
        lift      = hwMap.get(Servo.class, "lift");   //Vex motor 
        //arm       = hwMap.get(Servo.class, "arm");    //Vex motor 
        //arm.setDirection(Servo.Direction.REVERSE);  //to be consistant on gamepad controller
        hands= hwMap.get(Servo.class, "hands");   //Vex motor 
        //hands2.setDirection(Servo.Direction.REVERSE); //to be consistant on gamepad 
        
        // Color sensor
        //jewel = hwMap.get(ColorSensor.class, "jewel");
        //jewel.enableLed(false);
        // get a reference to the distance sensor that shares the same name.
        //sensorDistance = hwMap.get(DistanceSensor.class, "jewel");
    
        
        // detect the picture
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = "AXf+Wrj/////AAAAGZGUqlzc6U6hmObEHD5F0Rs4UCf3b5DuQz1Ov1CU0o6AujYCKlpbLRsuum0xiPusHkjdutPw6Gd4J6VWT5e9W9enUQvi8TjXNkZi5xcfp0l+ZAkMa5Iu0OzkxrDYkYYz46CdkifIoDrCt6691m9nS3utYyYqX7nxpKqD4cu1sJRg9hSzmkij9ITjFzM7ezyT/FBtKlNViRdz+Vynpy/QyOA/oMJCCGpLtCiltHBfMxolp+ZU7D/Jyv/JPG926cHyH79WjKFAS8HmJfu1z1z6qpNNQ+MOrLHzvsw8UI7s8biHZg6batS6fVNcr1rAbTrXLEYw3x5VCmhYc/zz+B+YF6b4jFmJ//q5CxcQX6VAiXGr";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        
    }
    
    public void zeroFloat() {
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void zeroBreak() {
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    
    public void setPowerZero() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    public void move(double y, double x, double turn) {
        //y < 0 move forward, x > 0 move right, turn > 0 right
        double fleftPower  =  + y - x  - turn;
        double frightPower =  - y - x  - turn;
        double bleftPower  =  - y - x  + turn;
        double brightPower =  + y - x  + turn;
        
        // Send calculated power to wheels
        leftFront.setPower(Range.clip(fleftPower, -1.0, 1.0));
        rightFront.setPower(Range.clip(frightPower, -1.0, 1.0));
        leftBack.setPower(Range.clip(bleftPower, -1.0, 1.0));
        rightBack.setPower(Range.clip(brightPower, -1.0, 1.0));
    }
    
    public void moveByTime(double y, double x, double turn, int time) {
        move(y,x,turn);
        sleep(time);
        move(0,0,0);
    }
    /*
    public void imuTurnRight(int expectedDegrees) {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double heading  = Double.parseDouble(formatAngle(angles.angleUnit, angles.firstAngle));
        boolean notCloseEnough = true;
        while (notCloseEnough) {
            if(Math.abs(-expectedDegrees - heading) < 2) {
                move(0,0,0);
                notCloseEnough = false;
                return;
            }
            if(-expectedDegrees < heading) {
                move(0 ,0 , -0.25);
            }
            if(-expectedDegrees > heading) {
                move(0 ,0 , 0.25);
            }
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            heading  = Double.parseDouble(formatAngle(angles.angleUnit, angles.firstAngle));
        }
    }
    public void imuTurnLeft(int expectedDegrees) {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double heading  = Double.parseDouble(formatAngle(angles.angleUnit, angles.firstAngle));
        boolean notCloseEnough = true;
        while (notCloseEnough) {
            if(Math.abs(-expectedDegrees - heading) < 2) {
                move(0,0,0);
                notCloseEnough = false;
                return;
            }
            if(-expectedDegrees > heading) {
                move(0 ,0 , 0.25);
            }
            if(-expectedDegrees < heading) {
                move(0 ,0 , -0.25);
            }
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            heading  = Double.parseDouble(formatAngle(angles.angleUnit, angles.firstAngle));
        }
    }
    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
    */
    public void handsOpen(int index ) {
        if (index == 1) { // front hands
            leftHand.setPosition(0.9);   //smaller, closer
            rightHand.setPosition(0.05); //larger, closer
            //hands2.setPosition(0.1);
        }
        else if (index == 2) {
            relicHand.setPosition(0.4);
        }
        else if (index == 3) {
            leftHand2.setPosition(0.95);   //smaller, closer
            rightHand2.setPosition(0.05); //larger, closer
        }
    }
    
    public void handsRelease(int index ) {
        index = 1;
        if (index == 1) {
            leftHand.setPosition(0.5);
            rightHand.setPosition(0.5);
            //hands2.setPosition(0.4);
        }
    }
    
    public void handsClose(int index) {
        if (index == 1) {
            leftHand.setPosition(0.3);
            rightHand.setPosition(0.6);
            //hands2.setPosition(0.9);
        }
        else if (index == 2) {
            relicHand.setPosition(0.9);
        }
        /*
        if (index == 2) {
            hands2.setPosition(0.6);
        }
        */
        else if (index == 3) {
            leftHand2.setPosition(0.3);   //smaller, closer
            rightHand2.setPosition(0.6); //larger, closer
        }
        
    }
    
    public void handsLoose(double change) {
        /*
        double lh = leftHand.getPosition();
        lh += change;
        double rh = rightHand.getPosition();
        rh -= change;
        leftHand.setPosition(lh);
        rightHand.setPosition(rh);
        */
    }
    
    public void armUp(double change, int time) {
        double pos = 0.55 + change ;
        pos = Range.clip(pos, 0.5, 0.75);
        arm.setPosition(pos);
        //sleep(time);
    } 
    
    public void handsUp(double change, int time) {
        double pos = 0.55 + change ;
        pos = Range.clip(pos, 0.5, 0.75);
        hands.setPosition(pos);
        //sleep(time);
    }
    
    public void handsHold() {
        hands.setPosition(0.5);
    }
    
    public void handsDown(double change) {
        double pos = 0.5 - Math.min(change, 0.25);
        hands.setPosition(pos);
        //sleep(500);
    }
    
    public void armHold() {
        arm.setPosition(0.5);
    }
    
    public void armDown(double change) {
        double pos = 0.5 - Math.min(change, 0.25);
        arm.setPosition(pos);
        //sleep(500);
    }
    
    public void liftUp(double change, int time) {
        double pos = 0.5 + change ;
        pos = Range.clip(pos, 0.5, 0.75);
        lift.setPosition(pos);
        //sleep(time);
    }
    
    public void liftHold() {
        lift.setPosition(0.5);
    }
    
    public void liftDown(double change) {
        double pos = 0.5 - Math.min(change, 0.15);
        lift.setPosition(pos);
        //sleep(500);
    }
    
    public void hitArmUp() {
        //hitArm.setPosition(0.835);
        hitArm.setPosition(0.935);
    }
    
    public void hitArmDown() {
        //gradually down 
        double position = 0.2;
        hitArm.setPosition(position);
        
        while (position > 0.03) {
            sleep(100);
            position = hitArm.getPosition() - 0.02;
            hitArm.setPosition(position);
        }
    }
    /*
    public void detectColor() {
        //return if red color is detected
        jewel.enableLed(true);
        
        ElapsedTime runtime = new ElapsedTime();
        double time = runtime.seconds();
        //spend no more than 4 seconds to read the color sensor
        while (!jewelRedColor && !jewelBlueColor && runtime.seconds() < time + 4.0) {
            red = jewel.red();
            green = jewel.green();
            blue = jewel.blue();
            //make sure red and blue is strong enough
            if (red > green + 1.5 && red > blue + 1.5)
                jewelRedColor = true;
            if (blue > green + 1.5 && blue > red + 1.5)
                jewelBlueColor = true;
        }
    }
    
    public String hitJewel(boolean redJewel, boolean blueJewel, ElapsedTime runtime) {
        if (redJewel) { //turn right to hit jewel
            rightHit();
            move(0.1, 0 ,0);
            sleep(200);
            setPowerZero();
        }
        else if (blueJewel){
            leftHit();
            move(0.1, 0 ,0);
            sleep(600);
            setPowerZero();
        }
        else {
            hitArmUp();
        }
        return "red jewel = " + redJewel + ", blue jewel = " + blueJewel + " at: " + runtime.seconds();
    }
    public String hitJewelReverse(boolean redJewel, boolean blueJewel, ElapsedTime runtime) {
        if (blueJewel) { //turn right to hit jewel
            rightHit();
            move(-0.1, 0 ,0);
            sleep(800);
            setPowerZero();
        }
        else if (redJewel){
            leftHit();
            move(-0.1, 0 ,0);
            sleep(800);
            setPowerZero();
        }
        else {
            hitArmUp();
        }
        return "red jewel = " + redJewel + ", blue jewel = " + blueJewel + " at: " + runtime.seconds();
    }
    public void detectPic( ) {
        // detect picture
        relicTrackables.activate();
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        ElapsedTime runtime = new ElapsedTime();
        double time = runtime.seconds();
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && runtime.seconds() < time + 3.0) {
            //telemetry.addData("VuMark", "not visible");
            //telemetry.update();
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
        }
        relicTrackables.deactivate();
    }
    public void rightHit() {
        move(0., 0., -0.18);
        sleep(900);
        setPowerZero();
        // lift arm up
        hitArmUp();
        // moveforward back
        sleep(1000);
        move(0., 0., 0.2);
        sleep(1000);
        setPowerZero();
    }
    
    public void leftHit() {
        move(0., 0., 0.18);
        sleep(900);
        setPowerZero();
        // lift arm up
        hitArmUp();
        // moveforward back
        sleep(1000);
        move(0., 0., -0.18);
        sleep(1000);
        setPowerZero();
    }
    /*
    public int parking(int team) {
        int position = -1;
        if (team == 1 ) {//red left
            if (vuMark == RelicRecoveryVuMark.LEFT){ 
                moveByTime(0.25, 0, 0, 1600);
                position = 0;
                imuTurnRight(45);
            }else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                moveByTime(0.25, 0, 0, 2800);
                position = 1;
                imuTurnRight(135);
            }else if (vuMark == RelicRecoveryVuMark.CENTER){
                moveByTime(0.2, 0, 0, 800);
                position = 2;
                imuTurnRight(75);
            } else {
                moveByTime(0.25, 0, 0, 1500);
                position = -1;
                imuTurnRight(75);
                //oof
            }
            sleep(800);
            //move(0.0, 0.0, 1.0);
            
            //setPowerZero();
            //sleep(1000);
            
            //turn left a little, release glyph, and back off a little
            //move(0.0, 0.0, -0.28);
            //imuTurnRight(80);
            //sleep(1500);
            setPowerZero();
            liftDown(0.15);
            sleep(2000);
        }
        
        if (team == 2 ) {//red right
            move(0.5, 0, 0.0); //move forwards
            if (vuMark == RelicRecoveryVuMark.LEFT){ 
                move(-0.5, 0, 0);
                sleep(1500);
                position = 0;
            }else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                move(-0.1, 0, 0);
                sleep(800);
                position = 1;
            }else{
                move(-0.25, 0, 0);
                sleep(1000);
                position = 2;
            move(0.0, 0.0, 1.0);
            sleep(1000);
            setPowerZero();
            sleep(1000);}
        }
        
        if (team == 3 ) {//blue left
            move(-0.3, 0.0, 0.0); //move backward
            sleep(800);
            move(0.0, -0.3, 0.0); //move right
            if (vuMark == RelicRecoveryVuMark.LEFT){ 
                move(-0.5, 0, 0);
                sleep(1500);
                position = 0;
            }else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                move(-0.1, 0, 0);
                sleep(800);
                position = 1;
            }else{
                move(-0.25, 0, 0);
                sleep(1000);
                position = 2;
            move(0.0, 0.0, 1.0);
            }
        }
        if (team == 4) { //blue right
            if (vuMark == RelicRecoveryVuMark.RIGHT){ 
                moveByTime(-0.25, 0, 0, 2000);
                position = 1;
                imuTurnRight(120);
            }else if (vuMark == RelicRecoveryVuMark.LEFT) {
                moveByTime(-0.25, 0, 0, 2400);
                position = 1;
                imuTurnRight(45);
            }else if (vuMark == RelicRecoveryVuMark.CENTER){
                moveByTime(-0.2, 0, 0, 800);
                position = 2;
                imuTurnRight(135);
            } else {
                moveByTime(-0.25, 0, 0, 2000);
                position = -1;
                imuTurnRight(110);
                //oof
            }
            sleep(800);
            //move(0.0, 0.0, 1.0);
            
            //setPowerZero();
            //sleep(1000);
            
            //turn left a little, release glyph, and back off a little
            //move(0.0, 0.0, -0.28);
            //imuTurnRight(80);
            //sleep(1500);
            setPowerZero();
            liftDown(0.15);
            sleep(2000);
            
        }
        //push in
        handsOpen(1);
        sleep(1000);
        for (int i = 0; i <3; i++) {
            move(0.18, 0.0, 0.05);
            sleep(500);
            move(0.18, 0.0, -0.05);
            sleep(500);
        }
        setPowerZero();
        
        //move back
        move(-0.12, 0.0, 0.0);
        sleep(800);
        setPowerZero();
        return position;
    }
    */
    public void sleep(int ms) {
        //This is customized sleep as the system sleep function only
        //works in linear opMode 
        ElapsedTime time = new ElapsedTime();
        while (time.seconds() < ms/1000.00){};   //sleep(500);
    }
}
