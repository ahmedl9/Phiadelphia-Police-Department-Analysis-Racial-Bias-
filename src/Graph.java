import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


//NOTE: This class is similar to one used in Ahmed Lone's CIS 121 Final Project, implementing a
//Directed Graph using a node class that is also similar though not exactly the same as the one from the
//CIS 121 final project.
public class Graph {
	//give an in-degree graph representation, give an out-degree graph representation
	Map<Node, Set<Node>> outAdj = new HashMap<>();
	Map<Node, Set<Node>> inAdj = new HashMap<>();
	
	public Graph(Collection<Node> collection) {
		for (Node n: collection) {
			if (!inAdj.containsKey(n)) {
				inAdj.put(n, new HashSet<Node>());
			}
			outAdj.put(n, n.getNeighbors());
			if (n.getNeighbors() != null) {
				for (Node out : n.getNeighbors()) {
					if (inAdj.get(out) == null || !inAdj.containsKey(out)) {
						inAdj.put(out,  new HashSet<Node>());
					}
					inAdj.get(out).add(n);
				}
			};
		}
	}
	
	public Set<Node> vertexSet() {
		return outAdj.keySet();
	}

	public Set<Node> inNeighbors(Node vertex) {
		return inAdj.get(vertex);
	}

	public Set<Node> outNeighbors(Node vertex) {
		return outAdj.get(vertex);
	}
}
