package topoSort;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TopologicalSort
{
	private ArrayList<Integer> vertices;
	
	public TopologicalSort()
	{
		vertices = new ArrayList<Integer>();
	}
	
	/**Creates topologically sorted list of vertices in the graph*/
	public List<Integer> sort(Graph g)
	{
		List<Integer> toBeSorted = g.getVertices();
		PriorityQueue<Integer> toBeRemoved = new PriorityQueue<Integer>();
		// remove independent nodes -- how to find first independent node?
		for(int i : toBeSorted)
		{
			if(!g.hasParents(i))
			{
				toBeRemoved.add(i);
			}
		}
		while(toBeRemoved.size() > 0)
		{
			int vertex = toBeRemoved.poll();
			if(!g.hasParents(vertex))
			{
				toBeSorted.remove(toBeSorted.indexOf(vertex));
				g.unmarkAllEdges(vertex);
				g.addIndependentVertices(toBeRemoved,vertex);
				vertices.add(vertex);
			}
		}
		return vertices;
	}
}
