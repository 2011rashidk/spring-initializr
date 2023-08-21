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
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
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
                            setTextContentForDependency(document, dependencies, "org.springframework.boot", "spring-boot-starter-web");
                    case Constants.GRAPH_QL ->
                            setTextContentForDependency(document, dependencies, "org.springframework.boot", "spring-boot-starter-graphql");
                    case Constants.THYMELEAF ->
                            setTextContentForDependency(document, dependencies, "org.springframework.boot", "spring-boot-starter-thymeleaf");
                    case Constants.SECURITY ->
                            setTextContentForDependency(document, dependencies, "org.springframework.boot", "spring-boot-starter-security");
                    case Constants.JPA ->
                            setTextContentForDependency(document, dependencies, "org.springframework.boot", "spring-boot-starter-data-jpa");
                    case Constants.JDBC ->
                            setTextContentForDependency(document, dependencies, "org.springframework.boot", "spring-boot-starter-data-jdbc");
                    case Constants.MY_SQL ->
                            setTextContentForDependency(document, dependencies, "com.mysql", "mysql-connector-j");
                    case Constants.H_2 -> setTextContentForDependency(document, dependencies, "com.h2database", "h2");
                    case Constants.VALIDATION ->
                            setTextContentForDependency(document, dependencies, "org.springframework.boot", "spring-boot-starter-validation");
                    case Constants.LOMBOK ->
                            setTextContentForDependency(document, dependencies, "org.projectlombok", "lombok");
                    default -> log.error("No such dependency available");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static void updateXML(String filePath, Document document) {
        try {
            document.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
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
