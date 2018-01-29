package org.firstinspires.ftc.teamcode;

/*
Mr. Output 2018

Mr. Output is Defenestration's system made to handle sending
telemetry through a single line, so that it is less of a chore.
Mr. Output allows the display of a set number of"static lines"
as the lines shown at the top of telemetry. All other lines in
telemetry will function like regular console output.

Mr. Output 2018 is developed by Brian Lu
 */


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class MrOutput {
    /*
    Naming key:
    totalLines: Total number of telemetry lines allowed
    outputLines: Array of lines before being pushed to
                 telemetry
    staticLines: Amount of "static lines"
     */
    boolean debugOn = false;
    int totalLines = 6;
    int staticLines = 0;
    String[] statics;
    String[] outputs;
    String[] staticCaptions;
    String[] outputCaptions;
    String debugString = "";
    public Telemetry tele;
    /*
    debug: Mr. Output will output debug information to an
           additional line.
     */
    public MrOutput(Telemetry tele, int staticLines, boolean debug) {
        debugOn = debug;
        this.tele = tele;
        addTelemetryLine("Starting Mr. Output...");
        if (staticLines > totalLines) {
            staticLines = totalLines;
            addTelemetryLine(1, "staticLines > totalLines in MrOutput. Please fix.");
        }
        this.staticLines = staticLines;
        statics = new String[staticLines];
        outputs = new String[totalLines - staticLines];
        staticCaptions = new String[staticLines];
        outputCaptions = new String[totalLines - staticLines];
        addTelemetryLine("Mr. Output is online!");
        pushTelemetry();
    }

    public void printStaticLine(int staticLine, String caption, String output) {
        if (!(staticLine >= staticLines) && (staticLine > -1)) {
            statics[staticLine] = output;
            staticCaptions[staticLine] = caption;

        } else {
            buildDebugString("WARN", "Cannot add static line");
        }
        buildTelemetry();
        pushTelemetry();
    }
    public void printStaticLine(int staticLine, int captionLevel, String line) {
        printStaticLine(staticLine, captionCase(captionLevel), line);
    }
    public void printStaticLine(int staticLine, String output) {
        printStaticLine(staticLine, 0, output);
    }

    public void println(String caption, String output){
        for (int i = 0; i < outputs.length; i++) {
            if((i + 1) != outputs.length)
                outputs[i] = outputs[i+1];
        }
        for (int i = 0; i < outputCaptions.length; i++) {
            if((i + 1) != outputCaptions.length)
                outputCaptions[i] = outputCaptions[i+1];
        }
        outputs[outputs.length - 1] = output;
        outputCaptions[outputCaptions.length - 1] = caption;
        buildTelemetry();
        pushTelemetry();
    }
    public void println(int captionLevel, String output){
        println(captionCase(captionLevel), output);
    }
    public void println(String output){
        println(0, output);
    }

    private void addTelemetryLine(String caption, String line) {
        tele.addData(caption, line);
    }
    private void addTelemetryLine(int captionLevel, String line) {
        addTelemetryLine(captionCase(captionLevel), line);

    }
    private void addTelemetryLine(String line) {
        addTelemetryLine(0, line);
    }

    private void buildDebugString(String caption, String addition) {
        debugString = debugString + caption + ": " + addition + " ";
    }

    private void buildTelemetry() {
        tele.clear();
        for (int i = 0; i < statics.length; i++) {
            tele.addData(staticCaptions[i], statics[i]);
        }
        for (int i = 0; i < outputs.length; i++) {
            tele.addData(outputCaptions[i], outputs[i]);
        }
        if (debugOn) {
            buildDebugString("debug", "on");
        }
    }

    private void pushTelemetry() {
        if (debugOn) {
            addTelemetryLine(-2, debugString);
            debugString = "";
        }
        tele.update();
    }

    private String captionCase(int captionLevel) {
        switch (captionLevel) {
            case -2:
                return "DBUG"; //DeBUG, debug line for mr. output
            case -1:
                return "TEST"; //TESTing, for when a testing system is active
            case 0:
                return "INFO"; //INFOrmation, regular info about robot running
            case 1:
                return "WARN"; //WARNing, something non-essential failed, or failure was handled
            case 2:
                return "FAIL"; //FAILure, fatal error because robot cannot recover from situation
            default:
                return "NCAP"; //No CAPtion, captionLevel is not supported
        }
    }
}
