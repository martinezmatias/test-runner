package eu.stamp_project.testrunner.listener.impl;

import org.jacoco.core.internal.analysis.ClassCoverageImpl;
import org.jacoco.core.internal.analysis.StringPool;
import org.jacoco.core.internal.flow.ClassProbesVisitor;
import org.jacoco.core.internal.flow.MethodProbesVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Analyzes the structure of a class.
 */
public class ClassAnalyzerExt extends ClassProbesVisitor {

//	private final ClassCoverageImpl coverage;
	private final boolean[] probes;
	private final StringPool stringPool;

	/**
	 * Creates a new analyzer that builds coverage data for a class.
	 * 
	 * @param coverage   coverage node for the analyzed class data
	 * @param probes     execution data for this class or <code>null</code>
	 * @param stringPool shared pool to minimize the number of {@link String}
	 *                   instances
	 */
	public ClassAnalyzerExt(final ClassCoverageImpl coverage, final boolean[] probes, final StringPool stringPool) {
		// this.coverage = coverage;
		this.probes = probes;
		this.stringPool = stringPool;
	}

	@Override
	public void visit(final int version, final int access, final String name, final String signature,
			final String superName, final String[] interfaces) {
		// coverage.setSignature(stringPool.get(signature));
		// coverage.setSuperName(stringPool.get(superName));
		// coverage.setInterfaces(stringPool.get(interfaces));
	}

	@Override
	public void visitSource(final String source, final String debug) {
		// coverage.setSourceFileName(stringPool.get(source));
	}

	@Override
	public MethodProbesVisitor visitMethod(final int access, final String name, final String desc,
			final String signature, final String[] exceptions) {

		// InstrSupport.assertNotInstrumented(name, coverage.getName());

		if (isMethodFiltered(access, name)) {
			return null;
		}

		// return new MethodAnalyzer(stringPool.get(name), stringPool.get(desc),
		// stringPool.get(signature), probes) {
		return new MethodAnalyzerExt(stringPool.get(name), stringPool.get(desc), stringPool.get(signature), probes) {
			@Override
			public void visitEnd() {
				super.visitEnd();
//				final IMethodCoverage methodCoverage = getCoverage();
//				if (methodCoverage.getInstructionCounter().getTotalCount() > 0) {
//					// Only consider methods that actually contain code
//					// coverage.addMethod(methodCoverage);
//				}
			}
		};
	}

	// TODO: Use filter hook in future
	private boolean isMethodFiltered(final int access, final String name) {
		return (access & Opcodes.ACC_SYNTHETIC) != 0 && !name.startsWith("lambda$");
	}

	@Override
	public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
			final Object value) {
		// InstrSupport.assertNotInstrumented(name, coverage.getName());
		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public void visitTotalProbeCount(final int count) {
		// nothing to do
	}

}
