package Appli;

import java.util.List;

public record PluginDescriptor(String name, String description, List<String> dependenciesList,
                               int minInstanceNumber, int maxInstanceNumber, boolean enabled,
                               boolean required, String classPath) {
}
