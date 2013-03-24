package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackageUtil {

	public static List<ClassCode> getClassCoseList(File selectedDirectory) throws Exception{
		List<File> filesToSearchList= new ArrayList<>();
		List<File> knownJavaFiles= new ArrayList<>();
		filesToSearchList.add(selectedDirectory);
		while (!filesToSearchList.isEmpty()) {
			File fileToSearch= filesToSearchList.remove(0);	
			if(fileToSearch.isDirectory()){
				File[] subFiles= fileToSearch.listFiles();
				for(File f : subFiles){
					filesToSearchList.add(f);
				}
			}
			else if(fileToSearch.getName().endsWith(".java")){
				knownJavaFiles.add(fileToSearch);
			}
		}
		List<ClassCode> classCodeList= new ArrayList<>();		
		for(File file : knownJavaFiles){
			try{
				ClassCode classCode= new ClassCode(file);
				classCodeList.add(classCode);
			}
			catch(Exception e){
				System.out.println("could not make ClassCode from: "+file.getName());
			}
		}
		System.out.println("getClassCoseList() done");
		return classCodeList;
	}

	public static boolean mergeSingleGroup(ClassGroup group, List<ClassGroup> allGroups){
		System.out.println("Merging "+group.toString());
		List<ClassGroup> referencingGroups= new ArrayList<>();	
		for(ClassGroup groupIterator : allGroups){
			if(groupIterator==group){
				continue;
			}
			if(isClassFromGroupAExistInGroupB(group, groupIterator)){
				referencingGroups.add(groupIterator);
			}
		}
		if(referencingGroups.isEmpty()){
			return false;
		}
		mergeGroups(group, referencingGroups);
		allGroups.removeAll(referencingGroups);
		return true;
	}
	
	private static void mergeGroups(ClassGroup group, List<ClassGroup> referencingGroups){
		Set<ClassCode> newGroupClassList= new HashSet<>();	
		newGroupClassList.addAll(group.getClassCodeList());
		for(ClassGroup groupIterator : referencingGroups){
			newGroupClassList.addAll(groupIterator.getClassCodeList());
		}
		group.getClassCodeList().clear();
		group.getClassCodeList().addAll(newGroupClassList);
	}
	
	private static boolean isClassFromGroupAExistInGroupB(ClassGroup groupA, ClassGroup groupB){
		for(String className : groupA.getClassNames()){
			if(isClassNameExistInGroup(className, groupB)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean isClassNameExistInGroup(String className, ClassGroup group){
		for(ClassCode classCode : group.getClassCodeList()){
			if(classCode.getCode().contains(className)){
				return true;
			}
		}
		return false;
	}
	
}

