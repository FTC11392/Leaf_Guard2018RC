package org.team11392.lib;

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
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

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
    private Config config = new Config();
    private boolean debugOn = config.mrOutputDebug;
    private int totalLines = config.mrOutputLines;
    private int staticLines = 0;
    private String[] statics;
    private String[] outputs;
    private String[] staticCaptions;
    private String[] outputCaptions;
    private String debugString = "";
    private Telemetry tele;
    public MrOutput(Telemetry tele, int staticLines) {
        this.tele = tele;
        this.staticLines = staticLines;
        // Amount of static lines should not be larger than the amount of total lines
        if (staticLines > totalLines) {
            staticLines = totalLines;
            buildDebugString("WARN", "staticLines(" + staticLines
                    + ") > totalLines(" + totalLines + ")");
        }
        // Initialize arrays
        statics = new String[staticLines];
        outputs = new String[totalLines - staticLines];
        staticCaptions = new String[staticLines];
        outputCaptions = new String[totalLines - staticLines];
        println("Mr. Output v1.0, Initialised...");
        println("Built by Brian Lu, 2018, for 11392");
        println("There are " + totalLines + "total  lines.");
        println("There are " + staticLines + "static lines.");
        println("There are " + outputs.length + "output lines");
        println("Debug = " + debugOn);
    }
    // The public functions used to print "static" lines
    public void setStaticLine(int staticLine, String caption, String output) {
        // Check if static line number exceeds amount of allocated static lines.
        if (!(staticLine >= staticLines) && (staticLine > -1)) {
            // Set static line
            statics[staticLine] = output;
            staticCaptions[staticLine] = caption;
        } else {
            buildDebugString("WARN", "Cannot add static line for " + staticLine);
        }
        buildTelemetry();
        pushTelemetry();
    }
    // Use captionCase to provide captions instead
    public void setStaticLine(int staticLine, int captionLevel, String line) {
        setStaticLine(staticLine, captionCase(captionLevel), line);
    }
    // Use INFO as the default caption
    public void setStaticLine(int staticLine, String output) {
        setStaticLine(staticLine, 0, output);
    }
    public void buildStaticLine(int staticLine, String part) {
        if (!(staticLine >= staticLines) && (staticLine > -1)) {
            statics[staticLine] = statics[staticLine] + ", " + part;
        } else {
            buildDebugString("WARN", "Cannot build static line for " + staticLine);
        }
        buildTelemetry();
        pushTelemetry();
    }
    // The following lines are used to build data for telemetry. Beware, they are rather picky.
    public void buildStaticLine(int staticLine, String partCaption, String part) {
        buildStaticLine(staticLine, part + ": " + part);
    }
    public void setStaticCaption(int staticLine, String caption) {
        if (!(staticLine >= staticLines) && (staticLine > -1)) {
            staticCaptions[staticLine] = caption;
        } else {
            buildDebugString("WARN", "Cannot set static caption for " + staticLine);
        }
    }
    public void setStaticCaption(int staticLine, int captionLevel) {
        setStaticCaption(staticLine, captionCase(captionLevel));
    }
    public void clearStaticLine(int staticLine, boolean clearCaption) {
        if (!(staticLine >= staticLines) && (staticLine > -1)) {
            if (clearCaption) {
                staticCaptions[staticLine] = "";
            }
            statics[staticLine] = "";
        } else {
            buildDebugString("WARN", "Cannot clear line for " + staticLine);
        }
    }
    public void clearStaticLine(int staticLine) {
        clearStaticLine(staticLine, false);
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
            try {
                if (staticCaptions[i].length() > 8) {
                    staticCaptions[i] = staticCaptions[i].substring(0, 8);
                }
                if (statics[i].length() > 64) {
                    statics[i] = statics[i].substring(0, 64 - staticCaptions[i].length());
                }
            } catch (NullPointerException npe) {
                buildDebugString("WARN", "null line");
            }
            tele.addData(staticCaptions[i], statics[i]);
        }
        // Then add data for "output" lines
        for (int i = 0; i < outputs.length; i++) {
            try {
                if (outputCaptions[i].length() > 3) {
                    outputCaptions[i] = outputCaptions[i].substring(0,3);
                }
                if (outputs[i].length() > 64) {
                    outputs[i] = outputs[i].substring(0,64-outputCaptions[i].length());
                }
            } catch (NullPointerException npe) {
                buildDebugString("WARN", "null line");
            }
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

    String toString(String string) { return string; }

    String toString(int integer) {
        return integer + "";
    }

    String toString(double decimal) { return decimal + ""; }

    String toString(boolean boole) {
        if (boole) {
            return "true";
        }
        return "false";
    }

    String toString(RelicRecoveryVuMark vuMark) {
        switch (vuMark) {
            case LEFT:
                return "LEFT";
            case RIGHT:
                return "RIGHT";
            case CENTER:
                return "CENTER";
            case UNKNOWN:
                return "UNKNOWN";
            default:
                return "NODETECT";
        }
    }

    // Display all added telemetry
    private void pushTelemetry() {
        tele.update();
    }
    
    // Resolve an integer number to a caption, eases typing captions in functions
    private String captionCase(int captionLevel) {
        switch (captionLevel) {
            case -3:
                return "";     //Blank! No caption shown (there WILL be a colon)
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
                return "NCAP"; //No CAPtion, captionLevel is not supported, or is null.
        }
    }
}
