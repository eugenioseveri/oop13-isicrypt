package gui.views;
/**
 * @author Filippo Vimini
 * @data 30/04/2014
 */
import gui.models.ResizableImage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public abstract class AbstractGuiMethodSetter{
	/**
	 * Class that set all the variables of GridBagConstraints,
	 *  useful for a better views of code and for use only a one GridBagConstraints for all the elements
	 * , without have any problem with a mismatching.
	 * 	
	 * @param limit			GridBagConstraints 
	 * @param ipadx			Dimension in % of the element in x axis.
	 * @param ipady			Dimension in % of the element in y axis.
	 * @param insets[]		Array with insets values of GridBagLayout.
	 * @param fill			VIsible space of cell occupies by the element.
	 * @param anchor		Anchor of the element in the cell.
	 * @param container		JPanel where visualize the element
	 * @param C				Component to save on panel
	 */
	public static void setLimit( GridBagConstraints limit, int ipadx, int ipady, int insets[], int fill, int anchor,
			Container container, Component C ){
		limit.ipadx = ipadx;
		limit.ipady = ipady;
		limit.insets.top = insets[0];
		limit.insets.bottom = insets[1];
		limit.insets.left = insets[2];
		limit.insets.right = insets[3];
		limit.fill = fill;
		limit.anchor = anchor;
		container.add(C, limit);
	}
	/**
	 * Set default dimension for the panel that contains the image for standard view of all image.
	 * @param label			JLabel to change .
	 * @param srcImage		BufferedImage that contains image to set in label.
	 * @return imageIcon	optimized height and width ImageIcon.
	 * @throws IOException
	 */
	public ImageIcon iconOptimizer(JLabel label, BufferedImage srcImage, int height, int width){
		Dimension iconDimension = new Dimension();
		iconDimension.height = height;
		iconDimension.width = width;
		label.setMinimumSize(iconDimension);
		srcImage = new ResizableImage().getResizedImage(srcImage);
		ImageIcon imageIcon = new ImageIcon(srcImage);
		return imageIcon;
	}

	/**
	 * 
	 * @param button					JButton to modify
	 * @param backgroundColor			Color of background
	 * @param foregroundButtonColor		Color of foreground
	 * @param buttonFont				Text JButton font
	 * @param focusArea					Boolean for setFocusPainted
	 */
	public static void setJButton(Component button, Color backgroundColor, Color foregroundButtonColor,
			Font buttonFont, boolean focusArea, boolean border){
		if(button instanceof JButton){
			((JButton)button).setBackground(backgroundColor);
			((JButton)button).setForeground(foregroundButtonColor);
			((JButton)button).setFont(buttonFont);
			((JButton)button).setFocusPainted(focusArea);
			if(!border){
				((JButton)button).setBorder(BorderFactory.createEmptyBorder());
			}
		}
	}
	/**
	 * 
	 * @param gridx			Position for the element, associated with "limit", in the lines of matrix.
	 * @param gridy			position for the element, associated with "limit", in the columns of matrix.
	 * @param gridwidth		Number of cell that the element occupies in width.
	 * @param gridheight	Number of cell that the element occupies in height.
	 * @param weightx		Weight of the element on x axis of the cell.
	 * @param weighty		Weight of the element on y axis of the cell.
	 * @param container		JPanel where visualize the element
	 * @param C				Component to save on panel
	 */
	public static void setGridposition(GridBagConstraints limit, int gridx, int gridy, int gridwidth, int gridheight,
			int weightx, int weighty, Container container, Component C ){
		limit.gridx = gridx;
		limit.gridy = gridy;
		limit.gridwidth = gridwidth;
		limit.gridheight = gridheight;
		limit.weightx = weightx;
		limit.weighty = weighty;
		container.add(C, limit);
	}
}