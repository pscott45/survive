import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Menu extends JFrame implements ActionListener{
	
	private JButton play;
	private JButton exit;
	private JButton playAgain;
	private JPanel resultsPanel;
	private Dimension buttonsize = new Dimension(400, 100);
	
	public Menu()
	{
		super("Game");
		//getContentPane().setBackground(Color.BLACK);
	}
	
	public void displayIntro()
	{
		Level.createBackground("introBackground.jpg");
		BufferedImage pic = Level.getBackground();
		JLabel background = new JLabel(new ImageIcon(pic));
		add(background);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		GridBagLayout layout = new GridBagLayout();
		background.setLayout(layout);
		play = new JButton();
		background.add(play, gbc);
		play.setPreferredSize(buttonsize);
		play.setText("Play");
		play.setFont(new Font("Algerian", Font.PLAIN, 70));
		play.addActionListener(this);
		play.setForeground(Color.black);
		play.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	play.setFont(new Font("Algerian", Font.BOLD, 70));
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	play.setFont(new Font("Algerian", Font.PLAIN, 70));
		    }
		});
		gbc.gridy = 1;
		exit = new JButton();
		background.add(exit, gbc);
		exit.setPreferredSize(buttonsize);
		exit.setText("Exit");
		exit.setFont(new Font("Algerian", Font.PLAIN, 70));
		exit.addActionListener(this);
		exit.setForeground(Color.black);
		exit.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	exit.setFont(new Font("Algerian", Font.BOLD, 70));
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	exit.setFont(new Font("Algerian", Font.PLAIN, 70));
		    }
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Main.WIDTH, Main.HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void displayGameOver(int wave, int score)
	{
		Level.createBackground("Backgrounds/skull.jpg");
		BufferedImage pic = Level.getBackground();
		JLabel background = new JLabel(new ImageIcon(pic));
		add(background);
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		background.setLayout(layout);
		
		resultsPanel = new JPanel();
		//resultsPanel.setOpaque(false);
		resultsPanel.setBackground(new Color(0, 0, 0, 80));
		JLabel results = new JLabel("<html>Game Over!<br>Score: " + score 
				+ "<br>Wave: " + wave + "</html>");
		results.setFont(new Font("Algerian", Font.PLAIN, 70));
		results.setForeground(Color.WHITE);
		resultsPanel.add(results);
		background.add(resultsPanel, gbc);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Main.WIDTH, Main.HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public  void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() == "Play")
		{
			Main game = new Main();
			JFrame frame = new JFrame(Main.TITLE);
			frame.add(game);
			frame.setSize(Main.WIDTH, Main.HEIGHT);
			frame.setResizable(false);
			frame.setFocusable(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			game.start();
			dispose();
		}
		if (e.getActionCommand() == "Exit")
			System.exit(0);
	}
}
