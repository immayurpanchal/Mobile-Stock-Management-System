package MSMS.Application.Resources;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.jfoenix.controls.JFXSnackbar;
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

public class AddBrandController implements Initializable {
	@FXML private JFXTextField brandName;
	@FXML private JFXSnackbar status;
	
	//Database Connection
	Configuration config;
	SessionFactory sf;
	Session session;
	
	public void addBrand(ActionEvent event) {
		Brand b = new Brand();
		b.setBrand_name(brandName.getText());
		
		session.beginTransaction();
		session.save(b);
		session.getTransaction().commit();

		//Show snackbar for Successfully added Brand
		status.show("Brand Added Successfully", 2000);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Database Connection
		config = new Configuration().configure().addAnnotatedClass(Brand.class)
				.addAnnotatedClass(Product.class).addAnnotatedClass(SupplierBill.class)
				.addAnnotatedClass(CustomerBill.class).addAnnotatedClass(Stock.class).addAnnotatedClass(Supplier.class)
				.addAnnotatedClass(Customer.class);
		sf = config.buildSessionFactory();
		session = sf.openSession();
	}
}
