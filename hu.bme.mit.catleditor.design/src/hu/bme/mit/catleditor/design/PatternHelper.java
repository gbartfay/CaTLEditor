package hu.bme.mit.catleditor.design;

import hu.bme.mit.CaTLEditor.AndForm;
import hu.bme.mit.CaTLEditor.CaTLEditorFactory;
import hu.bme.mit.CaTLEditor.ImpForm;
import hu.bme.mit.CaTLEditor.OrForm;
import hu.bme.mit.CaTLEditor.Pattern;

public class PatternHelper {
	
	public static Pattern copyPattern(Pattern source) {
		Pattern target = null;
		//TODO
		if (source instanceof OrForm) {
			OrForm or = (OrForm) source;
			OrForm newItem = CaTLEditorFactory.eINSTANCE.createOrForm();
			for (Pattern subExp : or.getOp()) {
				newItem.getOp().add(copyPattern(subExp));
			}
			newItem.setName(or.getName());
			target = newItem;
		}
		
		if (source instanceof AndForm) {
			AndForm and = (AndForm) source;
			AndForm newItem = CaTLEditorFactory.eINSTANCE.createAndForm();
			for (Pattern subExp : and.getOp()) {
				newItem.getOp().add(copyPattern(subExp));
			}
			newItem.setName(and.getName());
			target = newItem;
		}
		
		if (source instanceof ImpForm) {
			ImpForm imp = (ImpForm) source;
			ImpForm newItem = CaTLEditorFactory.eINSTANCE.createImpForm();
			
			newItem.setLeftOp(CaTLEditorFactory.eINSTANCE.createLeftOp());
			newItem.getLeftOp().setName(imp.getLeftOp().getName());
			newItem.getLeftOp().setOp(copyPattern(imp.getLeftOp().getOp()));

			newItem.setRightOp(CaTLEditorFactory.eINSTANCE.createRightOp());
			newItem.getRightOp().setName(imp.getRightOp().getName());
			newItem.getRightOp().setOp(copyPattern(imp.getRightOp().getOp()));

			newItem.setName(imp.getName());
			target = newItem;
		}
		
//		if (source instanceof NegationForm) {
//			NegationForm neg = (NegationForm) source;
//			out.append(Character.toChars(172));
//			handleInnerElements(out, (neg.getOp()));
//		}
//		
//		if (source instanceof NextForm) {
//			NextForm next = (NextForm) source;
//			out = out.append("X");
//			handleInnerElements(out, (next.getOp()));
//		}
//		if (source instanceof Globally) {
//			Globally globally = (Globally) source;
//			out = out.append("G");
//			handleInnerElements(out, (globally.getOp()));
//		}
//		if (source instanceof Future) {
//			Future future = (Future) source;
//			out = out.append("F");
//			handleInnerElements(out, (future.getOp()));
//		}
//		if (source instanceof UntilForm) {
//			UntilForm until = (UntilForm) source;
//			out = out.append("(");
//			handleInnerElements(out, (until.getLeftOp().getOp()));
//			out = out.append(" U ");
//			handleInnerElements(out, (until.getRightOp().getOp()));
//			out = out.append(")");
//		}
//		
//		if (source instanceof AbstractAtomicFormulas) {
//			AbstractAtomicFormulas aaf = (AbstractAtomicFormulas) source;
//			handleAbstractElements(out, aaf);
//		}
		return target;
	}
}
