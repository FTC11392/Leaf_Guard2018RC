package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.team11392.lib.positron.IMU;
import org.team11392.lib.MrOutput;

/**
 * Created by lu_ha on 1/28/2018.
 */


@TeleOp(name="IMU Test", group="Def Bot")
public class IMUTest extends OpMode {
    IMU imu;
    int count = 0;
    public void init() {
        HardwareMap hwMap = null;
        imu = new IMU(hwMap, new MrOutput(telemetry, 1, true));
        Thread imuThread = new Thread(imu);
        imuThread.start();
    }
    public void loop() {

    }
    public void sleep(int ms) {
        ElapsedTime time = new ElapsedTime();
        while (time.seconds() < ms/1000.00){}
    }
}
