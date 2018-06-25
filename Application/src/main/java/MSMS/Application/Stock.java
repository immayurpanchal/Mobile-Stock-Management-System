package MSMS.Application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

@Entity
public class Stock extends RecursiveTreeObject<Stock>{
	
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	private int stock_id;
	private int product_qty;
	@ManyToOne
	@JoinColumn (name="pid")
	private Product sproduct;
	
	public int getStock_id() {
		return stock_id;
	}
	public void setStock_id(int stock_id) {
		this.stock_id = stock_id;
	}
	public int getProduct_qty() {
		return product_qty;
	}
	public void setProduct_qty(int product_qty) {
		this.product_qty = product_qty;
	}
	public Product getSproduct() {
		return sproduct;
	}
	public void setSproduct(Product sproduct) {
		this.sproduct = sproduct;
	}
	
	@Override
	public String toString() {
		return "Stock [stock_id=" + stock_id + ", product_qty=" + product_qty + ", sproduct=" + sproduct + "]";
	}
	
	
}
