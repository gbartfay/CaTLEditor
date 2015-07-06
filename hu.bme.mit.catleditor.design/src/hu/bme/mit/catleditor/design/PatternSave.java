package hu.bme.mit.catleditor.design;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

public class PatternSave implements IExternalJavaAction {

	public PatternSave() {
	}

	@Override
	public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {
		// TODO kivalasztott atmasolasa a store ref-be

	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		// TODO csak egy kijelolt lehessen
		//TODO mindig a kijelolted, vagy a legmelyebbet masolja?
		return false;
	}

}
