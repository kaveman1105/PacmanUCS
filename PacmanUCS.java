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
		debug = true;
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

			path = findPath(grid, i, j);
		}
		// need function
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
//			if (count >= 100)
//				return "";

			if (fringe.isEmpty()) {
				return null; // will crash
			}
			// get current node
			Node current = fringe.remove(0);
			System.out.print("Current node info ");
			current.info();
			if (isGoal(current, numPellets)){
				System.out.println("Goal path: "+ current.history);
				return current.history;
			}

			// visited.add(current);

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

	public boolean lowCostNode(Node current, ArrayList<Node> fringe) {
		for (int index = 0; index < fringe.size(); index++) {
			if (fringe.get(index).food.size() == current.food.size()
					&& fringe.get(index).location.compareTo(current.location) == 0) {
				if (fringe.get(index).steps == current.steps) {
					return true;
				} else if (fringe.get(index).steps > current.steps) {
					fringe.remove(index);
					for (; index < fringe.size(); index++) {
						if (fringe.get(index).food.size() == current.food
								.size()
								&& fringe.get(index).location
										.compareTo(current.location) == 0) {
							fringe.remove(index);
							index--;
						}
					}
					return true;
				}
			}
		}
		return false;
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

//
// // step right
//
// if (current.location.x + 1 <= lengthX) {// check if step is within
// // bounds
// System.out.println("in step right");
// Node right = createNode(current.location.x + 1,
// current.location.y, current, "E", grid);
// if (right != null) {
// count++;
//
// if (!visited.contains(right) && !fringe.contains(right)) {
// if (!(grid[current.location.x][current.location.y] instanceof FoodCell)) {
// if (current.history != ""
// && current.history.charAt(current.history
// .length() - 1) == 'W') {
// // do nothing
// } else {
// fringe.add(right);
// right.info();
// }
// } else {
// fringe.add(right);
// right.info();
// }
// } else if (!(grid[current.location.x][current.location.y] instanceof
// FoodCell)) {
// if (current.history != ""
// && current.history.charAt(current.history
// .length() - 1) == 'W') {
// // do nothing
// }
// } else if (fringe.contains(right)) {
// // function needed
// if (lowCostNode(right, fringe)) {
// fringe.add(right);
// right.info();
//
// }
// }
// }
//
// }