package carver;

public class Pixel
{
	private int x,y;
	private double energy;
	private Pixel parent;
	
	public Pixel(int xPos, int yPos, double startEnergy)
	{
		x = xPos;
		y = yPos;
		energy = startEnergy;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setParent(Pixel newParent)
	{
		parent = newParent;
	}
	
	public Pixel getParent()
	{
		return parent;
	}
	
	public void setEnergy(double newEnergy)
	{
		energy = newEnergy;
	}
	
	public double getEnergy()
	{
		return energy;
	}
}
