package org.happiest.minds.springinitializr.utility;

import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
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
import java.util.HashSet;
import java.util.Set;

import static org.happiest.minds.springinitializr.constant.StringConstants.*;
import static org.happiest.minds.springinitializr.enums.Constants.*;

@Component
@Slf4j
public class XMLUtility {

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
            Set<String> dependencyInput = new HashSet<>(springInitializrRequest.getDependencies());
            for (String dependency : dependencyInput) {
                NodeList dependencies = document.getElementsByTagName(DEPENDENCIES.getValue());
                switch (dependency) {
                    case WEB ->
                            setTextContentForDependency(document, dependencies, SPRING_FRAMEWORK_GROUP_ID.getValue(), WEB_ARTIFACT_ID.getValue());
                    case GRAPH_QL ->
                            setTextContentForDependency(document, dependencies, SPRING_FRAMEWORK_GROUP_ID.getValue(), GRAPHQL_ARTIFACT_ID.getValue());
                    case THYMELEAF ->
                            setTextContentForDependency(document, dependencies, SPRING_FRAMEWORK_GROUP_ID.getValue(), THYMELEAF_ARTIFACT_ID.getValue());
                    case SECURITY ->
                            setTextContentForDependency(document, dependencies, SPRING_FRAMEWORK_GROUP_ID.getValue(), SECURITY_ARTIFACT_ID.getValue());
                    case JPA ->
                            setTextContentForDependency(document, dependencies, SPRING_FRAMEWORK_GROUP_ID.getValue(), JPA_ARTIFACT_ID.getValue());
                    case JDBC ->
                            setTextContentForDependency(document, dependencies, SPRING_FRAMEWORK_GROUP_ID.getValue(), JDBC_ARTIFACT_ID.getValue());
                    case MY_SQL ->
                            setTextContentForDependency(document, dependencies, MYSQL_GROUP_ID.getValue(), MYSQL_ARTIFACT_ID.getValue());
                    case H_2 ->
                            setTextContentForDependency(document, dependencies, H2_GROUP_ID.getValue(), H2_ARTIFACT_ID.getValue());
                    case VALIDATION ->
                            setTextContentForDependency(document, dependencies, SPRING_FRAMEWORK_GROUP_ID.getValue(), VALIDATION_ARTIFACT_ID.getValue());
                    case LOMBOK ->
                            setTextContentForDependency(document, dependencies, LOMBOK_GROUP_ID.getValue(), LOMBOK_ARTIFACT_ID.getValue());
                    default -> log.error(DEPENDENCY_NOT_PRESENT.getValue());
                }
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
