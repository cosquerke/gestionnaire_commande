package plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Appli.PluginDescriptor;
import Appli.PluginMonitoring;

import interfaces.IPluginMonitorFrame;
import loader.Loader;


@SuppressWarnings("serial")
public class PluginMonitorFrame extends JFrame implements IPluginMonitorFrame{
  
	private List<PluginMonitoring> pluginsMonitoring;
	
    public PluginMonitorFrame() {
    	this.pluginsMonitoring = new ArrayList<PluginMonitoring>();
    }
    
	@Override
	public void display() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    this.setLocation(screenWidth - this.getWidth(), screenHeight - this.getHeight());
	    
	    this.setTitle("Moniteur de plugins");
	    this.setSize(700, screenHeight);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    int numColumns = 2;
	    int numRows = (int) Math.ceil((double) pluginsMonitoring.size() / numColumns);

	    JPanel cardsPanel = new JPanel(new GridLayout(numRows, numColumns));

	    for (PluginMonitoring pluginMonitoring : this.pluginsMonitoring) {
	        JPanel card = new JPanel(new BorderLayout());
	        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));

	        JLabel nameLabel = new JLabel("Nom du plugin : " + pluginMonitoring.name());
	        JLabel descriptorLoadedLabel = new JLabel("Descripteur chargé :" + Boolean.toString(pluginMonitoring.descriptorLoaded()));
	        JLabel classLoadedLabel = new JLabel("Classe chargée : " +Boolean.toString(pluginMonitoring.classLoaded()));

	        JPanel labelsPanel = new JPanel(new GridLayout(0, 1));
	        labelsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	        labelsPanel.add(nameLabel);
	        labelsPanel.add(descriptorLoadedLabel);
	        labelsPanel.add(classLoadedLabel);

	        card.add(labelsPanel, BorderLayout.CENTER);

	        cardsPanel.add(card);
	    }

	    JScrollPane scrollPane = new JScrollPane(cardsPanel);

	    this.add(scrollPane, BorderLayout.CENTER);

	    this.setVisible(true);
	}

	@Override
	public void setPlugins() {
		Loader loader = Loader.getInstance();
		List<PluginDescriptor> allPluginDescriptors = loader.loadAllPluginDescriptors();
		List<PluginDescriptor> loaderPluginDescriptors = loader.getPluginDescriptors();
		Map<String, Object> loaderPluginInstances = loader.getPluginInstances();
		
	    List<PluginMonitoring> previousPluginsMonitoring = new ArrayList<>(this.pluginsMonitoring);

		this.pluginsMonitoring = 
				allPluginDescriptors.stream()
                .map(descriptor -> {
                    boolean descriptorLoaded = loaderPluginDescriptors.contains(descriptor);
                    boolean classLoaded = loaderPluginInstances.containsKey(descriptor.name());
                    return new PluginMonitoring(descriptor.name(), descriptorLoaded, classLoaded);
                })
                .collect(Collectors.toList());

		if (!this.pluginsMonitoring.equals(previousPluginsMonitoring)) {
	        display();
	    }
	}
	
}
