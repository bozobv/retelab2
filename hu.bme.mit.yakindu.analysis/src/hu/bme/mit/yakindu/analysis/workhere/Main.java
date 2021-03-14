package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents(); 
		int nameNumber = 0;
		while (iterator.hasNext()) 
		{
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				System.out.println(state.getName());
				if (state.getOutgoingTransitions().size() == 0) 
				{
					System.out.println("Csapda: " + state.getName());
				}
				if (state.getName() == "Black") 
				{
					state.setName("State ");
					nameNumber++;
					System.out.println("new State: " + state.getName());
				}
				int outNumber = 0;
				
				for	(Transition t : state.getOutgoingTransitions()) 
				{				
					State outTransitions = (State) t.getTarget();
					System.out.println(state.getName() + " -> " + outTransitions.getName());
					if (outTransitions.getName() != state.getName())
						outNumber++;
				}
				if (outNumber == 0)
					System.out.println("Csapda: " + state.getName());
				
			}
			
		}
		
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
