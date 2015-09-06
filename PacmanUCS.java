/**
 * 
 * @author Kevin Anderson
 * @author Andrea Castillo
 * UCF
 * CAP4630 Fall 2015
 * 
 */
import java.awt.Point;
import java.io.File;
import java.util.*;
import javax.swing.JFileChooser;
import pacsim.FoodCell;
import pacsim.WallCell;
import pacsim.PacAction;
import pacsim.PacCell;
import pacsim.PacFace;
import pacsim.PacSim;
import pacsim.PacUtils;
import pacsim.PacmanCell;

public class PacmanUCS implements PacAction {

	private Point target;
	private ArrayList<node> fringe;
	private ArrayList<String> path;
	public boolean debug;

	public PacmanUCS(String fname) {
		PacSim sim = new PacSim(fname);
		sim.init(this);
	}

	public static void main(String[] args) {

		new PacmanUCS(args[0]);
	}

	@Override
	public void init() {
		target = null;
		fringe = new ArrayList<node>();
		path = null;
		debug = true;
	}

	@Override
	public PacFace action(Object state) {

		if (path == null) {
			PacCell[][] grid = (PacCell[][]) state;
			int i = 0, j = 0;
			PacCell pc;
			try {
				for (;; i++) {
					pc = grid[i][0];
				}

			} catch (Exception e) {
				i--;
				if (debug)
					System.out.println("i = " + i);
			}

			try {
				for (;; j++) {
					pc = grid[0][j];
				}

			} catch (Exception e) {
				j--;
				if (debug)
					System.out.println("j = " + j);
			}

			int num_pellets = 0;

			for (int k = 0; k < i; k++)
				for (int m = 0; m < j; m++) {
					if (grid[k][m] instanceof FoodCell)
						num_pellets++;
				}
			if (debug)
				System.out.println("the board has " + num_pellets + " pellets");
			// findPath(grid.clone());
			// need to return the first direction heres
		}

		return PacFace.N;
	}

	public void findPath(Object state) {

		// add initial starting point to fringe
		

	}

	/**
	 * this class will be on the fringe. it keeps track of the steps taken on
	 * each possible path. Also tracks how many pellets have been eaten
	 * 
	 */
	class node {
		int steps;
		int eaten;
		int x;
		int y;
		String history;

		public node(int x, int y) {
			this.eaten = 0;
			this.steps = 0;
			this.x = x;
			this.y = y;
			this.history = "";
		}
	}
}