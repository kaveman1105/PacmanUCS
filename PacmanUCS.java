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
		System.out.println("init");

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
		int count = 0;

		while (!pathfound) {
			count++;
			if (debug)
				System.out.println("\n>>>>>>>>>>>>>> count: " + count + "\n");
			if (count == 44)
				break;
			if (debug)
				System.out
						.println("fringe size before expand:" + fringe.size());
			fringe = expand(fringe, grid, lengthX, lengthY);
			if (debug)
				System.out.println("fringe size after expand:" + fringe.size());
			removePointlesspaths(fringe);
			if (debug)
				System.out
						.println("fringe size after removing pointless paths:"
								+ fringe.size());
			// pathfound = true;
		}

	}

	public void removePointlesspaths(ArrayList<node> fringe) {
		if (fringe.isEmpty())
			return;

		for (int i = 0; i < fringe.size(); i++) {
			if (fringe.get(i).history.contains("WEWE")
					|| fringe.get(i).history.contains("EWEW")
					|| fringe.get(i).history.contains("NSNS")
					|| fringe.get(i).history.contains("SNSN")) {
				fringe.remove(i);
				i--;
			}
		}
	}

	public ArrayList<node> expand(ArrayList<node> fringe, PacCell[][] grid,
			int lengthX, int lengthY) {

		// remove first node and repalce with 1 from a step in each direction
		node n = fringe.remove(0);
		if (debug) {
			System.out.println("node being expanded");
			System.out.println("eaten: " + n.eaten);
			System.out.println("history: " + n.history);
			n.info();
			System.out.println();
		}

		// step left
		if (n.x - 1 >= 0) { // check if step is within bounds
			node left = new node(n.x - 1, n.y, n.steps);
			left.addToHistory(n.history + "W");// update history
			left.eaten = n.eaten; // copy over history
//			left.food = n.food; // copy over eaten pellets
			for(int i = 0; i < n.food.size(); i++){
				left.food.add(n.food.get(i));
			}

			if (!(grid[left.x][left.y] instanceof WallCell)) {
				if (grid[left.x][left.y] instanceof FoodCell) {
					if (foundNewPellet(left)) {
						left.food.add(new pellet(left.x, left.y));
						++left.eaten;

					}
					if (left.eaten == numPellets) {
						pathfound = true;
						System.out.println("path found is: " + left.history);
					}
				}
				if (debug) {
					System.out.println("node from expansion (left)");
					System.out.println("eaten: " + left.eaten);
					System.out.println("history: " + left.history);
					System.out.println("food size: " + left.food.size()
							+ left.food.isEmpty());
					left.info();
					System.out.println();
				}
				fringe.add(left);
			}
		}

		// step up
		if (n.y - 1 >= 0) {// check if step is within bounds
			node up = new node(n.x, n.y - 1, n.steps);
			up.addToHistory(n.history + "N");
			up.eaten = n.eaten;
//			up.food = n.food;
			for(int i = 0; i < n.food.size(); i++){
				up.food.add(n.food.get(i));
			}
			if (!(grid[up.x][up.y] instanceof WallCell)) {
				if (grid[up.x][up.y] instanceof FoodCell) {
					if (foundNewPellet(up)) {
						up.food.add(new pellet(up.x, up.y));
						++up.eaten;

					}
					if (up.eaten == numPellets) {
						pathfound = true;
						System.out.println("path found is: " + up.history);
					}

				}
				if (debug) {
					System.out.println("node from expansion (up)");
					System.out.println("eaten: " + up.eaten);
					System.out.println("history: " + up.history);
					System.out.println("food size: " + up.food.size()
							+ up.food.isEmpty());
					up.info();
					System.out.println();
				}
				fringe.add(up);
			}
		}

		// step right
		if (n.x + 1 <= lengthX) {// check if step is within bounds
			node right = new node(n.x + 1, n.y, n.steps);
			right.addToHistory(n.history + "E");
			right.eaten = n.eaten;
			//right.food = n.food;
			for(int i = 0; i < n.food.size(); i++){
				right.food.add(n.food.get(i));
			}
			if (!(grid[right.x][right.y] instanceof WallCell)) {
				if (grid[right.x][right.y] instanceof FoodCell) {
					if (foundNewPellet(right)) {
						right.food.add(new pellet(right.x, right.y));
						++right.eaten;

					}
					if (right.eaten == numPellets) {
						pathfound = true;
						System.out.println("path found is: " + right.history);
					}
				}

				if (debug) {
					System.out.println("node from expansion (right)");
					System.out.println("eaten: " + right.eaten);
					System.out.println("history: " + right.history);
					System.out.println("food size: " + right.food.size()
							+ right.food.isEmpty());
					right.info();
					System.out.println();
				}
				fringe.add(right);
			}
		}

	
		// step down
		if (n.y + 1 <= lengthY) {// check if step is within bounds
			node down = new node(n.x, n.y + 1, n.steps);
			down.addToHistory(n.history + "S");
			down.eaten = n.eaten;
//			down.food = n.food;
			for(int i = 0; i < n.food.size(); i++){
				down.food.add(n.food.get(i));
			}
			if (!(grid[down.x][down.y] instanceof WallCell)) {
				if (grid[down.x][down.y] instanceof FoodCell) {
					if (foundNewPellet(down)) {
						down.food.add(new pellet(down.x, down.y));
						++down.eaten;

					}
					if (down.eaten == numPellets) {
						pathfound = true;
						System.out.println("path found is: " + down.history);
					}

				}
				if (debug) {
					System.out.println("node from expansion (down)");
					System.out.println("eaten: " + down.eaten);
					System.out.println("history: " + down.history);
					System.out.println("food size: " + down.food.size()
							+ down.food.isEmpty());
					down.info();
					System.out.println();
				}
				fringe.add(down);
			}
		}

		return fringe;
	}

	// returns true is this a new pellet added to node
	public boolean foundNewPellet(node n) {
		if (debug) {
			System.out.println("entered found new pellet");
			System.out.println(n.food.isEmpty());
			System.out.println(n.food.size());
		}
		if (n.food.size() == 0) {
			if (debug)
				System.out.println("food is empty");
			return true;
		}

		for (int i = 0; i < n.food.size(); i++) {
			if (debug)
				System.out.println("searching for new pellets");
			if (n.x == n.food.get(i).x && n.y == n.food.get(i).y) {
				if (debug)
					System.out.println("found a match at " + n.x + "," + n.y);
				return false;
			}
		}
		if (debug)
			System.out.println("no match found");
		return true;
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
		ArrayList<pellet> food;
		String history;

		public node(int x, int y, int steps) {
			this.eaten = 0;
			this.steps = steps + 1;
			this.x = x;
			this.y = y;
			this.food = new ArrayList<pellet>();
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

	/**
	 * class for storing what pellets have been seen by a path
	 */
	class pellet {
		int x;
		int y;

		public pellet(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}