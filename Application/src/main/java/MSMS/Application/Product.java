package MSMS.Application;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

@Entity
public class Product extends RecursiveTreeObject<Product> {
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	private int product_id;
	private String product_name;
	private String product_model;
	private String product_description;
	@ManyToOne
	@JoinColumn (name="bid")
	private Brand brand;
	@OneToMany (mappedBy="product")
	private List<SupplierBill> psBill = new ArrayList<>();
	@OneToMany (mappedBy="sproduct")
	private List<Stock> stocks = new ArrayList<>();
	@OneToMany (mappedBy="cproduct")
	private List<CustomerBill> pcustBill = new ArrayList<>();
	
	public List<Stock> getStocks() {
		return stocks;
	}
	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}
	public List<CustomerBill> getPcBill() {
		return pcustBill;
	}
	public void setPcBill(List<CustomerBill> pcBill) {
		this.pcustBill = pcBill;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	/*public List<Stock> getStock() {
		return stock;
	}
	public void setStock(List<Stock> stock) {
		this.stock = stock;
	}*/
	/*public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}*/
	public List<SupplierBill> getPsBill() {
		return psBill;
	}
	public void setPsBill(List<SupplierBill> psBill) {
		this.psBill = psBill;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public List<CustomerBill> getPcustBill() {
		return pcustBill;
	}
	public void setPcustBill(List<CustomerBill> pcustBill) {
		this.pcustBill = pcustBill;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_model() {
		return product_model;
	}
	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}
	public String getProduct_description() {
		return product_description;
	}
	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}
	@Override
	public String toString() {
		return "Product [product_id=" + product_id + ", product_name=" + product_name + ", product_model="
				+ product_model + ", product_description=" + product_description + ", psBill=" + psBill + "]";
	}
}
