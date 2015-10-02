
/**
 *
 * @author Kevin Anderson
 * @author Andrea Castillo UCF CAP4630 Fall 2015
 *
 */
import java.awt.Point;
import java.io.File;
import java.util.*;

import javax.swing.JFileChooser;

import org.w3c.dom.Node;

import pacsim.FoodCell;
import pacsim.WallCell;
import pacsim.PacAction;
import pacsim.PacCell;
import pacsim.PacFace;
import pacsim.PacSim;
import pacsim.PacUtils;
import pacsim.PacmanCell;

public class PacmanUCS implements PacAction {

<<<<<<< HEAD
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
=======
    private Point target;
    private int numPellets;
    private String path;
    public boolean debug = false;

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

            for (int k = 0; k < i; k++) {
                for (int m = 0; m < j; m++) {
                    if (grid[k][m] instanceof FoodCell) {
                        numPellets++;
                    }
                }
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
        // visited
        ArrayList<Node> visited = new ArrayList<Node>();
        PriorityQueue<Node> fringe = new PriorityQueue<Node>();

        fringe.add(start);
        int count = 0;

        while (!fringe.isEmpty()) {
            if (debug) {
                System.out.println("count:" + count);
            }
//            if (count >= 60) {
//                return "";
//            }

            if (fringe.isEmpty()) {
                return null; // will crash
            }
            // get current node
//			System.out.println("\npop fringe");
            Node current = fringe.remove();
            if (debug) {
                System.out.print("Current node info ");
                current.info();
            }
            count++;
            if (isGoal(current, numPellets)) {
                System.out.println("count:" + count);

                System.out.println("Goal path: " + current.history);
                return current.history;
            }

            // add current node to visited
            addToSet(visited, current);
			// printFringe(fringe);

            // create possible nodes
            // step up
            if (current.location.y - 1 >= 0) {
                if (debug) {
                    System.out.println("\nstep up");
                }
                Node up = createNode(current.location.x,
                        current.location.y - 1, current, "N", grid);
                // if node is valid location
                if (up != null) {

                    if (!checkVisited(up, visited) || !checkFringe(up, fringe)) {
                        if (debug) {
                            System.out.println("up node added to fringe");
                        }
                        if (current.history != ""
                                && current.history.charAt(current.history
                                        .length() - 1) == 'S') {
                            if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                fringe.add(up);
                                if (debug) {
                                    up.info();
                                }
                            }
                        } else {
                            fringe.add(up);
                            if (debug) {
                                up.info();
                            }
                        }

                    } else if (checkFringe(up, fringe)) {
                        Node temp = findLowerNode(up, fringe);
//                        int counterUp = numOfOccurance(up);
//                        int countTemp = numOfOccurance(temp);
//                        if (countTemp < counterUp) {
//
//                        } else if (countTemp > counterUp) {
                        if (up.steps < temp.steps) {
                            System.out.println(" > ");
                            if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'S') {
                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                    fringe.remove(temp);
                                    fringe.add(up);
                                }
                            } else {
                                fringe.remove(temp);
                                fringe.add(up);
                            }
                        } //                        Node temp = findLowerNode(up, fringe);
                        //                        if (up.food.size() == temp.food.size()) {
                        //                            if (debug) {
                        //                                System.out.println(" food equal ");
                        //                                temp.info();
                        //                            }
                        ////                            if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'S') {
                        ////                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                        ////                                    fringe.remove(temp);
                        ////                                    fringe.add(up);
                        ////                                }
                        ////                            } else {
                        ////                                fringe.remove(temp);
                        ////                                fringe.add(up);
                        ////                            }
                        //                        } 
                        else if (up.steps == temp.steps) {

                            if (current.history != ""
                                    && current.history.charAt(current.history
                                            .length() - 1) == 'S') {
                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                    fringe.add(up);
                                    if (debug) {
                                        up.info();
                                    }
                                }
                            } else {
                                fringe.add(up);
                                if (debug) {
                                    up.info();
                                }
                            }

                        }
                    }
                }
            }

            // step down
            if (current.location.y + 1 <= lengthY) {
                if (debug) {
                    System.out.println("\nstep down");
                }
                Node down = createNode(current.location.x,
                        current.location.y + 1, current, "S", grid);
                // if node is valid location
                if (down != null) {
                    if (!checkVisited(down, visited) || !checkFringe(down, fringe)) {
//						System.out.println("down node added to fringe");
                        if (current.history != ""
                                && current.history.charAt(current.history
                                        .length() - 1) == 'N') {
                            if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                fringe.add(down);
                                if (debug) {
                                    down.info();
                                }
                            }
                        } else {
                            if (debug) {
                                down.info();
                            }
                            fringe.add(down);
                        }
                    } else if (checkFringe(down, fringe)) {
                        Node temp = findLowerNode(down, fringe);
//                        int counterDown = numOfOccurance(down);
//                        int countTemp = numOfOccurance(temp);
//                        if (countTemp < counterDown) {
//
//                        } else if (countTemp > counterDown) {
                        if (down.steps > temp.steps) {
                            System.out.println(" < ");

                            if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'N') {
                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                    fringe.remove(temp);
                                    fringe.add(down);
                                }
                            } else {
                                fringe.remove(temp);
                                fringe.add(down);
                            }
                        } //                        Node temp = findLowerNode(down, fringe);
                        //                        if (down.food.size() == temp.food.size()) {
                        //                            if (debug) {
                        //                                System.out.println(" food equal ");
                        //                                temp.info();
                        //                            }
                        ////                            if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'N') {
                        ////                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                        ////                                    fringe.remove(temp);
                        ////                                    fringe.add(down);
                        ////                                }
                        ////                            } else {
                        ////                                fringe.remove(temp);
                        ////                                fringe.add(down);
                        ////                            }
                        //                        } 
                        else if (down.steps == temp.steps) {
                            

                            if (current.history != ""
                                    && current.history.charAt(current.history
                                            .length() - 1) == 'N') {
                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                    fringe.add(down);
                                    if (debug) {
                                        down.info();
                                    }
                                }
                            } else {
                                if (debug) {
                                    down.info();
                                }
                                fringe.add(down);
                            }
                        }
                    }
                }
            }

            if (current.location.x + 1 <= lengthX) {
                if (debug) {
                    System.out.println("STEP right E");
                }
                Node right = createNode(current.location.x + 1,
                        current.location.y, current, "E", grid);
                if (right != null) {
                    if (!checkVisited(right, visited) || !checkFringe(right, fringe)) {
//						System.out.println("right node added to fringe");
                        if (current.history != ""
                                && current.history.charAt(current.history
                                        .length() - 1) == 'W') {
                            if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                if (debug) {
                                    right.info();
                                }
                                fringe.add(right);
                            }
                        } else {
                            fringe.add(right);
                            if (debug) {
                                right.info();
                            }
                        }
                    } else if (checkFringe(right, fringe)) {

                        Node temp = findLowerNode(right, fringe);
//                        int counterRight = numOfOccurance(right);
//                        int countTemp = numOfOccurance(temp);
//                        if (countTemp < counterRight) {
//
//                        } else if (countTemp > counterRight && right.food.size() > temp.f) {
                        if (right.steps > temp.steps) {
                            System.out.println(" < ");

                            if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'W') {
                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                    fringe.remove(temp);
                                    fringe.add(right);
                                }
                            } else {
                                fringe.remove(temp);
                                fringe.add(right);
                            }
                        } //                        if (right.food.size() == temp.food.size()) {
                        //                            if (debug) {
                        //                                System.out.println(" food equal ");
                        //                                temp.info();
                        //                            }
                        ////                            if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'W') {
                        ////                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                        ////                                    fringe.remove(temp);
                        ////                                    fringe.add(right);
                        ////                                }
                        ////                            } else {
                        ////                                fringe.remove(temp);
                        ////                                fringe.add(right);
                        ////                            }
                        else if (right.steps == temp.steps) {
                            

                            if (current.history != ""
                                    && current.history.charAt(current.history
                                            .length() - 1) == 'W') {
                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                    if (debug) {
                                        right.info();
                                    }
                                    fringe.add(right);
                                }
                            } else {
                                fringe.add(right);
                                if (debug) {
                                    right.info();
                                }
                            }
                        }
                    }
                }
            }

            if (current.location.x - 1 >= 0) {
                if (debug) {
                    System.out.println("step left W");
                }
                Node left = createNode(current.location.x - 1,
                        current.location.y, current, "W", grid);
                if (left != null) {
                    if (!checkVisited(left, visited) || !checkFringe(left, fringe)) {
//						System.out.println("left node added to fringe");
                        if (current.history != ""
                                && current.history.charAt(current.history
                                        .length() - 1) == 'E') {
                            if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                if (debug) {
                                    left.info();
                                }
                                fringe.add(left);
                            }
                        } else {
                            if (debug) {
                                left.info();
                            }
                            fringe.add(left);
                        }
                    } else if (checkFringe(left, fringe)) {

                        Node temp = findLowerNode(left, fringe);
//                        int counterLeft = numOfOccurance(left);
//                        int countTemp = numOfOccurance(temp);
//                        if (countTemp < counterLeft) {
//
//                        } else if (countTemp > counterLeft) {
                        if (left.steps > temp.steps) {
                            System.out.println(" < ");

                            if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'E') {
                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                    fringe.remove(temp);
                                    fringe.add(left);
                                }
                            } else {
                                fringe.remove(temp);
                                fringe.add(left);
                            }
                        } //                        if (left.food.size() == temp.food.size()) {
                        //                            if (debug) {
                        //                                System.out.println(" food equal ");
                        //                                temp.info();
                        //                            }
                        ////                            if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'E') {
                        ////                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                        ////                                    fringe.remove(temp);
                        ////                                    fringe.add(left);
                        ////                                }
                        ////                            } else {
                        ////                                fringe.remove(temp);
                        ////                                fringe.add(left);
                        ////                            }
                        //                        }
                        else if (left.steps == temp.steps) {
                            

                            if (current.history != ""
                                    && current.history.charAt(current.history
                                            .length() - 1) == 'E') {
                                if (grid[current.location.x][current.location.y] instanceof FoodCell) {
                                    if (debug) {
                                        left.info();
                                    }
                                    fringe.add(left);
                                }
                            } else {
                                if (debug) {
                                    left.info();
                                }
                                fringe.add(left);
                            }
                        }
                    }

                }
            }

        }
        if (fringe.isEmpty()) {
            System.out.println("fringe is empty");
        }
        return "";
    }

    public int numOfOccurance(Node temp) {
        int tempCounter = 0;
        for (Location n : temp.locationHistory) {
            if (n.x == temp.location.x
                    && n.y == temp.location.y) {
                tempCounter++;
            }
        }

        return tempCounter;
    }

    /**
     * finds matching node within fringe
     */
    public Node findLowerNode(Node current, PriorityQueue<Node> fringe) {

//        Object[] list = fringe.toArray();
//        if (list != null) {
//            for (int i = 0; i < list.length; i++) {
//                Node n = (Node) list[i];
//                if (n.location.compareTo(current.location) == 0) {
//                    return n;
//
//                }
//            }
//        }
        for (Node n : fringe) {
            if (n.location.compareTo(current.location) == 0) {
//                System.out.println(n.location.x + " " + n.location.y);
                return n;

            }
        }
        return null;
    }

    /**
     * adds a node to the set if it doesn't already exists based only on
     * location of node
     */
    public void addToSet(ArrayList<Node> visited, Node node) {
        boolean exists = false;

        for (Node n : visited) {
            if (n.location.x == node.location.x
                    && n.location.y == node.location.y) {
                return;
            }
        }
        visited.add(node);
    }

    public void printFringe(PriorityQueue<Node> fringe) {
        // System.out.println("Nodes in fringe");
        Object[] temp = fringe.toArray();
        for (int i = 0; i < temp.length; i++) {
            Node n = (Node) temp[i];
            n.info();
        }
    }

    public void printVisited(Set<Node> visited) {
        // System.out.println("Nodes in visited");
        for (Node n : visited) {
            n.info();
        }

    }

    public boolean isGoal(Node current, int i) {
        if (current.food.size() == i) {
            return true;
        }
        return false;
    }

    /**
     * creates a new node. updates steps, history, historylocations and checks
     * if location is a pellet
     */
    public Node createNode(int x, int y, Node previous, String direction,
            PacCell[][] grid) {
        int index = 0;
        Node node = new Node(x, y, previous.steps);
        for (Location temp : previous.locationHistory) {
            node.locationHistory.add(new Location(temp.x, temp.y));
        }
        node.addToHistory(previous.history + direction, x, y);

        for (Location temp : previous.food) {
            node.food.add(new Location(temp.x, temp.y));
        }

        // check for wall
        if (!(grid[x][y] instanceof WallCell)) {
            // check for food
            if (grid[x][y] instanceof FoodCell) {
                node.food.add(node.location);
                if (node.food.isEmpty()) {
                    if (debug) {
                        System.out.println("food is empty");
                    }
                }

            }
        } else {
            return null;
        }

        for (int i = 0; i < node.locationHistory.size(); i++) {
            if (node.locationHistory.get(i).x == previous.location.x
                    && node.locationHistory.get(i).y == previous.location.y) {
                i++;
                if (i < node.locationHistory.size() && (node.locationHistory.get(i).x == x
                        && node.locationHistory.get(i).y == y)) {
                    index++;

                }
            }
        }
        if (index >= 2) {
            if (debug) {
                System.out.println("SUBPATH");
            }
            return null;
        }
        return node;
    }

    /**
     * checks if a node exists within the fringe
     */
    public boolean checkFringe(Node current, PriorityQueue<Node> fringe) {
        if (debug) {
            System.out.println("checking fringe");
        }
        if (fringe.isEmpty()) {
            if (debug) {
                System.out.println("fringe is empty");
            }
            return false;
        }

        Object[] list = fringe.toArray();
        Node n;

        for (int i = 0; i < list.length; i++) {
            n = (Node) list[i];
            if (n.location.x == current.location.x
                    && n.location.y == current.location.y) {
                if (debug) {
                    System.out.println("matching location found in fringe");
                }
                return true;
            }
        }
        if (debug) {
            System.out.println("not in fringe");
        }
        return false;
    }

    /**
     * checks if a node exists within visited
     */
    public boolean checkVisited(Node current, ArrayList<Node> visited) {
        if (debug) {
            System.out.println("checking visited");
        }
        for (Node n : visited) {
            if (n.location.x == current.location.x
                    && n.location.y == current.location.y) {
                if (debug) {
                    System.out.println("Found match in visited");
                }
                return true;
            }
        }
        if (debug) {
            System.out.println("not in visited");
        }
        return false;
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
        ArrayList<Location> locationHistory;

        public Node(int x, int y, int steps) {
            this.steps = steps + 1;
            this.location = new Location(x, y);
            this.food = new TreeSet<Location>();
            this.history = "";
            this.locationHistory = new ArrayList<Location>();

        }

        public void addToHistory(String s, int x, int y) {
            this.locationHistory.add(new Location(x, y));
            this.history = s;
        }

        @Override
        public int compareTo(Node other) {
            // if at the same location
            if (this.steps == other.steps) {
                return 0;
            }
            if (this.steps < other.steps) {
                return -1;
            } else {
                return 1;
            }

        }

        public void info() {
            System.out.println("x:" + this.location.x + " y:" + this.location.y
                    + " steps:" + this.steps + " food size:" + this.food.size()
                    + " history:" + this.history);
            for (Location x : this.locationHistory) {
                System.out.print("(" + x.x + "," + x.y + ") ");
            }
            System.out.println();
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
            if (this.x == other.x && this.y == other.y) {
                return 0;
            }
            return -1;
        }
    }
>>>>>>> andrea
}
