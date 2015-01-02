/**
 * Shows a frame with a image window, buttons for various actions.
 *
 * @author Marius Žilėnas
 *
 * @since 2014-12-31
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.image.BufferedImage;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics2D;

public class Application
{
	JFrame        mainFrame    = null;
	JLabel        pictureLabel = null;
	public static final Dimension PICTURE_PREFERRED_DIM = new Dimension(500, 500);

	/**
	 * Image to display on the panel.
	 */
	BufferedImage picture   = null;

	MouseListener pictureMouseListener = new PictureMouseListener();
	ButtonFactory buttonFactory = new ButtonFactory(
			new ButtonActionListener());

	public void setMainFrame(JFrame mainFrame)
	{
		this.mainFrame = mainFrame;
	}

	public JFrame getMainFrame()
	{
		return mainFrame;
	}

	public void setPictureLabel(JLabel pictureLabel)
	{
		this.pictureLabel = pictureLabel;
	}

	public JLabel getPictureLabel()
	{
		return pictureLabel;
	}

	public Dimension getPicturePreferredDimension()
	{
		return PICTURE_PREFERRED_DIM;
	}

	public void setPicture(BufferedImage picture)
	{
		this.picture = picture;
	}

	public BufferedImage getPicture()
	{
		return picture;
	}

	public void setPictureMouseListener(MouseListener pictureMouseListener)
	{
		this.pictureMouseListener = pictureMouseListener;
	}

	public MouseListener getPictureMouseListener()
	{
		return pictureMouseListener;
	}

	public void setButtonFactory(ButtonFactory buttonFactory)
	{
		this.buttonFactory = buttonFactory;
	}

	public ButtonFactory getButtonFactory()
	{
		return buttonFactory;
	}

	/**
	 * Resizes picture so that it fits to imageicon area.
	 */
	public void repaintImageIcon(BufferedImage image)
	{
		AffineTransform at = AffineTransform.getScaleInstance(
				getPicture().getWidth()  / (double) image.getWidth(),
				getPicture().getHeight() / (double) image.getHeight());
		AffineTransformOp aop = new AffineTransformOp(
				at, AffineTransformOp.TYPE_BICUBIC);
		Graphics2D g2d = (Graphics2D) (getPicture().createGraphics());
		g2d.drawImage(image, aop, 0, 0);
		getPictureLabel().setIcon(
				new ImageIcon(getPicture()));
	}

	/**
	 * Constructor.
	 *
	 * @param width  Width  of the image.
	 * @param height Height of the image.
	 */
	public Application()
	{
		setPicture(
				new BufferedImage(
					(int) getPicturePreferredDimension().getWidth(), 
					(int) getPicturePreferredDimension().getHeight(),
					BufferedImage.TYPE_INT_RGB));
	}

	public void createAndShowMainFrame()
	{
		JFrame frame = new JFrame("Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(makeMainPanel());

		frame.pack();
		frame.setVisible(true);
		setMainFrame(frame);
	}

	public class PictureMouseListener
			implements MouseListener
	{
		public void defaultAction(MouseEvent e)
		{
			JOptionPane.showMessageDialog(
					null, 
					"You clicked on a picture area (x,y): (" + e.getX() + "," + e.getY() + ")" ,
					"Info",
					JOptionPane.INFORMATION_MESSAGE);
		}

		public void mouseClicked(MouseEvent e)
		{
			defaultAction(e);
		}

		public void mouseEntered(MouseEvent e)
		{
		}

		public void mouseExited(MouseEvent e)
		{
		}

		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
		}
	}
	public class DefaultButtonActionListener
			implements ActionListener
	{ 
		/**
		 * Default action command.
		 */
		String defaultActionCommand = "default_action_command";

		public void setDefaultActionCommand(String defaultActionCommand)
		{
			this.defaultActionCommand = defaultActionCommand;
		}

		public String getDefaultActionCommand()
		{
			return defaultActionCommand;
		}

		public void defaultAction(Object source)
		{
			JOptionPane.showMessageDialog(
					null, 
					"You just pressed a button \"" + ((JButton)source).getText() + "\"",
					"Info",
					JOptionPane.INFORMATION_MESSAGE);
		}

		public void actionPerformed(ActionEvent e)
		{
			switch (e.getActionCommand())
			{
				default:
					defaultAction(e.getSource());
					break;
			}
		}
	}

	public class ButtonActionListener
			extends DefaultButtonActionListener
	{
		public void loadImageFromDisk(int i)
		{
			try
			{
				repaintImageIcon(
						ImageIO.read(
							new File("img\\hund"+i+".png")));
			}
			catch (IOException e)
			{
				System.out.println("File not found!");
			}
		}

		public void actionPerformed(ActionEvent e)
		{
			switch (e.getActionCommand())
			{
				case "load_from_disk1":
					loadImageFromDisk(1);
					break;
				case "load_from_disk2":
					loadImageFromDisk(2);
					break;

				default:
					defaultAction(e.getSource());
					break;
			}
		}
	}

	public class ButtonFactory
	{
		String         title          = "Button";
		ActionListener actionListener = null;

		public void setTitle(String title)
		{
			this.title = title;
		}

		/**
		 * Get default title for button.
		 */
		public String getTitle()
		{
			return title;
		}

		/**
		 * Sets default action listener.
		 */
		public void setListener(ActionListener actionListener)
		{
			this.actionListener = actionListener;
		}

		public ActionListener getActionListener()
		{
			return actionListener;
		}

		public ButtonFactory(ActionListener actionListener)
		{
			this.actionListener = actionListener;
		}

		public JButton createButton(
									String title,
									String actionCommand, 
									ActionListener listener)
		{
			JButton b = new JButton(title);
			b.setActionCommand(actionCommand);
			b.addActionListener(listener);
			return b;
		}

		public JButton createButton(String title)
		{
			return createButton(
								title,
								((ButtonActionListener) getActionListener()).getDefaultActionCommand(),
								getActionListener());
		}

		public JButton createButton()
		{
			return createButton(
								getTitle(),
								((ButtonActionListener) getActionListener()).getDefaultActionCommand(), 
								getActionListener());
		}
	}

	/**
	 * Creates a panel with an image placeholder and buttons.
	 */
	public JPanel makeMainPanel()
	{
		JPanel panel = new JPanel(
								  new GridBagLayout());
		GridBagConstraints c;

		JButton b1 = getButtonFactory().createButton("Load image from disk.");
		b1.setActionCommand("load_from_disk1");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(10,10,10,10);
		panel.add(b1, c);

		JButton b2 = getButtonFactory().createButton("Button 2");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(10,10,10,10);
		panel.add(b2, c);

		JButton b3 = getButtonFactory().createButton("Button 3");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(10,10,10,10);
		panel.add(b3, c);

		JButton b4 = getButtonFactory().createButton("Button 4");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(10,10,10,10);
		panel.add(b4, c);

		JRadioButton rb1 = new JRadioButton("Load picture 1 from disk.");
		rb1.setActionCommand("load_from_disk1");
		rb1.addActionListener(new ButtonActionListener());

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(10,10,10,10);
		panel.add(rb1, c);

		JRadioButton rb2 = new JRadioButton("Load picture 2 from disk.");
		rb2.setActionCommand("load_from_disk2");
		// reuse the same listener
		rb2.addActionListener(new ButtonActionListener());

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets(10,10,10,10);
		panel.add(rb2, c);

		ButtonGroup bgr = new ButtonGroup();
		bgr.add(rb1);
		bgr.add(rb2);

		JLabel picLabel = new JLabel(
									 new ImageIcon(getPicture()));
		setPictureLabel(picLabel);
		picLabel.addMouseListener(getPictureMouseListener());

		c = new GridBagConstraints();
		c.fill  = GridBagConstraints.BOTH;
		c.gridheight = 6;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10,10,10,10);
		panel.add(picLabel, c);


		return panel;
	}

	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				new Application().createAndShowMainFrame();
			}
		});
	}
}
