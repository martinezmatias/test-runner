package eu.stamp_project.testrunner.listener.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.data.ExecutionDataStore;

import eu.stamp_project.testrunner.listener.Coverage;
import eu.stamp_project.testrunner.listener.TestResult;
import eu.stamp_project.testrunner.runner.Loader;

/**
 * created by Benjamin DANGLOT benjamin.danglot@inria.fr on 14/11/18
 */
public class CoverageLineImpl implements Coverage, Serializable {

	private static final long serialVersionUID = 109548359596802378L;
	/**
	 * class name
	 */
	public Map<String, LinesCovMap> covered = new HashMap<>();

	public CoverageLineImpl() {
		// empty
	}

	@Override
	public int getInstructionsCovered() {
		return -1;
	}

	@Override
	public int getInstructionsTotal() {
		return -1;
	}

	@Override
	public String getExecutionPath() {
		return null;
	}

	@Override
	public boolean isBetterThan(Coverage that) {
		return false;
	}

	@Override
	public void collectData(ExecutionDataStore executionData, String classesDirectory) {

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);
		try {
			analyzer.analyzeAll(new File(classesDirectory));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//

		for (IClassCoverage classCoverage : coverageBuilder.getClasses()) {

			// classCoverage.getLine(0).getInstructionCounter().

			// System.out.println("\n-*-*-" + classCoverage.getName() + " f: " +
			// classCoverage.getFirstLine() + " l: "
			// + classCoverage.getLastLine());

			List<Integer> allLinesExecuted = new ArrayList<Integer>();
			Map<Integer, Integer> covClass = new HashMap<>();

			for (IMethodCoverage methodCoverage : classCoverage.getMethods()) {

				// System.out.println("--" + methodCoverage.getName() + " f: " +
				// methodCoverage.getFirstLine() + " l: "
				// + methodCoverage.getLastLine());

				if (!"<clinit>".equals(methodCoverage.getName())) {

					for (int i = methodCoverage.getFirstLine(); i <= methodCoverage.getLastLine() + 1; i++) {
						int coveredI = methodCoverage.getLine(i).getInstructionCounter().getCoveredCount();
						covClass.put(i, coveredI);
					}

				}
			}
			LinesCovMap l = new LinesCovMap(classCoverage.getName(), classCoverage.getPackageName(),
					classCoverage.getFirstLine(), classCoverage.getLastLine(), covClass);

			this.covered.put(classCoverage.getName(), l);

		}

	}

	@Override
	public String toString() {
		return null;// return this.instructionsCovered + " / " + this.instructionsTotal;
	}

	@Override
	public void save() {
		File outputDir = new File(TestResult.OUTPUT_DIR);
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				System.err.println("Error while creating output dir");
			}
		}
		File f = new File(outputDir, SERIALIZE_NAME + EXTENSION);
		try (FileOutputStream fout = new FileOutputStream(f)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
				oos.writeObject(this);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			System.err.println("Error while writing serialized file.");
			throw new RuntimeException(e);
		}
		System.out.println("File saved to the following path: " + f.getAbsolutePath());
	}

	/**
	 * Load from serialized object
	 *
	 * @return an Instance of JUnit4Coverage loaded from a serialized file.
	 */
	public static Coverage load() {
		return new Loader<Coverage>().load(SERIALIZE_NAME);
	}

	@Override
	public void setExecutionPath(String executionPath) {

	}

	public Map<String, LinesCovMap> getCovered() {
		return covered;
	}

	public void setCovered(Map<String, LinesCovMap> covered) {
		this.covered = covered;
	}

}
