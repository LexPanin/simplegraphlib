package ru.lexpanin.graphlib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Graph<T> {

    private boolean isWeighted;
    private boolean isDirected;
    private Map<T, Map<T, Integer>> mapVertices = new ConcurrentHashMap<>();

    public Graph() {
        this.isWeighted = false;
        this.isDirected = false;
    }

    public Graph(boolean isWeighted, boolean isDirected) {
        this.isWeighted = isWeighted;
        this.isDirected = isDirected;
    }

    public boolean isWeighted() {
        return isWeighted;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public boolean containsVertex(T vertex) {
        if(vertex != null) {
            if (mapVertices.keySet().contains(vertex)) {
                return true;
            }
            for (T firstVertext : mapVertices.keySet()) {
                if (mapVertices.get(firstVertext).keySet().contains(vertex)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addVertex(T vertex) {
        if(vertex != null) {
            mapVertices.putIfAbsent(vertex, new ConcurrentHashMap<>());
        }
    }

    synchronized public void removeVertex(T vertex) {
        if(vertex != null) {
            if (mapVertices.containsKey(vertex)) {
                mapVertices.remove(vertex);
                for (T firstVertex : mapVertices.keySet()) {
                    mapVertices.get(firstVertex).remove(vertex);
                }
            }
        }
    }

    private void addOneEdge(T firstVertex, T secondVertex, int weight) {
        if (!mapVertices.containsKey(firstVertex)) {
            addVertex(firstVertex);
        }
        mapVertices.get(firstVertex).put(secondVertex, weight);
    }

    public void addEdge(T firstVertex, T secondVertex, int weight) {
        if((firstVertex!= null)&&(secondVertex!=null)) {
            addOneEdge(firstVertex, secondVertex, isWeighted ? weight : 1);
            if (!isDirected) {
                addOneEdge(secondVertex, firstVertex, isWeighted ? weight : 1);
            }
        }
    }

    public void addEdge(T firstVertex, T secondVertex) {
        if((firstVertex!= null)&&(secondVertex!=null)) {
            addEdge(firstVertex, secondVertex, 1);
        }
    }

    public void removeEdge(T firstVertex, T secondVertex) {
        if((firstVertex!= null)&&(secondVertex!=null)) {
            mapVertices.get(firstVertex).remove(secondVertex);
            if (!isDirected) {
                mapVertices.get(secondVertex).remove(firstVertex);
            }
        }
    }

    private LinkedHashMap<T, Integer> recursionPatch(T currentVertex, T lastVertex, LinkedHashMap<T, Integer> currentPath){
        if(currentVertex.equals(lastVertex)) {
            return currentPath;
        }
        LinkedHashMap<T, Integer> tempPatch;
        if(mapVertices.containsKey(currentVertex)) {
            for (T nextVertex : mapVertices.get(currentVertex).keySet()) {
                if (!currentPath.containsKey(nextVertex)) {
                    LinkedHashMap<T, Integer> path = new LinkedHashMap<>();
                    path.putAll(currentPath);
                    path.put(nextVertex, mapVertices.get(currentVertex).get(nextVertex));
                    tempPatch = recursionPatch(nextVertex, lastVertex, path);
                    if (tempPatch.size() > 0) {
                        return tempPatch;
                    }
                }
            }
        }
        currentPath.clear();
        return currentPath;
    }


    synchronized public LinkedHashMap<T, Integer> getPatch(T firstVertex, T secondVertex) {
        LinkedHashMap<T, Integer> path = new LinkedHashMap<>();
        if((firstVertex!= null)&&(secondVertex!=null)) {
            path.put(firstVertex, 0);
            return recursionPatch(firstVertex, secondVertex, path);
        }
        return path;
    }

    public Set<T> traverse(T firstVertex, String userFunction) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<T> visitedVertex = new LinkedHashSet<>();
        if(firstVertex!= null) {
            Method methodUser = firstVertex.getClass().getMethod(userFunction);
            Queue<T> queueVertex = new LinkedList<>();
            if (containsVertex(firstVertex)) {
                queueVertex.add(firstVertex);
                methodUser.invoke(firstVertex);
                visitedVertex.add(firstVertex);
                while (!queueVertex.isEmpty()) {
                    if (mapVertices.containsKey(queueVertex.peek())) {
                        for (T currentVertex : mapVertices.get(queueVertex.poll()).keySet()) {
                            if (!visitedVertex.contains(currentVertex)) {
                                methodUser.invoke(currentVertex);
                                visitedVertex.add(currentVertex);
                                queueVertex.add(currentVertex);
                            }
                        }
                    } else {
                        queueVertex.poll();
                    }
                }
            }
        }
        return visitedVertex;
    }

    public Set<T> traverse(String userFunction) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(!mapVertices.entrySet().isEmpty()) {
            return traverse(mapVertices.entrySet().iterator().next().getKey(), userFunction);
        } else {
            return Collections.emptySet();
        }
    }

}