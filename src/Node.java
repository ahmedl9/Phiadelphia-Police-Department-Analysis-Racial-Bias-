import java.util.HashSet;
import java.util.Set;


//This class implements a node in our directed graph, containing the page information
public class Node implements Comparable<Node>{
	
	//String title; use String id from AbstractIdentifiable instead
	String abuse;
	Set<Node> outbound = new HashSet<Node>();
	String title;
	String ethnicity;
	String district; 
	String gender;
	
	//People/Complaints.
	protected Node(String name, String problem, String race, String dist) {
		title = name;
		abuse = problem;
		ethnicity = race;
		district = dist;
	}
	
	//Cops
    protected Node(String name, String race, String sex){
        title = name;
        ethnicity = race;
        gender = sex;
    }

	//Districts (just names)
	protected Node(String name) {
	    //find someway of getting the title
		title = name;
		abuse = null;
		ethnicity = null;
	}
	
	public String getAbuse() {
		return abuse;
	}
	
	public void setAbuse(String text) {
		abuse = text;
	}
	
	public String getEthnicity() {
		return ethnicity;
	}
	
	public void setEthnicity(String text) {
		ethnicity = text;
	}
	
	public String getTitle() {
		return title;
	}
	public String getGender() {
        return gender;
    }
	
	public void setNeighbors(Set<Node> toadd){
		outbound = toadd;
	}
	
	public Set<Node> getNeighbors() {
		return outbound;
	}
	
	//consider removing if node equality doesn't matter. 
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Node)) {
			return false;
		}
		if (this.title.equals(((Node) o).getTitle())) {
			return true;
		}
		return false;
	} 
	//new compareTo function for our Nodes
    @Override
    public int compareTo(Node o) {
        if(this.getTitle().equals(o.getTitle())
                && this.getEthnicity().equals(o.getEthnicity()) 
                && this.getGender().equals(o.getGender())){
            return 1;
        }
        else return -1;
    }
}
