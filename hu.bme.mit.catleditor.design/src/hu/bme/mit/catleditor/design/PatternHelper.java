package hu.bme.mit.catleditor.design;

import hu.bme.mit.CaTLEditor.AbstractAtomicFormulas;
import hu.bme.mit.CaTLEditor.AndForm;
import hu.bme.mit.CaTLEditor.CaTLEditorFactory;
import hu.bme.mit.CaTLEditor.ContextConst;
import hu.bme.mit.CaTLEditor.Future;
import hu.bme.mit.CaTLEditor.Globally;
import hu.bme.mit.CaTLEditor.ImpForm;
import hu.bme.mit.CaTLEditor.NegationForm;
import hu.bme.mit.CaTLEditor.NextForm;
import hu.bme.mit.CaTLEditor.OrForm;
import hu.bme.mit.CaTLEditor.Pattern;
import hu.bme.mit.CaTLEditor.PropertyConst;
import hu.bme.mit.CaTLEditor.Propositions;
import hu.bme.mit.CaTLEditor.TimingConst;
import hu.bme.mit.CaTLEditor.UntilForm;

public class PatternHelper {
	
	public static Pattern copyPattern(Pattern source) {
		Pattern target = null;
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
		
		if (source instanceof NegationForm) {
			NegationForm neg = (NegationForm) source;
			NegationForm newItem = CaTLEditorFactory.eINSTANCE.createNegationForm();
			newItem.setOp(copyPattern(neg.getOp()));
			newItem.setName(neg.getName());
			target = newItem;
		}
		
		if (source instanceof NextForm) {
			NextForm next = (NextForm) source;
			NextForm newItem = CaTLEditorFactory.eINSTANCE.createNextForm();
			newItem.setOp(copyPattern(next.getOp()));
			newItem.setName(next.getName());
			target = newItem;
		}
		
		if (source instanceof Globally) {
			Globally globally = (Globally) source;
			Globally newItem = CaTLEditorFactory.eINSTANCE.createGlobally();
			newItem.setOp(copyPattern(globally.getOp()));
			newItem.setName(globally.getName());
			target = newItem;
		}
		
		if (source instanceof Future) {
			Future future = (Future) source;
			Future newItem = CaTLEditorFactory.eINSTANCE.createFuture();
			newItem.setOp(copyPattern(future.getOp()));
			newItem.setName(future.getName());
			target = newItem;
		}
		
		if (source instanceof UntilForm) {
			UntilForm until = (UntilForm) source;	
			UntilForm newItem = CaTLEditorFactory.eINSTANCE.createUntilForm();
			
			newItem.setLeftOp(CaTLEditorFactory.eINSTANCE.createLeftOp());
			newItem.getLeftOp().setName(until.getLeftOp().getName());
			newItem.getLeftOp().setOp(copyPattern(until.getLeftOp().getOp()));

			newItem.setRightOp(CaTLEditorFactory.eINSTANCE.createRightOp());
			newItem.getRightOp().setName(until.getRightOp().getName());
			newItem.getRightOp().setOp(copyPattern(until.getRightOp().getOp()));

			newItem.setName(until.getName());
			target = newItem;
		}
		
		if (source instanceof AbstractAtomicFormulas) {
			AbstractAtomicFormulas aaf = (AbstractAtomicFormulas) source;
			target = copyAbstractAtomicFormulas(aaf);
		}
		
		return target;
	}
	
	private static AbstractAtomicFormulas copyAbstractAtomicFormulas(AbstractAtomicFormulas aaf) {
		AbstractAtomicFormulas target = null;

		if (aaf instanceof Propositions) {
			Propositions prop = (Propositions) aaf;
			Propositions newItem = CaTLEditorFactory.eINSTANCE.createPropositions();
			newItem.setProp(prop.getProp());
			target = newItem;
		}
		
		if (aaf instanceof TimingConst) {
			TimingConst timing = (TimingConst) aaf;
			TimingConst newItem = CaTLEditorFactory.eINSTANCE.createTimingConst();
			newItem.setDynamicClock(timing.getDynamicClock());
			newItem.setRelation(timing.getRelation());
			newItem.setStaticTimingVariable(timing.getStaticTimingVariable());
			target = newItem;
		}
		
		if (aaf instanceof ContextConst) {
			ContextConst cntx = (ContextConst) aaf;
			ContextConst newItem = CaTLEditorFactory.eINSTANCE.createContextConst();
			newItem.setCont(cntx.getCont());
			newItem.setOperator(cntx.getOperator());
			newItem.setUsedConnection(cntx.getUsedConnection());
			newItem.setUsedNode(cntx.getUsedNode());
			target = newItem;
		}
		
		if (aaf instanceof PropertyConst) {
			PropertyConst propconst = (PropertyConst) aaf;
			PropertyConst newItem = CaTLEditorFactory.eINSTANCE.createPropertyConst();
			newItem.setUsedProp(propconst.getUsedProp());
			newItem.setRelation(propconst.getRelation());
			newItem.setValue(propconst.getValue());
			target = newItem;
		}
		
		target.setTemperature(aaf.getTemperature());
		target.setName(aaf.getName());

		return target;
	}
	
	public static String generateCaTLForPattern(Pattern pattern) {
		GenerateCaTL catlGenerator = new GenerateCaTL();
		return catlGenerator.generateReducedFromPattern(pattern);
	}
}
