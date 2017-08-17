package ca.aseg.triplestore;

import java.util.HashMap;
import java.util.Map;

public abstract class TripleLoader {

	protected String defaultGraph = "";
	protected String defaultRuleset = "";
	protected Map<graphType, String> graphMapping = null;

	public enum graphType {
		DEFAULT, SCHEMA, DATA, RULESET
	};

	public void loadFromDirectory(Object graph,String dir, boolean intoSingleGraph) {
		if (intoSingleGraph) {
			if (defaultGraph.isEmpty()) {
				System.err.println("You have to set your default graph URI first!");
				return;
			}
			if (defaultRuleset.isEmpty()) {
				System.err.println("You have to set the name of your default ruleset first!");
				return;
			}
			loadFromDirectory(graph,dir, defaultGraph);
		} else {
			loadFromDirectory(graph,dir, graphMapping);
		}
	}

	public void loadFromFile(Object graph,String file) {
		if (defaultGraph.isEmpty()) {
			System.err.println("You have to set your default graph URI first!");
			return;
		}
		if (defaultRuleset.isEmpty()) {
			System.err.println("You have to set the name of your default ruleset first!");
			return;
		}
		loadFromFile(file, defaultGraph);

	}

	public abstract void loadFromDirectory(Object graph, String dir, String graphUri);

	public abstract void loadFromDirectory(Object graph,String dir, Map<graphType, String> graphMapping);

	public abstract void loadFromFile(Object graph,String file, String graphUri);

	public String getDefaultGraphURI() {
		return defaultGraph;
	}

	public void setDefaultGraphURI(String defaultGraph) {
		this.defaultGraph = defaultGraph;
	}

	public String getDefaultRuleset() {
		return defaultRuleset;
	}

	public void setDefaultRuleset(String defaultRuleset) {
		this.defaultRuleset = defaultRuleset;
	}

	
	public Map<graphType, String> getGraphMapping() {
		return graphMapping;
	}

	
	public void setGraphMapping(String defaultGraph, String schemaGraph, String dataGraph, String ruleset) {
		this.graphMapping = new HashMap<>();
		graphMapping.put(graphType.DEFAULT, defaultGraph);
		graphMapping.put(graphType.SCHEMA, schemaGraph);
		graphMapping.put(graphType.DATA, dataGraph);
		graphMapping.put(graphType.RULESET, ruleset);
	}

}
