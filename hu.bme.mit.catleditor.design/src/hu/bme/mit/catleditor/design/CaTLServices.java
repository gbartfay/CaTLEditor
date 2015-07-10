package hu.bme.mit.catleditor.design;

import org.eclipse.emf.ecore.EObject;

import hu.bme.mit.CaTLEditor.Context;
import hu.bme.mit.CaTLEditor.ContextConst;
import hu.bme.mit.CaTLEditor.Node;
import hu.bme.mit.CaTLEditor.PropertyConst;
import hu.bme.mit.CaTLEditor.Propositions;
import hu.bme.mit.CaTLEditor.TimingConst;

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
	
	public String getTimingConstLabel(EObject object) {
		TimingConst timing = (TimingConst) object;
		StringBuilder out = new StringBuilder();
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
		return out.toString();
	}
	
	public String getContextConstLabel(EObject object) {
		StringBuilder out = new StringBuilder();
		ContextConst cntx = (ContextConst) object;
		String contName = cntx.getCont().getCntxName();
		switch (cntx.getOperator()) {
		case NOTHING:
			out = out.append(contName);
			break;
		case NODE_ADD:
			out = out.append(contName);
			out = out.append(" + ");
			out = out.append(cntx.getUsedNode().getName());
			break;
		case NODE_EXC:
			out = out.append(contName);
			out = out.append(" - ");
			out = out.append(cntx.getUsedNode().getName());
			break;
		case CONNECTION_ADD:
			out = out.append(contName);
			out = out.append(" + + ");
			out = out.append(cntx.getUsedConnection().getName());
			break;
		case CONNECTION_EXC:
			out = out.append(contName);
			out = out.append(" - - ");
			out = out.append(cntx.getUsedConnection().getName());
			break;
		}
		out = out.append(" ");
		out = out.append(Character.toChars(8669));
		out = out.append(" e");
		return out.toString();
	}
}
