package test;

import java.awt.Adjustable;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import lap.Lap;
import menu.ConsoleMenu;
import menu.ConsumerOption;
import menu.RunnableOption;
import round.RoundContext;
import round.RoundList;
import tools.FileTools;
import tools.NumberTools;

public class Client {
	
	static ConsoleMenu main, valdifierMenu;
	ConsumerOption<LongValidifier> adjustRange;
	RunnableOption getRandomValidfier, getSequentialValidifer, roundTest, threadedTest, randomThreadedTest;
	
	Client() {
		main = new ConsoleMenu("Test Client");
		getRandomValidfier = new RunnableOption("Get ran Validifer", () -> generateRandomObjectToTest());
		main.add(getRandomValidfier);
		getSequentialValidifer = new RunnableOption("Get seq validifier", () -> generateSequtialObjectToTest());
		main.add(getSequentialValidifer);
		threadedTest = new RunnableOption("Get threaded validifer", () -> generateThreadedSequentialObject());
		main.add(threadedTest);
		roundTest = new RunnableOption("Round test", () -> testRoundList());
		main.add(roundTest);
		randomThreadedTest = new RunnableOption("get Random Threaded test", () -> generateThreadedRandomObject());
		main.add(randomThreadedTest);
		main.add(RunnableOption.EXIT_WITHOUT_SAFE);
		adjustRange = new ConsumerOption<>(
				"Adjust the acceptable range", 
				v -> 
				{
					System.out.println(
						v.getPassFromRange(
							NumberTools.generateDouble(
								"Please enter the acceptable range, 0.0 -> 1.0",
								true, 
								0, 
								1)
						).toString());
//					writeToFile("randomTest", v);
				});
		valdifierMenu = new ConsoleMenu("Modification of");
		valdifierMenu.add(adjustRange);
		valdifierMenu.add(RunnableOption.RETURN);
	}
	
	public void begin() {
		main.selection();
		
	
	}
	
	private void writeToFile(String name, LongValidifier v) {
		try {
			FileTools.writeToFile(new PrintWriter(
						FileTools.getUniqueTxtFile(FileTools.DEFAULT_IO_DIRECTORY + name + ".txt"))
					, v.toCSV());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(v.toCSV());
		}
	}
	
	private static void roundListTest() {
		
	}
	
	private static void testTheTestClient() {
//		/* SOMETHING LIKE:
//		 *  Validity x = LongValidifer.test();
//		 * 
//		 *  MenuOption redefine
//		 */
	}
	
	private void generateRandomObjectToTest() {
		int runFor = NumberTools.generateInt("Enter times to run for", false, 1, Integer.MAX_VALUE);
		double acceptableRange = NumberTools.generateDouble("Enter the acceptable ragne", false, 0, 1);
		int randomMAx = NumberTools.generateInt("Enter the maximum value", false, 0, Integer.MAX_VALUE);
		int offset = NumberTools.generateInt("enter the offset", false, 0, Integer.MAX_VALUE);
		LongValidifier v = LongValidifier.randomThreadTest(runFor, acceptableRange, randomMAx, offset);
		writeToFile("random." + runFor + "." + acceptableRange + "." + randomMAx + "." + offset, v);
		adjustRange.setObjectToConsume(v);
		valdifierMenu.selection();
	}
	
	private void generateSequtialObjectToTest() {
		int runFor = NumberTools.generateInt("Enter times to run for", false, 1, Integer.MAX_VALUE);
		double acceptableRange = NumberTools.generateDouble("Enter the acceptable ragne", false, 0, 1);
		int startValue = NumberTools.generateInt("Enter the millis to start at", false, 0, Integer.MAX_VALUE);
		int endValue = NumberTools.generateInt("enter the millis to end at", false, 0, Integer.MAX_VALUE);
		LongValidifier v = LongValidifier.sequentialThreadtest(startValue, endValue, runFor, acceptableRange);
		writeToFile("sequential." + runFor + "." + acceptableRange + "." + startValue + "-" + endValue, v);
		adjustRange.setObjectToConsume(v);
		valdifierMenu.selection();
	}
	
	private void generateThreadedSequentialObject() {
		int runFor = NumberTools.generateInt("Enter times to run for", false, 1, Integer.MAX_VALUE);
		double acceptableRange = NumberTools.generateDouble("Enter the acceptable ragne", false, 0, 1);
		int startValue = NumberTools.generateInt("Enter the millis to start at", false, 0, Integer.MAX_VALUE);
		int endValue = NumberTools.generateInt("enter the millis to end at", false, 0, Integer.MAX_VALUE);
		LongValidifier v = LongValidifier.sequentialTHREADEDTest(runFor, acceptableRange, startValue, endValue);
		adjustRange.setObjectToConsume(v);
		valdifierMenu.selection();
	}
	
	private void generateThreadedRandomObject() {
		int runFor = NumberTools.generateInt("Enter times to run for", false, 1, Integer.MAX_VALUE);
		double acceptableRange = NumberTools.generateDouble("Enter the acceptable ragne", false, 0, 1);
		int randomMAx = NumberTools.generateInt("Enter the maximum value", false, 0, Integer.MAX_VALUE);
		int offset = NumberTools.generateInt("enter the offset", false, 0, Integer.MAX_VALUE);
		LongValidifier v = LongValidifier.randomTHREADEDtest(runFor, acceptableRange, randomMAx, offset);
		adjustRange.setObjectToConsume(v);
		valdifierMenu.selection();
	}
	
	private void testRoundList() {
		String ran = "random", seq = "sequential";
		RoundContext context = RoundContext.from(ran, seq);
		int runFor = 10;
		RoundList test = new RoundList(context, runFor, true);
		for (int i = 0; i < runFor; i++) {
			test.lap(ran, Lap.from(i));
			test.lap(seq, Lap.from(i));
		}
		System.out.println(test);
	}
	
	
	public static void main(String[] args) {
		Client work = new Client();
		work.begin();
	}

}
