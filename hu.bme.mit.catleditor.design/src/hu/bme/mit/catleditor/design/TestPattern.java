package hu.bme.mit.catleditor.design;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

import hu.bme.mit.CaTLEditor.impl.CaTLExpressionImpl;
import hu.bme.mit.CaTLEditor.*;

public class TestPattern implements IExternalJavaAction {

	public TestPattern() {
		
	}

	@Override
	public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {
		EObject pattern = getSemanticTarget(selections);
		NextForm newNext = CaTLEditorFactory.eINSTANCE.createNextForm();
		NextForm oldNext = (NextForm) pattern;
		oldNext.setOp(newNext);
		
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {	
		return true;
	}
	
	private EObject getSemanticTarget(Collection<? extends EObject> selections) {
        if (selections == null || selections.size() != 1) {
            return null;
        } else {
            final EObject selection = selections.iterator().next();
            EObject semanticTarget = null;
            if (selection instanceof DSemanticDecorator) {
                semanticTarget = ((DSemanticDecorator) selection).getTarget();
            } else {
            	semanticTarget = selection;
            }

            return semanticTarget;
        }
    }

}
