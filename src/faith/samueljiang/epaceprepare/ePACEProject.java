package faith.samueljiang.epaceprepare;

import java.io.File;

public class ePACEProject {
    private String projectName;
    private String projectPath;
    private File pdfFile;
    private File annotFile;
    private File configFile;
    private String course;
    private Integer number;

    ePACEProject() {}

    ePACEProject(ePACEProject in) {
        this.projectName = in.getProjectName();
        this.projectPath = in.getProjectPath();
        this.configFile = new File(in.getConfigFile().getAbsolutePath());
        this.course = in.getCourse();
        this.number = in.getNumber();

        if (in.getPdfFile() != null) {
            this.pdfFile = new File(in.getPdfFile().getAbsolutePath());
        }
        if (in.getAnnotFile() != null) {
            this.annotFile = new File(in.getAnnotFile().getAbsolutePath());
        }
    }

    ePACEProject(String projectName, String projectPath, File config, String course, int number) {
        this.projectName = projectName;
        this.projectPath = projectPath;
        this.course = course;
        this.number = number;
        this.configFile = config;
    }

    ePACEProject(String projectName, String projectPath, File pdf, File annot, File config, String course, int number) {
        this(projectName, projectPath, config, course, number);
        this.pdfFile = pdf;
        this.annotFile = annot;
    }


    public void clone(ePACEProject source) {
        this.projectName    =   source.getProjectName();
        this.projectPath    =   source.getProjectPath();
        this.pdfFile        =   source.getPdfFile();
        this.annotFile      =   source.getAnnotFile();
        this.configFile     =   source.getConfigFile();
        this.course         =   source.getCourse();
        this.number         =   source.getNumber();
    }


    // Getters and setters
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }
    public void setAnnotFile(File annotFile) {
        this.annotFile = annotFile;
    }
    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getProjectName() {
        return projectName;
    }
    public String getProjectPath() {
        return projectPath;
    }
    public File getPdfFile() {
        return pdfFile;
    }
    public File getAnnotFile() {
        return annotFile;
    }
    public File getConfigFile() {
        return configFile;
    }
    public String getCourse() {
        return course;
    }
    public int getNumber() {
        return number;
    }
}
