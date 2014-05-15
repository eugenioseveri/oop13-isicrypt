package gui;
/**
 * @author Filippo Vimini
 * @data 30/04/2014
 */
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

public class GuiMethodSetter {
	/**
	 * Class that set all the variables of GridBagConstraints, useful for a better views of code and for use only a one GridBagConstraints for all the elements
	 * , without have any problem with a mismatching.
	 * 	
	 * @param limiti		GridBagConstraints 
	 * @param gridx			Position for the element, associated with "limit", in the lines of matrix.
	 * @param gridy			position for the element, associated with "limit", in the columns of matrix.
	 * @param gridwidth		Number of cell that the element occupies in width.
	 * @param gridheight	Number of cell that the element occupies in height.
	 * @param weightx		Weight of the element on x axis of the cell.
	 * @param weighty		Weight of the element on y axis of the cell.
	 * @param ipadx			Dimension in % of the element in x axis.
	 * @param ipady			Dimension in % of the element in y axis.
	 * @param insets[]		Array with insets values of GridBagLayout.
	 * @param fill			VIsible space of cell occupies by the element.
	 * @param anchor		Anchor of the element in the cell.
	 */
	public void setLimit( GridBagConstraints limiti, int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty, int ipadx, int ipady, int insets[], int fill, int anchor, Container contenitore, Component C ){
		limiti.gridx = gridx;
		limiti.gridy = gridy;
		limiti.gridwidth = gridwidth;
		limiti.gridheight = gridheight;
		limiti.weightx = weightx;
		limiti.weighty = weighty;
		limiti.ipadx = ipadx;
		limiti.ipady = ipady;
		limiti.insets.top = insets[0];
		limiti.insets.bottom = insets[1];
		limiti.insets.left = insets[2];
		limiti.insets.right = insets[3];
		limiti.fill = fill;
		limiti.anchor = anchor;
		contenitore.add(C, limiti);
	}
	/**
	 * Set default dimension for the panel that contains the image for standard view of all image.
	 * @param label			JLabel to change .
	 * @param srcImage		BufferedImage that contains image to set in label.
	 * @return imageIcon	optimized height and width ImageIcon.
	 * @throws IOException
	 */
	public ImageIcon iconOptimizer(JLabel label, BufferedImage srcImage) throws IOException{
		Dimension iconDimension = new Dimension();
		iconDimension.height = 480;
		iconDimension.width = 640;
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
	public void setJButton(Component button, Color backgroundColor, Color foregroundButtonColor, Font buttonFont, boolean focusArea, boolean border){
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
}