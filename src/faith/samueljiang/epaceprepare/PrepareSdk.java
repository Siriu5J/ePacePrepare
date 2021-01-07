package faith.samueljiang.epaceprepare;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

public class PrepareSdk {
    public static final String SdkVersion = "1.0";

    private ePACEProject project;
    private PDDocument pdfDoc;
    private FileHandler fileHandler;
    private Document annot;
    private Document config;


    PrepareSdk(ePACEProject openedProject) {
        this.project = new ePACEProject(openedProject);
        try {
            this.pdfDoc = PDDocument.load(this.project.getPdfFile());
        } catch (IOException e) {
            System.err.println("Cannot open PDF with PDDocument @PrepareSdk.\n" + e.toString());
        }
        this.fileHandler = new FileHandler();
    }


    /**
     *
     * @param page
     * @param save
     * @throws NoSuchFieldException
     */
    public void autoPreparePage(int page, boolean save) throws NoSuchFieldException {
        page--; // Change the 1-X based numbering to 0-X based numbering
        if (page > pdfDoc.getNumberOfPages() || page < 0) { // Check validity of the incoming page numbering
            throw new NoSuchFieldException("Invalid page number. Page number must be between 1 and " + pdfDoc.getNumberOfPages() + ". Given: " + (page + 1));
        }

        PDPage pdfPage = pdfDoc.getPage(page);
        System.out.println();
        PDRectangle mediaBox = pdfPage.getMediaBox();
        System.out.println(mediaBox.getHeight());
        System.out.println(mediaBox.getWidth());

        try {
            ArrayList<Node> activitiesOnPage = getPageActivitiesFromAnnot(page);
            Element configOnPage = getPageFromConfig(page + 1); // The PACE_Config.xml follows 1-X numbering, correct it
            Element createdBy = (Element) configOnPage.getElementsByTagName("CreatedBy").item(0);

            // Skip page if it has already been accessed fearing that we would make duplicates
            // TODO: Make this better
            if (createdBy.getTextContent() != "") {
                System.err.println("Page " + (page + 1) + " has already been modified. Skipped");
                return;
            }

            // Nothing found on this page. Skip it
            if (activitiesOnPage.size() == 0) {
                System.err.println("Nothing to add to page " + (page + 1) + ". Skipped");
                return;
            }

            // Get all the information that could be used to generate activity
            int id = 1;
            for (Node activity : activitiesOnPage) {
                // Parse <freetext> node
                switch (activity.getNodeName()) {
                    case "freetext":
                        parseFreeText(activity, configOnPage, id);
                        break;
                }
                id++;
            }

            // Set the CreatedBy to EPP
            createdBy.setTextContent("ePacePrepare");
            createdBy.setAttribute("sdk-version", this.SdkVersion.toString());
            createdBy.setAttribute("created-time", LocalTime.now().toString());
            createdBy.setAttribute("last-modified", LocalTime.now().toString());

            if (save) {
                saveConfig();
            }
        } catch (Exception e) {
            System.err.println("Exception thrown when trying to fetch activities from annotation file. Exception says:\n" + e.toString());
        }
    }


    /**
     *
     * @throws Exception
     */
    public void autoPreparePace() throws Exception {
        for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {
            this.autoPreparePage(i + 1, false);
        }
        saveConfig();
    }


    /**
     * This method returns all the annotations in the XFDF file within the given page.
     *
     * @param page The page where the annotations should be returned
     * @return An ArrayList of DOM Nodes
     * @throws NullPointerException The annotation cannot be found
     * @throws IOException The XML file cannot be opened
     * @throws ParserConfigurationException The XML parser isn't configured correctly
     * @throws SAXException The XML file cannot be parsed
     */
    private ArrayList<Node> getPageActivitiesFromAnnot(Integer page) throws NullPointerException, IOException, ParserConfigurationException, SAXException {
        if (project.getAnnotFile() == null) {   // Don't try to open an empty file
            throw new NullPointerException("Annotation file not found. Has an annotation file ever been imported?");
        }
        if (annot == null) {    // If the annotation file has never been opened in this session, open it
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            annot = docBuilder.parse(project.getAnnotFile());
        }

        // Get the annots element and its children
        Node annotNode = annot.getElementsByTagName("annots").item(0);
        NodeList annotations = annotNode.getChildNodes();

        ArrayList<Node> result = new ArrayList<>(); // This will be the return ArrayList
        for (int i = 0; i < annotations.getLength(); i++) {
            Node annotation = annotations.item(i);

            // Skip the non-element nodes
            if (annotation.getNodeType() != Node.ELEMENT_NODE) continue;

            int currentPage = Integer.parseInt(annotation.getAttributes().getNamedItem("page").getNodeValue());
            // Knowing that all the pages will be in order, we can improve the performance by stopping the loop once
            // We are done with the page
            if (currentPage > page) {
                break;
            }
            // Add the nodes to the ArrayList if the Node belongs to the page
            if (currentPage == page) {
                result.add(annotations.item(i));
            }
        }

        return result;
    }


    /**
     * This method returns the DOM Node from Document config that contains all the info of the given page
     *
     * @param page The page that is requested
     * @return The Element that contains the info of the given page
     * @throws IOException The XML file cannot be opened
     * @throws ParserConfigurationException The XML parser isn't configured correctly
     * @throws SAXException The XML file cannot be parsed
     */
    public Element getPageFromConfig(Integer page) throws NoSuchFieldException, IOException, ParserConfigurationException, SAXException {
        if (this.config == null) {  // If the config file has never been opened in this session, open it
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            this.config = docBuilder.parse(project.getConfigFile());
        }

        // Locate the entry
        NodeList pageList = config.getElementsByTagName("Page");
        for (int i = 0; i < pageList.getLength(); i++) {
            Element node = (Element) pageList.item(i);
            if (node.getAttribute("id").equals(page.toString())) {
                return node;
            }
        }

        throw new NoSuchFieldException("The given page cannot be found in the given PACE_Config.xml");
    }


    public void setPageToConfig(Integer page, Element pageNode) throws NoSuchFieldException, IOException, ParserConfigurationException, SAXException {
        if (this.config == null) {  // If the config file has never been opened in this session, open it
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            this.config = docBuilder.parse(project.getConfigFile());
        }

        NodeList pageList = config.getElementsByTagName("Page");
        for (int i = 0; i < pageList.getLength(); i++) {
            Element node = (Element) pageList.item(i);
            if (node.getAttribute("id").equals(page.toString())) {
                pageList.item(i).getParentNode().replaceChild((Node) pageNode, pageList.item(i));
            }
        }
    }


    private void parseFreeText(Node activity, Element configOnPage, Integer id) {
        // Only parse this <freetext> that is visible
        if (activity.getAttributes().getNamedItem("opacity").getNodeValue().equals("1")) {
            // Get the attributes from the <freetext> node
            String dimen = activity.getAttributes().getNamedItem("rect").getNodeValue();    // Get the dimensions of the textbox
            String[] dimens = dimen.split(","); // This array should contain 4 elements x1, y1, x2, y2
            String rotation = activity.getAttributes().getNamedItem("rotation").getNodeValue(); // Get the rotation of the textbox

            // Get the actual value of the <freetext>
            // Here I'm always using the second node to avoid the empty #text node
            Node contentNode = activity.getChildNodes().item(1);    // Get <contents-richtext>
            Node bodyNode = contentNode.getChildNodes().item(1);    // Get <body>
            Node pNode = bodyNode.getChildNodes().item(1);          // Get <p>
            NodeList spanNodes = pNode.getChildNodes();     // Get all the spans
            StringBuilder valueBuilder = new StringBuilder();
            for (int i = 0; i < spanNodes.getLength(); i++) {
                // Skip the non-element nodes
                if (spanNodes.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

                valueBuilder.append(spanNodes.item(i).getTextContent());
            }
            String value = valueBuilder.toString();

            // Create the element in PACE_Config.xml under configOnPage
            Element activityNode = config.createElement("Activity");
            activityNode.setAttribute("type", "Blank");
            activityNode.setAttribute("id", id.toString());
            activityNode.setAttribute("x1", dimens[0]);
            activityNode.setAttribute("y1", dimens[1]);
            activityNode.setAttribute("x2", dimens[2]);
            activityNode.setAttribute("y2", dimens[3]);
            activityNode.setAttribute("rotation", rotation);
            activityNode.setAttribute("value", value);
            configOnPage.appendChild(activityNode);
        }
    }


    private void saveConfig() throws TransformerConfigurationException, TransformerException {
        // Stream the DOM object into XML file
        DOMSource source = new DOMSource(this.config);
        StreamResult result = new StreamResult(project.getConfigFile());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 4);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(source, result);
    }
}
