package loader;

import Appli.PluginDescriptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import exception.DependencyNotEnabledException;

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
            try {
                Object plugin = loadPlugin(pluginDescriptor);
                this.pluginInstances.put(pluginDescriptor.name(), plugin);
            } catch (DependencyNotEnabledException e) {
                System.out.println(e.getMessage());
            }
        }

        return this.pluginInstances.get(pluginDescriptor.name());
    }

    public List<PluginDescriptor> loadAllPluginDescriptors() {
        File directory = new File("src/Appli/data");
        List<PluginDescriptor> allPluginDescriptors = new ArrayList<>();
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

        Map<String, String> additionalParameters = new HashMap<>();
        JsonNode additionalParametersNode = jsonNode.get("additionalParameters");
        if (additionalParametersNode != null) {
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = additionalParametersNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                additionalParameters.put(field.getKey(), field.getValue().asText());
            }
        }

        return new PluginDescriptor(
                jsonNode.get("name").asText(),
                jsonNode.get("description").asText(),
                dependencies,
                jsonNode.get("enabled").asBoolean(),
                jsonNode.get("classPath").asText(),
                additionalParameters
        );
    }

    private Object loadPlugin(PluginDescriptor pluginDescriptor) throws DependencyNotEnabledException {
        try {
            Object plugin = Class.forName(pluginDescriptor.classPath()).getConstructor().newInstance();
            for (String dependency : pluginDescriptor.dependenciesList()) {
                PluginDescriptor dependencyDescriptor = findPluginDescriptorByName(dependency);

                if (dependencyDescriptor == null) {
                    throw new DependencyNotEnabledException("La classe " + dependency + " n'est pas active.");
                }

                if (this.pluginInstances.get(dependencyDescriptor.name()) == null) {
                    Object dependencyPlugin = this.loadPlugin(dependencyDescriptor);
                    this.pluginInstances.put(dependency, dependencyPlugin);
                }
            }

            this.setDependencies(pluginDescriptor, plugin);

            this.setPluginParameters(pluginDescriptor, plugin);

            return plugin;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException
                 | ClassNotFoundException e) {
            System.err.println("Erreur de chargement du plugin: " + e.getMessage());
        }

        return null;
    }

    private void setDependencies(PluginDescriptor pluginDescriptor, Object instance) {
        for (String dependency : pluginDescriptor.dependenciesList()) {

            String setterMethodName = "set" + dependency;
            Method setterMethod;
            try {
                Class<?> dependencyInterface = this.pluginInstances.get(dependency).getClass().getInterfaces()[0];
                setterMethod = instance.getClass().getMethod(setterMethodName, dependencyInterface);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Échec lors de la récuperation de la méthode: " + setterMethodName);
            }

            try {
                Object dependencyInstance = this.pluginInstances.get(dependency);
                setterMethod.invoke(instance, dependencyInstance);
            } catch (Exception e) {
                throw new RuntimeException("Échec lors de l'appel à la méthode: " + setterMethodName);
            }
        }
    }

    private void setPluginParameters(PluginDescriptor pluginDescriptor, Object plugin) {
        String setterMethodName = "setParameters";
        Method setterMethod;
        try {
            setterMethod = plugin.getClass().getMethod("setParameters", Map.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Échec lors de la récuperation de la méthode: " + setterMethodName);
        }

        try {
            setterMethod.invoke(plugin, pluginDescriptor.additionalParameters());
        } catch (Exception e) {
            throw new RuntimeException("Échec lors de l'appel à la méthode: " + setterMethodName);
        }
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