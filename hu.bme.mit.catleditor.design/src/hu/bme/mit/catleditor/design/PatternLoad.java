package hu.bme.mit.catleditor.design;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

public class PatternLoad implements IExternalJavaAction {

	public PatternLoad() {
	}

	@Override
	public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {
		// TODO selection atmasolasa a root op refrerenciajaba, ott levo torlese
		// TODO lehessen mashova is rakni? Jo lenne! megoldhato? valami olyasmi
		// kene ahol a helyere hivjak meg, aztán kivalasztjak a beillesztendo
		// mintat
		// WIZARD?

	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		// TODO csak a root store referenciájaira lehessen
		// TODO csak egy kivalasztott lehessen
		return false;
	}

}
