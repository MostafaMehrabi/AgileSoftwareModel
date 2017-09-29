package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.SystemColor;

public class ErrorMessageBox {

	private JFrame errorFrame;
	private JTextArea errorMessageArea = new JTextArea();

	/**
	 * Create the application.
	 */
	public ErrorMessageBox(String errorMessage) {
		initialize(errorMessage);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String errorMessage) {
		errorFrame = new JFrame();
		errorFrame.setTitle("Error");
		errorFrame.setBounds(100, 100, 564, 250);
		errorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		errorFrame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ERROR!!!");
		lblNewLabel.setEnabled(false);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel.setBounds(151, 11, 240, 60);
		errorFrame.getContentPane().add(lblNewLabel);
		
		JButton OkButton = new JButton("OK");
		OkButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		OkButton.setBounds(151, 156, 240, 44);
		errorFrame.getContentPane().add(OkButton);
		errorMessageArea.setLineWrap(true);
		errorMessageArea.setWrapStyleWord(true);
		
		errorMessageArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		errorMessageArea.setBackground(SystemColor.menu);
		errorMessageArea.setBounds(10, 74, 528, 66);
		errorMessageArea.setText(errorMessage);
		
		errorFrame.getContentPane().add(errorMessageArea);
		
		OkButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				errorFrame.setVisible(false);
				errorFrame.dispose();
			}
		});
	}
	
	public void setVisible(boolean visibility) {
		errorFrame.setVisible(visibility);
	}
	
	public void setMessage(String message) {
		errorMessageArea.setText(message);
	}
}
