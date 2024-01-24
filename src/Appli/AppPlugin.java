package Appli;

import java.util.List;

public abstract class AppPlugin {
    private String name;
    private String description;
    private List<String> dependenciesList;
    private int maxInstanceNumber;
    private int minInstanceNumber;
    private boolean loaded;
    private String emplacement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDependenciesList() {
        return dependenciesList;
    }

    public void setDependenciesList(List<String> dependenciesList) {
        this.dependenciesList = dependenciesList;
    }

    public int getMaxInstanceNumber() {
        return maxInstanceNumber;
    }

    public void setMaxInstanceNumber(int maxInstanceNumber) {
        this.maxInstanceNumber = maxInstanceNumber;
    }

    public int getMinInstanceNumber() {
        return minInstanceNumber;
    }

    public void setMinInstanceNumber(int minInstanceNumber) {
        this.minInstanceNumber = minInstanceNumber;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }
}
