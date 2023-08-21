package org.happiest.minds.springinitializr.utility;

import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.constant.Constants;
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

            setTextContent(document, Constants.GROUP_ID, springInitializrRequest.getGroup());
            setTextContent(document, Constants.ARTIFACT_ID, springInitializrRequest.getArtifact());
            setTextContent(document, Constants.NAME, springInitializrRequest.getName());
            setTextContent(document, Constants.DESCRIPTION, springInitializrRequest.getDescription());
            setTextContent(document, Constants.PACKAGING, springInitializrRequest.getPackagingType());

            createDependency(springInitializrRequest, document);
            updateXML(filePath, document);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static void setTextContentForDependency(Document document, NodeList dependencies,
                                                    String groupIdValue, String artifactIdValue) {
        try {
            Element dependency = document.createElement(Constants.DEPENDENCY);
            Element dependenciesElement = (Element) dependencies.item(0);
            dependenciesElement.appendChild(dependency);
            Element groupIdOfDependency = document.createElement(Constants.GROUP_ID);
            Element artifactIdOfDependency = document.createElement(Constants.ARTIFACT_ID);
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

    private static void createDependency(SpringInitializrRequest springInitializrRequest, Document document) {
        try {
            Set<String> dependencyInput = new HashSet<>(springInitializrRequest.getDependencies());
            for (String dependency : dependencyInput) {
                NodeList dependencies = document.getElementsByTagName(Constants.DEPENDENCIES);
                switch (dependency) {
                    case Constants.WEB ->
                            setTextContentForDependency(document, dependencies, Constants.SPRING_FRAMEWORK_GROUD_ID, Constants.WEB_ARTIFACT_ID);
                    case Constants.GRAPH_QL ->
                            setTextContentForDependency(document, dependencies, Constants.SPRING_FRAMEWORK_GROUD_ID, Constants.GRAPHQL_ARTIFACT_ID);
                    case Constants.THYMELEAF ->
                            setTextContentForDependency(document, dependencies, Constants.SPRING_FRAMEWORK_GROUD_ID, Constants.THYMELEAF_ARTIFACT_ID);
                    case Constants.SECURITY ->
                            setTextContentForDependency(document, dependencies, Constants.SPRING_FRAMEWORK_GROUD_ID, Constants.SECURITY_ARTIFACT_ID);
                    case Constants.JPA ->
                            setTextContentForDependency(document, dependencies, Constants.SPRING_FRAMEWORK_GROUD_ID, Constants.JPA_ARTIFACT_ID);
                    case Constants.JDBC ->
                            setTextContentForDependency(document, dependencies, Constants.SPRING_FRAMEWORK_GROUD_ID, Constants.JDBC_ARTIFACT_ID);
                    case Constants.MY_SQL ->
                            setTextContentForDependency(document, dependencies, Constants.MYSQL_GROUP_ID, Constants.MYSQL_ARTIFACT_ID);
                    case Constants.H_2 ->
                            setTextContentForDependency(document, dependencies, Constants.H2_GROUP_ID, Constants.H2_ARTIFACT_ID);
                    case Constants.VALIDATION ->
                            setTextContentForDependency(document, dependencies, Constants.SPRING_FRAMEWORK_GROUD_ID, Constants.VALIDATION_ARTIFACT_ID);
                    case Constants.LOMBOK ->
                            setTextContentForDependency(document, dependencies, Constants.LOMBOK_GROUP_ID, Constants.LOMBOK_ARTIFACT_ID);
                    default -> log.error(Constants.DEPENDENCY_NOT_PRESENT);
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
