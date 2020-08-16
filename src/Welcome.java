import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Welcome {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Welcome window = new Welcome();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Welcome() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 990, 588);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.getContentPane().setLayout(null);
		
		JLabel lblMain = new JLabel("");
		lblMain.setBounds(0, 0, 993, 590);
		Image imgMainTheme = new ImageIcon(this.getClass().getResource("/Welcome.jpg")).getImage();
		
		JButton lblSignIn = new JButton("Sign In");
		lblSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignIn signIn = new SignIn();
				frame.dispose();
				signIn.setVisible(true);
			}
		});
		lblSignIn.setFocusable(false);
		lblSignIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSignIn.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblSignIn.setBounds(385, 522, 107, 45);
		lblSignIn.setBackground(new Color(30, 30, 30));
		lblSignIn.setForeground(new Color(182, 182, 182));
		frame.getContentPane().add(lblSignIn);
		
		JButton lblSignUp = new JButton("Sign Up");
		lblSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignUp signUp = new SignUp();
				frame.dispose();
				signUp.setVisible(true);
			}
		});
		lblSignUp.setFocusable(false);
		lblSignUp.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblSignUp.setBackground(new Color(30, 30, 30));
		lblSignUp.setForeground(new Color(182, 182, 182));
		lblSignUp.setBounds(502, 522, 107, 45);
		frame.getContentPane().add(lblSignUp);
		lblMain.setIcon(new ImageIcon(imgMainTheme));
		frame.getContentPane().add(lblMain);
		lblSignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
}
