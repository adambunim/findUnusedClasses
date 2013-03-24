package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClassCodeBean {

	private ClassCode classCode;
	
	public ClassCodeBean(ClassCode classCode){
		this.classCode= classCode;
	}
	
	public ClassCode getClassCode(){
		return classCode;
	}
	
	public StringProperty simpleNameProperty() { 
		return new SimpleStringProperty(classCode.getSimpleName());
	}
	
	public StringProperty groupProperty() { 
		return new SimpleStringProperty(classCode.getGroup());
	}
	
	public StringProperty referencesProperty() { 
		return new SimpleStringProperty(""+classCode.getReferences().size());
	}
	
	public StringProperty referencesInWorkspaceProperty() { 
		return new SimpleStringProperty(""+classCode.getReferencesInWorspace().size());
	}
	
}
