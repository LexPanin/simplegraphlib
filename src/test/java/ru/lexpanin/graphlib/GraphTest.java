package ru.lexpanin.graphlib;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphTest {

    Graph<VertexClass> operationsGraph = new Graph();
    Graph<VertexClass> undirectedGraph = new Graph(true, false);
    Graph<VertexClass> directedGraph = new Graph(true, true);

    VertexClass oliver = new VertexClass("OLIVER");
    VertexClass sophie = new VertexClass("SOPHIE");
    VertexClass jack   = new VertexClass("JACK");
    VertexClass emily  = new VertexClass("EMILY");
    VertexClass harry  = new VertexClass("HARRY");
    VertexClass ava    = new VertexClass("AVA");
    VertexClass thomas = new VertexClass("THOMAS");

    private void fillUndirectedGraph(){
        undirectedGraph.addEdge(oliver, sophie, 2);
        undirectedGraph.addEdge(oliver, jack, 3);
        undirectedGraph.addEdge(sophie, emily, 4);
        undirectedGraph.addEdge(sophie, harry, 5);
        undirectedGraph.addEdge(jack, ava, 6);
        undirectedGraph.addEdge(jack, thomas, 7);
    }

    private void clearUndirectedGraph(){
        undirectedGraph.removeVertex(oliver);
        undirectedGraph.removeVertex(sophie);
        undirectedGraph.removeVertex(jack);
        undirectedGraph.removeVertex(emily);
        undirectedGraph.removeVertex(harry);
        undirectedGraph.removeVertex(ava);
        undirectedGraph.removeVertex(thomas);
    }

    private void fillDirectedGraph(){
        directedGraph.addEdge(oliver, sophie, 2);
        directedGraph.addEdge(oliver, jack, 3);
        directedGraph.addEdge(sophie, emily, 4);
        directedGraph.addEdge(sophie, harry, 5);
        directedGraph.addEdge(jack, ava, 6);
        directedGraph.addEdge(jack, thomas, 7);
    }

    private void clearDirectedGraph(){
        directedGraph.removeVertex(oliver);
        directedGraph.removeVertex(sophie);
        directedGraph.removeVertex(jack);
        directedGraph.removeVertex(emily);
        directedGraph.removeVertex(harry);
        directedGraph.removeVertex(ava);
        directedGraph.removeVertex(thomas);
    }

    @Test
    void addVertex() {
        System.out.println("Add vertex");
        operationsGraph.addVertex(oliver);
        assertEquals(true, operationsGraph.containsVertex(oliver), "Vertex " + oliver + " not added");
        operationsGraph.removeVertex(oliver);
        System.out.println("Test passed");
    }

    @Test
    void removeVertex() {
        System.out.println("Remove vertex");
        operationsGraph.addVertex(sophie);
        assertEquals(true, operationsGraph.containsVertex(sophie), "Vertex " + sophie + " not added");
        operationsGraph.removeVertex(sophie);
        assertEquals(false, operationsGraph.containsVertex(sophie), "Vertex " + sophie + " not deleted");
        System.out.println("Test passed");
    }

    @Test
    void addEdge() {
        System.out.println("Add edge");
        operationsGraph.addEdge(jack, emily);
        assertEquals(true, operationsGraph.containsVertex(jack), "Vertex " + jack + " not added");
        assertEquals(true, operationsGraph.containsVertex(emily), "Vertex " + emily + " not added");
        operationsGraph.removeVertex(jack);
        operationsGraph.removeVertex(emily);
        System.out.println("Test passed");
    }

    @Test
    void getPatch() {
        System.out.println("Get patch");
        fillDirectedGraph();
        fillUndirectedGraph();
        assertEquals("{OLIVER OLIVER property=0, JACK JACK property=3, AVA AVA property=6}",
                directedGraph.getPatch(oliver, ava).toString(),
                "Directed graph. Wrong patch between " + oliver + " and " + ava);
        assertEquals("{}",
                directedGraph.getPatch(harry, jack).toString(),
                "Directed graph. Wrong patch between " + harry + " and " + jack);
        assertEquals("{OLIVER OLIVER property=0, SOPHIE SOPHIE property=2, HARRY HARRY property=5}",
                undirectedGraph.getPatch(oliver, harry).toString(),
                "Undirected graph. Wrong patch between " + oliver + " and " + harry);
        assertEquals("{HARRY HARRY property=0, SOPHIE SOPHIE property=5, OLIVER OLIVER property=2, JACK JACK property=3, THOMAS THOMAS property=7}",
                undirectedGraph.getPatch(harry, thomas).toString(),
                "Undirected graph. Wrong patch between " + harry + " and " + thomas);
        clearDirectedGraph();
        clearUndirectedGraph();
        System.out.println("Test passed");
    }

    @Test
    void traverse() {
        System.out.println("Traverse function");
        fillDirectedGraph();
        fillUndirectedGraph();
        try {
            assertEquals("[OLIVER OLIVER property  method, SOPHIE SOPHIE property  method, JACK JACK property  method, HARRY HARRY property  method, EMILY EMILY property  method, THOMAS THOMAS property  method, AVA AVA property  method]",
                    directedGraph.traverse(oliver, "userMethod").toString(),
                    "Wrong traverse function rezult. Directed graph started vertex " + oliver);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            assertEquals("[SOPHIE SOPHIE property  method  method, HARRY HARRY property  method  method, EMILY EMILY property  method  method]",
                    directedGraph.traverse(sophie, "userMethod").toString(),
                    "Wrong traverse function rezult. Directed graph started vertex " + sophie);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            assertEquals("[OLIVER OLIVER property  method  method, SOPHIE SOPHIE property  method  method  method, JACK JACK property  method  method, HARRY HARRY property  method  method  method, EMILY EMILY property  method  method  method, THOMAS THOMAS property  method  method, AVA AVA property  method  method]",
                    undirectedGraph.traverse(oliver, "userMethod").toString(),
                    "Wrong traverse function rezult. Undirected graph started vertex " + oliver);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            assertEquals("[EMILY EMILY property  method  method  method  method, SOPHIE SOPHIE property  method  method  method  method, HARRY HARRY property  method  method  method  method, OLIVER OLIVER property  method  method  method, JACK JACK property  method  method  method, THOMAS THOMAS property  method  method  method, AVA AVA property  method  method  method]",
                    undirectedGraph.traverse(emily, "userMethod").toString(),
                    "Wrong traverse function rezult. Undirected graph started vertex " + emily);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        clearDirectedGraph();
        clearUndirectedGraph();
        System.out.println("Test passed");
    }

}