package edu.carleton.comp4601.utility;

import java.io.Serializable;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class WebGraph implements Serializable{
	
	private static final long serialVersionUID = 4917971037047401385L;

	private static WebGraph graph = null;
	private Multigraph<Integer,DefaultEdge> mg;
	
	
	private WebGraph(){
	    
		mg = new Multigraph<Integer,DefaultEdge>(DefaultEdge.class);
		
	}
	
	public Multigraph<Integer,DefaultEdge> getMultigraph(){
		return mg;
	}
	
	public static WebGraph getInstance(){
		if(graph == null){
			graph = new WebGraph();
		}
		return graph;
	}
	
	public void addVertex(Integer id){
		mg.addVertex(id);
	}
	
	public void addEdge(Integer d1, Integer d2){
		mg.addEdge(d1, d2);
	}
	
	public void print(){
		BreadthFirstIterator<Integer, DefaultEdge> bfi = new BreadthFirstIterator<Integer, DefaultEdge>(mg);
		System.out.println("Printing Graph: BreadthFirst node traversal:");
        while(bfi.hasNext()){
        	System.out.println("	doc id: "+(Integer) bfi.next());
        }
	}
	
	
	
	
}

