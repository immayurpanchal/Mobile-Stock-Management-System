package MSMS.Application.Resources;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import MSMS.Application.Brand;
import MSMS.Application.Customer;
import MSMS.Application.CustomerBill;
import MSMS.Application.Login;
import MSMS.Application.Product;
import MSMS.Application.Stock;
import MSMS.Application.Supplier;
import MSMS.Application.SupplierBill;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.util.Callback;

public class SellController implements Initializable{
	@FXML private JFXTextField pid;
	@FXML private JFXTextField pname;
	@FXML private JFXTextField pmodel;
	@FXML private JFXTextField pdescription;
	@FXML private JFXTextField pbrand;
	@FXML private JFXTextField price;
	@FXML private JFXTextField sgst;
	@FXML private JFXTextField cgst;
	@FXML private JFXTextField total;
	@FXML private JFXTextField imei;
	
	
	@FXML private JFXTextField cid;
	@FXML private JFXTextField cname;
	@FXML private JFXTextField cmobile;
	@FXML private JFXTextField cemail;
	@FXML private JFXTextField cadd;
	
	@FXML private JFXTextField searchProduct;
	@FXML private JFXTextField searchCustomer;
	
	@FXML private JFXTreeTableView<Product> productTreeView;
	@FXML private JFXTreeTableView<Customer> customerTreeView;
	
	EntityManagerFactory emf;
	EntityManager em;
	
	Configuration config;
	SessionFactory sf;
	Session session;

	public void sell(ActionEvent event) {
		// Check any field should not be empty and all validations
		if (!validProduct() && !validCustomer())
			return;

		// End of Validation
		System.out.println("Inside Sell");
		long tempImei = 0;
		try {
			tempImei = Long.parseLong(imei.getText());
		} catch (NumberFormatException e) {
			System.out.println("Invalid IMEI number, Enter Only digits");
			return;
		}
		System.out.println("got imei");
		SupplierBill supplierBill = null;

		// Check whether entered imei no is valid or not by doing cross checking entry
		// in Supplier Table. If it doesn't exist throw Exception
		System.out.println("My imei is :" + tempImei);
		try {
			em.getTransaction().begin();
			supplierBill = (SupplierBill) em.createQuery("from SupplierBill where supplierIMEI="+tempImei, SupplierBill.class).getSingleResult();
			em.getTransaction().commit();
			
		} catch (NullPointerException e) {
			System.out.println("Enter Valid IMEI");
			return;
		}
		System.out.println(supplierBill.getSupplierIMEI() + " is in supplierbill table");
		System.out.println("found item in stock");

		try {
			// Check whether entered item is in stock. If not throw exception as without
			// stock Item can't be sold.
			em.getTransaction().begin();
			Stock st = (Stock) em.createQuery("from Stock where pid=" + pid.getText(), Stock.class).getSingleResult();
			em.getTransaction().commit();

			System.out.println("Stock Query Executed Succeed");
			if (st.getProduct_qty() <= 0) {
				System.out.println("Without stock it is not possible to sell any item");
				return;
			}

			CustomerBill customerBill = new CustomerBill();
			customerBill.setCustomerIMEI(tempImei);

			try {
				customerBill.setCustomerPrice(Double.parseDouble(price.getText().replaceAll(",", "")));
			} catch (NumberFormatException e) {
				System.out.println("Enter Price! Or Enter Price in digit only");
				return;
			}

			System.out.println("Getting Date");
			customerBill.setCustomerDate(LocalDate.now());
			System.out.println("Got Date");

			customerBill.setCustomerCGST(Double.parseDouble(cgst.getText().replaceAll(",", "")));
			customerBill.setCustomerSGST(Double.parseDouble(sgst.getText().replaceAll(",", "")));
			customerBill.setCustomerTotal(Double.parseDouble(total.getText().replaceAll(",", "")));
			System.out.println("Price , GST, Total are set successfully");

			// If Customer Doesn't exist in database, Create a new one.
			if (cid.getText() == "") {
				System.out.println("Inside customer IF");
				Customer c = new Customer();
				c.setCustomer_name(cname.getText());
				c.setCustomer_mobile(Long.parseLong(cmobile.getText())); // Check Entered Mobile is valid (Done in
																			// Beginning)
				c.setCustomer_add(cadd.getText());
				c.setCustomer_email(cadd.getText()); // Check Entered is valid email (done in beginning)

				session.beginTransaction();
				session.save(c);// Object Saved in database
				session.getTransaction().commit();

				// Now get the Customer object from database. Do it because ID is generated by
				// system so we can't use the
				// old Customer object
				Customer c1 = (Customer) em.createQuery("from Customer where customer_name=" + cname.getText()
						+ " and customer_mobile=" + cmobile.getText(), Customer.class);

				customerBill.setCcustomer(c1);
			} else {// Customer Exists in database//That means it is shown in the TreeTable as well.
				System.out.println("Customer Exist in database");
				Customer c = customerTreeView.getSelectionModel().getSelectedItem().getValue();
				customerBill.setCcustomer(c);
			}

			// If Product Doesn't exist in database, Create a new one.
			if (pid.getText() == "") {
				Product p = new Product();
				p.setProduct_name(pname.getText());
				p.setProduct_model(pmodel.getText());
				p.setProduct_description(pdescription.getText());

				try {// Check whether brand exists for that product or not
					System.out.println("Checking Brand Existance");
					em.getTransaction().begin();
					Brand b = (Brand) em.createQuery("from Brand where brand_name=" + pbrand.getText())
							.getSingleResult();
					em.getTransaction().commit();

					System.out.println("Brand Query Succeed");
					p.setBrand(b);
				} catch (NullPointerException e) {
					// Brand Doesn't Exist in Database, Create a new brand.
					System.out.println("Unable to find brand");
					Brand b = new Brand();
					b.setBrand_name(pbrand.getText());

					session.beginTransaction();
					session.save(b);
					session.getTransaction().commit();

					em.getTransaction().begin();
					Brand b1 = (Brand) em.createQuery("from Brand where brand_name=" + pbrand.getText())
							.getSingleResult();
					em.getTransaction().commit();

					p.setBrand(b1);
				}
				System.out.println("Product Trasaction Begin");
				session.beginTransaction();
				session.save(p);
				session.getTransaction().commit();
				System.out.println("Product Transaction commit");
			} else {
				System.out.println("Product Exists in database");
				Product p = productTreeView.getSelectionModel().getSelectedItem().getValue();
				System.out.println("Product Query succeed");
				customerBill.setCproduct(p);
			}

			System.out.println("Begin CustomerBill transaction");
			
			try {
				session.beginTransaction();
				session.save(customerBill);
				session.getTransaction().commit();
				System.out.println("CustomerBill Transaction Commit");
			}
			catch(Exception e) {
				System.out.println("Item is alreay sold You can't resell it");
			}
			
			// Now decrease Stock by one as transaction is successful at this stage

			st.setProduct_qty(st.getProduct_qty() - 1);

			System.out.println("Stock Transaction Begin");
			session.beginTransaction();
			session.update(st);
			session.getTransaction().commit();
			System.out.println("Stock Transaction Commit");
			// Clean Every TextField for new Entry After Successful Transaction
			pid.setText("");
			pname.setText("");
			pbrand.setText("");
			pdescription.setText("");
			pmodel.setText("");
			price.setText("");
			sgst.setText("");
			cgst.setText("");
			total.setText("");

			pname.setEditable(true);
			pbrand.setEditable(true);
			pdescription.setEditable(true);
			pmodel.setEditable(true);
			price.setEditable(true);

			cname.setText("");
			cadd.setText("");
			cemail.setText("");
			cmobile.setText("");
			cid.setText("");

			cname.setEditable(true);
			cadd.setEditable(true);
			cemail.setEditable(true);
			cmobile.setEditable(true);

			System.out.println("Congratulations!! you have sold one Mobile");
		} catch (NullPointerException e) {
			System.out.println("Sorry IMEI is wrong or you are trying to sell item which is not in stock");
		}
		System.out.println("Transaction succeed");
		
	}
	
	
	private boolean validCustomer() {
		System.out.println("Inside validate Customer");
		if(cadd.getText()!="" && cemail.getText()!="" && cname.getText()!="") {
			try {
				System.out.println("Inside if of validate Customer");
				Pattern p = Pattern.compile("\\b^\\d{10}\\b");
				Matcher m = p.matcher(cmobile.getText());
				System.out.println("Pattern has got match");
				
				Pattern p1 = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
				Matcher m1 = p1.matcher(cemail.getText());
				if(m.matches() && m1.matches())
					return true;
				return false;
			}
			catch(Exception e) {
				System.out.println("Unable to match mobile");
				return false;
			}
		}
		System.out.println("Outside if of Validate Customer");
		return false;
	}


	private boolean validProduct() {
		if(pbrand.getText()!="" && pdescription.getText()!="" && pmodel.getText()!="" && pname.getText()!="" && price.getText()!="") {
			try {
				Double.parseDouble(price.getText());
				return true;
			}
			catch(NumberFormatException e) {
				return false;
			}
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	public void searchCustomer(ActionEvent event) {
		customerTreeView.getColumns().removeAll(customerTreeView.getColumns());
		// Old style of fetching product data as done in BuyController for searchProduct
		// Option
		// Step 0 : Change request focus to Product Text Field in order to get Ripple
		// Effect
		searchCustomer.requestFocus();

		System.out.println("Request for focus is done");

		// Step 1 : Create Columns and Add column in the table view
		JFXTreeTableColumn<Customer, String> customerIDs = new JFXTreeTableColumn<>("ID");
		JFXTreeTableColumn<Customer, String> customerNames = new JFXTreeTableColumn<>("Name");
		JFXTreeTableColumn<Customer, String> customerMobiles = new JFXTreeTableColumn<>("Mobile");
		JFXTreeTableColumn<Customer, String> customerEmails = new JFXTreeTableColumn<>("Email");
		JFXTreeTableColumn<Customer, String> customerAddresses = new JFXTreeTableColumn<>("Address");
		customerTreeView.getColumns().addAll(customerIDs, customerNames, customerMobiles, customerAddresses, customerEmails);

		System.out.println("Columns Added");

		// Step 1.1 : Get the data from the database
		ArrayList<Customer> customerList = new ArrayList<>();
		
		em.getTransaction().begin();
		customerList = (ArrayList<Customer>) em.createQuery("from Customer").getResultList();
		em.getTransaction().commit();

		System.out.println("Query Succeed");

		// Step 2 : Create Observable list which contains your data
		ObservableList<Customer> ols = FXCollections.observableArrayList(customerList);

		System.out.println("Stored in ObsservableList");

		// Step 3: Create Each row and SimpleStringProperty contains the name of the
		// Customer class instance variables.
		customerIDs.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("customer_id").get()));
		customerNames.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("customer_name").get()));
		customerMobiles.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("customer_mobile").get()));
		customerAddresses.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("customer_add").get()));
		customerEmails.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("customer_email").get()));

		System.out.println("Values set to cellFactory");

		// Step 4 : Create Recursive Tree item and also don't forget to extend in
		// Product Class
		TreeItem<Customer> root = new RecursiveTreeItem<>(ols, RecursiveTreeObject::getChildren);

		// Step 5 : Set tree view and Done
		customerTreeView.setRoot(root);
		customerTreeView.setShowRoot(false);
	}

	public void selectedCustomer(InputEvent event) {
		try {
			//Set Text Fields values to the selected table items
			TreeItem<Customer> temp = customerTreeView.getSelectionModel().getSelectedItem();
			cid.setText(Integer.toString(temp.getValue().getCustomer_id()));
			cname.setText(temp.getValue().getCustomer_name());
			cmobile.setText(Long.toString(temp.getValue().getCustomer_mobile()));
			cadd.setText(temp.getValue().getCustomer_add());
			cemail.setText(temp.getValue().getCustomer_email());
			
			//Set Editable values to false because item is selected from the table
			cid.setEditable(false);
			cname.setEditable(false);
			cmobile.setEditable(false);
			cadd.setEditable(false);
			cemail.setEditable(false);
		}
		catch(Exception e) {
			System.out.println("Selection is not made accurately");
		}
	}
	@SuppressWarnings("unchecked")
	public void searchProduct(ActionEvent event) {
		productTreeView.getColumns().removeAll(productTreeView.getColumns());
		// Old style of fetching product data as done in BuyController for searchProduct
		// Option
		// Step 0 : Change request focus to Product Text Field in order to get Ripple
		// Effect
		searchProduct.requestFocus();
		
		System.out.println("Request for focus is done");

		// Step 1 : Create Columns and Add column in the table view
		JFXTreeTableColumn<Product, String> productNames = new JFXTreeTableColumn<>("Name");
		JFXTreeTableColumn<Product, String> productDescriptions = new JFXTreeTableColumn<>("Description");
		JFXTreeTableColumn<Product, String> productModels = new JFXTreeTableColumn<>("Model");
		JFXTreeTableColumn<Product, String> productBrands = new JFXTreeTableColumn<>("Brand");
		productTreeView.getColumns().addAll(productBrands, productNames, productModels, productDescriptions);
		
		System.out.println("Columns Added");

		// Step 1.1 : Get the data from the database
		ArrayList<Product> productList = new ArrayList<>();
		
		em.getTransaction().begin();
		productList = (ArrayList<Product>) em.createQuery("from Product").getResultList();
		em.getTransaction().commit();

		System.out.println("Query Succeed");
		
		// Step 2 : Create Observable list which contains your data
		ObservableList<Product> ols = FXCollections.observableArrayList(productList);
		
		System.out.println("Stored in ObsservableList");

		// Step 3: Create Each row and SimpleStringProperty contains the name of the
		// Product class instance variables.
		productNames.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("product_name").get()));
		productModels.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("product_model").get()));
		productDescriptions.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("product_description").get()));
		productBrands.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getBrand().getBrand_name());
					}
				});

		System.out.println("Values set to cellFactory");
		
		// Step 4 : Create Recursive Tree item and also don't forget to extend in
		// Product Class
		TreeItem<Product> root = new RecursiveTreeItem<>(ols, RecursiveTreeObject::getChildren);

		// Step 5 : Set tree view and Done
		productTreeView.setRoot(root);
		productTreeView.setShowRoot(false);
	}

	public void selectedProduct(InputEvent event) {
		try {
			System.out.println("Inside Selected product");
			//Set Text Fields values to the selected table items
			TreeItem<Product> temp = productTreeView.getSelectionModel().getSelectedItem();
			System.out.println("detected selection model");
			pid.setText(Integer.toString(temp.getValue().getProduct_id()));
			pname.setText(temp.getValue().getProduct_name());
			pmodel.setText(temp.getValue().getProduct_model());
			pdescription.setText(temp.getValue().getProduct_description());
			pbrand.setText(temp.getValue().getBrand().getBrand_name());
			System.out.println("text set for product in textfields");
			
			//Set Editable values to false because item is selected from the table
			pid.setEditable(false);
			pname.setEditable(false);
			pmodel.setEditable(false);
			pdescription.setEditable(false);
			pbrand.setEditable(false);
			
			
		}
		catch(Exception e) {
			System.out.println("Selection is not made accurately");
		}
	}
	
	public void setGST(InputEvent event) {
		System.out.println("Input detected");
		if(price.getText()=="")
			return;
		else {
			try {
				try {
					double priceForGST = Double.parseDouble(price.getText());
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					System.out.println(priceForGST);
					sgst.setText(df.format((priceForGST*0.1))+"");
					sgst.setEditable(false);
					cgst.setText(df.format((priceForGST*0.1))+"");
					sgst.setEditable(false);
					total.setText(df.format((priceForGST+(priceForGST*0.1)+(priceForGST*0.1)))+"");
				}catch(NumberFormatException e) {
					sgst.setText("");
					cgst.setText("");
					total.setText("");
				}
			}catch(NumberFormatException e) {
				sgst.setText("");
				cgst.setText("");
				total.setText("");
			}
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pid.setEditable(false);
		emf = Persistence.createEntityManagerFactory("pu");
		em = emf.createEntityManager();

		config = new Configuration().configure().addAnnotatedClass(Brand.class).addAnnotatedClass(Product.class)
				.addAnnotatedClass(SupplierBill.class).addAnnotatedClass(CustomerBill.class)
				.addAnnotatedClass(Stock.class).addAnnotatedClass(Supplier.class).addAnnotatedClass(Customer.class)
				.addAnnotatedClass(Login.class);
		sf = config.buildSessionFactory();
		session = sf.openSession();
		
	}
}
