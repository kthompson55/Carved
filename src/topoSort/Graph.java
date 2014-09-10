package topoSort;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Graph
{
	private int[][] matrix;
	private int[] marks; // graph coloring - indicate that a vertex is visited. Single array since it is per vertex, not edges
	private List<Integer> verts;
	
	public Graph(int size)
	{
		matrix = new int[size][size];
		marks = new int[size];
		verts = new ArrayList<Integer>();
		for(int i = 0; i < size; i++)
		{
			verts.add(i);
		}
	}
	
	public int getSize()
	{
		return matrix[0].length;
	}
	
	/**Add path between two vertices (directional)*/ 
	public boolean addEdge(int startNode, int endNode, int weight)
	{
		if(startNode < getSize() && endNode < getSize())
		{
			matrix[startNode][endNode] = weight;
			marks[endNode]++;
			return true;
		}
		else return false;
	}
	
	/**Remove path between two vertices, assuming the two vertices are within the matrix bounds*/
	public boolean removeEdge(int startNode, int endNode)
	{
		if(matrix[startNode][endNode] > 0)
		{
			matrix[startNode][endNode] = 0;
			marks[endNode]--;
			return true;
		}
		else return false;
	}
	
	/**Removes all edges from a vertex*/
	public void unmarkAllEdges(int vertex)
	{
		marks[vertex] = -1;
		for(int i = getFirstConnection(vertex); i < getSize(); i = getNextConnection(vertex,i))
		{
			marks[i]--;
		}
	}
	
	/**Retrieves path state between two nodes*/
	public boolean isEdge(int startNode, int endNode)
	{
		return matrix[startNode][endNode] != 0;
	}
	
	/**Find the first edge of the given value*/
	public int getFirstConnection(int vertex)
	{
		for(int i = 0; i < getSize(); i++)
		{
			if(matrix[vertex][i] > 0)
				return i;
		}
		return getSize(); // sentinel value
	}
	
	/**Find the next edge after the current edge*/
	public int getNextConnection(int vertex, int currentNeighbor)
	{
		for(int i = currentNeighbor+1; i < getSize(); i++)
		{
			if(matrix[vertex][i] > 0)
				return i;
		}
		return getSize();
	}
	
	/**Determines if vertex has incoming connection*/
	public boolean hasParents(int vertex)
	{
		return marks[vertex] > 0;
	}
	
	/**Get a list of the verts*/
	public List<Integer> getVertices()
	{
		return verts;
	}
	
	public void setMark(int vertex, int mark)
	{
		marks[vertex] = mark;
	}
	
	public int getMark(int vertex)
	{
		return marks[vertex];
	}
	
	public void addIndependentVertices(PriorityQueue<Integer> queue, int removedVertex)
	{
		for(int i = 0; i < getSize(); i++)
		{
			if(!hasParents(i) && 
					i != removedVertex && 
					!queue.contains(i) && 
					marks[i] > -1)
			{
				queue.add(i);
			}
		}
	}
}
