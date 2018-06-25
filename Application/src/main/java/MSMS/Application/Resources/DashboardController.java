package MSMS.Application.Resources;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ibm.icu.text.DecimalFormat;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import MSMS.Application.Stock;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.Callback;

public class DashboardController implements Initializable {
	@FXML private PieChart currentBrandPieChart;
	@FXML private Label currentSell;
	@FXML private Label currentPurchase;
	@FXML private JFXTreeTableView<Stock> stockTreeView;
	
	EntityManagerFactory emf;
	EntityManager em;

	public DashboardController() {
	}
	
	public void getCurrentSell() {
		String curDate = "\'" + LocalDate.now().toString() + "\'";
		
		em.getTransaction().begin();
		Object sell= em.createQuery("SELECT SUM(customerTotal) from CustomerBill where customerDate="+curDate).getSingleResult();
		em.getTransaction().commit();
		
		Double amount = (Double)sell;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
				
		if(sell==null) {
			currentSell.setText("0.0");
			currentSell.setVisible(true);
		}
		else {
			currentSell.setText(df.format(amount).toString());
			currentSell.setVisible(true);
		}
	}
	
	public void getCurrentPurchase() {
		String curDate = "\'" + LocalDate.now().toString() + "\'";
		
		em.getTransaction().begin();
		Object purchase= em.createQuery("SELECT SUM(supplierTotal) from SupplierBill where supplierDate="+curDate).getSingleResult();
		em.getTransaction().commit();
		
		Double amount = (Double) purchase;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		if(purchase==null) {
			currentPurchase.setText("0.0");
			currentPurchase.setVisible(true);
		}else {
			currentPurchase.setText(df.format(amount).toString());
			currentPurchase.setVisible(true);
		}
	}
	
	public void getCurrentBrandPieChart() {
		String curDate = "\'" + LocalDate.now() + "\'";
		
		em.getTransaction().begin();
		@SuppressWarnings("unchecked")
		List<Object[]> result = em.createQuery(
				"select b.brand_name, count(b.brand_name) from Product pr, CustomerBill cb, Brand b where cb.customerDate = "
						+ curDate + " and cb.cproduct=pr.product_id and pr.brand=b.brand_id group by b.brand_name")
				.getResultList();
		em.getTransaction().commit();

		ArrayList<PieChart.Data> pieChartArrayList = new ArrayList<>();
		result.stream().forEach((record) -> {
			System.out.println(record[0] + "..." + record[1]);
			pieChartArrayList.add(new PieChart.Data(record[0].toString(), Double.parseDouble(record[1].toString())));
		});
		
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(pieChartArrayList);

		currentBrandPieChart.setData(pieChartData);
	}
	
	@SuppressWarnings("unchecked")
	public void getCurrentStock() {
		// Step 1 : Create Columns and Add column in the table view
		JFXTreeTableColumn<Stock, Integer> stockQty= new JFXTreeTableColumn<>("Qty");
		JFXTreeTableColumn<Stock, String> productName= new JFXTreeTableColumn<>("Name");
		JFXTreeTableColumn<Stock, String> productModel= new JFXTreeTableColumn<>("Model");
		JFXTreeTableColumn<Stock, String> productBrand= new JFXTreeTableColumn<>("Brand");

		stockTreeView.getColumns().addAll(productBrand, productName, productModel, stockQty);

		// Step 1.1 : Get the data from the database
		ArrayList<Stock> stockList = new ArrayList<>();

		em.getTransaction().begin();
		stockList = (ArrayList<Stock>) em.createQuery("from Stock").getResultList();
		em.getTransaction().commit();

		// Step 2 : Create Observable list which contains your data
		ObservableList<Stock> ols = FXCollections.observableArrayList(stockList);

		// Step 3: Create Each row and SimpleStringProperty contains the name of the
		// Product class instance variables.		
		stockQty.setCellValueFactory(
				new TreeItemPropertyValueFactory<>(new SimpleStringProperty("product_qty").get()));
				
		productName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Stock,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Stock, String> param) {
				return new SimpleStringProperty(param.getValue().getValue().getSproduct().getProduct_name());
			}
		});
		
		productModel.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Stock,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Stock, String> param) {
				return new SimpleStringProperty(param.getValue().getValue().getSproduct().getProduct_model());
			}
		});
		
		productBrand.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Stock,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Stock, String> param) {
				return new SimpleStringProperty(param.getValue().getValue().getSproduct().getBrand().getBrand_name());
			}
		});
		
		// Step 4 : Create Recursive Tree item and also don't forget to extend in
		// Product Class
		TreeItem<Stock> root = new RecursiveTreeItem<>(ols, RecursiveTreeObject::getChildren);

		// Step 5 : Set tree view and Done
		stockTreeView.setRoot(root);
		stockTreeView.setShowRoot(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		emf = Persistence.createEntityManagerFactory("pu");
		em = emf.createEntityManager();
		
		this.getCurrentPurchase();
		this.getCurrentSell();
		this.getCurrentBrandPieChart();
		this.getCurrentStock();
	}
}
