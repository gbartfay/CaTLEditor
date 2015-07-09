package hu.bme.mit.catleditor.design;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

import hu.bme.mit.CaTLEditor.CaTLExpression;
import hu.bme.mit.CaTLEditor.LeftOp;
import hu.bme.mit.CaTLEditor.Pattern;
import hu.bme.mit.CaTLEditor.PatternStore;
import hu.bme.mit.CaTLEditor.RightOp;

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
		root.getPatternstore().getStore().add(PatternHelper.copyPattern(pattern));
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		if (selections == null || selections.size() != 1) {
			return false;
		} else {
			final EObject selection = selections.iterator().next();
			if (selection instanceof Pattern || selection instanceof LeftOp || selection instanceof RightOp) {
				EObject container = selection.eContainer();
				if (container instanceof PatternStore) {
					PatternStore store = (PatternStore) container;
					if (store.getStore().contains(selection)) {
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

}
