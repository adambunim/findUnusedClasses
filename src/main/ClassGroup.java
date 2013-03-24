package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassGroup {

	public List<ClassCode> classCodeList;
	
	public ClassGroup(ClassCode...classCodes){
		classCodeList= new ArrayList<>();
	}
	
	public List<ClassCode> getClassCodeList(){
		return classCodeList;
	}
	
	public List<String> getClassNames(){
		Set<String> allImports= new HashSet<>();
		for(ClassCode c : classCodeList){
			allImports.add(c.getClassName());
		}
		List<String> answer= new ArrayList<>(allImports);
		return answer;
	}
	
	@Override
	public String toString() {
		return classCodeList.get(0).toString();
	}
	
}
