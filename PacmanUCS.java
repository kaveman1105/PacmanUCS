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
	private boolean pathfound;
	private int numPellets;
	public boolean debug;
	private String path;

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
		pathfound = false;
		numPellets = 0;

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

			for (int k = 0; k < i; k++)
				for (int m = 0; m < j; m++) {
					if (grid[k][m] instanceof FoodCell)
						numPellets++;
				}
			if (debug)
				System.out.println("the board has " + numPellets + " pellets");
			findPath(grid, i, j);
			// need to return the first direction heres
			path = "";
		} else {
			// step through path here
		}

		return PacFace.N;
	}

	public void findPath(PacCell[][] grid, int lengthX, int lengthY) {

		// get starting position of pacman
		PacmanCell pacman = PacUtils.findPacman(grid);

		// initialize fringe with starting point
		ArrayList<node> fringe = new ArrayList<node>();
		fringe.add(new node(pacman.getX(), pacman.getY(), -1));

		while (!pathfound) {
			System.out.println("fringe size before expand:" + fringe.size());
			fringe = expand(fringe, grid, lengthX, lengthY);
			System.out.println("fringe size after expand:" + fringe.size());
			// pathfound = true;
		}

	}

	public ArrayList<node> expand(ArrayList<node> fringe, PacCell[][] grid,
			int lengthX, int lengthY) {

		// remove first node and repalce with 1 from a step in each direction
		node n = fringe.remove(0);

		// step left
		if (n.x - 1 >= 0) { // check if step is within bounds
			node left = new node(n.x - 1, n.y, n.steps);
			left.addToHistory(n.history + "W");
			left.eaten = n.eaten;
			if (!(grid[left.x][left.y] instanceof WallCell)) {
				if (grid[left.x][left.y] instanceof FoodCell) {
					left.eaten++;
					if (left.eaten == numPellets) {
						pathfound = true;
						System.out.println("path found is: " + left.history);
					}
				}
				left.info();
				fringe.add(left);
			}
		}

		// step right
		if (n.x + 1 <= lengthX) {// check if step is within bounds
			node right = new node(n.x + 1, n.y, n.steps);
			right.addToHistory(n.history + "E");
			right.eaten = n.eaten;
			if (!(grid[right.x][right.y] instanceof WallCell)) {
				if (grid[right.x][right.y] instanceof FoodCell) {
					right.eaten++;
					if (right.eaten == numPellets) {
						pathfound = true;
						System.out.println("path found is: " + right.history);
					}
				}
				right.info();
				fringe.add(right);
			}
		}

		// step up
		if (n.y - 1 >= 0) {// check if step is within bounds
			node up = new node(n.x, n.y - 1, n.steps);
			up.addToHistory(n.history + "N");
			up.eaten = n.eaten;
			if (!(grid[up.x][up.y] instanceof WallCell)) {
				if (grid[up.x][up.y] instanceof FoodCell) {
					up.eaten++;
					if (up.eaten == numPellets) {
						pathfound = true;
						System.out.println("path found is: " + up.history);
					}

				}
				up.info();
				fringe.add(up);
			}
		}

		// step down
		if (n.y + 1 <= lengthY) {// check if step is within bounds
			node down = new node(n.x, n.y + 1, n.steps);
			down.addToHistory(n.history + "S");
			down.eaten = n.eaten;
			if (!(grid[down.x][down.y] instanceof WallCell)) {
				if (grid[down.x][down.y] instanceof FoodCell) {
					down.eaten++;
					if (down.eaten == numPellets) {
						pathfound = true;
						System.out.println("path found is: " + down.history);
					}

				}
				down.info();
				// add to end of fringe
				fringe.add(down);
			}
		}

		return fringe;
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

		public node(int x, int y, int steps) {
			this.eaten = 0;
			this.steps = steps + 1;
			this.x = x;
			this.y = y;
			this.history = "";
		}

		public void addToHistory(String s) {
			this.history += s;
		}

		public void info() {
			System.out.println("x:" + this.x + " y:" + this.y + " steps:"
					+ this.steps + " eaten:" + this.eaten);
		}
	}
}