package MSMS.Application.Resources;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;


public class ViewProductController {
	@FXML private TableView<ArrayList<TableRow<HBox>>> table;
	
	public void search(ActionEvent event) {
		System.out.println("Clicked");
		ArrayList<TableRow<HBox>> al = new ArrayList<>();
		TableRow<HBox> row = new TableRow<>();
		HBox hbox = new HBox();
		Label myLabel = new Label("Mayur");
		hbox.setUserData(myLabel);
		al.add(row);
		table.setUserData(al);
		System.out.println("Succeed");
	}
}
