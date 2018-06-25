package MSMS.Application.Resources;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;

import MSMS.Application.Brand;
import MSMS.Application.Customer;
import MSMS.Application.CustomerBill;
import MSMS.Application.Login;
import MSMS.Application.Product;
import MSMS.Application.Stock;
import MSMS.Application.Supplier;
import MSMS.Application.SupplierBill;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class LoginController implements Initializable {
	@FXML private JFXTextField lblUsername;
	@FXML private JFXPasswordField lblPassword;
	@FXML private JFXSnackbar snackbar;
	private MainController mainController;
	
	Configuration config;
	SessionFactory sf;
	Session session;
	
	public void login(ActionEvent event){	
		Login l = new Login();
		l.setPassword(lblPassword.getText());
		l.setUsername(lblUsername.getText());

		session.beginTransaction();
		Login temp = (Login) session.get(Login.class, lblUsername.getText());
		session.getTransaction().commit();

		if (temp == null) {
			System.out.println("User not");
			snackbar.show("Username Not Found", 3000);
			return;
		}
		
		lblUsername.requestFocus(); 

		if (temp.getUsername().equals(lblUsername.getText()) && temp.getPassword().equals(lblPassword.getText())) {
			mainController.getMenuBar().setDisable(false);
			mainController.getTabpane().getTabs().remove(0);
			snackbar.show("Login Succeed", 3000);
			mainController.viewDashboard(null);
		}
		else
			snackbar.show("Wrong Password", 3000);

		lblUsername.setText("");
		lblPassword.setText("");
	}
	
	public void injectMainController(MainController mc) {
		this.mainController = mc;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Inside Login Controller");
		System.out.println(mainController);
		/*mainController.getMenuBar().setDisable(true);*/
		System.out.println("Inside Login Controller" + this);
		config = new Configuration().configure().addAnnotatedClass(Login.class).addAnnotatedClass(Brand.class).addAnnotatedClass(Product.class).addAnnotatedClass(SupplierBill.class).addAnnotatedClass(CustomerBill.class).addAnnotatedClass(Stock.class).addAnnotatedClass(Supplier.class).addAnnotatedClass(Customer.class);
		sf = config.buildSessionFactory();
		session = sf.openSession();
	}
}
