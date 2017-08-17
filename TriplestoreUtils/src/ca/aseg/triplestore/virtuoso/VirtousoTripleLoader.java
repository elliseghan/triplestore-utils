package ca.aseg.triplestore.virtuoso;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.Map;

import ca.aseg.triplestore.TripleLoader;
import virtuoso.jena.driver.VirtGraph;

public class VirtousoTripleLoader extends TripleLoader {
	public VirtousoTripleLoader() {

	}

	public VirtousoTripleLoader(String defaultGraph, String defaultRuleset) {
		this.defaultGraph = defaultGraph;
		this.defaultRuleset = defaultRuleset;
	}

	public VirtousoTripleLoader(String defaultGraph, String schemaGraph, String dataGraph, String ruleset) {
		this.defaultGraph = defaultGraph;
		this.defaultRuleset = ruleset;
		this.setGraphMapping(defaultGraph, schemaGraph, dataGraph, ruleset);
	}

	@Override
	public void loadFromDirectory(Object graph, String dir, String graphUri) {
		File directory = new File(dir);
		String[] files = directory.list();

	}

	@Override
	public void loadFromDirectory(Object graph, String dir, Map<graphType, String> graphMapping) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFromFile(Object graph, String file, String graphUri) {
		String ruleset;
		if (graphMapping == null) {
			if (defaultRuleset.isEmpty()) {
				System.err.println("You have to set the name of your default ruleset first!");
				return;
			} else {
				ruleset = defaultRuleset;
			}
		} else {
			ruleset = graphMapping.get(graphType.RULESET);
			if (ruleset == null || ruleset.isEmpty()) {
				System.err.println("You have to set the name of your default ruleset first!");
				return;
			}
		}
		try {
			String staNtriple = "ld_dir (?, ?, ?)";
			PreparedStatement querySQLNtriple = ((VirtGraph) graph).getConnection().prepareStatement(staNtriple);
			// querySQLNtriple.setString(1, dirpath);
			querySQLNtriple.setString(2, file);
			querySQLNtriple.setString(3, graphUri);
			long start = System.currentTimeMillis();
			System.out.println("\n" + file + ": Start uploading file content into memory ...");
			querySQLNtriple.executeQuery();
			System.out.println("Elapsed time: " + (System.currentTimeMillis() - start) / 1000.00 + " seconds.");

			String inferenceRule = "rdfs_rule_set (?,?)";
			PreparedStatement queryInferenceRules = ((VirtGraph) graph).getConnection().prepareStatement(inferenceRule);
			System.out.println("\nPerforming extraction of rules from schema . . .");
			start = System.currentTimeMillis();
			queryInferenceRules.setString(1, ruleset);
			queryInferenceRules.setString(2, graphUri);
			queryInferenceRules.executeQuery();
			System.out.println("Elapsed time: " + (System.currentTimeMillis() - start) / 1000.00 + " seconds.");

			String bulkRun = "rdf_loader_run()";
			PreparedStatement querySQLBulkRun = ((VirtGraph) graph).getConnection().prepareStatement(bulkRun);
			System.out.println("\nPerforming the bulk load of all data . . .");
			start = System.currentTimeMillis();
			querySQLBulkRun.executeQuery();
			System.out.println("Elapsed time: " + (System.currentTimeMillis() - start) / 1000.00 + " seconds.");
			System.out.println("Done.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
