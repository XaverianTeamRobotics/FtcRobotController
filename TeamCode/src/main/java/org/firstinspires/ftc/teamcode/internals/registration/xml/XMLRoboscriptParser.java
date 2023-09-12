package org.firstinspires.ftc.teamcode.internals.registration.xml;

import android.content.res.AssetManager;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class XMLRoboscriptParser {

    public String filePath;
    private static AssetManager am;
    public Document xmlDocument;
    public Element rootElement;

    public static AssetManager getAm() {
        return am;
    }

    public static void setAm(AssetManager am) {
        XMLRoboscriptParser.am = am;
    }

    public static ArrayList<String> getRoboscriptXMLFiles() {
        am = FtcRobotControllerActivity.getAppContext().getAssets();
        System.out.println("[XML] AM: " + am);

        ArrayList<String> fileNames;

        try {
            fileNames = new ArrayList<>(Arrays.asList(am.list("roboscript")));
        } catch (Exception e) {
            System.out.println("[XML] Error getting roboscript files: " + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.out.println("[XML] File names: " + fileNames);

        fileNames.removeIf(f -> !f.contains(".xml"));

        return fileNames;
    }

    public XMLRoboscriptParser(String filePath) {
        this.filePath = filePath;
        System.out.println("[XML] File path: " + this.filePath);
        if (!getRoboscriptXMLFiles().contains(this.filePath)) {
            throw new RuntimeException(this.filePath + " not found");
        }
        if (am == null) {
            am = FtcRobotControllerActivity.getAppContext().getAssets();
        }

        this.filePath = "roboscript/" + this.filePath;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            System.out.println("[XML] Error creating document builder: " + e);
            throw new RuntimeException(e);
        }

        System.out.println("[XML] Builder: " + builder);
        System.out.println("[XML] Building file...");
        InputStream file = null;
        try {
            file = am.open(this.filePath);
        } catch (Exception e) {
            System.out.println("[XML] Error opening file: " + e);
            throw new RuntimeException(e);
        }
        Document doc;
        try {
            doc = builder.parse(file);
        } catch (Exception e) {
            System.out.println("[XML] Error parsing file: " + e);
            throw new RuntimeException(e);
        }

        xmlDocument = doc;
        rootElement = xmlDocument.getDocumentElement();
    }

    public boolean isTeleOp() {
        if (rootElement.getElementsByTagName("mode").item(0).getTextContent().equals("teleop")) return true;
        else if (rootElement.getElementsByTagName("mode").item(0).getTextContent().equals("auto")) return false;
        else {
            System.out.println("[XML] Error: mode is unknown (" + rootElement.getElementsByTagName("mode").item(0).getTextContent() + ")");
            throw new RuntimeException("Invalid XML Data");
        }
    }

    public String getGroup() {
        return rootElement.getElementsByTagName("group").item(0).getTextContent();
    }

    public String getName() {
        return rootElement.getElementsByTagName("name").item(0).getTextContent();
    }

    public Element getCodeBlock() {
        return (Element) rootElement.getElementsByTagName("code").item(0);
    }
}
