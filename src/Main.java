import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;

public class Main {

    private final static int FIRSTNAME_COLUMN = 0;
    private final static int LASTNAME_COLUMN = 1;
    private final static int NICKNAME_COLUMN = 2;
    private final static int SALARY_COLUMN = 3;
    private static Workbook workbook;
    private static int rowNum;

    public static void main(String[] args) {
        String xmlFileName = "/Users/eralp/Desktop/droolsprojects/Prg.SimpleXml/src/sample.xml";
        String excelFileName = "/Users/eralp/Desktop/droolsprojects/Prg.SimpleXml/src/sample.xlsx";
        XmlToScreen(xmlFileName);
        XmlLooping(xmlFileName);
        XmlToExcel(xmlFileName,excelFileName);
        System.out.println("Please control the result!");
    }

    private static void XmlToExcel(String xmlFileName, String xlsxFileName) {
        try {
            File fXlsFile = new File(xlsxFileName);
            if (fXlsFile.exists()) {
                System.out.println("delete file-> " + fXlsFile.getAbsolutePath());
                if (!fXlsFile.delete()) {
                    System.out.println("file '" + fXlsFile.getAbsolutePath() + "' was not deleted!");
                }
            }
            InitXls();
            /* ****************************************************************************************** */
            Sheet sheet = workbook.getSheetAt(0);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFileName);
            int totalStaff = 0;

            NodeList nList = doc.getElementsByTagName("company");
            for (int i = 0; i < nList.getLength(); i++) {
                System.out.println("Processing element " + (i + 1) + "/" + nList.getLength());
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    //String substanceName = element.getElementsByTagName("Name").item(0).getTextContent();
                    NodeList staffs = element.getElementsByTagName("staff");
                    for (int j = 0; j < staffs.getLength(); j++) {
                        totalStaff = staffs.getLength();
                        Node prod = staffs.item(j);
                        if (prod.getNodeType() == Node.ELEMENT_NODE) {
                            Element staff = (Element) prod;
                            String firstname = staff.getElementsByTagName("firstname").item(0).getTextContent();
                            String lastname = staff.getElementsByTagName("lastname").item(0).getTextContent();
                            String nickname = staff.getElementsByTagName("nickname").item(0).getTextContent();
                            String salary = staff.getElementsByTagName("salary").item(0).getTextContent();

                            Row row = sheet.createRow(rowNum++);
                            Cell cell = row.createCell(FIRSTNAME_COLUMN);
                            cell.setCellValue(firstname);

                            cell = row.createCell(LASTNAME_COLUMN);
                            cell.setCellValue(lastname);

                            cell = row.createCell(NICKNAME_COLUMN);
                            cell.setCellValue(nickname);

                            cell = row.createCell(SALARY_COLUMN);
                            cell.setCellValue(salary);
                        }
                    }
                }
            }

            FileOutputStream fileOut = new FileOutputStream(xlsxFileName);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();

            System.out.println("XmlToExcel finished, processed " + totalStaff + " staff(s)!");
            /* ****************************************************************************************** */

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void XmlToScreen(String fileName) {
        try {
            File fXmlFile = new File(fileName);
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                NodeList nList = doc.getElementsByTagName("staff");
                System.out.println("----------------------------");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    System.out.println("\nCurrent Element :" + nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        System.out.println("Staff id : " + eElement.getAttribute("id"));
                        System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                        System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                        System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
                        System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
                    }
                }
            } else {
                System.out.println("Xml file doesn't exist!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void XmlLooping(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                if (doc.hasChildNodes()) {
                    printNote(doc.getChildNodes());
                }
            } else {
                System.out.println("Xml file doesn't exist!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printNote(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                System.out.println("Node Value =" + tempNode.getTextContent());
                if (tempNode.hasAttributes()) {
                    NamedNodeMap nodeMap = tempNode.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());
                    }
                }
                if (tempNode.hasChildNodes()) {
                    // loop again if has child nodes
                    printNote(tempNode.getChildNodes());
                }
                System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
            }
        }
    }

    private static void InitXls() {
        workbook = new HSSFWorkbook();
        //workbook = new   XSSFWorkbook();

        CellStyle style = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        style.setFont(boldFont);

        Sheet sheet = workbook.createSheet();
        rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(FIRSTNAME_COLUMN);
        cell.setCellValue("First Name");
        cell.setCellStyle(style);

        cell = row.createCell(LASTNAME_COLUMN);
        cell.setCellValue("Last Name");
        cell.setCellStyle(style);

        cell = row.createCell(NICKNAME_COLUMN);
        cell.setCellValue("Nick Name");
        cell.setCellStyle(style);

        cell = row.createCell(SALARY_COLUMN);
        cell.setCellValue("Salary");
        cell.setCellStyle(style);

    }
}
