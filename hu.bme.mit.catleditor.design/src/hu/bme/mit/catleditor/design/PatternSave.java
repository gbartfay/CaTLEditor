package hu.bme.mit.catleditor.design;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

import hu.bme.mit.CaTLEditor.AbstractAtomicFormulas;
import hu.bme.mit.CaTLEditor.AndForm;
import hu.bme.mit.CaTLEditor.CaTLEditorFactory;
import hu.bme.mit.CaTLEditor.CaTLExpression;
import hu.bme.mit.CaTLEditor.Future;
import hu.bme.mit.CaTLEditor.Globally;
import hu.bme.mit.CaTLEditor.ImpForm;
import hu.bme.mit.CaTLEditor.LeftOp;
import hu.bme.mit.CaTLEditor.NegationForm;
import hu.bme.mit.CaTLEditor.NextForm;
import hu.bme.mit.CaTLEditor.OrForm;
import hu.bme.mit.CaTLEditor.Pattern;
import hu.bme.mit.CaTLEditor.RightOp;
import hu.bme.mit.CaTLEditor.UntilForm;

public class PatternSave implements IExternalJavaAction {

	public PatternSave() {
	}

	@Override
	public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {
		EObject selection = selections.iterator().next();
		Pattern pattern = null;
		if (selection instanceof LeftOp) {
			pattern = ((LeftOp) selection).getOp();
		} else if (selection instanceof RightOp) {
			pattern = ((RightOp) selection).getOp();
		} else {
			pattern = (Pattern) selection;
		}
		CaTLExpression root = getRootExp(pattern);
		root.getStore().add(copyPattern(pattern));
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		if (selections == null || selections.size() != 1) {
			return false;
		} else {
			final EObject selection = selections.iterator().next();
			if (selection instanceof Pattern || selection instanceof LeftOp || selection instanceof RightOp) {
				EObject container = selection.eContainer();
				if (container instanceof CaTLExpression) {
					CaTLExpression root = (CaTLExpression) container;
					if (root.getStore().contains(selection)) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
	}

	private CaTLExpression getRootExp(Pattern pattern) {
		EObject container = pattern.eContainer();
		while (!(container instanceof CaTLExpression)) {
			container = container.eContainer();
		}
		return (CaTLExpression) container;
	}

	public Pattern copyPattern(Pattern source) {
		Pattern target = null;
		//TODO
		if (source instanceof OrForm) {
			OrForm or = (OrForm) source;
			target = CaTLEditorFactory.eINSTANCE.createOrForm();
			for (Pattern subExp : or.getOp()) {
				handleInnerElements(out, (subExp));
				out = out.append(" ");
				out = out.append(Character.toChars(709));
				out = out.append(" ");
			}
			out.setLength(out.length() - 3);
			out = out.append(")");
		}
		
//		if (pattern instanceof AndForm) {
//			AndForm and = (AndForm) pattern;
//			out = out.append("(");
//			for (Pattern subExp : and.getOp()) {
//				handleInnerElements(out, (subExp));
//				out = out.append(" ");
//				out = out.append(Character.toChars(708));
//				out = out.append(" ");
//			}
//			out.setLength(out.length() - 3);
//			out = out.append(")");
//		}
//		
//		if (pattern instanceof ImpForm) {
//			ImpForm imp = (ImpForm) pattern;
//			out = out.append("(");
//			handleInnerElements(out, (imp.getLeftOp().getOp()));
//			out = out.append(" ");
//			out = out.append(Character.toChars(8594));
//			out = out.append(" ");
//			handleInnerElements(out, (imp.getRightOp().getOp()));
//			out = out.append(")");
//		}
//		
//		
//		if (pattern instanceof NegationForm) {
//			NegationForm neg = (NegationForm) pattern;
//			out.append(Character.toChars(172));
//			handleInnerElements(out, (neg.getOp()));
//		}
//		
//		if (pattern instanceof NextForm) {
//			NextForm next = (NextForm) pattern;
//			out = out.append("X");
//			handleInnerElements(out, (next.getOp()));
//		}
//		if (pattern instanceof Globally) {
//			Globally globally = (Globally) pattern;
//			out = out.append("G");
//			handleInnerElements(out, (globally.getOp()));
//		}
//		if (pattern instanceof Future) {
//			Future future = (Future) pattern;
//			out = out.append("F");
//			handleInnerElements(out, (future.getOp()));
//		}
//		if (pattern instanceof UntilForm) {
//			UntilForm until = (UntilForm) pattern;
//			out = out.append("(");
//			handleInnerElements(out, (until.getLeftOp().getOp()));
//			out = out.append(" U ");
//			handleInnerElements(out, (until.getRightOp().getOp()));
//			out = out.append(")");
//		}
//		
//		if (pattern instanceof AbstractAtomicFormulas) {
//			AbstractAtomicFormulas aaf = (AbstractAtomicFormulas) pattern;
//			handleAbstractElements(out, aaf);
//		}
		return target;
	}

}
