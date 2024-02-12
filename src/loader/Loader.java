package loader;

import Appli.PluginDescriptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Loader {
    private static Loader loader_singleton;
    private final List<PluginDescriptor> pluginDescriptors;
    private final Map<String, Object> pluginInstances;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Loader() {
        this.pluginDescriptors = new ArrayList<>();
        this.pluginInstances = new HashMap<>();
        loadPluginDescriptors();
    }

    public static Loader getInstance() {
        return lazyLoader();
    }

    private static Loader lazyLoader() {
        if (loader_singleton == null) {
            loader_singleton = new Loader();
        }
        return loader_singleton;
    }

    public void loadPluginDescriptors() {
        File directory = new File("src/Appli/data");

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Le répertoire spécifié n'existe pas.");
            return;
        }

        File[] files = directory.listFiles();
        if (null == files) {
            System.err.println("Le répertoire spécifié ne contient aucun fichiers");
            return;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    JsonNode pluginDescription = objectMapper.readTree(file);

                    // if plugin is enabled
                    if (pluginDescription.get("enabled").asBoolean()) {
                        PluginDescriptor plugin = loadPluginDescriptor(pluginDescription);
                        this.pluginDescriptors.add(plugin);
                    }
                } catch (IOException e) {
                    System.err.println("Erreur lors de la lecture du fichier " + file.getName());
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public List<PluginDescriptor> getPluginDescriptorsForInterface(Class<?> interfaceClass) {
        List<PluginDescriptor> correspondingDescriptors = new ArrayList<>();
        this.pluginDescriptors.forEach(pluginDescriptor -> {
            try {
                if (getPluginInterfaces(pluginDescriptor).contains(interfaceClass)) {
                    correspondingDescriptors.add(pluginDescriptor);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        return correspondingDescriptors;
    }

    public List<Class<?>> getPluginInterfaces(PluginDescriptor pluginDescriptor) throws ClassNotFoundException {
        return Arrays.asList(Class.forName(pluginDescriptor.classPath()).getInterfaces());
    }

    public Object getPlugin(PluginDescriptor pluginDescriptor) {
        if (!this.pluginInstances.containsKey(pluginDescriptor.name())) {
            this.pluginInstances.put(pluginDescriptor.name(), loadPlugin(pluginDescriptor));
        }

        return this.pluginInstances.get(pluginDescriptor.name());
    }
    
    public List<PluginDescriptor> loadAllPluginDescriptors() {
        File directory = new File("src/Appli/data");
        List<PluginDescriptor> allPluginDescriptors = new ArrayList<PluginDescriptor>();
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Le répertoire spécifié n'existe pas.");
            return allPluginDescriptors;
        }

        File[] files = directory.listFiles();
        if (null == files) {
            System.err.println("Le répertoire spécifié ne contient aucun fichiers");
            return allPluginDescriptors;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    JsonNode pluginDescription = objectMapper.readTree(file);
                    PluginDescriptor plugin = loadPluginDescriptor(pluginDescription);
                    allPluginDescriptors.add(plugin);
                } catch (IOException e) {
                    System.err.println("Erreur lors de la lecture du fichier " + file.getName());
                    System.err.println(e.getMessage());
                }
            }
        }
		return allPluginDescriptors;
    }

    public List<PluginDescriptor> getPluginDescriptors() {
		return pluginDescriptors;
	}

	public Map<String, Object> getPluginInstances() {
		return pluginInstances;
	}

	private PluginDescriptor loadPluginDescriptor(JsonNode jsonNode) {
		List<String> dependencies = new ArrayList<>();
	    ArrayNode dependencyNode = (ArrayNode) jsonNode.get("dependencyList");
	    if (dependencyNode != null) {
	        for (JsonNode categoryNode : dependencyNode) {
	        	dependencies.add(categoryNode.asText());
	        }
	    }
		
        return new PluginDescriptor(
                jsonNode.get("name").asText(),
                jsonNode.get("description").asText(),
                dependencies,
                jsonNode.get("enabled").asBoolean(),
                jsonNode.get("classPath").asText()
        );
    }

    private Object loadPlugin(PluginDescriptor pluginDescriptor) {
        try {
            Object plugin = Class.forName(pluginDescriptor.classPath()).getConstructor().newInstance();
            Object dependencyPlugin = null;
            for (String dependency : pluginDescriptor.dependenciesList()) {
                if (!this.pluginInstances.containsKey(dependency)) {
                    PluginDescriptor dependencyDescriptor = findPluginDescriptorByName(dependency);
                    if (dependencyDescriptor == null) {
                        File dependencyFile = new File("src/Appli/data/" + dependency + ".json");
                        if (dependencyFile.exists() && dependencyFile.isFile()) {
                            JsonNode dependencyJson = objectMapper.readTree(dependencyFile);
                            dependencyDescriptor = loadPluginDescriptor(dependencyJson);
                            this.pluginDescriptors.add(dependencyDescriptor);
                        } else {
                            System.err.println("Fichier JSON de la dépendance non trouvé: " + dependency);
                            continue; 
                        }
                    }
                    
                    dependencyPlugin = Class.forName(dependencyDescriptor.classPath()).getConstructor().newInstance();
                    this.pluginInstances.put(dependency, dependencyPlugin);
                } else {
                	dependencyPlugin = this.pluginInstances.get(dependency);
                }
                
                String dependencyClassName = dependencyPlugin.getClass().getSimpleName();
                String setterMethodName = "set" + dependencyClassName;
                Method setterMethod = null;
                try {
                	Class<?> dependencyInterface = dependencyPlugin.getClass().getInterfaces()[0];
                    setterMethod = plugin.getClass().getMethod(setterMethodName, dependencyInterface);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Échec lors de la récuperation de la méthode" + setterMethodName);
                }
                
                if (setterMethod != null) {
                    try {
                        setterMethod.invoke(plugin, dependencyPlugin);
                    } catch (Exception e) {
                        e.printStackTrace(); 
                        throw new RuntimeException("Échec lors de l'appel à la méthode" + setterMethodName);
                    }
                }
            }
            
            return plugin;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException
                 | ClassNotFoundException | IOException e) {
            System.err.println("Erreur de chargement du plugin: " + e.getMessage());
        }

        return null;
    }
    
    private PluginDescriptor findPluginDescriptorByName(String descriptorName) {
        for (PluginDescriptor descriptor : this.pluginDescriptors) {
            if (descriptor.name().equals(descriptorName)) {
                return descriptor;
            }
        }
        return null;
    }
}