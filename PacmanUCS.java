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
	private int numPellets;
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
		path = null;
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
			}

			try {
				for (;; j++) {
					pc = grid[0][j];
				}

			} catch (Exception e) {
				j--;

			}

			for (int k = 0; k < i; k++)
				for (int m = 0; m < j; m++) {
					if (grid[k][m] instanceof FoodCell)
						numPellets++;
				}

			path = findPath(grid, i, j);
		}
		if (path != "" && path.length() > 0) {
			char c = path.charAt(0);
			path = path.substring(1, path.length());

			switch (c) {
			case 'N':
				return PacFace.N;

			case 'S':
				return PacFace.S;

			case 'W':
				return PacFace.W;
			case 'E':
				return PacFace.E;

			}
		}

		return PacFace.N;
	}

	public String findPath(PacCell[][] grid, int lengthX, int lengthY) {
		// find starting point
		PacmanCell pacman = PacUtils.findPacman(grid);
		// starting node
		Node start = new Node(pacman.getX(), pacman.getY(), -1);
		// create fringe
		ArrayList<Node> fringe = new ArrayList<Node>();
		fringe.add(start);
		// create visited set
		// Set<Node> visited = new TreeSet<Node>();
		int count = 0;

		while (!fringe.isEmpty()) {
			System.out.println("count:" + count);
			// if (count >= 100)
			// return "";

			if (fringe.isEmpty()) {
				return null; // will crash
			}
			// get current node
			Node current = fringe.remove(0);
			System.out.print("Current node info ");
			current.info();
			if (isGoal(current, numPellets)) {
				System.out.println("Goal path: " + current.history);
				return current.history;
			}

			// step up
			if (current.location.y - 1 >= 0) {// check if step is within bounds
				System.out.println("in step up");
				Node up = createNode(current.location.x,
						current.location.y - 1, current, "N", grid);

				if (up != null) {
					// if step down and up
					if (current.history != ""
							&& current.history
									.charAt(current.history.length() - 1) == 'S') {
						// only add if current was a food cell
						if (grid[current.location.x][current.location.y] instanceof FoodCell) {
							count++;
							up.info();
							fringe.add(up);
						} else {
							// dont add
						}
					} else {
						// add possible step
						count++;
						up.info();
						fringe.add(up);
					}

				}
			}

			// step down
			if (current.location.y + 1 <= lengthY) {// check if step is within
													// bounds
				System.out.println("in step down");
				Node down = createNode(current.location.x,
						current.location.y + 1, current, "S", grid);

				if (down != null) {
					// if step down and up
					if (current.history != ""
							&& current.history
									.charAt(current.history.length() - 1) == 'N') {
						// only add if current was a food cell
						if (grid[current.location.x][current.location.y] instanceof FoodCell) {
							count++;
							down.info();
							fringe.add(down);
						} else {
							// dont add
						}
					} else {
						// add possible step
						count++;
						down.info();
						fringe.add(down);
					}

				}
			}

			// step left
			if (current.location.x - 1 >= 0) {// check if step is within
												// bounds
				System.out.println("in step left");
				Node left = createNode(current.location.x - 1,
						current.location.y, current, "W", grid);

				if (left != null) {
					// if step left and right
					if (current.history != ""
							&& current.history
									.charAt(current.history.length() - 1) == 'E') {
						// only add if current was a food cell
						if (grid[current.location.x][current.location.y] instanceof FoodCell) {
							count++;
							left.info();
							fringe.add(left);
						} else {
							// dont add
						}
					} else {
						// add possible step
						count++;
						left.info();
						fringe.add(left);
					}

				}
			}

			// step right
			if (current.location.x + 1 <= lengthX) {// check if step is within
				// bounds
				System.out.println("in step right");
				Node right = createNode(current.location.x + 1,
						current.location.y, current, "E", grid);

				if (right != null) {
					// if step left and right
					if (current.history != ""
							&& current.history
									.charAt(current.history.length() - 1) == 'W') {
						// only add if current was a food cell
						if (grid[current.location.x][current.location.y] instanceof FoodCell) {
							count++;
							right.info();
							fringe.add(right);
						} else {
							// dont add
						}
					} else {
						// add possible step
						count++;
						right.info();
						fringe.add(right);
					}

				}
			}

		}
		if (fringe.isEmpty())
			System.out.println("fringe is empty");
		return "";
	}

	public boolean isGoal(Node current, int i) {
		if (current.food.size() == i)
			return true;
		return false;
	}

	public Node createNode(int x, int y, Node previous, String direction,
			PacCell[][] grid) {

		Node node = new Node(x, y, previous.steps);
		node.addToHistory(previous.history + direction);
		for (Location temp : previous.food) {
			node.food.add(new Location(temp.x, temp.y));
		}

		// check for wall
		if (!(grid[x][y] instanceof WallCell)) {
			// check for food
			if (grid[x][y] instanceof FoodCell) {
				node.food.add(node.location);
				if (node.food.isEmpty())
					System.out.println("food is empty");

			}
		} else
			return null;
		return node;
	}

	/**
	 * this class will be on the fringe. it keeps track of the steps taken on
	 * each possible path. Also tracks how many pellets have been eaten
	 * 
	 * updated node based on changes we talked about
	 */
	class Node implements Comparable<Node> {
		int steps;
		Location location;
		Set<Location> food;
		String history;

		public Node(int x, int y, int steps) {
			this.steps = steps + 1;
			this.location = new Location(x, y);
			this.food = new TreeSet<Location>();
			this.history = "";
		}

		public void addToHistory(String s) {
			this.history = s;
		}

		@Override
		public int compareTo(Node other) {
			if (this.location.compareTo(other.location) == 0)
				return 0;
			return -1;
		}

		public void info() {
			System.out.println("x:" + this.location.x + " y:" + this.location.y
					+ " steps:" + this.steps + " food size:" + this.food.size()
					+ " history:" + this.history);
		}

	}

	/**
	 * class for storing position
	 */
	class Location implements Comparable<Location> {
		int x;
		int y;

		public Location(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void set(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Location other) {
			if (this.x == other.x && this.y == other.y)
				return 0;
			return -1;
		}
	}
}
