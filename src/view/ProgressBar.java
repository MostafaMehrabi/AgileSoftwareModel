package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;

public class ProgressBar {

	private JFrame frame;
	JProgressBar progressBar;

	/**
	 * Create the application.
	 */
	public ProgressBar(String waitMessage) {
		initialize(waitMessage);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String waitMessage) {
		frame = new JFrame();
		frame.setBounds(100, 100, 732, 195);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.GREEN);
		progressBar.setBounds(10, 90, 696, 36);
		frame.getContentPane().add(progressBar);
		
		JLabel waitLabel = new JLabel("");
		waitLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		waitLabel.setBounds(10, 29, 696, 36);
		waitLabel.setText(waitMessage);
		frame.getContentPane().add(waitLabel);
	}
	
	public void setValue(int value){
		EventQueue.invokeLater(new Runnable() {			
			@Override
			public void run() {
				progressBar.setValue(value);
				progressBar.repaint();
			}
		});
	}
	
	public void close() {
		frame.setVisible(false);
		frame.dispose();
	}
	
	public void setVisible(boolean visibility) {
		frame.setVisible(visibility);
	}
}
