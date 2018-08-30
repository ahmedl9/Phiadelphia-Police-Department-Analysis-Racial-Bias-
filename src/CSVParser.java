import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVParser {
    //Initializing our required Maps
    private static HashMap<String, StringBuilder> idToSummary = new HashMap<String, StringBuilder>();
    private static HashMap<String, String> idToAbuse = new HashMap<String, String>();
    private static HashMap<String, String> idToDistrict = new HashMap<String, String>();
    private static HashMap<String, TreeSet<String>> districtToType = new HashMap<String, TreeSet<String>>();
    private static HashMap<String, TreeSet<String>> idToCops = new HashMap<String, TreeSet<String>>();

    // Function reads the given csv file and creates the adjacency lists desired
    public static void readFileIntoList() throws Exception {
        String csvFile1 = "ppd_complaints.csv";
        String csvFile2 = "ppd_complaint_disciplines.csv";
        String splitter = ",";
        boolean header = true;
        FileReader f1 = new FileReader(csvFile1);
        BufferedReader b1 = new BufferedReader(f1);
        String line = b1.readLine();
        while (line != null) {
            String[] text = line.split(splitter);
            if (header == false) {
                idToSummaryMap(text);
                idToAbuseMap(text);
                idToDistrictMap(text);
                districtToTypeMap(text);
            }
            header = false;
            line = b1.readLine();
        }
        b1.close();
        header = true;
        FileReader f2 = new FileReader(csvFile2);
        BufferedReader b2 = new BufferedReader(f2);
        line = b2.readLine();
        while (line != null) {
            String[] text = line.split(splitter);
            if (header == false) {
                idToCopsMap(text);
            }
            header = false;
            line = b2.readLine();
        }
        b2.close();
    }

    /**
     * Creates a map of police abuse cases to the cops responsible
     * @param csvParts that is our parsed CSV file
     */
    public static HashMap<String, TreeSet<String>> idToCopsMap(String[] csvParts) throws IOException {
        // 4th index is where the summary starts
        TreeSet<String> cops = new TreeSet<String>();
        if (idToCops.containsKey(csvParts[0])) {
            cops = idToCops.get(csvParts[0]);
        }
        StringBuilder copDetails = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            copDetails.append(csvParts[i] + " ");
        }
        if (!csvParts[1].equals("UNK")) {
            cops.add(copDetails.toString());
            idToCops.put(csvParts[0], cops);
            return idToCops;
        }
        return null;
    }

    /**
     * Takes in a string[] of text and uses it to build a map of crime ids to descriptions
     * (Creates a map of police abuse cases to the summary of the case)
     * @param csvParts that is our parsed CSV file
     */
    public static HashMap<String, StringBuilder> idToSummaryMap(String[] csvParts) throws IOException {
        // 4th index is where the summary starts
        StringBuilder crimeSummary = new StringBuilder();
        for (int i = 4; i < csvParts.length; i++) {
            crimeSummary.append(csvParts[i]);
        }
        idToSummary.put(csvParts[0], crimeSummary);
        return idToSummary;
    }
    /**
     * Creates a map of police abuse cases to the abuse type
     * @param csvParts that is our parsed CSV file
     */
    public static HashMap<String, String> idToAbuseMap(String[] csvParts) throws IOException {
        // 4th index is where the summary starts
        StringBuilder crimeSummary = new StringBuilder();
        for (int i = 4; i < csvParts.length; i++) {
            crimeSummary.append(csvParts[i]);
        }
        idToAbuse.put(csvParts[0], csvParts[3]);
        return idToAbuse;
    }
    
    /**
     * Creates a map of police abuse cases to the districts they occured in
     * @param csvParts that is our parsed CSV file
     */
    public static HashMap<String, String> idToDistrictMap(String[] csvParts) throws IOException {
        // 4th index is where the summary starts
        StringBuilder crimeSummary = new StringBuilder();
        for (int i = 4; i < csvParts.length; i++) {
            crimeSummary.append(csvParts[i]);
        }
        idToDistrict.put(csvParts[0], csvParts[2]);
        return idToDistrict;
    }
    
    /**Takes in a string[] of text and uses it to build a map of districts to all type of
     * crimes that have occurred within the district (creates a map of police abuse cases to the
     * cops responsible)
     * @param csvParts that is our parsed CSV file
     */
    public static HashMap<String, TreeSet<String>> districtToTypeMap(String[] csvParts) throws IOException {
        if (districtToType.containsKey(csvParts[2])) {
            TreeSet<String> crimes = districtToType.get(csvParts[2]);
            crimes.add(csvParts[3]);
            districtToType.put(csvParts[2], crimes);
        } else {
            TreeSet<String> crimes = new TreeSet<String>();
            crimes.add(csvParts[3]);
            districtToType.put(csvParts[2], crimes);
        }
        return districtToType;
    }

    public static void main(String[] args) throws Exception {
        readFileIntoList();
        double B = 0;
        double W = 0;
        double A = 0;
        double H = 0;
        double O = 0;
        HashMap<String, Set<Node>> districtMap = new HashMap<String, Set<Node>>();
        HashMap<String, Set<Node>> abuseMap = new HashMap<String, Set<Node>>();
        Set<Node> allVertices = new HashSet<Node>();
        Set<Node> empty = new HashSet<Node>();

        // Tests whether districtToType map is instantiated well
        for (String district : districtToType.keySet()) {
            districtMap.put(district, empty);
        }

        Set<String> abuses = new HashSet<String>(idToAbuse.values());
        // Tests whether districtToType map is instantiated well
        for (String abuse : abuses) {
            // System.out.println("abuse is " + abuse);
            abuseMap.put(abuse, empty);
        }

        // Tests whether idToSummary map is instantiated well
        for (String id : idToSummary.keySet()) {
            // System.out.println(id + " " + idToSummary.get(id));
            String district = idToDistrict.get(id);
            String abuse = idToAbuse.get(id);

            StringBuilder text = idToSummary.get(id);
            String stringText = text.toString();
            // We use Regex to get the required demographic info of the person filing the complaint
            Pattern demographicInfoPattern = Pattern.compile(".././.");
            Matcher demographicInfoMatcher = demographicInfoPattern.matcher(stringText);
            String race = null;
            if (demographicInfoMatcher.find()) {
                char raceInitial = demographicInfoMatcher.group().charAt(3);
                if (raceInitial == 'B') {
                    race = "black";
                    B++;
                } else if (raceInitial == 'A') {
                    race = "asian";
                    A++;
                } else if (raceInitial == 'W') {
                    race = "white";
                    W++;
                } else if (raceInitial == 'H') {
                    race = "hispanic";
                    H++;
                } else if (raceInitial == 'I' || raceInitial == 'F') {
                    race = "other";
                    O++;
                }
            }

            if (!(race == null)) {
                Node n = new Node(id, abuse, race, district);
                allVertices.add(n);
                HashSet<Node> toadd = new HashSet<Node>(districtMap.get(district));
                toadd.add(n);
                districtMap.put(district, toadd);

                toadd = new HashSet<Node>(abuseMap.get(abuse));
                toadd.add(n);
                abuseMap.put(abuse, toadd);
            }
        }
        //Homophily Calculations

        double total = B + W + H + A + O;
        B = B / total;
        H = H / total;
        W = W / total;
        A = A / total;
        O = O / total;

        double expectedCross = 1 - (B * B + H * H + W * W + A * A + O * O);

        for (String district : districtToType.keySet()) {
            for (Node n : districtMap.get(district)) {
                HashSet<Node> toset = new HashSet<Node>(districtMap.get(district));
                toset.remove(n);
                n.setNeighbors(toset);
            }
        }

        System.out.println("Allowing districts to be the edges:");
        System.out.println("The expected ratio of cross edges is: " + expectedCross);
        Graph graph = new Graph(allVertices);

        double crossEdges = 0;
        double totalEdges = 0;
        for (Node n : graph.vertexSet()) {
            for (Node neighbor : graph.outNeighbors(n)) {
                if (!neighbor.getEthnicity().equals(n.getEthnicity())) {
                    crossEdges++;
                }
                totalEdges++;
            }
        }
        System.out.println("The actual ratio of cross edges was: " + (crossEdges / totalEdges));

        for (String abuse : abuses) {
            for (Node n : abuseMap.get(abuse)) {
                HashSet<Node> toset = new HashSet<Node>(abuseMap.get(abuse));
                toset.remove(n);
                n.setNeighbors(toset);
            }
        }

        System.out.println("Allowing abuse category to be the edges:");
        System.out.println("The expected ratio of cross edges is: " + expectedCross);
        Graph graph2 = new Graph(allVertices);

        crossEdges = 0;
        totalEdges = 0;
        for (Node n : graph2.vertexSet()) {
            for (Node neighbor : graph2.outNeighbors(n)) {
                if (!neighbor.getEthnicity().equals(n.getEthnicity())) {
                    crossEdges++;
                }
                totalEdges++;
            }
        }
        System.out.println("The actual ratio of cross edges was: " + (crossEdges / totalEdges));
        System.out.println(" \nLoading Crime Data (Takes some time due to dataset size!)... \n");
        
        //Calculations for breakdown of demographic of cop to the demographics of people filing
        //cases against them
        HashMap<Node, TreeSet<String>> copGraph = new HashMap<Node, TreeSet<String>>();
        Set<Node> nodeSet = new TreeSet<Node>();
        Node n = null;
        Node n2 = null;

        for (String id : idToCops.keySet()) {
            for (String copDetails : idToCops.get(id)) {
                String[] pieces = copDetails.split(" ");
                for (int i = 0; i < pieces.length; i++) {
                    n = new Node(pieces[0].replace("[", ""), pieces[1], pieces[2].replace("]", ""));
                    nodeSet.add(n);
                    TreeSet<String> victims = new TreeSet<String>();
                    copGraph.put(n, victims);
                }
            }
        }

        for (String id : idToCops.keySet()) {
            for (String copDetails2 : idToCops.get(id)) {
                String[] pieces2 = copDetails2.split(" ");
                for (int i = 0; i < pieces2.length; i++) {
                    n2 = new Node(pieces2[0].replace("[", ""), pieces2[1], pieces2[2].replace("]", ""));
                }
                for (Node cop : nodeSet) {
                    if (cop.compareTo(n2) > 0) {
                        TreeSet<String> victims = copGraph.get(cop);
                        victims.add(id);
                        copGraph.put(cop, victims);
                    }
                }
            }
        }
        //Creates an aggregate map of cop demographics to races of people complaining
        HashMap<String, HashMap<String, Integer>> copToRaces = new HashMap<>();
        for (Node cop : copGraph.keySet()) {
            TreeSet<String> people = copGraph.get(cop);
            HashMap<String, Integer> raceCount = new HashMap<String, Integer>();
            String copType = cop.getEthnicity() + cop.getGender();
            if (copToRaces.containsKey(copType)) {
                raceCount = copToRaces.get(copType);
            }
            for (String person : people) {
                for (Node node : allVertices) {
                    if (person.equals(node.getTitle())) {
                        if (raceCount.containsKey(node.getEthnicity())) {
                            int numPeople = raceCount.get(node.getEthnicity());
                            numPeople++;
                            raceCount.put(node.getEthnicity(), numPeople);
                        } else {
                            raceCount.put(node.getEthnicity(), 1);
                        }
                    }
                }
            }
            copToRaces.put(copType, raceCount);
        }
        //Prints out our calculations
        for (String key : copToRaces.keySet()) {
            HashMap<String, Integer> count = copToRaces.get(key);
            System.out.print(key +" Cop : ");
            for (String race : count.keySet()) {
                System.out.print(race + " " + count.get(race) + " ");
            }
            System.out.println(" ");
        }
    }
}
