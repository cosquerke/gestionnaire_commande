package loader;

import Appli.Plugin;
import Appli.PluginDescriptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import exception.DependencyNotEnabledException;
import interfaces.IPluginInterface;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Loader est une classe singleton qui gère le chargement et l'instanciation des plugins à partir de fichiers JSON.
 */
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

    /**
     * Obtient l'instance unique du Loader (singleton).
     *
     * @return Instance unique du Loader
     */
    public static Loader getInstance() {
        return lazyLoader();
    }

    // Méthode pour l'instanciation paresseuse du singleton.
    private static Loader lazyLoader() {
        if (loader_singleton == null) {
            loader_singleton = new Loader();
        }
        return loader_singleton;
    }

    /**
     * Charge les descripteurs de plugins qui doivent être chargé au démarrage à partir des fichiers JSON.
     */
    public void loadPluginDescriptors() {
        File directory = new File("src/Appli/data");

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Le répertoire spécifié n'existe pas.");
            return;
        }

        File[] files = directory.listFiles();
        if (null == files) {
            System.err.println("Le répertoire spécifié ne contient aucun fichier.");
            return;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    JsonNode pluginDescription = objectMapper.readTree(file);

                    // Si le plugin est activé
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

    /**
     * Récupère les descripteurs de plugins correspondant à une interface donnée.
     *
     * @param interfaceClass L'interface recherchée.
     * @return Liste des descripteurs de plugins correspondant à l'interface donnée.
     */
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

    /**
     * Récupère les interfaces implémentées par un plugin donné.
     *
     * @param pluginDescriptor Descripteur du plugin
     * @return Liste des interfaces implémentées par le plugin
     * @throws ClassNotFoundException Si une classe n'a pas pu être trouvée
     */
    public List<Class<?>> getPluginInterfaces(PluginDescriptor pluginDescriptor) throws ClassNotFoundException {
        return Arrays.asList(Class.forName(pluginDescriptor.classPath()).getInterfaces());
    }

    /**
     * Obtient une instance du plugin correspondant au descripteur donné.
     *
     * @param pluginDescriptor Descripteur du plugin
     * @return Instance du plugin
     */
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

    /**
     * Charge tous les descripteurs de plugins à partir des fichiers JSON.
     *
     * @return Liste de tous les descripteurs de plugins.
     */
    public List<PluginDescriptor> loadAllPluginDescriptors() {
        File directory = new File("src/Appli/data");
        List<PluginDescriptor> allPluginDescriptors = new ArrayList<>();
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Le répertoire spécifié n'existe pas.");
            return allPluginDescriptors;
        }

        File[] files = directory.listFiles();
        if (null == files) {
            System.err.println("Le répertoire spécifié ne contient aucun fichier.");
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

    /**
     * Obtient la liste des descripteurs de plugins.
     *
     * @return Liste des descripteurs de plugins
     */
    public List<PluginDescriptor> getPluginDescriptors() {
        return pluginDescriptors;
    }

    /**
     * Obtient les instances de plugins.
     *
     * @return Les instances de plugins.
     */
    public Map<String, Object> getPluginInstances() {
        return pluginInstances;
    }

    // Charge un descripteur de plugin à partir d'un nœud JSON
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

    /**
     * Charge un plugin ainsi que toutes ses dépendances recursivement tout en settant pour chaque plugin une instanciation de ses dependances.
     * @param pluginDescriptor Le descripteur du plugin à charger.
     * @return L'instanciation correspondant au descripteur de plugin donné.
     * @throws DependencyNotEnabledException exception si la dépendance n'est pas activée
     */
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

            if (Plugin.class.isAssignableFrom(plugin.getClass())) {
                this.setPluginParameters(pluginDescriptor, plugin);
            }

            return plugin;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException
                 | ClassNotFoundException e) {
            System.err.println("Erreur de chargement du plugin: " + e.getMessage());
        }

        return null;
    }

    /**
     * Set les instances des dépendances pour le plugin donné
     *
     * @param pluginDescriptor le descripteur du plugin sur lequel ajouter les dépendances
     * @param instance l'instance du plugin sur lequel set les dépendances
     */
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

    /**
     * Donne au plugin les paramètres additionnels contenus dans le descripteur de plugin.
     *
     * @param pluginDescriptor le descripteur du plugin à paramétrer
     * @param plugin le plugin à paramétrer
     */
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


    /**
     * Récupère un descripteur de plugin à partir de son nom.
     * @param descriptorName Le nom du descripteur.
     * @return Le descripteur.
     */
    private PluginDescriptor findPluginDescriptorByName(String descriptorName) {
        for (PluginDescriptor descriptor : this.pluginDescriptors) {
            if (descriptor.name().equals(descriptorName)) {
                return descriptor;
            }
        }
        return null;
    }
}