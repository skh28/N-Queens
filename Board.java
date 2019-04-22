//Scott Ha
//CS 420
package nQueen;

public class Board implements Comparable{
	int board[];
	int search;
	int HCost;
	int stepCost;
	
	public Board(){
		search = 0;
	}
	
	public Board(int board[], int search){
		stepCost = 0;
		this.board = board;
		this.search = search;
		HCost = Hueristic(board);
	}
	
	public Board(int board[], int search, int stepCost){
		this.board = board;
		this.search = search;
		HCost = Hueristic(board);
		this.stepCost = stepCost;
	}
	public int getStepCost(){
		return stepCost;
	}
	
	public int getHueristic(){
		return HCost;
	}
	public int[] getBoard(){
		return board;
	}
	
	public boolean Conflicted(int[] a, int col)
	{
		//right
		for (int i = col; i < col+1; i++)
		{
			for (int j = 1;  j+i< a.length ; j++)
			{
				if (a[i] == a[j+i])
					return true;
				if (a[i]-j == a[j+i])
					return true;
				if (a[i]+j == a[j+i])
					return true;
			}
		}
		//left
		for (int i = col; i > col-1; i--)
		{
			for (int j = 1; i-j>= 0 ; j++)
			{
				if (a[i] == a[i-j])
					return true;
				if (a[i]-j == a[i-j])
					return true;
				if (a[i]+j == a[i-j])
					return true;
			}
		}
		return false;
	}
	
	public int Hueristic(int a[])
	{
		int val = 0;

		for (int i = 0; i < a.length; i++)
		{
			for (int j = 1;  j+i< a.length ; j++)
			{
				//row
				if (a[i] == a[j+i])
					val++;
				//upper diagonal
				if (a[i]-j == a[j+i])
					val++;
				//lower diagonal
				if (a[i]+j == a[j+i])
					val++;
			}
		}
		
		return val;
	}
	
	@Override
	public int compareTo(Object o) {
		Board other = (Board) o;
		
		if (this.getHueristic() < other.getHueristic())
			return -1;
		else if ( this.getHueristic() > other.getHueristic())
			return 1;
		return 0;
	}

}
