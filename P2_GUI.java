package application;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class P2_GUI extends Application {
	DataCenter data=DataCenter.getInstance();
	ObservableList<Book> tvData=FXCollections.observableArrayList(data.getAllBooks());
	ObservableList<Node> avdStatData=null;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		//Init objects
		
		//Left Nodes
		SplitPane root=new SplitPane();
		SplitPane rightRoot=new SplitPane();
		BorderPane left=new BorderPane();
		Button btnA=new Button("Add");
		Button btnR=new Button("Remove");
		GridPane leftBttm =new GridPane();
		
		//Right bttm nodes
		GridPane rightBttm =new GridPane();
		Label filter=new Label("Filter By:");
		Label keyLbl=new Label("Enter Key:");
		TextField filterTxt=new TextField();
		ChoiceBox<String> choiceBox=new ChoiceBox<>();
		choiceBox.setValue("0.ISBN");
		choiceBox.getItems().add("0.ISBN");
		choiceBox.getItems().add("1.Title");
		choiceBox.getItems().add("2.Author");
		Button subFilter=new Button("Submit");		
		
		//Right Top nodes
		BorderPane rightTop=new BorderPane();
		TabPane tp=new TabPane();
		rightTop.setPadding(new Insets(5));
		tp.setPadding(new Insets(20));
		
		
		//Setting up table view for left side.
		TableView<Book> tbl =new TableView<>();
		TableColumn<Book, String> col1=new TableColumn<>("Title");
		col1.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<Book, String> col2=new TableColumn<>("Author");
		col2.setCellValueFactory(new PropertyValueFactory<>("author"));
		TableColumn<Book, String> col3=new TableColumn<>("ISBN");
		col3.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		TableColumn<Book, String> col4=new TableColumn<>("Unique Word Count");
		col4.setCellValueFactory(new PropertyValueFactory<>("individualWordCount"));
		
		col1.setPrefWidth(129);
		col2.setPrefWidth(129);
		col3.setPrefWidth(55);
		col4.setPrefWidth(130);
		tbl.getColumns().add(col1);
		tbl.getColumns().add(col2);
		tbl.getColumns().add(col3);
		tbl.getColumns().add(col4);
		tbl.setItems(tvData);
		tbl.setPadding(new Insets(5));
		
		//Setting up pic chart.
		PieChart pie=new PieChart();
		tbl.getSelectionModel().selectFirst();
		if(!tvData.isEmpty())updatePie(pie,tbl.getSelectionModel().getSelectedItem());
		
		//Setting up advanced tbl stats
		TableView<Node> tblAdvStats=new TableView<>();
		TableColumn<Node, String> colA1=new TableColumn<>("Word");
		TableColumn<Node, String> colA2=new TableColumn<>("Occurance's");
		TableColumn<Node, String> colA3=new TableColumn<>("Percent of words (%)");
		colA1.setCellValueFactory(new PropertyValueFactory<>("word"));
		colA2.setCellValueFactory(new PropertyValueFactory<>("occurances"));
		colA3.setCellValueFactory(new PropertyValueFactory<>("intpercent"));
	
		colA1.setPrefWidth(185);
		colA2.setPrefWidth(80);
		colA3.setPrefWidth(150);
		tblAdvStats.getColumns().add(colA1);
		tblAdvStats.getColumns().add(colA2);
		tblAdvStats.getColumns().add(colA3);
		tblAdvStats.setItems(avdStatData);
		tblAdvStats.setPadding(new Insets(5));
		
		if(!tvData.isEmpty())updateStatTable(tblAdvStats,tbl.getSelectionModel().getSelectedItem(),avdStatData);
		
		//Add Function
		btnA.setOnAction(e->{//So we preform file checking opperations in the GUI, init new Book in add parameter.
			String[] bookInfo=UserInput.display();
			FileChooser filePick=new FileChooser();
			File selFile=filePick.showOpenDialog(stage);
			addToDataCenter(selFile,tbl,bookInfo);
		});
		
		//Remove function
		btnR.setOnAction(e->{//So get seleted book, pass to dc and remove there.
			if(data.remove(tbl.getSelectionModel().getSelectedItem())) {
				tvData=FXCollections.observableArrayList(data.getAllBooks());
				tbl.setItems(tvData);
			}else {
				Alert alert=new Alert(AlertType.ERROR);
				alert.setTitle("File remove Error!");
				alert.setHeaderText("File could not be removed from data base!");
				alert.setContentText("Please correct any incorrect infomation.");
				alert.showAndWait();
			}
			
		});
		
		//Filter main list
		subFilter.setOnAction(e->
			updateTableView(choiceBox.getValue().charAt(0)-48,filterTxt.getText(),tbl)
		);
		
		tbl.setOnMouseClicked(e->{//This will refresh top right charts.
			if(!tvData.isEmpty())updatePie(pie,tbl.getSelectionModel().getSelectedItem());
			if(avdStatData!=null)avdStatData.clear();
			if(!tvData.isEmpty())updateStatTable(tblAdvStats,tbl.getSelectionModel().getSelectedItem(),avdStatData);
		});
		
		//Wrapping graphs
		BorderPane pieWrap=new BorderPane();
		BorderPane tblWrap=new BorderPane();
		pieWrap.setPadding(new Insets(10));
		tblWrap.setPadding(new Insets(10));
		pieWrap.setCenter(pie);
		pieWrap.setStyle("-fx-background-color: #FFFFFF");
		tblWrap.setCenter(tblAdvStats);
		
		//Setting nodes in splitpane.
		tp.getTabs().add(new Tab("Pie Chart",pieWrap));
		tp.getTabs().add(new Tab("Advanced Stats",tblWrap));
		tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		rightTop.setCenter(tp);

		//Adding nodes to and altering right bttm.
		rightBttm.setAlignment(Pos.CENTER_LEFT);
		rightBttm.setHgap(10);
		rightBttm.setVgap(8);
		rightBttm.setPadding(new Insets(20));
		rightBttm.add(filter, 0, 0);
		rightBttm.add(choiceBox, 1,0);
		rightBttm.add(keyLbl, 2, 0);
		rightBttm.add(filterTxt, 3, 0);
		rightBttm.add(subFilter, 4, 0);
		rightBttm.setPrefSize(50,50);	
		
		//Setting up and adding nodes to right root.
		rightRoot.setOrientation(Orientation.VERTICAL);
		rightRoot.setDividerPositions(0.9f, 0.6f, 0.9f);
		rightRoot.getItems().add(rightTop);
		rightRoot.getItems().add(rightBttm);
		
		//Setting up and adding nodes the left.
		leftBttm.setPadding(new Insets(10));
		leftBttm.setAlignment(Pos.CENTER);
		leftBttm.setHgap(10);
		leftBttm.setVgap(8);
		leftBttm.add(btnA,0,0);
		leftBttm.add(btnR,1,0);
		
		//Adding nodes to roots
		left.setPadding(new Insets(20));
		left.setCenter(tbl);
		left.setBottom(leftBttm);
		root.getItems().add(left);
		root.getItems().add(rightRoot);
		
		Image icon=new Image("P2_Icon.png");
		Scene scene=new Scene(root,1000,500);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.getIcons().add(icon);
		stage.setOnCloseRequest(e->data.dump());
		stage.setTitle("Book Database");
		stage.show();
	}

	public boolean updateTableView(int choice,String key,TableView<Book> tbl) {
		if(tbl==null||key==null)return false;
		tvData=FXCollections.observableArrayList(data.getAllBooks());
		if(tvData.isEmpty())return false;
		if((choice>2||choice<0)||key.equals(null))return false;
		switch(choice) {
			case 0://isbn
				if(key.isEmpty()||key.equals("")||key.isBlank()) {
					Collections.sort(tvData);
					tbl.setItems(tvData);
					return true;
				}else {
					ArrayList<Book> t=new ArrayList<>();
					for(int i=0;i<tvData.size();i++) {
						if(tvData.get(i).getIsbn()==Integer.valueOf(key)) {
							t.add(tvData.get(i));
							break;
						}
					}
					tvData=FXCollections.observableList(t);
					tbl.setItems(tvData);
					return true;
				}
			case 1://Title	
					Comparator<Book> sortByTitle =new Comparator<Book>() {
						@Override
						public int compare(Book o1, Book o2) {
							char b1=o1.getTitle().charAt(0);
							char b2=o1.getTitle().charAt(0);
							return b2-b1;//Orders such that Caps are fist, and in abcd order.
						}
					};
				if(key.isEmpty()||key.equals("")||key.isBlank()) {		
					Collections.sort(tvData,sortByTitle);
					tbl.setItems(tvData);
					return true;
				}else {
					ArrayList<Book> t=new ArrayList<>();
					for(int i=0;i<tvData.size();i++) {
						if(tvData.get(i).getTitle().contains(key)) {
							t.add(tvData.get(i));
						}
					}
					tvData=FXCollections.observableList(t);
					Collections.sort(tvData,sortByTitle);
					tbl.setItems(tvData);
					return true;
			}
		   default://Author 	
				Comparator<Book> sortByAuthor=new Comparator<Book>() {
					@Override
					public int compare(Book o1, Book o2) {
						char b1=o1.getAuthor().charAt(0);
						char b2=o1.getAuthor().charAt(0);
						return b2-b1;//Orders such that Caps are fist, and in abcd order.
					}
				};
				if(key.isEmpty()||key.equals("")||key.isBlank()) {		
					Collections.sort(tvData,sortByAuthor);
					tbl.setItems(tvData);
					return true;
				}else {
					ArrayList<Book> t=new ArrayList<>();
					for(int i=0;i<tvData.size();i++) {
						if(tvData.get(i).getAuthor().contains(key)) {
							t.add(tvData.get(i));
						}
					}
					tvData=FXCollections.observableList(t);
					Collections.sort(tvData,sortByAuthor);
					tbl.setItems(tvData);
					return true;
				}	
				
		}
	}
	
	public boolean updatePie(PieChart pie,Book x) {
		if(pie==null||x==null)return false;
		ObservableList<PieChart.Data> listP = FXCollections.observableArrayList();
		pie.setPadding(new Insets(10));
			pie.setTitle("Top 10 words in "+x.getTitle());
			Node[] arrN=x.getTopTenWords();
				if(arrN!=null) {
					for(int k=0;k<arrN.length;k++) {
						if(arrN[k]!=null&&arrN[k].getWord()!=null) {
							listP.add(new PieChart.Data(arrN[k].getWord(),roundToThreeDecimal(arrN[k].getPercent())));
						}	
					}
				}
		pie.setData(listP);
		return true;
	}
	
	public boolean updateStatTable(TableView<Node> tbl,Book x,ObservableList<Node> avdStatData) {
		if(x==null||x.getTopTenWords()==null/*||avdStatData==null*/)return false;
		avdStatData=FXCollections.observableArrayList(x.getTopTenWords());
		if(tbl!=null) {
			if(tbl.getItems()!=null) {
				ObservableList<Node> temp=tbl.getItems();
				for(int i=0;i<temp.size();i++) {
					if(temp==null||temp.get(i)==null||temp.get(i).getWord()==null||temp.get(i).getWord().equals(null)) {
						temp.remove(i);
						i--;
					}
				}
			}
		}	
		
		for(int i=0;i<avdStatData.size();i++) {
			if(avdStatData.get(i)==null||avdStatData.get(i).getWord()==null||avdStatData.get(i).getWord().equals(null)) {
				avdStatData.remove(i);
				i--;
			}
		}
		
		tbl.setItems(avdStatData);
		return true;
	}
	
	public int roundToThreeDecimal(double y){
		y=y*100;
		int n=(int)((y%1)*10);
		if(n>=5)y=y+1-(n/10.0);
		else y=(int)y;
		return(int)y;
	}
	
	public boolean addToDataCenter(File selFile,TableView<Book> tbl,String[] bookInfo) {
		String s0=selFile.getAbsolutePath().substring(selFile.getAbsolutePath().lastIndexOf('.'));
		boolean isTxt=s0.contains("txt");
		
		if(selFile.exists()&&selFile.isFile()&&selFile!=null&&isTxt) {
			if(data.add(new FileWrapper(selFile,bookInfo))){
				tvData=FXCollections.observableArrayList(data.getAllBooks());
				tbl.setItems(tvData);
				return true;
			}else {
				Alert alert=new Alert(AlertType.ERROR);
				alert.setTitle("File add Error!");
				alert.setHeaderText("File could not be added to data base!");
				alert.setContentText("Please correct any incorrect infomation.");
				alert.showAndWait();
				return false;
			}
		}else {
			if(!selFile.exists()) {
				Alert alert=new Alert(AlertType.ERROR);
				alert.setTitle("File Read Error!");
				alert.setHeaderText("File selected does not exist!");
				alert.setContentText("File path \""+selFile.getAbsolutePath()+"\" is invalid. \nYou must enter a valid path to read from.");
				alert.showAndWait();
				return false;
			}else if(!selFile.isFile()) {
				Alert alert=new Alert(AlertType.ERROR);
				alert.setTitle("File Read Error!");
				alert.setHeaderText("File selected is not a file!");
				alert.setContentText("File path \""+selFile.getAbsolutePath()+"\" is invalid. \nYou must enter a file path to read from.");
				alert.showAndWait();
				return false;
			}else if(!isTxt) {
				Alert alert=new Alert(AlertType.ERROR);
				alert.setTitle("File Read Error!");
				alert.setHeaderText("File selected is not a text file!");
				alert.setContentText("File path \""+selFile.getAbsolutePath()+"\" is invalid. \nYou must enter a file path with a text file to read from.");
				alert.showAndWait();
				return false;
			}else{
				Alert alert=new Alert(AlertType.ERROR);
				alert.setTitle("File Read Error!");
				alert.setHeaderText("File selected can not be read!");
				alert.setContentText("File path \""+selFile.getAbsolutePath()+"\" is invalid. \nYou must enter a new path to read from.");
				alert.showAndWait();
				return false;
			}
		}
	}
}