package hu.bme.mit.catleditor.design;

import org.eclipse.emf.ecore.EObject;

import hu.bme.mit.CaTLEditor.Context;
import hu.bme.mit.CaTLEditor.Node;
import hu.bme.mit.CaTLEditor.PropertyConst;
import hu.bme.mit.CaTLEditor.Propositions;

public class CaTLServices {

	public String getPropertyConstLabel(EObject object) {
		PropertyConst propconst = (PropertyConst) object;
		StringBuilder out = new StringBuilder();
		out = out.append(((Context) propconst.getUsedProp().eContainer().eContainer()).getCntxName());
		out = out.append(".");
		out = out.append(((Node) propconst.getUsedProp().eContainer()).getName());
		out = out.append(".");
		out = out.append(propconst.getUsedProp().getName());
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
		return out.toString();
	}
	
	public String getPropositionsLabel(EObject object) {
		Propositions prop = (Propositions) object;
		StringBuilder out = new StringBuilder();
		out = out.append(prop.getProp().getLabel());
		return out.toString();
	}
}
