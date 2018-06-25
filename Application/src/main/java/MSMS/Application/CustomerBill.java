package MSMS.Application;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CustomerBill {
	@Id
	private long customerIMEI;
	private double customerSGST;
	private double customerCGST;
	private double customerTotal;
	private double customerPrice;
	private LocalDate customerDate;
	@ManyToOne
	@JoinColumn (name="cid")
	private Customer ccustomer;
	@ManyToOne 
	@JoinColumn (name="pid")
	private Product cproduct;
	public long getCustomerIMEI() {
		return customerIMEI;
	}
	public void setCustomerIMEI(long customerIMEI) {
		this.customerIMEI = customerIMEI;
	}
	public double getCustomerSGST() {
		return customerSGST;
	}
	public void setCustomerSGST(double customerSGST) {
		this.customerSGST = customerSGST;
	}
	public double getCustomerCGST() {
		return customerCGST;
	}
	public void setCustomerCGST(double customerCGST) {
		this.customerCGST = customerCGST;
	}
	public double getCustomerTotal() {
		return customerTotal;
	}
	public void setCustomerTotal(double customerTotal) {
		this.customerTotal = customerTotal;
	}
	public double getCustomerPrice() {
		return customerPrice;
	}
	public void setCustomerPrice(double customerPrice) {
		this.customerPrice = customerPrice;
	}
	public LocalDate getCustomerDate() {
		return customerDate;
	}
	public void setCustomerDate(LocalDate customerDate) {
		this.customerDate = customerDate;
	}
	public Customer getCcustomer() {
		return ccustomer;
	}
	public void setCcustomer(Customer ccustomer) {
		this.ccustomer = ccustomer;
	}
	public Product getCproduct() {
		return cproduct;
	}
	public void setCproduct(Product cproduct) {
		this.cproduct = cproduct;
	}
	@Override
	public String toString() {
		return "CustomerBill [customerIMEI=" + customerIMEI + ", customerSGST=" + customerSGST + ", customerCGST="
				+ customerCGST + ", customerTotal=" + customerTotal + ", customerPrice=" + customerPrice
				+ ", customerDate=" + customerDate + ", ccustomer=" + ccustomer + ", cproduct=" + cproduct + "]";
	}
}
