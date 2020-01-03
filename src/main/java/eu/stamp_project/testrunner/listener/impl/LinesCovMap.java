package eu.stamp_project.testrunner.listener.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Matias Martinez
 *
 */
public class LinesCovMap {
	String classname;
	String packageName;
	int init;
	int end;
	Map<Integer, Integer> cov = new HashMap<>();

	public LinesCovMap(String classname, String packageName, int init, int end, Map<Integer, Integer> cov) {
		super();
		this.classname = classname;
		this.packageName = packageName;
		this.init = init;
		this.end = end;
		this.cov = cov;
	}

	public int getInit() {
		return init;
	}

	public void setInit(int init) {
		this.init = init;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public Map<Integer, Integer> getCov() {
		return cov;
	}

	public void setCov(Map<Integer, Integer> cov) {
		this.cov = cov;
	}

	@Override
	public String toString() {
		return "LinesCovMap [init=" + init + ", end=" + end + ", cov=" + cov + "]";
	}

}
