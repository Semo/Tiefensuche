package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Tiefensuche {

    // Knoten nicht besucht
    private static final int WHITE = 0xFFFFFF;

    // Knoten dessen Nachbarn ebenfalls besucht wurden ist Schwarz
    private static final int BLACK = 0x000000;

    // Knoten wurde besucht
    private static final int GREY = 0xCCCCCC;

    // Liste der besuchten Knoten
    private int[] col;

    // Liste der Vorläufer
    private int[] pred;

    // zuerst besuchter Knoten
    private int[] first;

    // zuletzt besuchter Knoten
    private int[] last;

    // Laufvariable
    private int timestamp;

    // Anzahl vorhandener Knoten in einem Graphen.
    private final int numVertices;

    // Liste der in einem Graphen vorhandenen Vertices.
    private Collection<Vertex> vertices;

    // ...und der GRAF...
    private Graph<Vertex, Edge<Vertex>> graph;

    private boolean isDAG = true;

    public Tiefensuche(final Graph<Vertex, Edge<Vertex>> graph, Vertex firstV) {
        this.graph = graph;
        this.numVertices = graph.getNumberVertices();
        this.vertices = graph.getVertices();
        initDepthGraph();
        depthSearch(firstV);
    }

    /**
     * Setzt die Größe der Hilfsarrays auf die Länge des übergebenen Graphen.
     * Alle Knoten des Graphen werden mit Weiß initialisiert.
     * 
     * @return {@link Graph} graph
     */
    private Graph<Vertex, Edge<Vertex>> initDepthGraph() {
        col = new int[numVertices];
        pred = new int[numVertices];
        first = new int[numVertices];
        last = new int[numVertices];

        for (Vertex v : vertices) {
            int vID = v.getId();
            col[vID] = WHITE;
        }
        return this.graph;
    }

    /**
     * Implementation der Tiefensuche. Es wird ein Knoten übergeben, von dem die
     * Tiefensuche aus starten soll.
     * 
     * @param v
     *            - Vertex
     */
    private void depthSearch( Vertex v) {

        // ID des übergebenen Knotens bestimmen
        int vID = v.getId();

        // besuchten Knoten Grau färben.
        col[vID] = GREY;

        // Zeitpunkt des 1. Besuchs
        first[vID] = ++timestamp;

        // Iteriert über alle Nachbarn eines Knotens.
        for (Vertex neighbour : graph.getNeighbours(vID)) {
            if(col[neighbour.getId()] == WHITE) {
                pred[neighbour.getId()] = vID;
                if (isCyclic(neighbour)) {
                    isDAG = false;
                }
                depthSearch(neighbour);
            }
        }

        // wenn alle Kinder/Nachbarn besucht wurden, wird der Knoten Schwarz
        // gefärbt.
        col[vID] = BLACK;
        last[vID] = ++timestamp;
    }

    /**
     * Diese Methode prüft, ob eine rückwärts gerichtete Kante existiert.
     * 
     * @param v
     *            - {@link Vertex}
     */
    private boolean isCyclic(Vertex v) {
        Collection<Edge<Vertex>> incidentEdges = graph.getIncidentEdges(v);
        for (Edge<Vertex> edge : incidentEdges) {
            int neighbourId = edge.getVertexB().getId();
            if (v.getId() == pred[neighbourId]) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Vertex> topoDescSort() {
        ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
        for (Vertex v : vertices) {
            vertexList.add(v);
        }
        if (isDAG) {

            Collections.sort(vertexList, new Greater());
            return vertexList;
        }
        return vertexList;
    }

    private class Greater implements Comparator<Vertex> {

        @Override
        public int compare(Vertex a, Vertex b) {
            return last[b.getId()] - last[a.getId()];
        }
    }
}
