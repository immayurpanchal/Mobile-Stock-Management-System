package MSMS.Application.Resources;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AddProductController implements Initializable {
	@FXML private JFXTextField pname;
	@FXML private JFXTextField pmodel;
	@FXML private JFXTextArea pdescription;
	@FXML private JFXComboBox<String> brandList;
	@FXML private JFXSnackbar snackbar;
	List<Brand> l;
	
	Configuration config;
	SessionFactory sf;
	Session session;
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
	EntityManager em = emf.createEntityManager();
	
	public void addProduct(ActionEvent event) {
		//Save Selected Brand in temp variable 
		Brand temp=null;
		
		//Get the selected Brand and store it in temp
		for(Brand b : l)
			if(b.getBrand_name()==brandList.getSelectionModel().getSelectedItem())
				temp = b;
		
		//Create Product Object 
		Product p = new Product();
		p.setBrand(temp);
		p.setProduct_model(pmodel.getText());
		p.setProduct_name(pname.getText());
		p.setProduct_description(pdescription.getText());
		
		//Save Product Object
		session.beginTransaction();
		session.save(p);
		session.getTransaction().commit();
		
		//Show success message
		snackbar.show("Product Added Successfully", 3000);
		
		//Clear Fields after Successfully adding the product
		pdescription.setText("");
		pmodel.setText("");
		pname.setText("");
		pname.requestFocus();
	}
	
	//When ComboBox gets Selected Fetch fill it with list
	public void fillComboBox(Event event) {
		ArrayList<String> brands = new ArrayList<>();
		
		for(Brand b : l)
			brands.add(b.getBrand_name());
		
		ObservableList<String> ol = FXCollections.observableArrayList(brands);
		brandList.setItems(ol);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		config = new Configuration().configure().addAnnotatedClass(Brand.class).addAnnotatedClass(Product.class)
				.addAnnotatedClass(SupplierBill.class).addAnnotatedClass(CustomerBill.class)
				.addAnnotatedClass(Stock.class).addAnnotatedClass(Supplier.class).addAnnotatedClass(Customer.class);
		sf = config.buildSessionFactory();
		session = sf.openSession();
		
		//Get the list of Brands from Database and store it in list l 
		l = em.createQuery("from Brand", Brand.class).getResultList();
	}
}
