package interfaces;

import java.util.List;

public interface IMainAppPlugin {

	 	String getName();
	    void setName(String name);

	    String getDescription();
	    void setDescription(String description);

	    List<String> getDependencyList();

	    int getMaxInstanceNumber();
	    void setMaxInstanceNumber(int maxInstanceNumber);

	    int getMinInstanceNumber();
	    void setMinInstanceNumber(int minInstanceNumber);

	    boolean isLoad();
	    void setLoad(boolean load);
	    
	    String getEmplacement();
	    void setEmplacement(String emplacement);
}
