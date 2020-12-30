package faith.samueljiang.epaceprepare;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileHandler {

    /**
     * This method is a unified way of creating a new project object.
     * To pass the returned ePACEProject to another ePACEProject, use the clone() method provided by the ePACEProject class.
     *
     * @param projectName Name of the project
     * @param projectPath Path to the project directory
     * @param pdf The PACE PDF
     * @param annot The XFDF annotation file
     * @param config The configuration.xml file
     * @param course The course name
     * @param number The PACE number
     * @return The created project object
     */
    public ePACEProject createProject(String projectName, String projectPath, File pdf, File annot, File config, String course, int number) {
        // Use a different constructor if user chose not to import PDFs or annotations just yet
        if (pdf == null || annot == null) {
            // Create a hidden properties file for the project
            createProperties(projectPath, projectName, "1.0", 1, PrepareSdk.SdkVersion, ePacePrepare.version);
            // Initialize PACE_Config.xml
            initConfigFileHeader(config, course, number);

            return new ePACEProject(projectName, projectPath, config, course, number);
        }

        // Copy the PDF and Annotation to project folder
        Path pdfSource = Paths.get(pdf.getAbsolutePath());
        Path pdfDest = Paths.get(projectPath + "/" + pdf.getName());
        Path annotSource = Paths.get(annot.getAbsolutePath());
        Path annotDest = Paths.get(projectPath + "/" + annot.getName());
        try {
            Files.copy(pdfSource, pdfDest, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(annotSource, annotDest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("ePACEPrepare Exception thrown when copying PACE PDF and XFDF.\n" + e.toString());
        }

        // Create a hidden properties file for the project
        createProperties(projectPath, projectName, "1.0", 1, PrepareSdk.SdkVersion, ePacePrepare.version);

        // Add pdf and annot files to properties
        setProperty(projectPath, "pdf_path", pdf.getAbsolutePath());
        setProperty(projectPath, "annot_path", annot.getAbsolutePath());

        // Initialize PACE_Config.xml
        initConfigFileHeader(config, course, number);

        return new ePACEProject(projectName, projectPath, pdf, annot, config, course, number);
    }


    /**
     * Given the path to the project, this method fetches the useful files recorded in the .properties and PACE_Config.xml.
     * The method creates an ePACEProject from the files indexed. It will intend to fix any inconsistency, but it might still fail in some case.
     * To pass the returned ePACEProject to another ePACEProject, use the clone() method provided by the ePACEProject class.
     *
     * @param projectPath Path to the project directory given
     * @return The created project object
     */
    public ePACEProject openProject(File projectPath) {
        // TODO: Check project integrity and compatibility and fix path mismatch (i.e. project path has been moved)

        // Get info from .properties and PACE_Config.xml
        File config = new File(projectPath.getAbsolutePath() + "/PACE_Config.xml");
        String name = getProperty(projectPath.getAbsolutePath(), "project_name");

        // Get necessary information
        String path = projectPath.getAbsolutePath(); // Get the String path of the projectPath
        String course = "", number = "";
        try {
            course = getXMLNodeTextContent(config, "PACE_Info", "", "Course", "");
            number = getXMLNodeTextContent(config, "PACE_Info", "", "Number", "");
        } catch (Exception e) {
            System.err.println("Error while getting the information from PACE_Config.xml.\n" + e.toString());
        }
        String pdfPath = getProperty(projectPath.getAbsolutePath(), "pdf_path");
        String annotPath = getProperty(projectPath.getAbsolutePath(), "annot_path");

        if (!pdfPath.equals("") && !annotPath.equals("")) {
            return new ePACEProject(name, path, new File(pdfPath), new File(annotPath), config, course, Integer.parseInt(number));
        } else {
            ePACEProject openedProject = new ePACEProject(name, path, config, course, Integer.parseInt(number));
            // Add pdf or annotation to the project path
            if (!pdfPath.equals("")) {
                openedProject.setPdfFile(new File(pdfPath));
            } else if (!annotPath.equals("")) {
                openedProject.setAnnotFile(new File(annotPath));
            }
            return openedProject;
        }
    }


    /**
     * This method creates the ./properties file necessary for the program to work with the project.
     *
     * @param projectPath Path to the project directory
     * @param projectName Name of the project
     * @param version Version of the project
     * @param workingPage The current page that the program is working on. Can be defaulted to 1 if you want the created project to open the PACE to page 1.
     * @param sdk The SDK version of PrepareSdk in the program. This is to prevent possible incompatibility
     */
    public void createProperties(String projectPath, String projectName, String version, Integer workingPage, String sdk, String programVersion) {
        try {
            // Write information
            FileWriter writer = new FileWriter(projectPath + "/.properties");
            JSONObject json = new JSONObject();
            json.put("project_name", projectName);
            json.put("version", version);
            json.put("working_page", workingPage);
            json.put("program_version", programVersion);
            json.put("sdk", sdk);
            json.put("pdf_path", "");
            json.put("annot_path", "");
            json.put("config_populated", "0");
            writer.write(json.toJSONString());
            writer.close();

        } catch (IOException e) {
            System.err.println("ePACEPrepare Exception thrown when  creating the /.properties file.\n" + e.toString());
        }
    }


    /**
     * This method sets a field in the ./properties. If the field doesn't exist, it creates one.
     *
     * @param path Path to the /.properties file. DO NOT include "/.properties" in the path
     * @param key The field in /.properties
     * @param value The value that needs to be added
     */
    public void setProperty(String path, String key, String value) {
        try {
            // Parse the file
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(path + "/.properties");
            JSONObject json = (JSONObject) parser.parse(reader);

            // Replace or create value
            if (json.get(key) == null) {
                json.put(key, value);
            } else {
                json.replace(key, value);
            }

            // Update the file
            FileWriter writer = new FileWriter(path + "/.properties");
            writer.write(json.toJSONString());
            writer.close();
        } catch (ParseException e) {
            System.err.println("ePACEPrepare Exception thrown when parsing /.properties to JSONObject in setProperty().\n" + e.toString());
        } catch (IOException e) {
            System.err.println("ePACEPrepare Exception thrown when updating /.properties in setProperty().\n" + e.toString());
        }
    }


    /**
     * This method returns the requested field from the /.properties file.
     * Returns null if the field cannot be found.
     *
     * @param path Path to the /.properties file. DO NOT include "/.properties" in the path
     * @param key The field in /.properties
     * @return The value of the field. Returns null if the field cannot be found
     */
    public String getProperty(String path, String key) {
        try {
            // Parse the file
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(path + "/.properties");
            JSONObject json = (JSONObject) parser.parse(reader);

            // The working_page field returns a double, so it needs to be parsed separately.
            if (key.equals("working_page")) {
                return String.valueOf(json.get(key));
            }
            return (String) json.get(key);
        } catch (ParseException e) {
            System.err.println("ePACEPrepare Exception thrown when parsing /.properties to JSONObject in getProperty().\n" + e);
        } catch (IOException e) {
            System.err.println("ePACEPrepare Exception thrown when updating /.properties in getProperty().\n" + e.toString());
        }
        return null;
    }


    /**
     * This class calls JavaFX's directory chooser library to fetch a path (folder)
     *
     * @param title The title for the file chooser window. It will not show on MacOS
     * @return The selected path
     */
    public File selectDirectoryWithGUI(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        return directoryChooser.showDialog(new Stage());
    }


    /**
     * This class calls JavaFX's file chooser library to fetch a file
     *
     * @param title The title for the file chooser window. It will not show on MacOS
     * @param filters The file filters
     * @return The selected file
     */
    public File selectFileWithGUI(String title, FileChooser.ExtensionFilter[] filters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(filters);
        return fileChooser.showOpenDialog(new Stage());
    }


    /**
     * This method creates the PACE_Config.xml to store the activities and other information about the PACE.
     * The method also initializes the PACE_Info Node in the XML.
     *
     * @param path Project Path. Not the path to PACE_Config.xml
     * @return The File object of PACE_Config.xml
     */
    public File createConfigFile(String path) {
        File output = new File(path + "/PACE_Config.xml");
        try {
            // Initialize the the XML doc using Java DOM
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Set <PACE> root node
            Element root = doc.createElement("PACE");
            doc.appendChild(root);

            // Add <PACE_Info> attribute and its sub-attributes
            Element paceInfo = doc.createElement("PACE_Info");
            Element course = doc.createElement("Course");
            Element number = doc.createElement("Number");
            Element paceVersion = doc.createElement("PaceVersion");
            Element configVersion = doc.createElement("ConfigVersion");
            Element lastModified = doc.createElement("LastModified");
            Element totalPages = doc.createElement("TotalPages");
            root.appendChild(paceInfo);
            paceInfo.appendChild(course);
            paceInfo.appendChild(number);
            paceInfo.appendChild(paceVersion);
            paceInfo.appendChild(configVersion);
            paceInfo.appendChild(lastModified);
            paceInfo.appendChild(totalPages);

            // Stream the DOM object into XML file
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);

            // Format XML and write to file
            autoFormatXML(source, result, true);
        } catch (ParserConfigurationException e) {
            System.err.println("ePACEPrepare Exception thrown when creating config file.\n" + e.toString());
        }

        return output;
    }


    /**
     * This method updates the value of an XML node given its node name and/or its ID. If there isn't an ID given, just pass in ID = "".
     * The method only expects to update one node. IOException will be thrown if more than one node can be specified.
     *
     * @param xml The XML file
     * @param node The Node/Element that needs to be updated
     * @param id The optional ID that specifies the Node (use "" if ID DNE)
     * @param value The new text value
     * @throws NoSuchFieldException The node cannot be found.
     * @throws ParserConfigurationException The XML parser isn't configured correctly.
     * @throws IOException The XML file cannot be opened, or the given node isn't unique.
     * @throws SAXException The XML file cannot be parsed.
     */
    public void updateXMLNode(File xml, String node, String id, String value) throws NoSuchFieldException, ParserConfigurationException, IOException, SAXException {
        boolean hasRecorded = false;

        // Open File
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xml);

        // Get the node by searching by node name
        NodeList fields = doc.getElementsByTagName(node);
        if (id.isEmpty()) { // No id
            if (fields.getLength() > 1) {
                throw new IOException("Too many results when searching for target node. Try to specify by id or use updateXMLNodeWithParent()");
            } else {
                fields.item(0).setTextContent(value);
                hasRecorded = true;
            }
        } else {
            for (int i = 0; i < fields.getLength(); i++) {  // has id
                Element field = (Element) fields.item(i);
                if (field.getAttribute("id").equals(id)) {  // Loop through the matched nodes and look for the one with corresponding id
                    if (hasRecorded) {
                        throw new IOException("Too many results when searching for target node. Try to specify by id or use updateXMLNodeWithParent()");
                    }

                    field.setTextContent(value);
                    hasRecorded = true;
                }
            }
        }

        if (!hasRecorded) {
            throw new NoSuchFieldException("The node <" + node + "> cannot be found or no node with the given id can be found.");
        }

        // Stream the DOM object into XML file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xml);

        // Format XML and write result
        autoFormatXML(source, result, false);
    }


    /**
     * This method updates the value of an XML node given its node name and/or its ID that matches the given parent node. If there isn't an ID or parent ID given, just pass them as "".
     * The method only expects to update one node. IOException will be thrown if more than one node can be specified.
     *
     * @param xml The XML file
     * @param parentNode The parent Node of the specified Node
     * @param parentId The optional parent ID (use "" if ID DNE)
     * @param node The Node/Element that needs to be updated
     * @param id The optional ID that specifies the Node (use "" if ID DNE)
     * @param value The new text value
     * @throws NoSuchFieldException The node cannot be found.
     * @throws ParserConfigurationException The XML parser isn't configured correctly.
     * @throws IOException The XML file cannot be opened, or the given node isn't unique.
     * @throws SAXException The XML file cannot be parsed.
     */
    public void updateXMLNodeWithParent(File xml, String parentNode, String parentId, String node, String id, String value) throws NoSuchFieldException, ParserConfigurationException, IOException, SAXException {
        boolean hasRecorded = false;

        // Open File
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xml);

        NodeList fields = doc.getElementsByTagName(node);
        for (int i = 0; i < fields.getLength(); i++) {
            Element field = (Element) fields.item(i);
            Element parent = (Element) fields.item(i).getParentNode();
            if (parent.getTagName().equals(parentNode)) {    // Found matching parent
                if (parentId.isEmpty() || parent.getAttribute("id").equals(parentId)) { // No ID or matching ID
                    if (field.getTagName().equals(node)) {   // Found matching Node name
                        if (id.isEmpty() || field.getAttribute("id").equals(id)) {
                            if (hasRecorded) { // Don't allow more than one update
                                throw new IOException("Too many results when searching for target node.");
                            }
                            fields.item(i).setTextContent(value);
                            hasRecorded = true;
                        }
                    }
                }
            }
        }

        if (!hasRecorded) {
            throw new NoSuchFieldException("The node <" + node + "> cannot be found or no node with the given id can be found.");
        }

        // Stream the DOM object into XML file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xml);

        // Format XML and write result
        autoFormatXML(source, result, false);
    }


    /**
     * This method creates a new Node under the parent Node given.
     * The method only expects one creation.
     * 
     * @param xml The XML file
     * @param parentNode The parent Node where the new node should be appended to
     * @param parentId The optional parent ID (use "" if ID DNE)
     * @param node The Node/Element that needs to be created
     * @param id The optional ID that specifies the Node (use "" if ID DNE)
     * @param value The new text value
     * @throws NoSuchFieldException The node target location cannot be found.
     * @throws ParserConfigurationException The XML parser isn't configured correctly.
     * @throws IOException The XML file cannot be opened, or the given create location isn't unique.
     * @throws SAXException The XML file cannot be parsed.
     */
    public void createXMLNode(File xml, String parentNode, String parentId, String node, String id, String value) throws NoSuchFieldException, ParserConfigurationException, IOException, SAXException {
        boolean hasCreated = false;

        // Open File
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xml);

        NodeList parents = doc.getElementsByTagName(parentNode);
        for (int i = 0; i < parents.getLength(); i++) {
            Element parent = (Element) parents.item(i);
            if (parent.getTagName().equals(parentNode)) {    // Found matching parent
                if (parentId.isEmpty() || parent.getAttribute("id").equals(parentId)) { // No ID or matching ID
                    if (hasCreated) { // Don't allow more than one create
                        throw new IOException("Too many possible insert locations. Only one allowed.");
                    }
                    // Found location. Append new node to parent
                    Element newNode = doc.createElement(node);
                    newNode.setAttribute("id", id);
                    newNode.setTextContent(value);
                    parent.appendChild(newNode);
                    hasCreated = true;
                }
            }
        }

        if (!hasCreated) {
            throw new NoSuchFieldException("The node <" + parentNode + "> cannot be found or no node with the given id can be found. No node created");
        }

        // Stream the DOM object into XML file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xml);

        // Format XML and write result
        autoFormatXML(source, result, false);
    }

    /**
     * This method retrieves the first text content of an XML Node given its optional parent Node, optional parent ID as well as Node name or optional ID.
     * This will return the first XML Node found.
     *
     * @param xml The XML file
     * @param parentNode The parent Node of the specified Node
     * @param parentId The optional parent ID (use "" if ID DNE)
     * @param node The Node/Element that needs to be retrieved
     * @param id The optional ID that specifies the Node (use "" if ID DNE)
     * @return The first found text context
     * @throws ParserConfigurationException The XML parser isn't configured correctly.
     * @throws IOException The XML file cannot be opened.
     * @throws SAXException The XML file cannot be parsed.
     */
    public String getXMLNodeTextContent(File xml, String parentNode, String parentId, String node, String id) throws ParserConfigurationException, IOException, SAXException {
        // Open File
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xml);

        NodeList fields = doc.getElementsByTagName(node);
        for (int i = 0; i < fields.getLength(); i++) {
            Element field = (Element) fields.item(i);
            Element parent = (Element) fields.item(i).getParentNode();
            if (parentNode.equals(parent.getTagName()) || parentNode.isEmpty()) {    // Found matching parent or no parent
                if (parentId.isEmpty() || parent.getAttribute("id").equals(parentId)) { // No ID or matching ID
                    if (field.getTagName().equals(node)) {   // Found matching Node name
                        if (id.isEmpty() || field.getAttribute("id").equals(id)) {
                            return fields.item(i).getTextContent();
                        }
                    }
                }
            }
        }

        return null;
    }


    /**
     * Make this obsolete
     *
     * @param xmlFile
     * @param page
     * @return
     */
    public Node getPageElements(File xmlFile, String page) {
        // TODO: Make this obsolete
        try {
            // Open File
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);

            // Locate the entry
            NodeList parentList = doc.getElementsByTagName("Page");
            for (int i = 0; i < parentList.getLength(); i++) {
                Element node = (Element) parentList.item(i);
                if (node.getAttribute("id").equals(page)) {
                    return parentList.item(i);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        return null;
    }


    /**
     * This method automatically formats and writes the DOM stream into the result XML file
     *
     * @param source The DOMSource of the DOM object of the XML
     * @param output The Stream result of the XML file
     * @param includeHeader True only on file create to include the XML header
     */
    private void autoFormatXML(DOMSource source, StreamResult output, boolean includeHeader) {
        // TODO: Fix space issue

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // Beautify the format of the resulted XML
            if (includeHeader) {
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            }
            transformer.transform(source, output);

        } catch (Exception e) {
            System.err.println(e);
        }
    }


    /**
     * Creates any intermediate directory to get to the requested directory
     *
     * @param path The target file/directory path
     */
    public void createDirIfNotExist(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }


    /**
     * Initialize the PACE_Config.xml when given the information
     *
     * @param configFile The PACE_Config.xml file
     * @param course The course name of the PACE
     * @param number The course number of the PACE
     */
    private void initConfigFileHeader(File configFile, String course, Integer number) {
        // TODO: Add other info
        FileHandler fileHandler = new FileHandler();
        try {
            fileHandler.updateXMLNode(configFile, "Course", "", course);
            fileHandler.updateXMLNodeWithParent(configFile, "PACE_Info", "", "Number", "", number.toString());
        } catch (Exception e) {
            System.err.println("Error while initializing PACE_Config.xml.\n" + e.toString());
        }
    }


    /**
     * Initializes the pages structure of the PACE_Config.xml.
     *
     * @param configFile The PACE_Config.xml file
     * @param pageNumber The total page number in the PACE
     */
    public void initConfigFilePages(File configFile, int pageNumber) {
        try{
            // Open File
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(configFile);

            // For each page, populate empty framework
            Node root = doc.getFirstChild();
            for (int i = 1; i <= pageNumber; i++) {
                Element page = doc.createElement("Page");
                page.setAttribute("id", Integer.toString(i));
                Element type = doc.createElement("Type");
                root.appendChild(page);
                page.appendChild(type);
            }

            // write the content into xml file
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(configFile);
            autoFormatXML(source, result, false);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
