package MSMS.Application.Resources;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController implements Initializable{
	@FXML private TabPane tabpane;
	@FXML private Tab tab;
	@FXML private MenuBar menuBar;
	
	public TabPane getTabpane() {
		return tabpane;
	}

	public void setTabpane(TabPane tabpane) {
		this.tabpane = tabpane;
	}

	public Tab getTab() {
		return tab;
	}
	
	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(MenuBar menuBar) {
		this.menuBar = menuBar;
	}

	@Override
	public String toString() {
		return "MainController [tabpane=" + tabpane + ", tab=" + tab + ", menuBar=" + menuBar + "]";
	}
	
	public void exit(ActionEvent event) {
		System.out.println("Inside Exit Menu");
		System.exit(0);
	}
	
	public void addBrand(ActionEvent event) {
		System.out.println("Inside AddBrand Menu");
		try {
			tab = new Tab("Add Brand", FXMLLoader.load(getClass().getResource("AddBrand.fxml")));
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addProduct(ActionEvent event) {
		System.out.println("Inside AddProduct Menu");
		try {
			tab = new Tab("Add Product", FXMLLoader.load(getClass().getResource("AddProduct.fxml")));
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addCustomer(ActionEvent event) {
		System.out.println("Inside AddCustomer Menu");
		try {
			tab = new Tab("Add Customer", FXMLLoader.load(getClass().getResource("AddCustomer.fxml")));
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addSupplier(ActionEvent event) {
		System.out.println("Inside AddSupplier Menu");
		try {
			tab = new Tab("Add Supplier", FXMLLoader.load(getClass().getResource("AddSupplier.fxml")));
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void buy(ActionEvent event) {
		System.out.println("Inside AddSupplier Menu");
		try {
			tab = new Tab("Buy", FXMLLoader.load(getClass().getResource("Buy.fxml")));
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sell(ActionEvent event) {
		System.out.println("Inside AddSupplier Menu");
		try {
			tab = new Tab("Sell", FXMLLoader.load(getClass().getResource("Sell.fxml")));
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void viewProduct(ActionEvent event) throws ClassNotFoundException, SQLException {
		PrintReport viewReport = new PrintReport("select p.product_id, p.product_name, p.product_model, p.product_description, b.brand_name from product p, brand b where b.brand_id=p.bid","../Report/ViewProduct.jrxml");
		viewReport.showReport();
	}
	
	public void viewCustomer(ActionEvent event) throws ClassNotFoundException, SQLException {
		PrintReport viewReport = new PrintReport("select * from Customer","../Report/ViewCustomer.jrxml");
		viewReport.showReport();
	}
	
	public void viewBrand(ActionEvent event) throws ClassNotFoundException, SQLException {
		PrintReport viewReport = new PrintReport("select * from Brand","../Report/ViewBrand.jrxml");
		viewReport.showReport();
	}
	
	public void viewSupplier(ActionEvent event) throws ClassNotFoundException, SQLException {
		PrintReport viewReport = new PrintReport("select * from Supplier","../Report/ViewSupplier.jrxml");
		viewReport.showReport();
	}
	
	public void viewDashboard(ActionEvent event) {
		System.out.println("Inside AddSupplier Menu");
		try {
			tab = new Tab("Dashboard", FXMLLoader.load(getClass().getResource("Dashboard.fxml")));
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buyCurrentMonthReport(ActionEvent event) throws ClassNotFoundException, SQLException {
		System.out.println("Inside BuyCurrentMonth Menu");
		int month = LocalDate.now().getMonthValue();
		int year = LocalDate.now().getYear();
		String startOfMonth = "\'" + year + "-"+ month + "-" + "01" +"\'";
		System.out.println("startOfMonth" + month);
		String today = "\'" + LocalDate.now().toString() + "\'";
		PrintReport viewReport = new PrintReport(
				"select sb.supplierdate, sb.supplierimei, pr.product_name, s.supplier_name, sb.supplierprice, s.supplier_mobile, sb.suppliertotal from supplierbill sb, product pr, supplier s where supplierdate>= "+ startOfMonth +"and supplierdate<= "+ today +" and sb.pid=pr.product_id and sb.sid=s.supplier_id",
				"../Report/BuyReport.jrxml");
		viewReport.showReport();
	}
	
	public void buyCustomReport(ActionEvent event) throws ClassNotFoundException, SQLException {

		try {
			tab = new Tab("Buy Custom Report", FXMLLoader.load(getClass().getResource("BuyDatePicker.fxml")));
			tabpane.getTabs().add(tab);
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sellCurrentMonthReport(ActionEvent event) throws ClassNotFoundException, SQLException {
		System.out.println("Inside SellCurrentMonth Menu");
		int month = LocalDate.now().getMonthValue();
		int year = LocalDate.now().getYear();
		String startOfMonth = "\'" + year + "-"+ month + "-" + "01" +"\'";
		System.out.println("startOfMonth" + month);
		String today = "\'" + LocalDate.now().toString() + "\'";
		PrintReport viewReport = new PrintReport(
				"select cb.customerdate, pr.product_name, cb.customerimei, c.customer_name, cb.customertotal from customerbill cb, product pr, customer c where customerdate>= "+ startOfMonth +" and customerdate<= "+ today + " and cb.pid=pr.product_id and cb.cid=c.customer_id",
				"../Report/SaleReport.jrxml");
		viewReport.showReport();
	}
	
	public void sellCustomReport(ActionEvent event) throws ClassNotFoundException, SQLException {
		
		try {
			tab = new Tab("Sell Custom Report", FXMLLoader.load(getClass().getResource("SaleDatePicker.fxml")));
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void login() {		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Login.fxml"));
			tab = new Tab("Login", loader.load());
			tabpane.getTabs().add(tab);	
			tabpane.getSelectionModel().select(tab);
			System.out.println("In Login");
			LoginController loginCon = loader.getController();
			System.out.println("Got login controller");
			loginCon.injectMainController(this);
			System.out.println("After login Controller");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Inside Init" + this);
		menuBar.setDisable(true);
		System.out.println("Menu disabled");
		this.login();
		System.out.println("After Login in Maincontroller");
	}
}
