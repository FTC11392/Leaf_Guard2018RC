package org.firstinspires.ftc.teamcode.lib;

/*
Mr. Output 2018

Mr. Output is Defenestration's system made to handle sending
telemetry through a single line, so that it is less of a chore.
Mr. Output allows the display of a set number of "static lines"
as the lines shown at the top of telemetry. All other lines in
telemetry will function like regular console output.

Mr. Output 2018 is developed by Brian Lu
 */


import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MrOutput {
    /*
    Naming key:
    debugOn: Whether the debug line (the last line) of
             Mr. Output is activated.
    totalLines: Total number of telemetry lines allowed
    staticLines: Amount of "static lines"
    statics: Array of all "static" lines
    outputs: Array of all scrolling "output" lines
    staticCaptions: Every line needs a caption, a four 
                    letter identifier. staticCaptions
                    are the captions for the static lines.
    outputCaptions: outputCaptions are the captions for 
                    the output lines.
    debugString: A string that is stuffed with debug 
                 before every telemetry push.
    tele: The Telemetry object used by Mr. Output, it uses
          it to push telemetry to phones.
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
    public MrOutput(Telemetry tele, int staticLines, boolean debug) {
        debugOn = debug;
        this.tele = tele;
        addTelemetryLine("Starting Mr. Output...");
        this.staticLines = staticLines;
        // Amount of static lines should not be larger than the amount of total lines
        if (staticLines > totalLines) {
            staticLines = totalLines;
            addTelemetryLine(1, "staticLines > totalLines in MrOutput. Please fix.");
        }
        // Initialize arrays
        statics = new String[staticLines];
        outputs = new String[totalLines - staticLines];
        staticCaptions = new String[staticLines];
        outputCaptions = new String[totalLines - staticLines];
        addTelemetryLine("Mr. Output is online!");
        pushTelemetry();
    }
    // The public functions used to print "static" lines
    public void printStaticLine(int staticLine, String caption, String output) {
        // Check if static line number exceeds amount of allocated static lines.
        if (!(staticLine >= staticLines) && (staticLine > -1)) {
            // Set static line
            statics[staticLine] = output;
            staticCaptions[staticLine] = caption;

        } else {
            buildDebugString("WARN", "Cannot add static line");
        }
        buildTelemetry();
        pushTelemetry();
    }
    // Use captionCase to provide captions instead
    public void printStaticLine(int staticLine, int captionLevel, String line) {
        printStaticLine(staticLine, captionCase(captionLevel), line);
    }
    // Use INFO as the default captions
    public void printStaticLine(int staticLine, String output) {
        printStaticLine(staticLine, 0, output);
    }
    // The public functions used to print "output" lines
    public void println(String caption, String output){
        // Cycle all output lines up one line
        for (int i = 0; i < outputs.length; i++) {
            if((i + 1) != outputs.length)
                outputs[i] = outputs[i+1];
        }
        // Cycle all corresponding captions up one line
        for (int i = 0; i < outputCaptions.length; i++) {
            if((i + 1) != outputCaptions.length)
                outputCaptions[i] = outputCaptions[i+1];
        }
        // Set last line to output
        outputs[outputs.length - 1] = output;
        outputCaptions[outputCaptions.length - 1] = caption;
        buildTelemetry();
        pushTelemetry();
    }
    // Use captionCase to provide captions instead
    public void println(int captionLevel, String output){
        println(captionCase(captionLevel), output);
    }
    // Use INFO as the default captions
    public void println(String output){
        println(0, output);
    }
    // Add a line to telemetry
    private void addTelemetryLine(String caption, String line) {
        tele.addData(caption, line);
    }
    // Add a line with captionCase
    private void addTelemetryLine(int captionLevel, String line) {
        addTelemetryLine(captionCase(captionLevel), line);
    }
    // Add a line with caption "INFO"
    private void addTelemetryLine(String line) {
        addTelemetryLine(0, line);
    }
    // Add a phrase to the debug string
    private void buildDebugString(String caption, String addition) {
        debugString = debugString + caption + ": " + addition + " ";
    }
    // Write all data to telemetry before displaying
    private void buildTelemetry() {
        // Clearing telemetry, not required, but good idea
        tele.clear();
        // Add data for all "static" lines first
        for (int i = 0; i < statics.length; i++) {
            tele.addData(staticCaptions[i], statics[i]);
        }
        // Then add data for "output" lines
        for (int i = 0; i < outputs.length; i++) {
            tele.addData(outputCaptions[i], outputs[i]);
        }
        if (debugOn) {
            // If there are no debug phrases, make sure that the debug line
            // is visible for the driver operators.
            buildDebugString("debug", "on");
            // Write debug line to telemetry
            addTelemetryLine(-2, debugString);
            debugString = "";
        }
    }
    // Display all added telemetry
    private void pushTelemetry() {
        tele.update();
    }
    
    // Resolve an integer number to a caption, eases typing captions in functions
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
