package com.icsd.doctor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.filechooser.FileSystemView;

import oracle.jdbc.pool.OracleDataSource;

/**
 * 
 * @author Sanchit
 *
 */
public class LoginFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;

	//Login frame labels and fields 
	JLabel lblWelcome,lblMessage,lblUsername,lblPassword,lblLogo,lblPasswordIcon,lblUserNameIcon;
	JTextField txtUsername;
	JPasswordField txtPassword;
	JButton btnLogin,btnUpload;
	JPanel pnl1,pnl2,mainPanel;
	Connection connection =null;

	
	//three entries for dropdown
	
	String caseTypes[]= {"ULTRASOUND","CTSCAN","POLICECASE"};
	Container c;
	public LoginFrame() {
			c=getContentPane();
			this.pack();
			mainPanel = new JPanel(new BoxLayout(c, BoxLayout.X_AXIS));
			System.out.println("height is"+c.getHeight()+"widht is"+c.getWidth());
		
		 lblWelcome = new JLabel("Login");
		lblWelcome.setBounds(20, 60, 80, 60);
		lblWelcome.setFont(new Font("Monospaced",Font.BOLD, 24));
		

		lblUsername=new JLabel("Username");
		lblUsername.setBounds(20, 100, 80, 40);
		lblUsername.setFont(new Font("Monospaced",Font.BOLD, 14));
		lblUsername.setForeground(Color.GRAY);
		
		lblUserNameIcon = new JLabel(new ImageIcon("Images/user.png"));
		lblUserNameIcon.setBounds(80, 100, 40,40);
		
		 txtUsername = new JTextField();
		txtUsername.setBounds(20, 150, 350, 40);
		
		 lblPassword=new JLabel("Password");
		lblPassword.setBounds(20, 190, 300, 40);
		lblPassword.setFont(new Font("Monospaced",Font.BOLD, 14));
		lblPassword.setForeground(Color.GRAY);
		
		lblPasswordIcon = new JLabel(new ImageIcon("Images/key.png"));
		lblPasswordIcon.setBounds(80, 190, 40,40);
		
		 txtPassword = new JPasswordField();
		txtPassword.setBounds(20, 240, 350, 40);
		
		
		txtPassword.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
		           int key = e.getKeyCode();
		           if (key == KeyEvent.VK_ENTER) {
		        	   checkCredentials();
		              }
		           }
		});
		 btnLogin = new JButton("Login");
		btnLogin.setBounds(20,300,100,40);
		btnLogin.setBackground(Color.decode("#cb202d"));
		btnLogin.setForeground(Color.white);
		btnLogin.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
		           int key = e.getKeyCode();
		           if (key == KeyEvent.VK_ENTER) {
		        	   checkCredentials();
		              }
		           }
		});
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkCredentials();
			}

		
		});
		
		 btnUpload= new JButton("Upload File");
		 btnUpload.setBounds(150,760,100,40);
		 btnUpload.setBackground(Color.CYAN);
		 btnUpload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				callFileChooser();
			}
		});
			
		lblLogo = new JLabel(new ImageIcon("Images/demo.jpg"));
		lblLogo.setBounds(400, 0, 800,800);
		
		c.add(lblWelcome);
		c.add(lblUsername);
		c.add(txtUsername);
		c.add(lblPassword);
		c.add(txtPassword);
		c.add(btnLogin);
		c.add(lblLogo);
		c.add(btnUpload);
		c.add(lblPasswordIcon);
		c.add(lblUserNameIcon);
		c.setBackground(Color.white);
		
		this.setTitle("Hospital");
		this.setLayout(null);
		
		this.setSize(new Dimension(1200,600));
		this.setResizable(false);
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	/**
	 * @description Method to check the credentials of User
	 */
	private void checkCredentials() {
		
		String username=txtUsername.getText();
		String password=txtPassword.getText();
		if(username.equals("") || password.equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Username And Password");
		}
		
		String selectSql = "select * from fileusers where usr=? and pwd=?";
		  try {
			  connection = getdbConn();
			  if(connection!=null){
					 PreparedStatement statement = connection.prepareStatement(selectSql);
					 statement.setString(1, username);
					 statement.setString(2, password);
					 ResultSet rs=statement.executeQuery();
					 if(rs.next()){
						 this.dispose();
						 new HomePage();
						 JOptionPane.showMessageDialog(null, "Login Successfull", "Success", 0);			
						 
					 }
					 else
					 {
						 JOptionPane.showMessageDialog(null, "Invalid Credentials", "Oops!", 0);
					 }
					
			  }
			  
		  }
		  catch(Exception e){
			  System.out.println("Exception occured"+e);

		  }
		
	}
	
	public void callFileChooser() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnValue = jfc.showOpenDialog(null);
		if(returnValue==JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			System.out.println("selectedFile"+selectedFile);
			openPopup();
			
		}
	}
	public void openPopup() {
		PopupFactory popupfactory = new PopupFactory();
		Popup sendOptionPopup;
		
		JPanel popupPanel = new JPanel();
		popupPanel.setBackground(Color.red);
		JComboBox caseTypeComboBox=new JComboBox(caseTypes);    
		caseTypeComboBox.setBounds(50, 50,90,20);  
		
	    popupPanel.add(caseTypeComboBox);
	    
	    sendOptionPopup=popupfactory.getPopup(this, popupPanel, 300, 400);
	    sendOptionPopup.show();
	}

	  public Connection getdbConn()
	  {
	  	OracleDataSource ods;
	  	Connection conn = null;
	  	try {
				ods=new OracleDataSource();
				ods.setURL("jdbc:oracle:thin:@localhost:1521:xe");
				conn=ods.getConnection("ankit","icsd");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  	return conn;
	  }
}
