package faith.samueljiang.epaceprepare;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class FileHandler {

    /**
     * This method is a unified way of creating a new project object
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
            createProperties(projectPath, projectName, "1.0", 1, PrepareSdk.SdkVersion);

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
        } catch (Exception e) {
            System.err.println(e);
        }

        // Create a hidden properties file for the project
        createProperties(projectPath, projectName, "1.0", 1, PrepareSdk.SdkVersion);

        // Redirect PDF and Annotation files
        pdf = pdfDest.toFile();
        annot = annotDest.toFile();

        // Add pdf and annot files to properties
        updateProperty(projectPath, "pdf_path", pdf.getAbsolutePath());
        updateProperty(projectPath, "annot_path", annot.getAbsolutePath());

        // Initialize PACE_Config.xml
        initConfigFileHeader(config, course, number);

        return new ePACEProject(projectName, projectPath, pdf, annot, config, course, number);
    }


    /**
     *
     * @param projectPath
     * @return
     */
    public ePACEProject openProject(File projectPath) {
        // TODO: Check project integrity

        // Get info from .properties
        File config = new File(projectPath.getAbsolutePath() + "/PACE_Config.xml");
        String name = "";
        try {
            name = getProperty(projectPath.getAbsolutePath(), "project_name");
        } catch (Exception e) {
            System.err.println(e);
        }

        // Get necessary information
        String path = projectPath.getAbsolutePath();
        String course = getXMLEntry(config, "PACE_Info", "Course", "");
        String number = getXMLEntry(config, "PACE_Info", "Number", "");
        String pdfPath = getProperty(projectPath.getAbsolutePath(), "pdf_path");
        String annotPath = getProperty(projectPath.getAbsolutePath(), "annot_path");

        if (!pdfPath.equals("") && !annotPath.equals("")) {
            return new ePACEProject(name, path, new File(pdfPath), new File(annotPath), config, course, Integer.parseInt(number));
        } else {
            return new ePACEProject(name, path, config, course, Integer.parseInt(number));
        }
    }


    /**
     *
     * @param projectPath
     * @param projectName
     * @param version
     * @param workingPage
     * @param sdk
     */
    public void createProperties(String projectPath, String projectName, String version, Integer workingPage, String sdk) {
        try {
            // Write information
            FileWriter writer = new FileWriter(projectPath + "/.properties");
            JSONObject json = new JSONObject();
            json.put("project_name", projectName);
            json.put("version", version);
            json.put("working_page", workingPage);
            json.put("sdk", sdk);
            json.put("pdf_path", "");
            json.put("annot_path", "");
            json.put("config_populated", "0");
            writer.write(json.toJSONString());
            writer.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }


    /**
     *
     * @param path
     * @param key
     * @param value
     */
    public void updateProperty(String path, String key, String value) {
        try {
            // Parse the file
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(path + "/.properties");
            JSONObject json = (JSONObject) parser.parse(reader);

            // Replace value and save
            json.replace(key, value);
            FileWriter writer = new FileWriter(path + "/.properties");
            writer.write(json.toJSONString());
            writer.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    /**
     *
     * @param path
     * @param key
     * @return
     */
    public String getProperty(String path, String key) {
        try {
            // Parse the file
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(path + "/.properties");
            JSONObject json = (JSONObject) parser.parse(reader);

            return (String) json.get(key);
        } catch (Exception e) {
            System.err.println(e);
        }

        return "";
    }


    /**
     * This class calls JavaFX's directory chooser library to fetch a path (folder)
     * @param title The title for the file chooser window
     * @return The selected path
     */
    public File selectDirectoryWithGUI(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        return directoryChooser.showDialog(new Stage());
    }


    /**
     * This class calls JavaFX's file chooser library to fetch a file
     * @param title The title for the file chooser window
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
     *
     * @param path
     * @return
     */
    public File createConfigFile(String path) {
        File output = new File(path + "/PACE_Config.xml");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // PACE Root
            Element root = doc.createElement("PACE");
            doc.appendChild(root);

            // Add PACE Info attribute and sub attributes
            Element paceInfo = doc.createElement("PACE_Info");
            Element course = doc.createElement("Course");
            Element number = doc.createElement("Number");
            Element paceVersion = doc.createElement("paceVersion");
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

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);

            // Format XML
            autoFormatXML(source, result, true);
        } catch (Exception e) {
            System.err.println(e);
        }

        return output;
    }


    /**
     *
     * @param xmlFile
     * @param parent
     * @param entryName
     * @param id
     * @param entry
     */
    public void recordEntryToXML(File xmlFile, String parent, String entryName, String id, String entry) {
        try{
            // Open File
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);

            // Locate the entry
            Node parentNode = doc.getFirstChild();
            if (id.isEmpty()) {
                parentNode = doc.getElementsByTagName(parent).item(0);
            } else {
                NodeList parentList = doc.getElementsByTagName(parent);
                for (int i = 0; i < parentList.getLength(); i++) {
                    if (parentList.item(i).getFirstChild().getNodeValue().equals(id)) {
                        parentNode = parentList.item(i);
                        break;
                    }
                }
            }

            NodeList childNoteList = parentNode.getChildNodes();
            for (int i = 0; i < childNoteList.getLength(); i++) {
                Node node = childNoteList.item(i);
                if (entryName.equals(node.getNodeName())) {
                    node.setTextContent(entry);
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    public void newXMLNode(File xml, String parent, String parentId, String nodeName, String nodeId, String value) {
        try{
            // Open File
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xml);

            // Locate the entry
            Node parentNode = doc.getFirstChild();
            if (parentId.isEmpty()) {
                parentNode = doc.getElementsByTagName(parent).item(0);
            } else {
                NodeList parentList = doc.getElementsByTagName(parent);
                for (int i = 0; i < parentList.getLength(); i++) {
                    if (parentList.item(i).getFirstChild().getNodeValue().equals(parentId)) {
                        parentNode = parentList.item(i);
                        break;
                    }
                }
            }

            // Add node
            Element node = doc.createElement(nodeName);
            if (!nodeId.isEmpty()) {
                node.setIdAttribute(nodeId, true);
            }
            if (!value.isEmpty()) {
                node.setTextContent(value);
            }
            parentNode.appendChild(node);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xml);
            transformer.transform(source, result);
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    /**
     *
     * @param xmlFile
     * @param parent
     * @param entryName
     * @param id
     * @return
     */
    public String getXMLEntry(File xmlFile, String parent, String entryName, String id) {
        try {
            // Open File
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);

            // Locate the entry
            Node parentNode = doc.getFirstChild();
            if (id.isEmpty()) {
                parentNode = doc.getElementsByTagName(parent).item(0);
            } else {
                NodeList parentList = doc.getElementsByTagName(parent);
                for (int i = 0; i < parentList.getLength(); i++) {
                    if (parentList.item(i).getFirstChild().getNodeValue().equals(id)) {
                        parentNode = parentList.item(i);
                        break;
                    }
                }
            }

            NodeList childNoteList = parentNode.getChildNodes();
            for (int i = 0; i < childNoteList.getLength(); i++) {
                Node node = childNoteList.item(i);
                if (entryName.equals(node.getNodeName())) {
                    return node.getTextContent();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        return "";
    }


    /**
     *
     * @param source
     * @param output
     */
    public void autoFormatXML(DOMSource source, StreamResult output, boolean includeHeader) {
        // TODO: Fix space issue

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // Beautify the format of the resulted XML
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            if (includeHeader) {
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            }
            transformer.transform(source, output);

        } catch (Exception e) {
            System.err.println(e);
        }
    }


    /**
     *
     * @param path
     */
    public void createDirIfNotExist(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }


    /**
     *
     * @param configFile
     */
    private void initConfigFileHeader(File configFile, String course, Integer number) {
        // TODO: Add other info
        FileHandler fileHandler = new FileHandler();
        fileHandler.recordEntryToXML(configFile, "PACE_Info", "Course", "", course);
        fileHandler.recordEntryToXML(configFile, "PACE_Info", "Number", "", number.toString());
    }


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
