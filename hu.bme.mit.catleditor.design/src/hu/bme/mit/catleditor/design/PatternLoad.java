package hu.bme.mit.catleditor.design;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

import hu.bme.mit.CaTLEditor.AndForm;
import hu.bme.mit.CaTLEditor.Future;
import hu.bme.mit.CaTLEditor.Globally;
import hu.bme.mit.CaTLEditor.LeftOp;
import hu.bme.mit.CaTLEditor.NegationForm;
import hu.bme.mit.CaTLEditor.NextForm;
import hu.bme.mit.CaTLEditor.OrForm;
import hu.bme.mit.CaTLEditor.Pattern;
import hu.bme.mit.CaTLEditor.RightOp;
import hu.bme.mit.CaTLEditor.RootExpression;

public class PatternLoad implements IExternalJavaAction {

	public PatternLoad() {
	}

	@Override
	public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {
		//selections: the destination
		//parameters: element - the pattern to load
		final EObject selection = selections.iterator().next();
		Pattern source = (Pattern) parameters.get("element");
		Pattern element = PatternHelper.copyPattern(source);
		if (selection instanceof LeftOp) {
			LeftOp lop = (LeftOp) selection;
			lop.setOp(element);
		} else if (selection instanceof RightOp) {
			RightOp rop = (RightOp) selection;
			rop.setOp(element);
		} else if (selection instanceof AndForm) {
			AndForm and = (AndForm) selection;
			and.getOp().add(element);
		} else if (selection instanceof OrForm) {
			OrForm or = (OrForm) selection;
			or.getOp().add(element);
		} else if (selection instanceof NegationForm) {
			NegationForm item = (NegationForm) selection;
			item.setOp(element);
		} else if (selection instanceof Globally) {
			Globally item = (Globally) selection;
			item.setOp(element);
		} else if (selection instanceof Future) {
			Future item = (Future) selection;
			item.setOp(element);
		} else if (selection instanceof NextForm) {
			NextForm item = (NextForm) selection;
			item.setOp(element);
		} else if (selection instanceof RootExpression) {
			RootExpression item = (RootExpression) selection;
			item.setOp(element);
		}
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		final EObject selection = selections.iterator().next();
		if (selection instanceof LeftOp) {
			LeftOp lop = (LeftOp) selection;
			if (lop.getOp() == null) {
				return true;
			}
		} else if (selection instanceof RightOp) {
			RightOp rop = (RightOp) selection;
			if (rop.getOp() == null) {
				return true;
			}
		} else if (selection instanceof AndForm) {
			return true;
		} else if (selection instanceof OrForm) {
			return true;
		} else if (selection instanceof NegationForm) {
			NegationForm item = (NegationForm) selection;
			if (item.getOp() == null) {
				return true;
			}
		} else if (selection instanceof Globally) {
			Globally item = (Globally) selection;
			if (item.getOp() == null) {
				return true;
			}
		} else if (selection instanceof Future) {
			Future item = (Future) selection;
			if (item.getOp() == null) {
				return true;
			}
		} else if (selection instanceof NextForm) {
			NextForm item = (NextForm) selection;
			if (item.getOp() == null) {
				return true;
			}
		} else if (selection instanceof RootExpression) {
			RootExpression item = (RootExpression) selection;
			if (item.getOp() == null) {
				return true;
			}
		}
		return false;
	}

}
