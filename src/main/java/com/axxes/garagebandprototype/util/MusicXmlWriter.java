package com.axxes.garagebandprototype.util;

import com.axxes.garagebandprototype.model.instrument.Instrument;
import com.axxes.garagebandprototype.model.loop.Drumloop;
import com.axxes.garagebandprototype.model.measures.Beat;
import com.axxes.garagebandprototype.model.measures.Measure;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

@Component
public class MusicXmlWriter {

    public boolean writeXMLFromDrumloop(Drumloop drumloop, File file) {
        boolean isWritten = false;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Drumloop
            Document doc = db.newDocument();
            Element rootElement = doc.createElement("drumloop");
            doc.appendChild(rootElement);
            rootElement.setAttribute("bpm", String.valueOf(drumloop.getBpm().get()));

            // Measures
            List<Measure> measures = drumloop.getMeasures();
            for (int i = 0; i < measures.size(); i++) {
                Element measure = doc.createElement("measure");
                measure.setAttribute("id", "" + i);
                rootElement.appendChild(measure);

                // Beats
                List<Beat> beats = measures.get(i).getBeats();
                for (int j = 0; j < beats.size(); j++) {
                    Element beat = doc.createElement("beat");
                    beat.setAttribute("id", "" + j);
                    measure.appendChild(beat);

                    // Instruments
                    List<Instrument> instruments = beats.get(j).getInstruments();
                    for (int k = 0; k < instruments.size(); k++) {
                        Element instrument = doc.createElement("instrument");
                        instrument.setAttribute("id", "" + k);
                        beat.appendChild(instrument);

                        Element instrumentName = doc.createElement("name");
                        instrumentName.appendChild(doc.createTextNode(instruments.get(k).getClass().getSimpleName()));
                        instrument.appendChild(instrumentName);
                    }
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(source, result);

            isWritten = true;

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

        return isWritten;

    }

}
