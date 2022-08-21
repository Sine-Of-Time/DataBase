package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserInput {
	static String[] info=new String[3];
	
	public static String[] display() {
		Stage stage=new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		
		BorderPane root=new BorderPane();
		GridPane center=new GridPane();
		Label lbl=new Label("Please Enter Book Info.");
		Label lblT=new Label("Title:");
		Label lblA=new Label("Author:");
		Label lblI=new Label("ISBN:");
		TextField txtT=new TextField();
		TextField txtA=new TextField();
		TextField txtI=new TextField();
		Button bttm=new Button("Submit");
		//bttm.setPadding(new Insets(20));
		bttm.setDisable(true);
		
		txtT.setOnKeyReleased(e->{
			if((!txtT.getText().isEmpty()||!txtT.getText().isBlank())&&(!txtA.getText().isEmpty()||!txtA.getText().isBlank())&&(!txtI.getText().isEmpty()||!txtI.getText().isBlank())) {
				bttm.setDisable(false);
			}else {
				bttm.setDisable(true);
			}
		});
		

		txtA.setOnKeyReleased(e->{
			if((!txtT.getText().isEmpty()||!txtT.getText().isBlank())&&(!txtA.getText().isEmpty()||!txtA.getText().isBlank())&&(!txtI.getText().isEmpty()||!txtI.getText().isBlank())) {
				bttm.setDisable(false);
			}else {
				bttm.setDisable(true);
			}
		});
	
		txtI.setOnKeyReleased(e->{
			if((!txtT.getText().isEmpty()||!txtT.getText().isBlank())&&(!txtA.getText().isEmpty()||!txtA.getText().isBlank())&&(!txtI.getText().isEmpty()||!txtI.getText().isBlank())) {
				bttm.setDisable(false);
			}else {
				bttm.setDisable(true);
			}
		});
				
		bttm.setOnAction(e->{
			boolean isDigits=true;
			//Checking if ISBN is an int.
			for(int i=0;i<txtI.getText().length()-1;i++) {
				int z=txtI.getText().charAt(i)-48;
				if((z>=-1&&9>=z)&&i<19) {//i<19 ensure its a long.
					
					continue;
				}else {
					isDigits=false;
					break;
				}
			}
			
			if(isDigits) {
				info[0]=txtT.getText();
				info[1]=txtA.getText();
				info[2]=txtI.getText();
				stage.close();
			}else {
				Alert alert=new Alert(AlertType.ERROR);
				alert.setTitle("Type Mismatch Error!");
				alert.setHeaderText("ISBN Field Type Mismatch Error!");
				alert.setContentText("The textfield for ISBN can only accept a number. \nPlease change input to continue.");
				txtI.setText("");
				alert.showAndWait();
			}
		});
		
		
		
		
		center.add(lbl,0,0,4,1);
		center.add(lblT,1,1);
		center.add(lblA,1,2);
		center.add(lblI,1,3);
		center.add(txtT,2,1);
		center.add(txtA,2,2);
		center.add(txtI,2,3);
		center.setAlignment(Pos.CENTER);
		center.setHgap(10);
		center.setVgap(10);
		root.setCenter(center);
		root.setBottom(bttm);
		root.setPadding(new Insets(3));
		BorderPane.setAlignment(bttm,Pos.CENTER);
		
		Scene scene=new Scene(root,230,165);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Add File");
		Image icon=new Image("P2_Icon.png");
		stage.getIcons().add(icon);
		stage.showAndWait();
		return info;
	}
}