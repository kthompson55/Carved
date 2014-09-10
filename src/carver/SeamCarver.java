package carver;

import java.awt.Color;

import edu.neumont.ui.Picture;

public class SeamCarver
{
	private Picture carvingPicture;

	public SeamCarver(Picture pic)
	{
		carvingPicture = pic;
	}

	public Picture getPicture()
	{
		return carvingPicture;
	}

	public int width()
	{
		return carvingPicture.width();
	}

	public int height()
	{
		return carvingPicture.height();
	}

	/** Calculate pixel energy at x,y */
	public double energy(int x, int y)
	{
		if (x < 0 || y < 0 || x >= width() || y >= height())
		{
			throw new IndexOutOfBoundsException("Passed in coordinate is beyond bounds of image");
		}
		Color leftColor = new Color(0, 0, 0);
		Color rightColor = new Color(0, 0, 0);
		Color upColor = new Color(0, 0, 0);
		Color downColor = new Color(0, 0, 0);

		if (x == 0)
		{
			leftColor = carvingPicture.get(width() - 1, y);
			rightColor = carvingPicture.get(x + 1, y);
		}
		else if (x == width() - 1)
		{
			leftColor = carvingPicture.get(x - 1, y);
			rightColor = carvingPicture.get(0, y);
		}
		else
		{
			leftColor = carvingPicture.get(x - 1, y);
			rightColor = carvingPicture.get(x + 1, y);
		}

		if (y == 0)
		{
			upColor = carvingPicture.get(x, height() - 1);
			downColor = carvingPicture.get(x, y + 1);
		}
		else if (y == height() - 1)
		{
			upColor = carvingPicture.get(x, y - 1);
			downColor = carvingPicture.get(x, 0);
		}
		else
		{
			upColor = carvingPicture.get(x, y - 1);
			downColor = carvingPicture.get(x, y + 1);
		}

		double redShiftLR = Math.pow(leftColor.getRed() - rightColor.getRed(), 2);
		double greenShiftLR = Math.pow(leftColor.getGreen() - rightColor.getGreen(), 2);
		double blueShiftLR = Math.pow(leftColor.getBlue() - rightColor.getBlue(), 2);
		double horizontalEnergy = redShiftLR + greenShiftLR + blueShiftLR;

		double redShiftUD = Math.pow(upColor.getRed() - downColor.getRed(), 2);
		double greenShiftUD = Math.pow(upColor.getGreen() - downColor.getGreen(), 2);
		double blueShiftUD = Math.pow(upColor.getBlue() - downColor.getBlue(), 2);
		double verticalEnergy = redShiftUD + greenShiftUD + blueShiftUD;

		return horizontalEnergy + verticalEnergy;
	}

	/** Carve horizontal line across image */
	public int[] findHorizontalSeam()
	{
		Pixel[][] picture = new Pixel[width()][height()];
		for(int j = 0; j < height(); j++)
		{
			for(int i = 0; i < width(); i++)
			{
				double pixelEnergy = energy(i,j);
				if(picture[i][j] == null)
				{
					picture[i][j] = new Pixel(i,j,pixelEnergy);
				}
				if(i < width() - 1)
				{
					int nextColumn = i + 1;
					if(j != 0)
					{
						int topChild = j - 1;
						if(picture[nextColumn][topChild] == null)
						{
							picture[nextColumn][topChild] = new Pixel(nextColumn, topChild, picture[i][j].getEnergy() + energy(nextColumn,topChild));
							picture[nextColumn][topChild].setParent(picture[i][j]);
						}
						else
						{
							double newEnergy = energy(nextColumn, topChild) + picture[i][j].getEnergy();
							if(picture[nextColumn][topChild].getEnergy() > newEnergy)
							{
								picture[nextColumn][topChild].setEnergy(newEnergy);
								picture[nextColumn][topChild].setParent(picture[i][j]);
							}
						}
					}
					int middleChild = j;
					if(picture[nextColumn][middleChild] == null)
					{
						picture[nextColumn][middleChild] = new Pixel(nextColumn, middleChild, picture[i][j].getEnergy() + energy(nextColumn,middleChild));
						picture[nextColumn][middleChild].setParent(picture[i][j]);
					}
					else
					{
						double newEnergy = energy(nextColumn, middleChild) + picture[i][j].getEnergy();
						if(picture[nextColumn][middleChild].getEnergy() > newEnergy)
						{
							picture[nextColumn][middleChild].setEnergy(newEnergy);
							picture[nextColumn][middleChild].setParent(picture[i][j]);
						}
					}
					if(j != height() - 1)
					{
						int bottomChild = j + 1;
						if(picture[nextColumn][bottomChild] == null)
						{
							picture[nextColumn][bottomChild] = new Pixel(nextColumn, bottomChild, picture[i][j].getEnergy() + energy(nextColumn,bottomChild));
							picture[nextColumn][bottomChild].setParent(picture[i][j]);
						}
						else
						{
							double newEnergy = energy(nextColumn, bottomChild) + picture[i][j].getEnergy();
							if(picture[nextColumn][bottomChild].getEnergy() > newEnergy)
							{
								picture[nextColumn][bottomChild].setEnergy(newEnergy);
								picture[nextColumn][bottomChild].setParent(picture[i][j]);
							}
						}
					}
				}
			}
		}
		int[] yPositions = new int[width()];
		int minIndex = width();
		double minEnergy = Double.MAX_VALUE;
		for(int j = 0; j < height(); j++)
		{
			if(picture[width()-1][j].getEnergy() < minEnergy)
			{
				minIndex = j;
				minEnergy = picture[width()-1][j].getEnergy();
			}
		}
		Pixel currentPixel = picture[width()-1][minIndex];
		int index = 0;
		while(currentPixel.getParent() != null)
		{
			yPositions[index++] = currentPixel.getY();
			currentPixel = currentPixel.getParent();
		}
		yPositions[index] = currentPixel.getY();
		return yPositions;
	}

	/** Carve vertical line across image, int[] is bottom-up */
	public int[] findVerticalSeam()
	{
		Pixel[][] picture = new Pixel[width()][height()];
		for (int i = 0; i < width(); i++)
		{
			for (int j = 0; j < height(); j++)
			{
				double pixelEnergy = energy(i, j);
				if (picture[i][j] == null)
				{
					picture[i][j] = new Pixel(i, j, pixelEnergy);
				}
				if (j < height() - 1)
				{
					int nextRow = j + 1;
					if (i != 0)
					{
						int leftChild = i - 1;
						if (picture[leftChild][nextRow] == null)
						{
							picture[leftChild][nextRow] = new Pixel(leftChild, nextRow,
									picture[i][j].getEnergy() + energy(leftChild, nextRow));
							picture[leftChild][nextRow].setParent(picture[i][j]);
						}
						else
						{
							double newEnergy = energy(leftChild, nextRow)
									+ picture[i][j].getEnergy();
							if (picture[leftChild][nextRow].getEnergy() > newEnergy)
							{
								picture[leftChild][nextRow].setParent(picture[i][j]);
								picture[leftChild][nextRow].setEnergy(newEnergy);
							}
						}
					}
					int middleChild = i;
					if (picture[middleChild][nextRow] == null)
					{
						picture[middleChild][nextRow] = new Pixel(middleChild, nextRow,
								picture[i][j].getEnergy() + energy(middleChild, nextRow));
						picture[middleChild][nextRow].setParent(picture[i][j]);
					}
					else
					{
						double newEnergy = energy(middleChild, nextRow) + picture[i][j].getEnergy();
						if (picture[middleChild][nextRow].getEnergy() > newEnergy)
						{
							picture[middleChild][nextRow].setParent(picture[i][j]);
							picture[middleChild][nextRow].setEnergy(newEnergy);
						}
					}
					if (i != width() - 1)
					{
						int rightChild = i + 1;
						if (picture[rightChild][nextRow] == null)
						{
							picture[rightChild][nextRow] = new Pixel(middleChild, nextRow,
									picture[i][j].getEnergy() + energy(rightChild, nextRow));
							picture[rightChild][nextRow].setParent(picture[i][j]);
						}
						else
						{
							double newEnergy = energy(rightChild, nextRow)
									+ picture[i][j].getEnergy();
							if (picture[rightChild][nextRow].getEnergy() > newEnergy)
							{
								picture[rightChild][nextRow].setParent(picture[i][j]);
								picture[rightChild][nextRow].setEnergy(newEnergy);
							}
						}
					}
				}
			}
		}
		int[] xPositions = new int[height()];
		int minIndex = 0;
		double minEnergy = Double.MAX_VALUE;
		for (int i = 0; i < width(); i++)
		{
			if (picture[i][height() - 1].getEnergy() < minEnergy)
			{
				minIndex = i;
				minEnergy = picture[i][height() - 1].getEnergy();
			}
		}
		Pixel currentPixel = picture[minIndex][height() - 1];
		int index = 0;
		while (currentPixel.getParent() != null)
		{
			xPositions[index++] = currentPixel.getX();
			currentPixel = currentPixel.getParent();
		}
		xPositions[index] = currentPixel.getX();
		return xPositions;
	}

	/** Remove seam from image */
	public void removeHorizontalSeam(int[] indices)
	{
		Picture tempPicture = new Picture(width(), height() - 1);
		int currentX = 0;
		int currentY = 0;
		for (int i = 0; i < width(); i++)
		{
			for (int j = 0; j < height(); j++)
			{
				if (indices[currentX] != j)
				{
					tempPicture.set(currentX, currentY, carvingPicture.get(i, j));
					currentY++;
				}
			}
			currentY = 0;
			currentX++;
		}
		carvingPicture = tempPicture;
	}

	/** Remove seam from image */
	public void removeVerticalSeam(int[] indices)
	{
		Picture tempPicture = new Picture(width() - 1, height());
		int currentX = 0;
		int currentY = 0;
		for (int i = 0; i < height(); i++)
		{
			for (int j = 0; j < width(); j++)
			{
				if (indices[currentY] != j)
				{
					tempPicture.set(currentX, currentY, carvingPicture.get(j, i));
					currentX++;
				}
			}
			currentX = 0;
			currentY++;
		}
		carvingPicture = tempPicture;
	}
}
