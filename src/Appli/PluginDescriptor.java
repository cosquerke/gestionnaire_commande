package Appli;

import java.util.List;

public record PluginDescriptor(String name, String description, List<String> dependenciesList, boolean enabled, String classPath) {
}
