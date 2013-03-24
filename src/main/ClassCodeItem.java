package main;

import javafx.scene.control.TreeItem;

public class ClassCodeItem extends TreeItem<String>{

	private ClassCode classCode;
	
	public ClassCodeItem(ClassCode classCode){
		this.classCode= classCode;
		setValue(classCode.toString());
	}
	
	public ClassCode getClassCode(){
		return classCode;
	}
	
}
