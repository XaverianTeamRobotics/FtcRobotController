package org.firstinspires.ftc.teamcode.internals.registration.xml;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

import java.util.ArrayList;

public class XMLOpModeTemplate extends OperationMode {
    public XMLRoboscriptParser roboscriptParser;
    public XMLCodeSegment codeBlock;
    public XmlEvironment environment = new XmlEvironment();
    public ArrayList<XMLCodeLine> xmlCodeLines;

    public XMLOpModeTemplate(XMLRoboscriptParser roboscriptParser) {
        this.roboscriptParser = roboscriptParser;
        this.codeBlock = new XMLCodeSegment(roboscriptParser.getCodeBlock());
        this.xmlCodeLines = codeBlock.getContainedCode();
        environment.init();
    }

    @Override
    public void construct() {
        telemetry.setAutoClear(false);
    }

    @Override
    public void run() {
        XMLCodeLine n = xmlCodeLines.get(0);
        n.runAction(environment);
        xmlCodeLines.remove(n);
        sleep(10);
    }
}
