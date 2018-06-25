package MSMS.Application;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SupplierBill {
	
	@Id
	private long supplierIMEI;
	@ManyToOne
	@JoinColumn (name="sid")
	private Supplier supplier;
	@ManyToOne
	@JoinColumn (name="pid")
	private Product product; 
	private double supplierSGST;
	private double supplierCGST; 
	private LocalDate supplierDate;
	private double supplierTotal;
	private double supplierPrice;
	public long getSupplierIMEI() {
		return supplierIMEI;
	}
	public void setSupplierIMEI(long supplierIMEI) {
		this.supplierIMEI = supplierIMEI;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getSupplierSGST() {
		return supplierSGST;
	}
	public void setSupplierSGST(double supplierSGST) {
		this.supplierSGST = supplierSGST;
	}
	public double getSupplierCGST() {
		return supplierCGST;
	}
	public void setSupplierCGST(double supplierCGST) {
		this.supplierCGST = supplierCGST;
	}
	public LocalDate getSupplierDate() {
		return supplierDate;
	}
	public void setSupplierDate(LocalDate supplierDate) {
		this.supplierDate = supplierDate;
	}
	public double getSupplierTotal() {
		return supplierTotal;
	}
	public void setSupplierTotal(double supplierTotal) {
		this.supplierTotal = supplierTotal;
	}
	public double getSupplierPrice() {
		return supplierPrice;
	}
	public void setSupplierPrice(double supplierPrice) {
		this.supplierPrice = supplierPrice;
	}
	@Override
	public String toString() {
		return "SupplierBill [supplierIMEI=" + supplierIMEI + ", supplier=" + supplier + ", product=" + product
				+ ", supplierSGST=" + supplierSGST + ", supplierCGST=" + supplierCGST + ", supplierDate=" + supplierDate
				+ ", supplierTotal=" + supplierTotal + ", supplierPrice=" + supplierPrice + "]";
	}
}
