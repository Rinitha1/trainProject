package com.rinitha.trains;
import java.util.ArrayList;
import java.util.Hashtable;

public class Routes {
	public Hashtable<Node, Edge> routeTable;

	public Routes() {
		this.routeTable = new Hashtable<Node, Edge>();
	}

	
	public int distanceBetween(ArrayList<Node> cities) throws Exception {

		if(cities.size() < 2)
			return 0;
		int distance, depth, i;
		distance = depth = i = 0;

	
		while(i < cities.size() - 1) {
			if(this.routeTable.containsKey(cities.get(i))) {
				Edge route = this.routeTable.get(cities.get(i));
				
				while(route != null) {
					if(route.destination.equals(cities.get(i + 1))) {
						distance += route.weight;
						depth++;
						break;
					}
					route = route.next;
				}
			}
			else
				throw new Exception("NO SUCH ROUTE");
			i++;
		}
		
		if(depth != cities.size() - 1)
			throw new Exception("NO SUCH ROUTE");

		return distance;
	}

	
	public int numStops(Node start, Node end, int maxStops) throws Exception{
		//Wrapper to maintain depth of traversal
		return findRoutes(start, end, 0, maxStops);
	}

	
	private int findRoutes(Node start, Node end, int depth, int maxStops) throws Exception{
		int routes = 0;
		//Check if start and end nodes exists in route table
		if(this.routeTable.containsKey(start) && this.routeTable.containsKey(end)) {
		
			depth++;
			if(depth > maxStops)		//Check if depth level is within limits
				return 0;
			start.visited = true;		//Mark start node as visited
			Edge edge = this.routeTable.get(start);
			while(edge != null) {
				
				if(edge.destination.equals(end)) {
					routes++;
					edge = edge.next;
					continue;
				}
				
				else if(!edge.destination.visited) {
					routes += findRoutes(edge.destination, end, depth, maxStops);
					depth--;
				}
				edge = edge.next;
			}
		}
		else
			throw new Exception("NO SUCH ROUTE");

	
		start.visited = false;
		return routes;
	}

	
	public int shortestRoute(Node start, Node end) throws Exception {
		//Wrapper to maintain weight
		return findShortestRoute(start, end, 0, 0);

	}

	
	private int findShortestRoute(Node start, Node end, int weight, int shortestRoute) throws Exception{
		//Check if start and end nodes exists in route table
		if(this.routeTable.containsKey(start) && this.routeTable.containsKey(end)) {
			
			start.visited = true;		//Mark start node as visited
			Edge edge = this.routeTable.get(start);
			while(edge != null) {
				//If node not already visited, or is the destination, increment weight
				if(edge.destination == end || !edge.destination.visited)
					weight += edge.weight;

				
				if(edge.destination.equals(end)) {
					if(shortestRoute == 0 || weight < shortestRoute)
						shortestRoute = weight;
					start.visited = false;
					return shortestRoute; 			//Unvisit node and return shortest route
				}
				
				else if(!edge.destination.visited) {
					shortestRoute = findShortestRoute(edge.destination, end, weight, shortestRoute);
					//Decrement weight as we backtrack
					weight -= edge.weight;
				}
				edge = edge.next;
			}
		}
		else
			throw new Exception("NO SUCH ROUTE");

		
		start.visited = false;
		return shortestRoute;

	}

	
	public int numRoutesWithin(Node start, Node end, int maxDistance) throws Exception {
		return findnumRoutesWithin(start, end, 0, maxDistance);
	}


	private int findnumRoutesWithin(Node start, Node end, int weight, int maxDistance) throws Exception{
		int routes = 0;
		//Check if start and end nodes exists in route table
		if(this.routeTable.containsKey(start) && this.routeTable.containsKey(end)) {
		
			Edge edge = this.routeTable.get(start);
			while(edge != null) {
				weight += edge.weight; 
				
				if(weight <= maxDistance) {
					if(edge.destination.equals(end)) {
						routes++;
						routes += findnumRoutesWithin(edge.destination, end, weight, maxDistance);
						edge = edge.next;
						continue;
					}
					else {
						routes += findnumRoutesWithin(edge.destination, end, weight, maxDistance);
						weight -= edge.weight;	//Decrement weight as we backtrack
					}
				}
				else 
					weight -= edge.weight;

				edge = edge.next;	
			}
		}
		else
			throw new Exception("NO SUCH ROUTE");

		return routes;

	}	
}