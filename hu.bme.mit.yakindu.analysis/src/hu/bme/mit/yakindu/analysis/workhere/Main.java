package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.util.formallang.FollowerFunctionImpl.Direction;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;
import org.yakindu.sct.model.stext.stext.impl.EventDefinitionImpl;
import org.yakindu.sct.model.stext.stext.impl.VariableDefinitionImpl;


import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) 
	{
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		
		System.out.println(
				"public static void main(String[] args) throws IOException {\n" +
				"	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));\n" +	
				"	ExampleStatemachine s = new ExampleStatemachine();\n" +
				"	s.setTimer(new TimerService());\n" +
				"	RuntimeService.getInstance().registerStatemachine(s, 200);\n" +
				"	s.init();\n" +
				"	s.enter();\n" +
				"	s.runCycle();\n" +
				"	\n" +
				"	boolean on = true;\n" +
				"	while(on)\n" +
				"	{\n" +
				"		String cmd = reader.readLine();\n" +
				"		switch(cmd)\n" +
				"		{\n"
		);
		
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof EventDefinition) {
				EventDefinition b = (EventDefinition) content;
				
					System.out.println(
					"			case \""+ b.getName() +"\": \n" + 
					"			sm.raise"+ firstLetter(b.getName()) +"();\n" +
					"			sm.runCycle();\n" +
					"			break;\n" 
					);
				
			}
		}
		
		System.out.println(
				"			case \"exit\":\n" +
				"			run = false;\n" +
				"			break;\n" +
				"			\n" +
				"			default:\n" +
				"			System.out.println(\"Unrecognised command.\");\n" +
				"			break; \n" +
				"		}\n" +
				"		print(s);\n" +
				"	}\n" +
				"	reader.close();\n" +
				"	System.exit(0);\n" +
				"}" +
				"\n"
		);
		System.out.println("}");
		
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
		
		
	}
	
	private static String firstLetter(String s) 
	{
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}
	
	public static void task2()
	{
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
				if (state.getName() == "") 
				{
					state.setName("State " + nameNumber);
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
	
	public static void task4_4() 
	{
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		ArrayList<VariableDefinition> varDefs = new ArrayList<>();
		ArrayList<EventDefinition> evDefs = new ArrayList<>();
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();

			if(content instanceof EventDefinition) {
				EventDefinition evDef = (EventDefinition) content;
				evDefs.add(evDef);
			}
			if(content instanceof VariableDefinition) {
				VariableDefinition varDef = (VariableDefinition) content;
				varDefs.add(varDef);
			}
		}
		
		System.out.println("public static void print(IExampleStatemachine s)" + "\n" + "{");
		for(int i = 0; i < varDefs.size(); i++) 
		{
			String varName = varDefs.get(i).getName();
			String firstLetter = varName.toUpperCase().substring(0,1);
			System.out.println("\t" + "System.out.println(\"" + firstLetter + " = \" + s.getSCInterface().get" + varName + "());");
		}
		System.out.println("}");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
