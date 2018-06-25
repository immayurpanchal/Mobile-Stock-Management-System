package MSMS.Application;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;

@Entity
public class Supplier extends RecursiveTreeObject<Supplier> {
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	private int supplier_id;
	@OneToMany (mappedBy="supplier")
	private List<SupplierBill> ssBill = new ArrayList<>();
	private String supplier_name;
	private long supplier_mobile;
	private String supplier_add;
	private String supplier_gst;
	
	public List<SupplierBill> getSsBill() {
		return ssBill;
	}
	public void setSsBill(List<SupplierBill> ssBill) {
		this.ssBill = ssBill;
	}
	public int getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public long getSupplier_mobile() {
		return supplier_mobile;
	}
	public void setSupplier_mobile(long supplier_mobile) {
		this.supplier_mobile = supplier_mobile;
	}
	public String getSupplier_add() {
		return supplier_add;
	}
	public void setSupplier_add(String supplier_add) {
		this.supplier_add = supplier_add;
	}
	public String getSupplier_gst() {
		return supplier_gst;
	}
	public void setSupplier_gst(String supplier_gst) {
		this.supplier_gst = supplier_gst;
	}
	
	
	public String getSimpleSupplierName() {
		final SimpleStringProperty myName = new SimpleStringProperty(this.getSupplier_name());
		/*myName.set(this.getSupplier_name());*/
		return myName.get();
	}
	
	@Override
	public String toString() {
		return "Supplier [supplier_id=" + supplier_id + ", supplier_name=" + supplier_name + ", supplier_mobile="
				+ supplier_mobile + ", supplier_add=" + supplier_add + ", supplier_gst=" + supplier_gst + ", ssBill="
				+ ssBill + "]";
	}
}
