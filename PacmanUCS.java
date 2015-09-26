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
		// visited
		Set<Node> visited = new TreeSet<Node>();
		fringe.add(start);
		int count = 0;

		while (!fringe.isEmpty()) {
			System.out.println("count:" + count);
			if (count >= 4)
				return "";

			if (fringe.isEmpty()) {
				return null; // will crash
			}
			// get current node
			Node current = fringe.remove(0);
			System.out.print("Current node info ");
			current.info();
			count++;
			if (isGoal(current, numPellets)) {
				System.out.println("Goal path: " + current.history);
				return current.history;
			}
			printVisited(visited);
			printFringe(fringe);
			// add current node to visited
			visited.add(current);

			// step up
			if (current.location.y - 1 >= 0) {// check if step is within bounds
				System.out.println("in step up");
				Node up = createNode(current.location.x,
						current.location.y - 1, current, "N", grid);
				
				if(up != null){
					//if(!visited.contains(up) || !fringe.contains(up)){
					if(!checkVisited(up, visited) || !checkFringe(up, fringe)){
						if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'S'){
							// only add if current was a food cell
							if (grid[current.location.x][current.location.y] instanceof FoodCell) {
								up.info();
								fringe.add(up);
							}
						}
						else{
							up.info();
							fringe.add(up);
						}
						
					}
					else if(checkFringe(up, fringe)){
						System.out.println("in fringe");
						int index = fringe.indexOf(up);
						if(fringe.get(index).steps < up.steps){
							System.out.println("replaced node in fringe");
							fringe.remove(index);
							fringe.add(index, up);
							up.info();
						}
							
					}
				}
			}

//			// step up
//			if (current.location.y - 1 >= 0) {// check if step is within bounds
//				System.out.println("in step up");
//				Node up = createNode(current.location.x,
//						current.location.y - 1, current, "N", grid);
//
//				if (up != null) {
//					
//					// check fringe and visited
//					if (!visited.contains(up) || !fringe.contains(up)) {
//						// if step down and up
//						if (current.history != "" && current.history.charAt(current.history.length() - 1) == 'S') {
//							// only add if current was a food cell
//							if (grid[current.location.x][current.location.y] instanceof FoodCell) {
//
//								up.info();
//								fringe.add(up);
//							} else {
//								// dont add
//							}
//						} else {
//							// add possible step
//
//							up.info();
//							fringe.add(up);
//						}
//					} else if (fringe.contains(up)){
//						
//						System.out.println("up is in fringe");
//						for (int i = 0; i < fringe.size(); i++) {
//							if (fringe.get(i).compareTo(up) == -1) {
//								fringe.remove(i);
//								i--;
//							}
//						}
//					
//					}
//				}
//			}

			// step down
			if (current.location.y + 1 <= lengthY) {// check if step is within
													// bounds
				System.out.println("in step down");
				Node down = createNode(current.location.x,
						current.location.y + 1, current, "S", grid);

				if (down != null) {
					if (!visited.contains(down) || !fringe.contains(down)) {
						// if step down and up
						if (current.history != ""
								&& current.history.charAt(current.history
										.length() - 1) == 'N') {
							// only add if current was a food cell
							if (grid[current.location.x][current.location.y] instanceof FoodCell) {

								down.info();
								fringe.add(down);
							} else {
								// dont add
							}
						} else {
							// add possible step

							down.info();
							fringe.add(down);
						}

					} else if (fringe.contains(down)) {
						System.out.println("down is in fringe");
						for (int i = 0; i < fringe.size(); i++) {
							if (fringe.get(i).compareTo(down) == -1) {
								fringe.remove(i);
								i--;
							}
						}
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
					if (!visited.contains(left) || !fringe.contains(left)) {
						// if step left and right
						if (current.history != ""
								&& current.history.charAt(current.history
										.length() - 1) == 'E') {
							// only add if current was a food cell
							if (grid[current.location.x][current.location.y] instanceof FoodCell) {

								left.info();
								fringe.add(left);
							} else {
								// dont add
							}
						} else {
							// add possible step

							left.info();
							fringe.add(left);
						}

					} else if (fringe.contains(left)) {
						System.out.println("left is in fringe");
						for (int i = 0; i < fringe.size(); i++) {
							if (fringe.get(i).compareTo(left) == -1) {
								fringe.remove(i);
								i--;
							}
						}
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
					if (!visited.contains(right) || !fringe.contains(right)) {
						// if step left and right
						if (current.history != ""
								&& current.history.charAt(current.history
										.length() - 1) == 'W') {
							// only add if current was a food cell
							if (grid[current.location.x][current.location.y] instanceof FoodCell) {

								right.info();
								fringe.add(right);
							} else {
								// dont add
							}
						} else {
							// add possible step

							right.info();
							fringe.add(right);
						}

					} else if (fringe.contains(right)) {
						System.out.println("right is in fringe");
						for (int i = 0; i < fringe.size(); i++) {
							if (fringe.get(i).compareTo(right) == -1) {
								fringe.remove(i);
								i--;
							}
						}
					}
				}
			}

		}
		if (fringe.isEmpty())
			System.out.println("fringe is empty");
		return "";
	}

	public void printFringe(ArrayList<Node> fringe) {
		System.out.println("Nodes in fringe");
		for (Node n : fringe) {
			n.info();
		}
	}

	public void printVisited(Set<Node> visited) {
		System.out.println("Nodes in visited");
		for (Node n : visited) {
			n.info();
		}

	}

	public boolean isGoal(Node current, int i) {
		if (current.food.size() == i)
			return true;
		return false;
	}

	public Node createNode(int x, int y, Node previous, String direction,
			PacCell[][] grid) {

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
				if (node.food.isEmpty())
					System.out.println("food is empty");

			}
		} else
			return null;
		return node;
	}

	public boolean checkFringe( Node current, ArrayList<Node> fringe){
		System.out.println("checking fringe");
		for(int i = 0; i < fringe.size(); i++){
			if(fringe.get(i).location.x == current.location.x && fringe.get(i).location.y == current.location.y){
				System.out.println("found in fringe");
				return true;
			}
		}
		return false;
	}
	
	public boolean checkVisited( Node current, Set<Node> visited){
		System.out.println("checking visited");
		if(visited.contains(current)){
			System.out.println("found in visited");
			return true;
		}
//		Node[] list = visited.toArray();
//		
//		for(int i = 0; i < list.length; i++){
//			if(list[i].location.x == current.location.x && list[i].location.y == current.location.y){
//				System.out.println("found in visited");
//				return true;
//			}
//		}
		System.out.println("not in visited");
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
			if (this.location.compareTo(other.location) == 0) 
				return 0;
			else 
				return 1;
				
		}

		public void info() {
			System.out.println("x:" + this.location.x + " y:" + this.location.y
					+ " steps:" + this.steps + " food size:" + this.food.size()
					+ " history:" + this.history);
			for(Location x: this.locationHistory){
				System.out.print("("+ x.x + "," + x.y+") ");
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
			if (this.x == other.x && this.y == other.y)
				return 0;
			return -1;
		}
	}
}