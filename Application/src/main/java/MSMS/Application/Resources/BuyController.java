package MSMS.Application.Resources;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

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
import MSMS.Application.Product;
import MSMS.Application.Stock;
import MSMS.Application.Supplier;
import MSMS.Application.SupplierBill;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class BuyController implements Initializable{
	@FXML private JFXTextField pid;
	@FXML private JFXTextField pname;
	@FXML private JFXTextField pmodel;
	@FXML private JFXTextField pdescription;
	@FXML private JFXTextField price;
	@FXML private JFXTextField sgst;
	@FXML private JFXTextField cgst;
	@FXML private JFXTextField total;
	
	@FXML private JFXTextField sid;
	@FXML private JFXTextField sname;
	@FXML private JFXTextField smobile;
	@FXML private JFXTextField gstNo;
	@FXML private JFXTextField sadd;
	@FXML private JFXTextField imei;
	
	@FXML private JFXTextField searchProduct;
	@FXML private JFXTextField searchSupplier;
	
	@FXML private JFXTreeTableView<Product> productTreeView;
	@FXML private JFXTreeTableView<Supplier> supplierTreeView;
	
	//Database Variables
	Configuration config;
	SessionFactory sf;
	Session session;
	
	//JPA to fetch Data using Primary Key
	EntityManagerFactory emf;
	EntityManager em;
	
	public void buy(ActionEvent event) {
		//Create SupplierBill Object and set all required values
		SupplierBill sb = new SupplierBill();
		sb.setSupplierIMEI(Long.parseLong(imei.getText()));
		sb.setSupplierSGST(Double.parseDouble(sgst.getText().replaceAll(",", "")));
		sb.setSupplierCGST(Double.parseDouble(cgst.getText().replaceAll(",", "")));
		sb.setSupplierTotal(Double.parseDouble(total.getText().replaceAll(",", "")));
		sb.setSupplierPrice(Double.parseDouble(price.getText().replaceAll(",", "")));
		
		//Set selected Product Object in tempProduct from TreeTableView of Product 
		Product tempProduct = productTreeView.getSelectionModel().getSelectedItem().getValue();
		
		//Set selected Product Object in tempSupplirt from TreeTableView of Supplier 
		Supplier tempSupplier = supplierTreeView.getSelectionModel().getSelectedItem().getValue();
		
		sb.setProduct(tempProduct);
		sb.setSupplier(tempSupplier);
		
		//Set System Date as Current Bill date as it is offline App
		sb.setSupplierDate(LocalDate.now());
		
		session.beginTransaction();
		session.save(sb);
		
		//When you purchase a product Stock will be increased by one
		Stock st = new Stock();
		try { 
			//Remember here name of the class is written in query So give exact name as your class name
			st = (Stock) session.createQuery("from Stock where pid="+tempProduct.getProduct_id(), Stock.class).getSingleResult();
			st.setProduct_qty(st.getProduct_qty()+1);
			session.save(st);
		}
		catch(Exception e) {
			//If item is not in database then create a new Stock object and save it
			st = new Stock();
			st.setSproduct(tempProduct);
			st.setProduct_qty(1);
			session.save(st);
		}
		session.getTransaction().commit();
		
		//Clean Text Fields after saving data successfully
		pid.setText("");
		pname.setText("");
		pdescription.setText("");
		pmodel.setText("");
		price.setText("");
		imei.setText("");
		sgst.setText("");
		cgst.setText("");
		total.setText("");
		sid.setText("");
		sname.setText("");
		sadd.setText("");
		smobile.setText("");
		gstNo.setText("");
	}
	
	//When Search Button of Product clicked 
	@SuppressWarnings("unchecked")
	public void searchProduct(ActionEvent event) {
		//Clear the old table if exists  
		productTreeView.getColumns().removeAll(productTreeView.getColumns());
		
		//Step 0 : Change request focus to Product Text Field in order to get Ripple Effect
		searchProduct.requestFocus();
		
		//Step 1 : Create Columns and Add column in the table view
		JFXTreeTableColumn<Product, String> productNames = new JFXTreeTableColumn<>("Name");
		JFXTreeTableColumn<Product, String> productDescriptions  = new JFXTreeTableColumn<>("Description");
		JFXTreeTableColumn<Product, String> productModels = new JFXTreeTableColumn<>("Model");
		JFXTreeTableColumn<Product, String> productBrands = new JFXTreeTableColumn<>("Brand");
		
		productTreeView.getColumns().addAll(productBrands, productNames, productModels, productDescriptions);
		
		//Step 1.1 : Get the data from the database 
		ArrayList<Product> productList = new ArrayList<>();

		em.getTransaction().begin();
		productList = (ArrayList<Product>) em.createQuery("from Product").getResultList();
		em.getTransaction().commit();
		
		//Step 2 : Create Observable list which contains your data
		ObservableList<Product> ols = FXCollections.observableArrayList(productList);
		
		//Step 3: Create Each row and SimpleStringProperty contains the name of the Product class instance variables.
		productNames.setCellValueFactory(new TreeItemPropertyValueFactory<>(new SimpleStringProperty("product_name").get()));
		productModels.setCellValueFactory(new TreeItemPropertyValueFactory<>(new SimpleStringProperty("product_model").get()));
		productDescriptions.setCellValueFactory(new TreeItemPropertyValueFactory<>(new SimpleStringProperty("product_description").get()));
		productBrands.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Product,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Product, String> param) {
				return new SimpleStringProperty(param.getValue().getValue().getBrand().getBrand_name());
			}
		});

		//Step 4 : Create Recursive Tree item and also don't forget to extend in Product Class 
		TreeItem<Product> root = new RecursiveTreeItem<>(ols, RecursiveTreeObject::getChildren);
		
		//Step 5 : Set tree view and Done 
		productTreeView.setRoot(root);
		productTreeView.setShowRoot(false);
		
		//Filtering Table as user gives Input 
		searchProduct.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				productTreeView.setPredicate(new Predicate<TreeItem<Product>>() {
					
					@Override
					public boolean test(TreeItem<Product> product) {
						Boolean flag = product.getValue().getProduct_name().contains(newValue)
								|| product.getValue().getBrand().getBrand_name().contains(newValue)
								|| product.getValue().getProduct_model().contains(newValue)
								|| product.getValue().getProduct_description().contains(newValue);
						return flag;
					}
				});
			}
		});
	}
	
	//Fill the TextFields when user selects any Product TreeTableView Item 
	public void selectedProduct(MouseEvent event) {
		try {
			//Set Text Fields values to the selected table items
			TreeItem<Product> temp = productTreeView.getSelectionModel().getSelectedItem();
			pid.setText(Integer.toString(temp.getValue().getProduct_id()));
			pname.setText(temp.getValue().getProduct_name());
			pmodel.setText(temp.getValue().getProduct_model());
			pdescription.setText(temp.getValue().getProduct_description());
			
			//Set Editable values to false because item is selected from the table
			pid.setEditable(false);
			pname.setEditable(false);
			pmodel.setEditable(false);
			pdescription.setEditable(false);
			
		}
		catch(Exception e) {
			System.out.println("Selection is not made accurately");
		}
	}
	
	//Set GST price according to the input of price 
	public void setGST(InputEvent event) {
		//If price is not set return else set SGST = 10% and CGST = 10% of Price 
		if(price.getText()=="")
			return;
		else {
			try {
				try {
					double priceForGST = Double.parseDouble(price.getText());
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					
					sgst.setText(df.format((priceForGST*0.1))+"");
					sgst.setEditable(false);
					cgst.setText(df.format((priceForGST*0.1))+"");
					sgst.setEditable(false);
					total.setText(df.format((priceForGST+(priceForGST*0.1)+(priceForGST*0.1)))+"");
				}catch(NumberFormatException e) {
					//If input value is not a Number set TextFields to Empty
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
	
	@SuppressWarnings("unchecked")
	public void searchSupplier(ActionEvent event) {
		//Clear old table data if exists 
		supplierTreeView.getColumns().removeAll(supplierTreeView.getColumns());
		
		//Step 0 : Change Text field focus to Search Supplier Text Field in order to get Ripple Effect 
		searchSupplier.requestFocus();
		
		//Step 1 : Create Columns and Add column in the table view
		JFXTreeTableColumn<Supplier, String> supplierId = new JFXTreeTableColumn<>("Id");
		JFXTreeTableColumn<Supplier, String> supplierNames = new JFXTreeTableColumn<>("Name");
		JFXTreeTableColumn<Supplier, String> supplierMobile = new JFXTreeTableColumn<>("Mobile");
		JFXTreeTableColumn<Supplier, String> supplierGST = new JFXTreeTableColumn<>("GST No");
		JFXTreeTableColumn<Supplier, String> supplierAddress = new JFXTreeTableColumn<>("Address");
		
		//Step 1.1 : Set All columns in TreeTableView in required Order. 
		supplierTreeView.getColumns().addAll(supplierId, supplierNames, supplierMobile, supplierGST, supplierAddress);
		
		//Step 1.2 : Store Suppliers in ArrayList 
		ArrayList<Supplier> supplierList = new ArrayList<>();
		
		//Get Supplier Objects 
		em.getTransaction().begin();
		supplierList = (ArrayList<Supplier>) em.createQuery("from Supplier").getResultList();
		em.getTransaction().commit();
		
		//Step 2 : Create Observable list which contains your data
		ObservableList<Supplier> ols = FXCollections.observableArrayList(supplierList);
		
		//Step 3: Create Each row and SimpleStringProperty contains the name of the Supplier class instance variables.
		supplierId.setCellValueFactory(new TreeItemPropertyValueFactory<>(new SimpleStringProperty("supplier_id").get()));
		supplierNames.setCellValueFactory(new TreeItemPropertyValueFactory<>(new SimpleStringProperty("supplier_name").get()));
		supplierGST.setCellValueFactory(new TreeItemPropertyValueFactory<>(new SimpleStringProperty("supplier_gst").get()));
		supplierAddress.setCellValueFactory(new TreeItemPropertyValueFactory<>(new SimpleStringProperty("supplier_add").get()));
		supplierMobile.setCellValueFactory(new TreeItemPropertyValueFactory<>(new SimpleStringProperty("supplier_mobile").get()));
	
		//Step 4 : Create Recursive Tree item and also don't forget to extend in Product Class 
		TreeItem<Supplier> root = new RecursiveTreeItem<>(ols, RecursiveTreeObject::getChildren);
		
		//Step 5 : Set tree view and Done 
		supplierTreeView.setRoot(root);
		supplierTreeView.setShowRoot(false);
		
		//Filter Table Data
		searchSupplier.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				supplierTreeView.setPredicate(new Predicate<TreeItem<Supplier>>() {
					
					@Override
					public boolean test(TreeItem<Supplier> supplier) {
						Boolean flag = supplier.getValue().getSupplier_name().contains(newValue)
								|| supplier.getValue().getSupplier_gst().contains(newValue)
								|| supplier.getValue().getSupplier_add().contains(newValue)
								|| Long.toString(supplier.getValue().getSupplier_mobile()).contains(newValue);
						return flag;
					}
				});
			}
		});
	}
	
	public void selectedSupplier(MouseEvent event) {
		try {
			TreeItem<Supplier> temp = supplierTreeView.getSelectionModel().getSelectedItem();
			
			//Set Text Fields values to the selected table items
			sid.setText(Integer.toString(temp.getValue().getSupplier_id()));
			sname.setText(temp.getValue().getSupplier_name());
			smobile.setText(Long.toString(temp.getValue().getSupplier_mobile()));
			sadd.setText(temp.getValue().getSupplier_add());
			gstNo.setText(temp.getValue().getSupplier_gst());
			
			//Set Editable values to false because item is selected from the TreeTableView
			sid.setEditable(false);
			sname.setEditable(false);
			smobile.setEditable(false);
			sadd.setEditable(false);
			gstNo.setEditable(false);
		}
		catch(Exception e) {
			System.out.println("Selection is not made properly");
		}
	}

	@Override
	public String toString() {
		return "SupplierBillController [pid=" + pid + ", pname=" + pname + ", pmodel=" + pmodel + ", pdescription="
				+ pdescription + ", price=" + price + ", sgst=" + sgst + ", cgst=" + cgst + ", total=" + total
				+ ", sid=" + sid + ", sname=" + sname + ", smobile=" + smobile + ", gstNo=" + gstNo + ", sadd=" + sadd
				+ "]";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Database Connection
		config = new Configuration().configure().addAnnotatedClass(Brand.class).addAnnotatedClass(Product.class)
				.addAnnotatedClass(SupplierBill.class).addAnnotatedClass(CustomerBill.class)
				.addAnnotatedClass(Stock.class).addAnnotatedClass(Supplier.class).addAnnotatedClass(Customer.class);
		sf = config.buildSessionFactory();
		session = sf.openSession();

		// Using JPA to fetch the Data using Primary Key
		emf = Persistence.createEntityManagerFactory("pu");
		em = emf.createEntityManager();
	}
}
