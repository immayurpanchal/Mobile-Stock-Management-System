package MSMS.Application.Resources;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

@SuppressWarnings("serial")
public class PrintReport  extends JFrame {    
	String myDriver;
	String myUrl;
	Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;   
    String query;
    String reportFileName;
    
    public PrintReport(String query, String reportFileName) {
    	this.query = query;
    	this.reportFileName = reportFileName;
	}

    public void showReport() throws ClassNotFoundException, SQLException{
    	String myDriver = "org.postgresql.Driver";
    	String myUrl = "jdbc:postgresql://localhost:1234/stmgmt";
    	Class.forName(myDriver);
    	Connection conn = DriverManager.getConnection(myUrl, "postgres", "root");
        Statement stmt = conn.createStatement();
        ResultSet rs;
         try {
        	 rs = stmt.executeQuery(this.query);
        	 JRResultSetDataSource rsdt = new JRResultSetDataSource(rs);
        	 	
        	 InputStream is = this.getClass().getResourceAsStream(this.reportFileName);
        	 JasperDesign jd = JRXmlLoader.load(is);
             JasperReport jasperReport = JasperCompileManager.compileReport(jd);
             
             JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, null, rsdt);
             JRViewer viewer = new JRViewer(JasperPrint);
             
             viewer.setOpaque(true);
             viewer.setVisible(true);
             
             this.add(viewer);
             this.setSize(900,500); // JFrame size
             this.setVisible(true);
            
         } catch (Exception e) {
             JOptionPane.showMessageDialog(rootPane, e.getMessage());
         }
   
    }
}