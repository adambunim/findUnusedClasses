package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

public class GroupTreePane extends VBox{

	private TreeView<String> treeView;
	private TreeItem<String> root;
	
	private void createBody(){
		setPadding(new Insets(10,10,10,10));
		setSpacing(10);
		treeView= new TreeView<>();
		root= new TreeItem<>("root");
		treeView.setRoot(root);
		treeView.setShowRoot(false);		
	}
	
	private void buildTree(List<ClassGroup> allGroups){
		Collections.sort(allGroups, new ClassGroupComparator());
		List<ClassGroupItem> allGroupItems= new ArrayList<>();
		for(ClassGroup groupIter : allGroups){
			allGroupItems.add(new ClassGroupItem(groupIter));
		}
		root.getChildren().clear();
		root.getChildren().addAll(allGroupItems);
	}
	
}
