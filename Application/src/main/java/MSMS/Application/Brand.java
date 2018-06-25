package MSMS.Application;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

@Entity
public class Brand extends RecursiveTreeObject<Brand>{
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	private int brand_id;
	private String brand_name;
	@OneToMany (mappedBy="brand")
	private List<Product> products;
	
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public int getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	@Override
	public String toString() {
		return "Brand [brand_id=" + brand_id + ", brand_name=" + brand_name + ", products=" + products + "]";
	}
}
