package loader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.IMainAppPlugin;

public class Loader {
    private static Loader loader_singleton;
    private List<IMainAppPlugin> pluginList;
    private Map<String, JsonNode> pluginsJson;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Loader() {
        pluginList = new ArrayList<>();
        pluginsJson = new HashMap<>();
        loadNecessaryPlugins();
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

    public void loadNecessaryPlugins() {
        File directory = new File("src/Appli/data");

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Le répertoire spécifié n'existe pas.");
            return;
        }

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(file);

                    boolean load = jsonNode.get("load").asBoolean();
                    if (load) {
                        IMainAppPlugin plugin = LoadPlugin(jsonNode);
                        this.pluginList.add(plugin);
                    } else {
                        String name = jsonNode.get("name").asText();
                        this.pluginsJson.put(name, jsonNode);
                    }
                } catch (IOException e) {
                    System.err.println("Erreur lors de la lecture du fichier " + file.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    public IMainAppPlugin GetOrLoadPlugin(String pluginName) {
        IMainAppPlugin plugin = null;
        boolean presence = false;

        for (IMainAppPlugin p : pluginList) {
            if (p.getName().equals(pluginName)) {
                presence = true;
                plugin = p;
            }
        }

        if (!presence) {
            JsonNode jsonNode = this.pluginsJson.get(pluginName);
            if (jsonNode != null) {
                IMainAppPlugin p = LoadPlugin(jsonNode);
                this.pluginList.add(p);
                plugin = p;
            }
        }

        return plugin;
    }

    private IMainAppPlugin LoadPlugin(JsonNode jsonNode) {
        String emplacement = jsonNode.get("emplacement").asText();
            try {
				return (IMainAppPlugin) Class.forName(emplacement).getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
        
        return null;
    }
}