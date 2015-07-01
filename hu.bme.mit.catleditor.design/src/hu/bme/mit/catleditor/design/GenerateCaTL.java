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
		System.out.println("Mukodik az external java action!");
		
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
			out = out.append("or");
		}
		if (pattern instanceof NextForm) {
			NextForm next = (NextForm) pattern;
			out = out.append("X(");
			handleInnerElements(out, ((NextForm) pattern).getOp());
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
