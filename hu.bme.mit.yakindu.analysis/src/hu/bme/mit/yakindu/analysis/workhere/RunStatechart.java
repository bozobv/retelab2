package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;

import java.io.InputStreamReader; 
import java.io.BufferedReader;

public class RunStatechart {
	
	public static void main(String[] args) throws IOException 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		boolean run = true;
		while(run) 
		{
			String cmd = reader.readLine();
			switch(cmd) 
			{
				case "start":
					s.raiseStart();
					s.runCycle();
					break;
					
				case "white":
					s.raiseWhite();
					s.runCycle();
					break;
					
				case "black":
					s.raiseBlack();
					s.runCycle();
					break;
					
				case "exit":
					run = false;
					break;
					
				default:
					System.out.println("Unrecognised command.");
					break;
			}
			print(s);
		}
		reader.close();
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) 
	{
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
