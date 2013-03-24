package main;

import java.util.Comparator;

public class ClassGroupComparator implements Comparator<ClassGroup>{

	@Override
	public int compare(ClassGroup g1, ClassGroup g2) {
		int size1= g1.getClassCodeList().size();
		int size2= g2.getClassCodeList().size();
		if(size1>size2){
			return -1;
		}
		else if(size2>size1){
			return 1;
		}
		return g1.toString().compareTo(g2.toString());
	}
	
}
