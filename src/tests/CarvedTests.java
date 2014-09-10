package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import carver.SeamCarver;
import edu.neumont.ui.Picture;
import topoSort.Graph;
import topoSort.TopologicalSort;

public class CarvedTests
{
	Graph g;
	TopologicalSort sort;
	
	SeamCarver carver;
	
	// Milestone 1
	
	public void setUpLineGraph()
	{
		g = new Graph(5);
		g.addEdge(0, 1, 1);
		g.addEdge(0, 2, 1);
		g.addEdge(2, 3, 1);
		g.addEdge(3, 4, 1);
		sort = new TopologicalSort();
	}
	
	public void setUpMultiParentGraph()
	{
		g = new Graph(4);
		g.addEdge(0,1,1);
		g.addEdge(1,2,1);
		g.addEdge(1,3,1);
		g.addEdge(3,2,1);
		sort = new TopologicalSort();
	}
	
	@Test
	public void testTopoSort()
	{
		setUpLineGraph();
		String desired = "[0, 1, 2, 3, 4]";
		String results = sort.sort(g).toString();
		assertEquals(desired,results);
	}

	@Test
	public void testMultiParentTopoSort()
	{
		setUpMultiParentGraph();
		String desired = "[0, 1, 3, 2]";
		String results = sort.sort(g).toString();
		assertEquals(desired,results);
	}
	
	// LAB
	
	public void setUpCarver()
	{
		Picture picture = new Picture("overlayimagewithhiddenmessage.png");
		carver = new SeamCarver(picture);
	}
	
	@Test
	public void testVerticalCarvingStepUp()
	{
		setUpCarver();
		int[] seam = carver.findVerticalSeam();
		assert(seam[seam.length-1] > seam[seam.length-2]);
	}
	
	@Test
	public void testVerticalCarvingUpFullPicture()
	{
		setUpCarver();
		int[] seam = carver.findVerticalSeam();
		assert(seam[seam.length-1] > seam[0]);
	}
	
	@Test
	public void testVerticalCarvingStepDown()
	{
		setUpCarver();
		int[] seam = carver.findVerticalSeam();
		assert(seam[0] < seam[1]);
	}
	
	@Test
	public void testVerticalCarvingDownFullPicture()
	{
		setUpCarver();
		int[] seam = carver.findVerticalSeam();
		assert(seam[0] < seam[seam.length-1]);
	}
	
	@Test
	public void testFullPictureSteps()
	{
		setUpCarver();
		int[] seam = carver.findVerticalSeam();
		for(int i = 0; i < seam.length-2; i++)
		{
			assert(seam[i] < seam[i+1]);
		}
	}
	
	@Test
	public void testRemoval()
	{
		setUpCarver();
		for(int i = 0; i < 180; i++)
		{
			int[] seam = carver.findVerticalSeam();
			carver.removeVerticalSeam(seam);
		}
		for(int i = 0; i < 180; i++)
		{
			int[] seam = carver.findHorizontalSeam();
			carver.removeHorizontalSeam(seam);
		}
		carver.getPicture().save("Carved.png");
	}
}
