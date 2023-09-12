package org.firstinspires.ftc.teamcode.internals.registration.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XMLCodeSegment {
    public Element node;

    public XMLCodeSegment(Element node) {
        this.node = node;
    }

    public ArrayList<XMLCodeLine> getContainedCode() {
        NodeList list = node.getElementsByTagName("expression");
        ArrayList<XMLCodeLine> segments = new ArrayList<>();
        for (Node i: IntStream.range(0, list.getLength())
            .mapToObj(list::item)
            .collect(Collectors.toList())) {

            segments.add(new XMLCodeLine((Element) i));
        }

        return segments;
    }
}
