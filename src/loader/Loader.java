package loader;

import Appli.PluginDescriptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

    private PluginDescriptor loadPluginDescriptor(JsonNode jsonNode) {
        return new PluginDescriptor(
                jsonNode.get("name").asText(),
                jsonNode.get("description").asText(),
                new ArrayList<>(),
                jsonNode.get("minInstanceNumber").asInt(),
                jsonNode.get("maxInstanceNumber").asInt(),
                jsonNode.get("enabled").asBoolean(),
                jsonNode.get("required").asBoolean(),
                jsonNode.get("classPath").asText()
        );
    }

    private Object loadPlugin(PluginDescriptor pluginDescriptor) {
        try {
            Object plugin = Class.forName(pluginDescriptor.classPath()).getConstructor().newInstance();
            
            for (String dependency : pluginDescriptor.dependenciesList()) {
                if (!this.pluginInstances.containsKey(dependency)) {
                    PluginDescriptor dependencyDescriptor = findPluginDescriptorByName(dependency);
                    if (dependencyDescriptor == null) {
                        File dependencyFile = new File("src/Appli/data/" + dependency + ".json");
                        if (dependencyFile.exists() && dependencyFile.isFile()) {
                            JsonNode dependencyJson = objectMapper.readTree(dependencyFile);
                            dependencyDescriptor = loadPluginDescriptor(dependencyJson);
                        } else {
                            System.err.println("Fichier JSON de la dépendance non trouvé: " + dependency);
                            continue; 
                        }
                    }
                    
                    Object dependencyPlugin = Class.forName(dependencyDescriptor.classPath()).getConstructor().newInstance();
                    this.pluginInstances.put(dependency, dependencyPlugin);
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