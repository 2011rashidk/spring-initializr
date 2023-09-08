package org.happiest.minds.springinitializr.utility;

import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.config.SpringDependencyConfig;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;
import java.util.Map;

import static org.happiest.minds.springinitializr.enums.Constants.*;

@Component
@Slf4j
public class XMLUtility {

    @Autowired
    SpringDependencyConfig springDependencyConfig;

    public void updateXMLElementValue(String filePath, SpringInitializrRequest springInitializrRequest) {

        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();

            setTextContent(document, GROUP_ID.getValue(), springInitializrRequest.getGroup());
            setTextContent(document, ARTIFACT_ID.getValue(), springInitializrRequest.getArtifact());
            setTextContent(document, NAME.getValue(), springInitializrRequest.getName());
            setTextContent(document, DESCRIPTION.getValue(), springInitializrRequest.getDescription());
            setTextContent(document, PACKAGING.getValue(), springInitializrRequest.getPackagingType());

            createDependency(springInitializrRequest, document);
            updateXML(filePath, document);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void setTextContentForDependency(Document document, NodeList dependencies,
                                             String groupIdValue, String artifactIdValue) {
        try {
            Element dependency = document.createElement(DEPENDENCY.getValue());
            Element dependenciesElement = (Element) dependencies.item(0);
            dependenciesElement.appendChild(dependency);
            Element groupIdOfDependency = document.createElement(GROUP_ID.getValue());
            Element artifactIdOfDependency = document.createElement(ARTIFACT_ID.getValue());
            dependency.appendChild(groupIdOfDependency);
            dependency.appendChild(artifactIdOfDependency);
            groupIdOfDependency.setTextContent(groupIdValue);
            artifactIdOfDependency.setTextContent(artifactIdValue);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static void setTextContent(Document doc, String items, String springInitializrRequest) {
        try {
            NodeList nodeList = doc.getElementsByTagName(items);
            if (nodeList.getLength() > 0) {
                Element groupIdElement = (Element) nodeList.item(0);
                groupIdElement.setTextContent(springInitializrRequest);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void createDependency(SpringInitializrRequest springInitializrRequest, Document document) {
        try {
            List<String> dependencyInputs = springInitializrRequest.getDependencies();
            Map<String, Map<String, String>> dependenciesConfig = springDependencyConfig.getDependencies();
            for (String dependencyInput : dependencyInputs) {
                NodeList dependencies = document.getElementsByTagName(DEPENDENCIES.getValue());
                String groupId = dependenciesConfig.get(dependencyInput).get(GROUP_ID.getValue());
                String artifactId = dependenciesConfig.get(dependencyInput).get(ARTIFACT_ID.getValue());
                setTextContentForDependency(document, dependencies, groupId, artifactId);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static void updateXML(String filePath, Document document) {
        try {
            document.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newDefaultInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
