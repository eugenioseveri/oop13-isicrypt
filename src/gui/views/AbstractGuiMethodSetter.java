package gui.views;
/**
 * @author Filippo Vimini
 * @data 30/04/2014
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public abstract class AbstractGuiMethodSetter extends JFrame {

	private static final long serialVersionUID = 2515239709227375299L;
	/**
	 * Class that set all the variables of GridBagConstraints,
	 *  useful for a better views of code and for use only a one
	 *   GridBagConstraints for all the elements
	 * , without have any problem with a mismatching.
	 * 	
	 * @param limit			GridBagConstraints 
	 * @param ipadx			Dimension in % of the element in x axis.
	 * @param ipady			Dimension in % of the element in y axis.
	 * @param insets[]		Array with insets values of GridBagLayout.
	 * @param fill			VIsible space of cell occupies by the element.
	 * @param anchor		Anchor of the element in the cell.
	 * @param container		JPanel where visualize the element
	 * @param component				Component to save on panel
	 */
	protected static void setLimit( final GridBagConstraints limit, final int ipadx, final int ipady,
			final int insets[], final int fill, final int anchor, final Container container,final Component component ){
		limit.ipadx = ipadx;
		limit.ipady = ipady;
		limit.insets.top = insets[0];
		limit.insets.bottom = insets[1];
		limit.insets.left = insets[2];
		limit.insets.right = insets[3];
		limit.fill = fill;
		limit.anchor = anchor;
		container.add(component, limit);
	}
	/**
	 * Set color, font and border of a button
	 * @param button					JButton to modify
	 * @param backgroundColor			Color of background
	 * @param foregroundButtonColor		Color of foreground
	 * @param buttonFont				Text JButton font
	 * @param focusArea					Boolean for setFocusPainted
	 */
	protected static void setJButton(final JButton button, final Color backgroundColor, final Color foregroundButtonColor,
		final Font buttonFont, final boolean focusArea, final boolean border){
		button.setBackground(backgroundColor);
		button.setForeground(foregroundButtonColor);
		button.setFont(buttonFont);
		button.setFocusPainted(focusArea);
		if(!border){
			button.setBorder(BorderFactory.createEmptyBorder());
		}
	}
	/**
	 * Set the position and the behavior of the element on grid of GridBagLayout
	 * @param gridx			Position for the element, associated with "limit", in the lines of matrix.
	 * @param gridy			position for the element, associated with "limit", in the columns of matrix.
	 * @param gridwidth		Number of cell that the element occupies in width.
	 * @param gridheight	Number of cell that the element occupies in height.
	 * @param weightx		Weight of the element on x axis of the cell.
	 * @param weighty		Weight of the element on y axis of the cell.
	 * @param container		JPanel where visualize the element
	 * @param component				Component to save on panel
	 */
	protected static void setGridposition(final GridBagConstraints limit, final int gridx, final int gridy, final int gridwidth, final int gridheight,
			final int weightx, final int weighty, final Container container, final Component component ){
		limit.gridx = gridx;
		limit.gridy = gridy;
		limit.gridwidth = gridwidth;
		limit.gridheight = gridheight;
		limit.weightx = weightx;
		limit.weighty = weighty;
		container.add(component, limit);
	}
	/**
	 * Set default dimension for the panel that contains the image
	 *  for standard view of all image.
	 * @param label			JLabel to change .
	 * @param originalImage		BufferedImage that contains image to set in label.
	 * @return imageIcon	optimized height and width ImageIcon.
	 * @throws IOException
	 */
	public static ImageIcon iconOptimizer(final JLabel label, final BufferedImage originalImage, final int height, final int width){
		BufferedImage newImage = originalImage;
		//Set new dimension
		final Dimension iconDimension = new Dimension();
		iconDimension.height = height;
		iconDimension.width = width;
		label.setMinimumSize(iconDimension);
		//Resize image
		newImage = getResizedImage(originalImage, height, width);
		final ImageIcon imageIcon = new ImageIcon(newImage);
		return imageIcon;
	}
	/**
	 * take an image in input that resize it whit specific width and height
	 * take from input
	 * @param originalImage
	 * @return BufferedImage
	 */
	private static BufferedImage getResizedImage(final BufferedImage originalImage, final int maxWidth, final int maxHeight){

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		 //get original dimension of original image
		final Dimension originalDimension = new Dimension(originalImage.getWidth(), originalImage.getHeight());
		//Set max dimension
		final Dimension boundaryDimension = new Dimension(maxWidth, maxHeight);
		//Scaling the dimension
       	final Dimension scalingDimension = getResizedDimension(originalDimension, boundaryDimension);
		 //Sen new dimension
		width = (int) scalingDimension.getWidth();
		height = (int) scalingDimension.getHeight();
		//Create new image with the new resized dimesion
		final BufferedImage resizedImage = new BufferedImage(width, height,originalImage.getType());
		final Graphics2D graphic= resizedImage.createGraphics();
		graphic.drawImage(originalImage, 0, 0, width, height, null);
		return resizedImage; 
	}
	/**
	 * Replace the Dimension type with default dimension
	 * 
	 * @param imageDimension 	the dimension of original image
	 * @param limit 	the new specific dimension of the image
	 */
	private static Dimension getResizedDimension(final Dimension imageDimension, final Dimension limit){
	   //Set first the original dimension, in case that the image is already of the correct dimension
		int newWidth = imageDimension.width;
	    int newHeight = imageDimension.height;
	 
	    // Check if is necessary doing the scaling
	    if (imageDimension.width > limit.width) {
	        //scaling to the max width
	        newWidth = limit.width;
	        //Height scaling for have same dimension
	        newHeight = (newWidth * imageDimension.height) / imageDimension.width;
	    }
	    // Check if is out of boundries
	    if (newHeight > limit.height) {
	         
	        newHeight = limit.height;
	        //new scaling for right dimension
	        newWidth = (newHeight * imageDimension.width) / imageDimension.height;
	    }
	    return new Dimension(newWidth, newHeight);
	}
}