package hu.bme.mit.catleditor.design;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

import hu.bme.mit.CaTLEditor.*;
import hu.bme.mit.CaTLEditor.impl.CaTLExpressionImpl;

public class GenerateCaTL implements IExternalJavaAction {
	
	private Map<Object, Integer> dictionarySysProp = null;
	private int numberTimingConst = 0;
	private int numberContextConst = 0;
	private int numberPropertyConst = 0;

	public GenerateCaTL() {
		dictionarySysProp = new HashMap<Object, Integer>();
	}

	@Override
	public void execute(Collection<? extends EObject> selections,
			Map<String, Object> parameters) {
		CaTLExpressionImpl catlexpr = getSemanticTarget(selections);
        generateExpression(catlexpr);
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		return true;
	}
	
	private void generateExpression(CaTLExpressionImpl root) {
		StringBuilder out = new StringBuilder();
		Pattern pattern = root.getRootexpression().getOp();
		handleInnerElements(out, pattern, false);

		root.setOutput(out.toString());
		generateFile(out, root);
	}
	
	private void handleInnerElements(StringBuilder out, Pattern pattern, boolean reduced) {
		if (pattern instanceof OrForm) {
			OrForm or = (OrForm) pattern;
			out = out.append("(");
			for (Pattern subExp : or.getOp()) {
				handleInnerElements(out, subExp, reduced);
				out = out.append(" ");
				out = out.append(Character.toChars(709));
				out = out.append(" ");
			}
			out.setLength(out.length() - 3);
			out = out.append(")");
		}
		
		if (pattern instanceof AndForm) {
			AndForm and = (AndForm) pattern;
			out = out.append("(");
			for (Pattern subExp : and.getOp()) {
				handleInnerElements(out, subExp, reduced);
				out = out.append(" ");
				out = out.append(Character.toChars(708));
				out = out.append(" ");
			}
			out.setLength(out.length() - 3);
			out = out.append(")");
		}
		
		if (pattern instanceof ImpForm) {
			ImpForm imp = (ImpForm) pattern;
			out = out.append("(");
			handleInnerElements(out, (imp.getLeftOp().getOp()), reduced);
			out = out.append(" ");
			out = out.append(Character.toChars(8594));
			out = out.append(" ");
			handleInnerElements(out, (imp.getRightOp().getOp()), reduced);
			out = out.append(")");
		}
		
		
		if (pattern instanceof NegationForm) {
			NegationForm neg = (NegationForm) pattern;
			out.append(Character.toChars(172));
			handleInnerElements(out, (neg.getOp()), reduced);
		}
		
		if (pattern instanceof NextForm) {
			NextForm next = (NextForm) pattern;
			out = out.append("X");
			handleInnerElements(out, (next.getOp()), reduced);
		}
		if (pattern instanceof Globally) {
			Globally globally = (Globally) pattern;
			out = out.append("G");
			handleInnerElements(out, (globally.getOp()), reduced);
		}
		if (pattern instanceof Future) {
			Future future = (Future) pattern;
			out = out.append("F");
			handleInnerElements(out, (future.getOp()), reduced);
		}
		if (pattern instanceof UntilForm) {
			UntilForm until = (UntilForm) pattern;
			out = out.append("(");
			handleInnerElements(out, (until.getLeftOp().getOp()), reduced);
			out = out.append(" U ");
			handleInnerElements(out, (until.getRightOp().getOp()), reduced);
			out = out.append(")");
		}
		
		if (pattern instanceof AbstractAtomicFormulas) {
			AbstractAtomicFormulas aaf = (AbstractAtomicFormulas) pattern;
			handleAbstractElements(out, aaf, reduced);
		} 
	}
	
	private void handleAbstractElements(StringBuilder out, AbstractAtomicFormulas aaf, boolean reduced) {
		//temperature
		boolean isCold = aaf.getTemperature().equals(TemperatureEnum.COLD);
		if (isCold) {
			out = out.append("<");
		}
		
		if (aaf instanceof Propositions) {
			Propositions prop = (Propositions) aaf;
			if (reduced) {
				out = out.append("prop" + getReducedIdForSysProp(prop.getProp()));
			} else {
				out = out.append(prop.getProp().getLabel());
			}
			
		}
		if (aaf instanceof TimingConst) {
			TimingConst timing = (TimingConst) aaf;
			if (reduced) {
				out = out.append("timing" + (++numberTimingConst));
			} else {
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
			
		}
		if (aaf instanceof ContextConst) {
			ContextConst cntx = (ContextConst) aaf;
			if (reduced) {
				out = out.append("cntx" + (++numberContextConst));
			} else {
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
			}
		}
		if (aaf instanceof PropertyConst) {
			PropertyConst propconst = (PropertyConst) aaf;
			if (reduced) {
				out = out.append("pconst" + (++numberPropertyConst));
			} else {
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
			}
		}
		if (isCold) {
			out = out.append(">");
		}
	}
	
    private CaTLExpressionImpl getSemanticTarget(Collection<? extends EObject> selections) {
        if (selections == null || selections.size() != 1) {
            return null;
        } else {
            final EObject selection = selections.iterator().next();
            EObject semanticTarget = null;
            if (selection instanceof DSemanticDecorator) {
                semanticTarget = ((DSemanticDecorator) selection).getTarget();
            }   
    		while (!(semanticTarget instanceof CaTLExpressionImpl)) {
    			semanticTarget = semanticTarget.eContainer();
    		}
            CaTLExpressionImpl root = (CaTLExpressionImpl) semanticTarget;
            return root;
        }
    }
    
    private void generateFile(StringBuilder out, CaTLExpressionImpl root) {
    	
    	String path = System.getProperty("user.home");
    	
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path + File.separator + root.getOutputFileName() + ".txt"), "utf-8"))) {
			writer.write(out.toString());
			System.out.println("Output generated to file: " + path + File.separator + root.getOutputFileName() + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public String generateFromPattern(Pattern pattern) {
    	StringBuilder out = new StringBuilder();
		handleInnerElements(out, pattern, false);
    	return out.toString();
    }
    
    public String generateReducedFromPattern(Pattern pattern) {
    	StringBuilder out = new StringBuilder();
		handleInnerElements(out, pattern, true);
    	return out.toString();
    }
    
    private Integer getReducedIdForSysProp(SystemProperty sp) {
    	if (dictionarySysProp.containsKey(sp)) {
    		return dictionarySysProp.get(sp);
    	} else {
    		Integer id = dictionarySysProp.size() + 1;
    		dictionarySysProp.put(sp, id);
    		return id;
    	}
    	
    	
    }

}
