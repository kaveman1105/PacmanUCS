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
	}

	@Override
	public PacFace action(Object state) {
		PacCell[][] grid = (PacCell[][]) state;

		int i = 0, j = 0;
		PacCell pc;
		try {
			for (;; i++) {
				pc = grid[i][0];
			}

		} catch (Exception e) {
			i--;
			System.out.println("i = " + i);
		}

		try {
			for (;; j++) {
				pc = grid[0][j];
			}

		} catch (Exception e) {
			j--;
			System.out.println("j = " + j);
		}

		int num_pellets = 0;

//		for (int k = 0; k < i; k++)
//			for (int m = 0; m < j; m++) {
//				pc = grid[k][m];
//				if (PacUtils.neighbor(PacFace.W, pc, grid) instanceof FoodCell)
//					num_pellets++;
//			}
		System.out.println("the board has " + num_pellets + " pellets");

		if (path == null) {
			// findPath(grid.clone());
			// need to return the first direction heres
		}

		return PacFace.N;
	}

	public void findPath(Object state) {

		// add initial starting point to fringe
		fringe.add(new node(state));
		fringe.get(0).grid[1][1] = new PacCell(1, 1);

	}

	/**
	 * this class will be on the fringe. it keeps track of the steps taken on
	 * each possible path. It has its own copy of the the grid to edit as needed
	 * 
	 */
	class node {
		int steps;
		PacCell[][] grid;
		ArrayList<String> history;

		public node(Object state) {
			this.grid = (PacCell[][]) state;
			this.steps = 0;
			this.history = new ArrayList<String>();
		}
	}
}