package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

/**
 * Created by lu_ha on 1/28/2018.
 */


@TeleOp(name="Mr. Output Test", group="Def Bot")
public class MrOutputTest extends OpMode {
    MrOutput out;
    int count = 0;
    public void init() {
        out = new MrOutput(telemetry, 1, true);
    }
    public void loop() {
        out.printStaticLine(0, "Looped: " + count);
        out.println(1, "foo" + (count - 1));
        out.println(count + "");
        out.println(-1, "bar");
        sleep(500);
        count++;
    }
    public void sleep(int ms) {
        ElapsedTime time = new ElapsedTime();
        while (time.seconds() < ms/1000.00){}
    }
}
