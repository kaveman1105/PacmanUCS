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
		PacFace newFace = null;
		PacmanCell pc = PacUtils.findPacman(grid);

		PacCell[][] test = grid.clone();
		test[1][1] = new PacCell(1, 1);
		if (path == null) {
//			findPath(grid.clone());
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