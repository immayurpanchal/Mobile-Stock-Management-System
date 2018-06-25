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

public class AddCustomerController implements Initializable{
    @FXML private JFXTextField cname;
    @FXML private JFXTextField cemail;
    @FXML private JFXTextField cmobile;
    @FXML private JFXTextArea caddress;
    @FXML private VBox snackBar; 
    
    Configuration config;
	SessionFactory sf;
	Session session;
	
	public void addCustomer(ActionEvent event) {	
		//Creating customer object
		Customer c = new Customer();
		c.setCustomer_name(cname.getText());
		c.setCustomer_mobile(Long.parseLong(cmobile.getText()));
		c.setCustomer_email(cemail.getText());
		c.setCustomer_add(caddress.getText());
		
		//Saving Customer Object to Database
		session.beginTransaction();
		session.save(c);
		session.getTransaction().commit();
		
		JFXSnackbar sb = new JFXSnackbar(snackBar);
		sb.show("Customer Added Successfully", 3000);
		
		//Clear TextFields After Succesfully Adding the Customer
		cname.setText("");
		caddress.setText("");
		cemail.setText("");
		cmobile.setText("");
		cname.requestFocus();
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
