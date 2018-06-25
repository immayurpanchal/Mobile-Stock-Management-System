package MSMS.Application;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

@Entity
public class Customer extends RecursiveTreeObject<Customer> {
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	private int customer_id;
	private String customer_name;
	private long customer_mobile;
	private String customer_add;
	private String customer_email;
	@OneToMany (mappedBy="ccustomer")
	private List<CustomerBill> ccBill = new ArrayList<>();
	
	public List<CustomerBill> getCcBill() {
		return ccBill;
	}
	public void setCcBill(List<CustomerBill> ccBill) {
		this.ccBill = ccBill;
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public long getCustomer_mobile() {
		return customer_mobile;
	}
	public void setCustomer_mobile(long customer_mobile) {
		this.customer_mobile = customer_mobile;
	}
	public String getCustomer_add() {
		return customer_add;
	}
	public void setCustomer_add(String customer_add) {
		this.customer_add = customer_add;
	}
	public String getCustomer_email() {
		return customer_email;
	}
	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}
	@Override
	public String toString() {
		return "Customer [customer_id=" + customer_id + ", customer_name=" + customer_name + ", customer_mobile="
				+ customer_mobile + ", customer_add=" + customer_add + ", customer_email=" + customer_email
				+ ", ccBill=" + ccBill + "]";
	}
}
