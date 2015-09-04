package jProtocol.helper;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * A class containing static methods for GridBagConstraints creation.
 * 
 * @author Tom Petersen
 */
public class GridBagConstraintsHelper {

	/**
	 * Creates standard constraints for form elements in GridBagLayout.
	 * 
	 * @param gridX the column
	 * @param gridY the row
	 * @param gridwidth row spanning
	 * @return the constraints
	 */
	public static GridBagConstraints createFormConstraints(int gridX, int gridY, int gridwidth) {
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = gridX;
		c.gridy = gridY;
		c.gridwidth = gridwidth;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		
		boolean shouldFillColumn = ((gridX == 1) || (gridwidth == 2));
		c.fill = shouldFillColumn ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
		c.weightx = shouldFillColumn ? 1 : 0;
		
		return c;
	}
	
	/**
	 * Creates standard constraints for elements in GridBagLayout.
	 * 
	 * @param gridX the column
	 * @param gridY the row
	 * @param gridwidth row spanning
	 * @param insets a Insets object for inner margins of the element
	 * 
	 * @return the constraints
	 */
	public static GridBagConstraints createNormalConstraints(int gridX, int gridY, int gridwidth, Insets insets) {
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = gridX;
		c.gridy = gridY;
		c.gridwidth = gridwidth;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = insets;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		
		return c;
	}
	
	/**
	 * Creates standard constraints for elements in GridBagLayout with default insets.
	 * 
	 * @param gridX the column
	 * @param gridY the row
	 * @param gridwidth row spanning
	 * 
	 * @return the constraints
	 */
	public static GridBagConstraints createNormalConstraints(int gridX, int gridY, int gridwidth) {
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = gridX;
		c.gridy = gridY;
		c.gridwidth = gridwidth;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		
		return c;
	}
}
