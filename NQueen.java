//Scott Ha
//CS 420

package nQueen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import static java.lang.System.*;
import static java.lang.Math.*;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class NQueen {


	final static int hillClimb = 0;
	final static int minConflict = 1;
	final static int Maximum = 1000;
	
	static boolean check = false;

	//Main class allows for user choice, user input
	public static void main(String args[]) throws IOException {
		int Puzzlenum;
		int Num;
		String print;
		String print2;
		Scanner scan = new Scanner(System.in);
		int choice = 1;
        
		System.out.println("                          N-Queens Problem");
		
        while (choice != 0){
        	System.out.println("\n" +
        			"Menu:\n" +
        			"(1) Generate 1 random N-Queens and solve using Hill Climb and Min-Conflict\n" +
        			"(2) Generate n random N-Queens and solve using Hill Climb and Min-Conflict\n" +
        			"(3) Exit Program\n");
        	System.out.print("Enter Option: ");
        	choice = scan.nextInt();
        	switch (choice){
        	case 1:
        		Puzzlenum = 1;
        		System.out.println("Enter number of Queens: ");
        		Num = scan.nextInt();
        		System.out.println("\nSolving with Hill Climb:");
        		
        		print = Run(Puzzlenum,Num,1);
        		System.out.println("\nSolving with Min-Conflict:");
        		print2 = Run(Puzzlenum,Num,2);
        		break;
        	case 2:
        		System.out.println("Enter number of Queens: ");
        		Num = scan.nextInt();
        		System.out.println("Enter the number of random puzzles: ");
        		Puzzlenum = scan.nextInt();
        		System.out.println("\nSolving with Hill Climb:");
        		print = Run(Puzzlenum,Num,1);
        		System.out.println("\nSolving with Min-Conflict:");
        		print2 = Run(Puzzlenum,Num,2);
        		break;
            case 3:
                System.exit(0);
            default:
        	}
        }
	}
	
	//Solve class takes in parameters number of queens and random puzzles.
	//Implements hill climbing and min conflicts algorithm.
	//Prints intiial and final board.
	//Calculates success rate and average run time.
	public static String Run(int Puzzles, int N, int search)
	{
		String Print = " ";
		int x[] = { 0 };

		Board B = new Board(x, 0);

		int percentage = 100;
		int StepCost = 0;
		int fail = 0;
		long Time = 0;
		long SuccTime = 0;
		
		for (int i = 0; i < Puzzles; i++) {
		
		long startTime = nanoTime();
		x = generateBoard(N);
			
			if (search == 1)
				B = HillClimb(x,hillClimb);
			else if (search == 2)
				B = minConflicts(x,minConflict, Maximum);
			 
			long endTime = nanoTime();
			Time += endTime - startTime;
			
			if (B != null) {
				System.out.println("Initial Board:\n "
					+ (getBoard(x)));
				System.out.println("Final Board:\n"
					+ (getBoard(B.getBoard())));
				SuccTime += endTime - startTime;
				StepCost += B.getStepCost();
			} else
				fail++; 
		}
		
		if (fail != Puzzles) {
			System.out.println("Success Percentage: "
				+ ((double) (Puzzles - fail) / Puzzles)*percentage + "%");
			System.out.println("Avg Step cost of Sucesses: "
				+ (StepCost / (Puzzles - fail)));
			System.out.println("Avg Time of search: "
					+ ((double) (Time) / Puzzles) + " nano seconds");
		}

		return Print;
	}

	//Linked list structure contains list of minimum cost conflicts.
	public static LinkedList<Integer> minimumConf(Board b) {

		int[] board = b.getBoard();
		LinkedList<Integer> list = new LinkedList<Integer>();

		for (int i = 0; i < board.length; i++) {
			if (b.Conflicted(board, i)) {
				list.add(i);
			}
		}

		return list;
	}

	//min Conflict algorithm chooses min conflict randomly
	public static Board minConflicts(int queen[], int search, int max) {
		Board current = new Board(queen, search);

		for (int i = 0; i < max; i++) {
			if (current.getHueristic() == 0)
				return current;
			long seed = System.nanoTime();
			Random rand = new Random();
			rand.setSeed(seed);

			LinkedList<Integer> List = minimumConf(current);
			int random = rand.nextInt(List.size());
			int var = List.get(random).intValue();
			for (int ij = 0; ij < List.size(); ij++) {
			}
			checkln("Current:");
			checkln("Column: " + var);

			Board value = optimalSuccessor(current, search, var);
			checkln("new Board:");
			checkln("H of value: " + value.getHueristic());
			current = value;
		}
		return null;
	}

	//Hill climbing algorithm implements local search and optimization
	public static Board HillClimb(int queen[], int search) {
		Board current = new Board(queen, search);
		Board neighbor;
		int stepCount = 0;

		while (true) {
			neighbor = optimalSuccessor(current, search, search);
			if (neighbor.getHueristic() == current.getHueristic())
				return null;
			else if (neighbor.getHueristic() == 0)
				return neighbor;
			current = neighbor;

			stepCount++;
		}
	}
	
	//priority queue implements generation of succesors
		public static PriorityQueue<Board> generateSuccessors(Board b,
				int search, int col) {
			int counter = 0;
			PriorityQueue<Board> list = new PriorityQueue<Board>();
			int[] board = b.getBoard();
			switch (search) {
			case 0:// hill Climb
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board.length; j++) {

						if (board[i] != j)// for steep climbing, doesn't check
											// current nodes
						{
							// generate and add
							int[] temp = board.clone();
							counter++;
							temp[i] = j;
							Board neighbor = new Board(temp, search,
									b.getStepCost() + 1);
							list.add(neighbor);
						}
					}
				}

				break;
			case 1:// min Conflict
				for (int j = 0; j < board.length; j++)
				{
				if (board[col] != j) {
						// generate and add
						int[] temp = board.clone();
						counter++;
						temp[col] = j;
						Board neighbor = new Board(temp, search, b.getStepCost() + 1);
						list.add(neighbor);
					}
				}
				break;
			}
			checkln("Created " + counter + " neighbors");

			return list;
		}

	//generates optimal successor
	public static Board optimalSuccessor(Board b, int search, int row) {
		// generate all successors
		PriorityQueue<Board> P = generateSuccessors(b, search, row);
		// make a list of minimum Cost
		LinkedList<Board> minimumCost = new LinkedList<Board>();
		int Minimum;
		Board front = P.poll();
		minimumCost.add(front);
		Minimum = front.getHueristic();
		while (P.size() > 0)
		{
			front = P.poll();
			if (Minimum > front.getHueristic()) {
				minimumCost.clear();
				minimumCost.add(front);
				Minimum = front.getHueristic();
			} else if (Minimum == front.getHueristic()) {
				minimumCost.add(front);
			}
		}
		checkln("The number of nodes " + minimumCost.size());
		// pick a random one from the start
		long seed = System.nanoTime();
		Random rand = new Random();
		rand.setSeed(seed);
		int random = rand.nextInt(minimumCost.size());
		Board min = minimumCost.remove(random);
		checkln("Min cost is " + min.getHueristic());

		return min;
	}

	//create board
	public static int[] generateBoard(int n) {

		int a[] = new int[n];
		long seed = System.nanoTime();
		Random rand = new Random();
		rand.setSeed(seed);
		for (int i = 0; i < a.length; i++) {
			int random = rand.nextInt(n);
			a[i] = random;
		}
		return a;
	}

	//print array in form of board
	public static void printBoard(int a[]) {
		for (int i = 0; i < a.length; i++)
			if (i < 10)
				System.out.print(i + " ");
			else
				System.out.print(i);
		System.out.println();
		for (int i = 0; i < a.length; i++) {
			if (i < 10)
				System.out.print(i + " ");
			else
				System.out.print(i);
			for (int j = 0; j < a.length; j++) {
				if (i == a[j])
					System.out.print(" Q");
				else
					System.out.print(" -");
			}
			System.out.println();
		}
	}
	
	//return 
	public static String getBoard(int a[]) {
		String output = "";
		for (int i = 0; i < a.length; i++)
			if (i < a.length)
				output += i + " ";
			else
				output += i;
				output += "\n";
		for (int i = 0; i < a.length; i++) {
			if (i < 10)
				output += i + " ";
			else
				output += i;
			for (int j = 0; j < a.length; j++) {
				if (i == a[j])
					output += " Q";
				else
					output += " -";
			}
			output += "\n";
		}
		return output;
	}

	public static void check(String string) {
		if (check)
			System.out.print(string);
	}

	public static void checkln(String string) {
		if (check)
			System.out.println(string);
	}
	
}

