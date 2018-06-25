package MSMS.Application.Resources;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import MSMS.Application.Brand;
import MSMS.Application.Customer;
import MSMS.Application.CustomerBill;
import MSMS.Application.Product;
import MSMS.Application.Stock;
import MSMS.Application.Supplier;
import MSMS.Application.SupplierBill;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class AddSupplierController implements Initializable{
	@FXML private JFXTextField sname;
	@FXML private JFXTextField semail;
	@FXML private JFXTextField smobile;
	@FXML private JFXTextArea saddress;
	@FXML private JFXTextField sgstNo;
	@FXML private VBox snackBar;
	
	//Database Connection
	Configuration config;
	SessionFactory sf;
	Session session;
	
	public void addSeller(ActionEvent event) {
		//Create Supplier Object
		Supplier s = new Supplier();
		s.setSupplier_name(sname.getText());
		s.setSupplier_mobile(Long.parseLong(smobile.getText()));
		s.setSupplier_add(saddress.getText());
		s.setSupplier_gst(sgstNo.getText());

		//Save object to Database
		session.beginTransaction();
		session.save(s);
		session.getTransaction().commit();

		//Show Success Message
		JFXSnackbar sb = new JFXSnackbar(snackBar);
		sb.show("Supplier Added Successfully", 1000);

		//Clear Textfields After adding
		saddress.setText("");
		semail.setText("");
		sgstNo.setText("");
		smobile.setText("");
		sname.setText("");
		sname.requestFocus();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		config = new Configuration().configure().addAnnotatedClass(Brand.class).addAnnotatedClass(Product.class)
				.addAnnotatedClass(SupplierBill.class).addAnnotatedClass(CustomerBill.class)
				.addAnnotatedClass(Stock.class).addAnnotatedClass(Supplier.class).addAnnotatedClass(Customer.class);
		sf = config.buildSessionFactory();
		session = sf.openSession();
	}
}
