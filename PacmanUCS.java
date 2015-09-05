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
	}

	@Override
	public PacFace action(Object state) {

		// if(something)
		findPath(state);
		// else
		// followPath();

		return PacFace.N;
	}

	public void findPath(Object state) {
		PacCell[][] grid = (PacCell[][]) state;
		PacmanCell pc = PacUtils.findPacman(grid);

		if (grid[1][1] instanceof FoodCell) {
			System.out.println("Its food");
			grid[1][1] = new PacCell(1, 1);
		} else
			System.out.println("Its something else");

	}

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