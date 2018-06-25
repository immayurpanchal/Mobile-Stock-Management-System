package MSMS.Application.Resources;

import java.sql.SQLException;

import com.jfoenix.controls.JFXDatePicker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BuyDatePickerController {
	@FXML private JFXDatePicker startDate;
	@FXML private JFXDatePicker endDate;
	
	public void buyPickDate(ActionEvent event) throws ClassNotFoundException, SQLException {
		String sDate = "\'" + startDate.getValue() + "\'";
		String eDate = "\'" + endDate.getValue() + "\'";

		PrintReport viewReport = new PrintReport(
				"select sb.supplierdate, sb.supplierimei, pr.product_name, s.supplier_name, sb.supplierprice, s.supplier_mobile, sb.suppliertotal from supplierbill sb, product pr, supplier s where supplierdate>= "
						+ sDate + " and supplierdate<= " + eDate + " and sb.pid=pr.product_id and sb.sid=s.supplier_id",
				"../Report/BuyReport.jrxml");
		viewReport.showReport();
	}
}
