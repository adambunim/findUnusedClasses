package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ClassesPane extends BorderPane{

	private Button browseButton;
	private Button refButton;
	private Button refAllButton;
	private Button refInWorkspaceForSelectedButton;
	private Button refInWorkspaceButton;
	private List<ClassCode> classCodeList;
	private TableView<ClassCodeBean> tableView;
	private Stage primaryStage;
	private Label classNameLabel;
	private TextField simpleNameTf;
	private Button searchButton;
	private Label referencesLabel;
	private Label referencesInWorkspaceLabel;
	
	public ClassesPane(Stage primaryStage){
		this.primaryStage= primaryStage;
		createBody();
	}
	
	private void createBody(){
		VBox middlePane= new VBox();		
		setPadding(new Insets(10,10,10,10));
		middlePane.setSpacing(10);
		setMargin(middlePane, new Insets(10,10,10,10));
		HBox buttonsHbox= new HBox();
		browseButton= new Button("Browse");
		refButton= new Button("Ref selected");
		refAllButton= new Button("Ref All");
		refInWorkspaceForSelectedButton= new Button("Ref In Workspace Selected");
		refInWorkspaceButton= new Button("All Ref In Workspace");
		createTable();
		classNameLabel= new Label();
		simpleNameTf= new TextField();
		searchButton= new Button("Search");
		referencesLabel= new Label();
		referencesInWorkspaceLabel= new Label();
		classCodeList= new ArrayList<>();
		buttonsHbox.setSpacing(10);
		buttonsHbox.getChildren().addAll(browseButton, refButton, refAllButton, refInWorkspaceForSelectedButton, refInWorkspaceButton);				
		browseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try{
					browsePressed();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		refButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ClassCodeBean selectedBean= tableView.getSelectionModel().getSelectedItem();
				if(selectedBean==null){
					return;
				}
				selectedBean.getClassCode().fillReferences();
				fillTable();
			}
		});
		refAllButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				for(ClassCodeBean iter : tableView.getItems()){
					iter.getClassCode().fillReferences();
				}
				fillTable();
			}
		});
		refInWorkspaceForSelectedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ClassCodeBean selectedBean= tableView.getSelectionModel().getSelectedItem();
				if(selectedBean==null){
					return;
				}
				fillReferencesInWorkspace(selectedBean.getClassCode());
				fillTable();
			}
		});
		refInWorkspaceButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				fillReferencesInWorkspace();
				fillTable();
			}
		});
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				search();
			}
		});
		HBox namesHbox= new HBox();
		namesHbox.setSpacing(10);
		namesHbox.getChildren().addAll(classNameLabel, simpleNameTf, searchButton);
		
		HBox referencesHbox= new HBox();
		referencesHbox.setSpacing(10);
		ScrollPane scroll= new ScrollPane();
		referencesHbox.getChildren().addAll(referencesLabel, new Separator(Orientation.VERTICAL), referencesInWorkspaceLabel);
		scroll.setContent(referencesHbox);
		scroll.setPrefHeight(200);
		
		middlePane.getChildren().addAll(tableView, namesHbox);
		setTop(buttonsHbox);
		setCenter(middlePane);
		setBottom(scroll);
	}
	
	@SuppressWarnings("unchecked")
	private void createTable(){
		tableView= new TableView<>();
		TableColumn<ClassCodeBean,String> nameCol = new TableColumn<>("Simple Name");	
		TableColumn<ClassCodeBean,String> groupCol = new TableColumn<>("Group");	
		TableColumn<ClassCodeBean,String> referencesCol = new TableColumn<>("References");
		TableColumn<ClassCodeBean,String> referencesInWorkspaceCol = new TableColumn<>("References in Workspace");
		nameCol.setPrefWidth(200);
		groupCol.setPrefWidth(100);
		referencesCol.setPrefWidth(150);
		referencesInWorkspaceCol.setPrefWidth(150);
		nameCol.setCellValueFactory(new PropertyValueFactory<ClassCodeBean,String>("simpleName"));		
		groupCol.setCellValueFactory(new PropertyValueFactory<ClassCodeBean,String>("group"));		
		referencesCol.setCellValueFactory(new PropertyValueFactory<ClassCodeBean,String>("references"));	
		referencesInWorkspaceCol.setCellValueFactory(new PropertyValueFactory<ClassCodeBean,String>("referencesInWorkspace"));
		tableView.getColumns().addAll(nameCol, groupCol, referencesCol, referencesInWorkspaceCol);
		tableView.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				selectionChanged();
			}
		});
		tableView.setMinHeight(500);
		tableView.setPrefHeight(500);
	}
	
	private void selectionChanged(){
		ClassCodeBean selectedItem= tableView.getSelectionModel().getSelectedItem();
		if(selectedItem==null){
			classNameLabel.setText("");
			simpleNameTf.setText("");
			referencesLabel.setText("");
			referencesInWorkspaceLabel.setText("");
		}
		else{
			ClassCode classCode= selectedItem.getClassCode();
			simpleNameTf.setText(classCode.getSimpleName());
			classNameLabel.setText(classCode.getClassName());			
			String referencesText= "";
			for(String s : classCode.getReferences()){
				referencesText+=s+"\n";
			}		
			referencesLabel.setText(referencesText);
			String referencesInWorkspaceText= "";
			for(String s : classCode.getReferencesInWorspace()){
				referencesInWorkspaceText+=s+"\n";
			}
			referencesInWorkspaceLabel.setText(referencesInWorkspaceText);
		}
	}
	
	private void browsePressed() throws Exception{
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose your workspace");
		File defaultDirectory = new File("C:/Users/adamb/workspace/5.7.1/");
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(primaryStage);
		if(selectedDirectory==null){
			return;
		}
		classCodeList= PackageUtil.getClassCoseList(selectedDirectory);	
		fillTable();
	}
	
	private void fillTable(){
		List<ClassCodeBean> beanList= new ArrayList<>();
		for(ClassCode classCode : classCodeList){
			beanList.add(new ClassCodeBean(classCode));
		}
		tableView.getItems().clear();
		tableView.getItems().addAll(beanList);
	}
		
	private void fillReferencesInWorkspace(){
		for(ClassCode c : classCodeList){
			fillReferencesInWorkspace(c);
		}
	}
		
	private void fillReferencesInWorkspace(ClassCode classCode){
		for(ClassCode c : classCodeList){
			if(c==classCode){
				continue;
			}
			if(classCode.getReferences().contains(c.getSimpleName())){
				c.getReferencesInWorspace().add(classCode.getSimpleName());
			}
		}
	}
	
	private void search(){
		String simpleName= simpleNameTf.getText();		
		if(simpleName.isEmpty()){
			return;
		}
		ClassCodeBean beanToSelect= null;
		for(ClassCodeBean iter : tableView.getItems()){
			if(iter.getClassCode().getSimpleName().startsWith(simpleName)){
				beanToSelect= iter;
				break;
			}
		}
		if(beanToSelect!=null){
			int beanIndex= -1;
			for(int i=0; i<tableView.getItems().size(); i++){
				if(tableView.getItems().get(i)==beanToSelect){
					beanIndex= i;
					break;
				}
			}
			tableView.getSelectionModel().select(beanToSelect);
			tableView.scrollTo(beanIndex);
		}
	}
	
}
