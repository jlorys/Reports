package reports.repository;

import reports.domain.AppUser;

public interface NoFile {
	
	Long getId();
	String getName(); 
    String getExtension();  
    String getDescription(); 
    String getGrade(); 
    String getDatetime(); 
    AppUser getOwner();
}
