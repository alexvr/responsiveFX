package com.axxes.garagebandprototype.util;

import com.axxes.garagebandprototype.Audio.effects.*;
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
    @Autowired
    private Echo echoEffect;
    @Autowired
    private NoEffect noEffect;
    @Autowired
    private Reverb reverbEffect;
    @Autowired
    private RingModulator ringModulatorEffect;
    @Autowired
    private Flanger flangerEffect;
    @Autowired
    private Distortion distortionEffect;

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
                        NodeList instruments = bElement.getElementsByTagName("instrument");

                        for (int k = 0; k < instruments.getLength(); k++) {
                            Node iNode = instruments.item(k);
                            if (iNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element iElement = (Element) bNode;

                                String instrumentName = iElement.getElementsByTagName("name").item(0).getTextContent();

                                System.out.println("\t\tInstrument name: " + instrumentName);
                                Instrument instrument = getInstrument(instrumentName);

                                String effectName = iElement.getElementsByTagName("effect").item(0).getTextContent();

                                Effect effect = getEffect(effectName);
                                drumloop.addInstrument(instrument, i, j, effect);

                            }
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

    private Effect getEffect(String effectName) {
        Effect effect;

        switch (effectName) {
            case "Distortion":
                effect = distortionEffect;
                break;
            case "Echo":
                effect = echoEffect;
                break;
            case "Flanger":
                effect = flangerEffect;
                break;
            case "NoEffect":
                effect = noEffect;
                break;
            case "Reverb":
                effect = reverbEffect;
                break;
            case "RingModulator":
                effect = ringModulatorEffect;
                break;
            default:
                effect = noEffect;
        }

        return effect;
    }

}
