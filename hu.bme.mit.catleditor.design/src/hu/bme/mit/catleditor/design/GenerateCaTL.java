package hu.bme.mit.catleditor.design;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

import hu.bme.mit.CaTLEditor.*;

public class GenerateCaTL implements IExternalJavaAction {

	public GenerateCaTL() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Collection<? extends EObject> selections,
			Map<String, Object> parameters) {
		//CaTLExpression container = (CaTLExpression) parameters.get("container");
		Object test = parameters.get("container");
		//NextForm test3 = (NextForm) test;
		CaTLExpression test2 = (CaTLExpression) test;
		System.out.println("Mukodik az external java action!");
		
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		// TODO Auto-generated method stub
		return true;
	}

}
