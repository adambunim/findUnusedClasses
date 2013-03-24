package main;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TreeItem;

public class ClassGroupItem extends TreeItem<String> {

	private ClassGroup classGroup;

	public ClassGroupItem(ClassGroup classGroup){
		this.classGroup= classGroup;	
		buildChildrenTree();
	}
	
	public void buildChildrenTree(){					
		List<TreeItem<String>> itemList= new ArrayList<>();
		for(ClassCode classCode : classGroup.getClassCodeList()){			
			itemList.add(new ClassCodeItem(classCode));
		}		
		getChildren().clear();
		getChildren().addAll(itemList);
		String classesString= "";
		if(itemList.isEmpty()){
			classesString= "";
		}
		else if(itemList.size()==1){
			classesString= ": "+itemList.get(0).getValue();
		}
		else{
			classesString= ": "+itemList.get(0).getValue()+"...";
		}
		setValue(""+classGroup.getClassCodeList().size()+ classesString);	
	}
	
	public ClassGroup getClassGroup(){
		return classGroup;
	}
	
}
