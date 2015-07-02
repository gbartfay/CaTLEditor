package hu.bme.mit.catleditor.design;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

import hu.bme.mit.CaTLEditor.*;
import hu.bme.mit.CaTLEditor.impl.CaTLExpressionImpl;

public class GenerateCaTL implements IExternalJavaAction {

	public GenerateCaTL() {
		
	}

	@Override
	public void execute(Collection<? extends EObject> selections,
			Map<String, Object> parameters) {
		EObject catlexpr = getSemanticTarget(selections);
		if (catlexpr instanceof CaTLExpressionImpl) {
            CaTLExpressionImpl root = (CaTLExpressionImpl) catlexpr;
            generateExpression(root);
            
        }
		//System.out.println("Mukodik az external java action!");
		
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private void generateExpression(CaTLExpressionImpl root) {
		StringBuilder out = new StringBuilder();
		//TODO rekurzio
		Pattern pattern = root.getPattern();
		handleInnerElements(out, pattern);

		root.setOutput(out.toString());
	}
	
	private void handleInnerElements(StringBuilder out, Pattern pattern) {
		if (pattern instanceof OrForm) {
			OrForm or = (OrForm) pattern;
			out = out.append("(");
			handleInnerElements(out, (or.getLeftOp()));
			out = out.append(" ");
			out = out.append(Character.toChars(709));
			out = out.append(" ");
			handleInnerElements(out, (or.getRightOp()));
			out = out.append(")");
		}
		
		if (pattern instanceof AndForm) {
			AndForm and = (AndForm) pattern;
			out = out.append("(");
			handleInnerElements(out, (and.getLeftOp()));
			out = out.append(" ");
			out = out.append(Character.toChars(708));
			out = out.append(" ");
			handleInnerElements(out, (and.getRightOp()));
			out = out.append(")");
		}
		
		if (pattern instanceof ImpForm) {
			ImpForm imp = (ImpForm) pattern;
			out = out.append("(");
			handleInnerElements(out, (imp.getLeftOp().getOp()));
			out = out.append(" ");
			out = out.append(Character.toChars(8594));
			out = out.append(" ");
			handleInnerElements(out, (imp.getRightOp().getOp()));
			out = out.append(")");
		}
		
		
		if (pattern instanceof NegationForm) {
			NegationForm neg = (NegationForm) pattern;
			out.append(Character.toChars(172));
			handleInnerElements(out, (neg.getOp()));
		}
		
		if (pattern instanceof NextForm) {
			NextForm next = (NextForm) pattern;
			out = out.append("X");
			handleInnerElements(out, (next.getOp()));
		}
		if (pattern instanceof Globally) {
			Globally globally = (Globally) pattern;
			out = out.append("G");
			handleInnerElements(out, (globally.getOp()));
		}
		if (pattern instanceof Future) {
			Future future = (Future) pattern;
			out = out.append("F");
			handleInnerElements(out, (future.getOp()));
		}
		if (pattern instanceof UntilForm) {
			UntilForm until = (UntilForm) pattern;
			out = out.append("(");
			handleInnerElements(out, (until.getLeftOp().getOp()));
			out = out.append(" U ");
			handleInnerElements(out, (until.getRightOp().getOp()));
			out = out.append(")");
		}
		
		//Abstract: nem kell tovabb hivni belole
		if (pattern instanceof AbstractAtomicFormulas) {
			AbstractAtomicFormulas aaf = (AbstractAtomicFormulas) pattern;
			handleAbstractElements(out, aaf);
		} 
	}
	
	private void handleAbstractElements(StringBuilder out, AbstractAtomicFormulas aaf) {
		//temperature
		boolean isCold = aaf.getTemperature().equals(TemperatureEnum.COLD);
		if (isCold) {
			out = out.append("<");
		}
		
		if (aaf instanceof Propositions) {
			Propositions prop = (Propositions) aaf;
			out = out.append(prop.getProp().getLabel());
		}
		if (aaf instanceof TimingConst) {
			TimingConst timing = (TimingConst) aaf;
			out = out.append(timing.getDynamicClock());
			switch (timing.getRelation()) {
				case EQUAL:
					out = out.append(" = ");
					break;
				case LESS:
					out = out.append(" < ");
					break;
				case MORE:
					out = out.append(" > ");
					break;
			}
			out = out.append(timing.getStaticTimingVariable());
		}
		if (aaf instanceof ContextConst) {
			ContextConst cntx = (ContextConst) aaf;
			out = out.append(Character.toChars(8669));
			//TODO
			out = out.append("TODO: contextconst is not implemented yet!");
		}
		if (aaf instanceof PropertyConst) {
			PropertyConst propconst = (PropertyConst) aaf;
			out = out.append(((Context) propconst.eContainer().eContainer()).getCntxName());
			out = out.append(".");
			out = out.append(((Node) propconst.eContainer()).getName());
			out = out.append(".");
			out = out.append(propconst.getName());
			switch (propconst.getRelation()) {
				case EQUAL:
					out = out.append(" = ");
					break;
				case LESS:
					out = out.append(" < ");
					break;
				case MORE:
					out = out.append(" > ");
					break;
			}
			out = out.append(propconst.getValue());
		}
		if (isCold) {
			out = out.append(">");
		}
	}
	
    private EObject getSemanticTarget(Collection<? extends EObject> selections) {
        if (selections == null || selections.size() != 1) {
            return null;
        } else {
            final EObject selection = selections.iterator().next();
            EObject semanticTarget = null;
            if (selection instanceof DSemanticDecorator) {
                semanticTarget = ((DSemanticDecorator) selection).getTarget();
            } 
            return semanticTarget.eContainer();

        }
    }

}
