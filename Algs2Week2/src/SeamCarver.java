import java.awt.Color;

import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture picture;
    private boolean pictureUpdated;
    private int width;
    private int height;
    
    private Pixel[][] pixels;
    
    private class Pixel {
        private double energy;
        private Color color;
        
        Pixel() {
            color = new Color(255, 255, 255);
            energy = 1000.0;
        }
        
        Pixel(double energy, Color color) {
            this.energy = energy;
            this.color = color;
        }
        
        Pixel(Color color) {
            this.color = color;
            energy = 1000.0;
        }
        
        public Color color() {
            return color;
        }
        
        public double energy() {
            return energy;
        }
    }
    
    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture
        this.picture = new Picture(picture);
        pictureUpdated = false;
        
        width = picture.width();
        height = picture.height();
        
        pixels = new Pixel[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = new Pixel(picture.get(x, y));
            }
        }
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x].energy = calculateEnergy(x, y);
            }
        }
    }
    
    public Picture picture() {
        // current picture
        if (!pictureUpdated) {
            return picture;
        }        
        else {
            picture = buildPicture();
            pictureUpdated = false;
            return picture;
        }
    }
    
    public int width() {
        // width of current picture
        return width;
    }
    
    public int height() {
        // height of current picture
        return height;
    }
    
    public double energy(int x, int y) {
        // energy of pixel at column x and row y
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) throw new IllegalArgumentException();
        
        return pixels[y][x].energy();
    }
    
    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        int V = (height * width) + 2;
        int startPoint = V - 2;
        int endPoint = V - 1;
        
        EdgeWeightedDigraph digraph = buildDigraph(Direction.HORIZONTAL, V, startPoint, endPoint);
        AcyclicSP sp = new AcyclicSP(digraph, startPoint);
        
        int[] seam = new int[width];
        int index = 0;
                
        for (DirectedEdge edge : sp.pathTo(endPoint)) {
            if (edge.from() == startPoint)
                continue;
            
            seam[index++] = edge.from() % height;
        }
        
        return seam;
    }
    
    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam
    	int V = (height * width) + 2;
        int startPoint = V - 2;
        int endPoint = V - 1;
        
        EdgeWeightedDigraph digraph = buildDigraph(Direction.VERTICAL, V, startPoint, endPoint);
        AcyclicSP sp = new AcyclicSP(digraph, startPoint);
        
        int[] seam = new int[height];
        int index = 0;
                
        for (DirectedEdge edge : sp.pathTo(endPoint)) {
            if (edge.from() == startPoint)
                continue;
            
            seam[index++] = edge.from() % width;
        }
        
        return seam;
    }
    
    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture
        if (seam == null || seam.length != width || height == 0) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
        	if (seam[i] < 0 || seam[i] >= height) throw new IllegalArgumentException();
        }
        
        Pixel[][] newPixels = new Pixel[height-1][width];
        
        for (int x = 0; x < width; x++) {
        	int newY = 0;
        	for (int y = 0; y < height; y++) {
        		if (seam[x] != y) {
        			newPixels[newY++][x] = new Pixel(pixels[y][x].color);
        		}
        	}
        }
        pixels = newPixels;
        height--;
        pictureUpdated = true;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x].energy = calculateEnergy(x, y);
            }
        }
    }
    
    public void removeVerticalSeam(int[] seam) {
        // remove vertical seam from current picture
        if (seam == null || seam.length != height || width == 0) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
        	if (seam[i] < 0 || seam[i] >= width) throw new IllegalArgumentException();
        }
        
        Pixel[][] newPixels = new Pixel[height][width-1];
        
        for (int y = 0; y < height; y++) {
        	int newX = 0;
        	for (int x = 0; x < width; x++) {
        		if (seam[y] != x) {
        			newPixels[y][newX++] = new Pixel(pixels[y][x].color);
        		}
        	}
        }
        
        pixels = newPixels;
        width--;
        pictureUpdated = true;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x].energy = calculateEnergy(x, y);
            }
        }
    }
    
    private enum Direction {
        HORIZONTAL,
        VERTICAL
    }
    
    private double calculateEnergy(int x, int y) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return 1000.0;
        
        int rX = pixels[y][x-1].color.getRed() - pixels[y][x+1].color.getRed();
        int bX = pixels[y][x-1].color.getBlue() - pixels[y][x+1].color.getBlue();
        int gX = pixels[y][x-1].color.getGreen() - pixels[y][x+1].color.getGreen();
        
        int deltaX = (rX * rX) + (bX * bX) + (gX * gX);
        
        int rY = pixels[y-1][x].color.getRed() - pixels[y+1][x].color.getRed();
        int bY = pixels[y-1][x].color.getBlue() - pixels[y+1][x].color.getBlue();
        int gY = pixels[y-1][x].color.getGreen() - pixels[y+1][x].color.getGreen();
        
        int deltaY = (rY * rY) + (bY * bY) + (gY * gY);
        
        return Math.sqrt(deltaX + deltaY);    
    }
    
    private Picture buildPicture( ) {
        Picture pic = new Picture(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pic.set(x, y, pixels[y][x].color());
            }
        }  
        return pic;
    }
    
    private EdgeWeightedDigraph buildDigraph(Direction direction, int V, int startPoint, int endPoint) {
        EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(V);
        
        if (direction == Direction.HORIZONTAL) {
            
            for (int vertex = 0; vertex < V-2-height; vertex++) {
                int xIndex = vertex / (height);
                int yIndex = vertex % height;
                
//                if (yIndex == 0) {
//                    digraph.addEdge(new DirectedEdge(vertex, vertex+height, pixels[yIndex][xIndex+1].energy()));
//                    digraph.addEdge(new DirectedEdge(vertex, vertex+height+1, pixels[yIndex+1][xIndex+1].energy()));
//                }
//                else if (yIndex == height-1) {
//                    digraph.addEdge(new DirectedEdge(vertex, vertex+height, pixels[yIndex][xIndex+1].energy()));
//                    digraph.addEdge(new DirectedEdge(vertex, vertex+height-1, pixels[yIndex-1][xIndex+1].energy()));
//                }
//                else {
//                    digraph.addEdge(new DirectedEdge(vertex, vertex+height, pixels[yIndex][xIndex+1].energy()));
//                    digraph.addEdge(new DirectedEdge(vertex, vertex+height+1, pixels[yIndex+1][xIndex+1].energy()));
//                    digraph.addEdge(new DirectedEdge(vertex, vertex+height-1, pixels[yIndex-1][xIndex+1].energy()));
//                }
                
                for (int y = -1; y < 2; y++) {
                	if (yIndex + y >= 0 && yIndex + y < height) {
                		digraph.addEdge(new DirectedEdge(vertex, vertex+height+y, pixels[yIndex+y][xIndex+1].energy()));
                	}
                }
            }
            
            int lastColIndex = V-2-height;
            
            for (int i = 0; i < height; i++) {
                digraph.addEdge(new DirectedEdge(startPoint, i, 1000));
                digraph.addEdge(new DirectedEdge(lastColIndex + i, endPoint, 1000));
            }
            
        }
        else {
            
            for (int vertex = 0; vertex < V-2-width; vertex++) {
                int xIndex = vertex % width;
                int yIndex = vertex / width;
                
                for (int x = -1; x < 2; x++) {
                	if (xIndex + x >= 0 && xIndex + x < width) {
                		digraph.addEdge(new DirectedEdge(vertex, vertex+width+x, pixels[yIndex+1][xIndex+x].energy()));
                	}
                }
            }
            
            int lastRowIndex = V-2-width;
            
            for (int i = 0; i < width; i++) {
                digraph.addEdge(new DirectedEdge(startPoint, i, 1000));
                digraph.addEdge(new DirectedEdge(lastRowIndex+i, endPoint, 1000));
            }

        }

        return digraph;
    }
    
    private double totalEnergyHor(int[] seam) {
    	double energy = 0.0;
    	
    	for (int i = 0; i < seam.length; i++) {
    		energy += pixels[seam[i]][i].energy;
    	}
    	
    	return energy;
    }
    
    private double totalEnergyVert(int[] seam) {
    	double energy = 0.0;
    	
    	for (int i = 0; i < seam.length; i++) {
    		energy += pixels[i][seam[i]].energy;
    	}
    	
    	return energy;
    }
    
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        
//        for (int y = 0; y < sc.height; y++) {
//            for (int x = 0; x < sc.width; x++) {
//                StdOut.print(sc.pixels[y][x].energy() + " ");
//            }
//            StdOut.print("\n");
//        }
//        
//        StdOut.println();
//        
//        for (int y = 0; y < sc.height; y++) {
//            for (int x = 0; x < sc.width; x++) {
//                StdOut.print(sc.pixels[y][x].color.toString() + " ");
//            }
//            StdOut.print("\n");
//        }
//        
//        Picture pic2 = sc.picture();
//        pic2.show();
//        StdOut.println("Width: " + sc.width);
//        StdOut.println("Height: " + sc.height);
        int[] seam = sc.findVerticalSeam();
        
        for (int i = 0; i < seam.length; i++) {
            StdOut.print(seam[i] + " ");
        }
        StdOut.println();
        
        StdOut.println(sc.totalEnergyVert(seam));
        StdOut.println();
        
        sc.removeVerticalSeam(seam);
        
        StdOut.println("Width: " + sc.width + ", Height: " + sc.height);

        int[] seamh1 = sc.findHorizontalSeam();
        
        for (int i = 0; i < seamh1.length; i++) {
            StdOut.print(seamh1[i] + " ");
        }
        StdOut.println();
        
        StdOut.println(sc.totalEnergyHor(seamh1));
        StdOut.println();
        
        sc.removeHorizontalSeam(seamh1);
        
        StdOut.println("Width: " + sc.width + ", Height: " + sc.height);
        
        int[] seam2 = sc.findVerticalSeam();
        
        for (int i = 0; i < seam2.length; i++) {
            StdOut.print(seam2[i] + " ");
        }
        StdOut.println();
        
        StdOut.println(sc.totalEnergyVert(seam2));
        StdOut.println();
        
        sc.removeVerticalSeam(seam2);

        
        StdOut.println("Width: " + sc.width + ", Height: " + sc.height);
        
        int[] seam3 = sc.findVerticalSeam();
        
        for (int i = 0; i < seam3.length; i++) {
            StdOut.print(seam3[i] + " ");
        }
        StdOut.println();
        
        StdOut.println(sc.totalEnergyVert(seam3));
        StdOut.println();
        
        sc.removeVerticalSeam(seam3);

        
        StdOut.println("Width: " + sc.width + ", Height: " + sc.height);

                
    }
}
