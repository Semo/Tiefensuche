package graph;

import java.util.ArrayList;

public class Main {

    
    public static void main(String[] argv) {
        
        Graph<Vertex, Edge<Vertex>> g = GraphLesen.FileToGraph("./GraphBeispiele/graph20.txt", true);

        Tiefensuche search = new Tiefensuche(g, g.getVertex(7));
        ArrayList<Vertex> vertices = search.topoDescSort();
        System.out.println(g.toString());
        for (Vertex vertex : vertices) {
            System.out.println(vertex.getId());
        }
    }
}
