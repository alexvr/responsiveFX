package com.axxes.garagebandprototype.util;

import com.axxes.garagebandprototype.model.instrument.*;
import com.axxes.garagebandprototype.model.loop.Drumloop;
import com.axxes.garagebandprototype.model.measures.Measure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Arrays;

@Component
public class MusicXmlParser {

    @Autowired
    private Kick kick;
    @Autowired
    private Cymbal cymbal;
    @Autowired
    private HiHat hiHat;
    @Autowired
    private Snare snare;
    @Autowired
    private Drumloop drumloop;

    public void parserDrumloopFromXml(File file) {
        // Empty the current drumloop.
        drumloop.setMeasures(Arrays.asList(new Measure(), new Measure()));
        drumloop.setCurrentMeasure(0);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            Node drumloopNode = doc.getElementsByTagName("drumloop").item(0);
            Element bpmElement = (Element) drumloopNode;
            drumloop.setBpm(Integer.parseInt(bpmElement.getAttribute("bpm")));
            System.out.println("Drumloop BPM: " + bpmElement.getAttribute("bpm"));

            // Measures
            NodeList nodeMeasures = doc.getElementsByTagName("measure");
            for (int i = 0; i < nodeMeasures.getLength(); i++) {
                Node mNode = nodeMeasures.item(i);
                Element mElement = (Element) mNode;
                System.out.println("Measure no: " + mElement.getAttribute("id"));

                // Beats
                NodeList nodeBeats = mElement.getElementsByTagName("beat");
                for (int j = 0; j < nodeBeats.getLength(); j++) {
                    Node bNode = nodeBeats.item(j);

                    if (bNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element bElement = (Element) bNode;
                        System.out.println("\tBeat no: " + bElement.getAttribute("id"));

                        // Instruments
                        NodeList instruments = bElement.getElementsByTagName("name");
                        for (int k = 0; k < instruments.getLength(); k++) {
                            System.out.println("\t\tInstrument name: " + instruments.item(k).getTextContent());
                            Instrument instrument = getInstrument(instruments.item(k).getTextContent());
                            drumloop.addInstrument(instrument, i, j);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Instrument getInstrument(String instrumentName) {
        Instrument instrument;

        switch (instrumentName) {
            case "Snare":
                instrument = this.snare;
                break;
            case "Cymbal":
                instrument = this.cymbal;
                break;
            case "HiHat":
                instrument = this.hiHat;
                break;
            case "Kick":
                instrument = this.kick;
                break;
            default:
                instrument = this.hiHat;
                break;
        }

        return instrument;
    }

}
