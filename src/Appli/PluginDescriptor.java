package Appli;

import java.util.List;
import java.util.Map;

public record PluginDescriptor(String name, String description, List<String> dependenciesList, boolean enabled, String classPath, Map<String, String> additionalParameters) {
}
