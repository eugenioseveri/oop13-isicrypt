package gui;
/**
 * @author Filippo Vimini //half take from Internet but optimized by myself
 * @data 25/04/2014
 */
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class ResizableImage {
/**
 *  Resize an image with SPECIFIC width and height
 * @param originalImage		BufferedImage to resize.
 * @return					BufferedImage resized.
 */
	public BufferedImage getResizedImage(BufferedImage originalImage){
	
		int MAX_WIDTH = 640;
		int MAX_HEIGHT = 480;
		
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		 
		Dimension originalDimension = new Dimension(originalImage.getWidth(), originalImage.getHeight());
	    Dimension boundaryDimension = new Dimension(MAX_WIDTH, MAX_HEIGHT);
       	Dimension scalingDimension = getResizedDimension(originalDimension, boundaryDimension);
		 
		width = (int) scalingDimension.getWidth();
		height = (int) scalingDimension.getHeight();
		
		BufferedImage resizedImage = new BufferedImage(width, height,originalImage.getType());
		Graphics2D graphic= resizedImage.createGraphics();
		graphic.drawImage(originalImage, 0, 0, width, height, null);
		 
		return resizedImage; 

	}
	/**
	 * Replace the Dimension type with default dimension
	 * 
	 * @param imageDimension 	the dimension of original image
	 * @param limit 	the new specific dimension of the image
	 */
	private Dimension getResizedDimension(Dimension imageDimension, Dimension limit){
		int original_width = imageDimension.width;
	    int original_height = imageDimension.height;
	    int bound_width = limit.width;
	    int bound_height = limit.height;
	    int new_width = original_width;
	    int new_height = original_height;
	 
	    // Check if is necessary doing the scaling
	    if (original_width > bound_width) {
	        //scaling to the max width
	        new_width = bound_width;
	        //Height scaling for have same dimension
	        new_height = (new_width * original_height) / original_width;
	    }
	    // Check if is out of boundries
	    if (new_height > bound_height) {
	         
	        new_height = bound_height;
	        //new scaling for right dimension
	        new_width = (new_height * original_width) / original_height;
	    }
	 
	    return new Dimension(new_width, new_height);
	}

}
