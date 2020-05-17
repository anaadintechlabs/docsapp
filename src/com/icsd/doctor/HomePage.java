package com.icsd.doctor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.print.DocFlavor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import oracle.jdbc.pool.OracleDataSource;


import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.ParagraphProperties;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class HomePage 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String strDate;
	JButton menuButtons[];
	String[] menuButtonStrings= {"DashBoard","Search","Upload","Logout"};
	JPanel menuPanel,contentPanel;
	JLabel lblUC,lblCT,lblPT,lblM,lblFM;
	String caseTypes[]= {"Select Case Type","ULTRASOUND","CTSCAN","POLICECASE","X-RAYS"};
	String relationTypes[]= {"S/O","W/O","D/O"};
	Connection connection =null;
	
	UtilDateModel model = new UtilDateModel();
	JDatePanelImpl datePanel = new JDatePanelImpl(model);
	JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
	JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel);
	String searchDate = "";

	String Maindate = "%%";
	
	
	//Labels for Search Content
	JLabel lblCaseType,lblFatherName,lblPatientName,lblGender,lblAge,lblUpload,lblFnm,lblGen,lblPtId,lblSerchByDate;
	JTextField txtPatientName ,txtPatientAge,txtFnm,txtFileName,txtPtId;
	JRadioButton btnMale,btnFem;
	ButtonGroup btngrp;
	
	//Labels for Dashboards
	JLabel lblUltraSound,lblCTScan,lblPoliceCase,lblFemalePatients,lblMalePatients,lblXray,lblXrayVal;
	File selectedFile;
	String directoryPath="C:\\DOCS";
	String baseDirectoryPath="C:\\DOCS";
	JButton btnChooseFile,btnUpload,btnSearch,btnrestDate;
	JComboBox<String> caseTypeComboBox;
	JComboBox<String> relationTypeComboBox;
	JFrame mainFrame;
	public HomePage() {
		mainFrame = new JFrame();
		initializePanel(mainFrame);
	}

	
	private void initializePanel(JFrame mainFrame) {
		
		
		//For setting menu and frame horizontally
		mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.X_AXIS));
		 menuPanel = new JPanel();
		 menuPanel.setLayout(null);
		 mainFrame.setSize(new Dimension(200,600));
		 contentPanel = new JPanel();
		
    /*LOGO*/
		 JLabel lblLogo = new JLabel(new ImageIcon("Images/logo.jpg"));
		 lblLogo.setBounds(20, 0, 200, 200);
		 menuPanel.add(lblLogo); 
		menuButtons= new JButton[menuButtonStrings.length];
		//Generating Menu Button
		int i=0;
		int xAxis;
		int yAxis=180;
		for( i =0;i<menuButtons.length;i++) {
			menuButtons[i]=new JButton(menuButtonStrings[i]);
			menuButtons[i].setForeground(Color.white);
			menuButtons[i].setBackground(Color.decode("#cb202d"));

			menuButtons[i].setBounds(0, yAxis, 300, 70);
			yAxis+=100;
			menuButtons[i].addKeyListener(new KeyAdapter() {
				 public void keyPressed(KeyEvent e) {
			           int key = e.getKeyCode();
			           if (key == KeyEvent.VK_ENTER) {
			        	   try {
			        		   JButton btn =(JButton) e.getSource();
			        		   manageButtonClicked(btn.getText());
								
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			              }
			           }
			});
			menuButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				try {
					manageButtonClicked(e.getActionCommand());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}				
			});
			menuPanel.add(menuButtons[i]);
		}
		
		menuPanel.setBackground(Color.white);
		menuPanel.setBorder(BorderFactory.createEmptyBorder());
		//Method for setting content

		contentPanel.setBackground(Color.black);
		contentPanel.setLayout(null);
		//Adding menuPanel and Content Panel to basePanel
		menuPanel.setVisible(true);
		menuPanel.setPreferredSize(new Dimension(100,100));
		contentPanel.setPreferredSize(new Dimension(700,1000));
		contentPanel.setVisible(true);
		mainFrame.add(menuPanel);
		mainFrame.add(contentPanel);
		
		
		mainFrame.setSize(new Dimension(1200,600));
		mainFrame.setResizable(false);
		
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		try {
			setDashboardContentInContentPanel();
			//setUploadContentInContentPanel("Upload");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	


	//Method to manage to click events
	private void manageButtonClicked(String btnName) throws SQLException {
		switch(btnName) {
		
		case "DashBoard":
			setDashboardContentInContentPanel();
			break;
		case "Search":
			setSearchContentInContentPanel();
			break;
		case "Upload":
			setUploadContentInContentPanel("Upload");
			break;
		case "Logout":
			logoutUser();
			break;
		
		}
		
	}


	private void logoutUser() {
		 mainFrame.dispose();
		 new LoginFrame();
		 JOptionPane.showMessageDialog(null, "User Logged out!", "Success", 0);			
		 
		
	}

	//On Dashboard We will display
	//1 Todays Patient
	//2 Total Ultrasount cases
	//3 Total CTscan cases
	//4 Total Police Case
	//5 Total Female Gender
	//6 Total Male Gender

	private void setDashboardContentInContentPanel() throws SQLException {
		contentPanel.removeAll();
		//getDataForDashboard();
		
		JLabel lblSelectDate=new JLabel("Select Date");
		lblSelectDate.setBounds(40, 60, 400, 40);
		lblSelectDate.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		
		datePicker.setBounds(420, 60, 200, 40);
		contentPanel.add(datePicker);
		
		datePicker.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Date selectedDate = (Date) datePicker.getModel().getValue();
				String pattern = "dd-MMM-yy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				 Maindate = simpleDateFormat.format(selectedDate);
				 strDate=Maindate;
				System.out.println("Sel Date"+ Maindate);
				System.out.println("Test"+selectedDate+" ");
				try {
					getDataForDashboard();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		lblXray=new JLabel("Total X-RAYS Cases");
		lblXray.setBounds(40, 120, 400, 40);
		lblXray.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblXrayVal=new JLabel("");
		lblXrayVal.setBounds(420, 120, 400, 40);
		lblXrayVal.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblUltraSound=new JLabel("Total Ultrasound Cases");
		lblUltraSound.setBounds(40, 160, 400, 40);
		lblUltraSound.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblUC=new JLabel("");
		lblUC.setBounds(420, 160, 400, 40);
		lblUC.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		
		lblCTScan=new JLabel("Total CT Scan Cases");
		lblCTScan.setBounds(40, 200, 400, 40);
		lblCTScan.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblCT=new JLabel("");
		lblCT.setBounds(420, 200, 400, 40);
		lblCT.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblPoliceCase=new JLabel("Total Police Cases");
		lblPoliceCase.setBounds(40, 240, 400, 40);
		lblPoliceCase.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblPT=new JLabel("");
		lblPT.setBounds(420, 240, 400, 40);
		lblPT.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblFemalePatients=new JLabel("Total Female Cases");
		lblFemalePatients.setBounds(40, 280, 400, 40);
		lblFemalePatients.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblFM=new JLabel("");
		lblFM.setBounds(420, 280, 400, 40);
		lblFM.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblMalePatients=new JLabel("Total Male Cases");
		lblMalePatients.setBounds(40, 320, 400, 40);
		lblMalePatients.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		lblM=new JLabel("");
		lblM.setBounds(420, 320, 400, 40);
		lblM.setFont(new Font("Monospaced",Font.BOLD, 24));
		
		
		contentPanel.add(lblMalePatients);
		contentPanel.add(lblFemalePatients);
		contentPanel.add(lblPoliceCase);
		contentPanel.add(lblCTScan);
		contentPanel.add(lblUltraSound);
		contentPanel.add(lblXray);
		contentPanel.add(lblXrayVal);
		
		contentPanel.add(lblSelectDate);
		contentPanel.add(lblUC);
		contentPanel.add(lblCT);
		contentPanel.add(lblPT);
		contentPanel.add(lblFM);
		contentPanel.add(lblM);
		
		contentPanel.setBackground(Color.decode("#33A2FF"));
		getDataForDashboard();
	}


	private void getDataForDashboard() throws SQLException {
		lblUC.setText("0 Files");
		lblPT.setText("0 Files");
		lblCT.setText("0 Files");
		lblM.setText("0 Files");
		lblFM.setText("0 Files");
		lblXrayVal.setText("0 Files"); 
		 try {
			 connection = getdbConn();
			 String selectSql1="select filetype,count(filetype) as total from PatientDetails where EntryDate like '"+Maindate+"' or EntryDate='"+strDate+"' group by filetype ";
			 String selectSql2="select gender,count(gender) as total from PatientDetails where EntryDate like '"+Maindate+"' or EntryDate='\"+strDate+\"' group by gender";
			 System.out.println("Query"+selectSql1+" dfv "+selectSql2);
			 if(connection!=null){
				 PreparedStatement statement = connection.prepareStatement(selectSql1);
				 ResultSet rs = statement.executeQuery();
				 while(rs.next()){
					 if("ULTRASOUND".equalsIgnoreCase(rs.getString("filetype"))){
						 lblUC.setText(rs.getString("total")+" Files");
					 }else if("POLICECASE".equalsIgnoreCase(rs.getString("filetype"))){
						 lblPT.setText(rs.getString("total")+" Files");
					 }else if("CTSCAN".equalsIgnoreCase(rs.getString("filetype"))){
						 lblCT.setText(rs.getString("total")+" Files");
					 }else{
						 lblXrayVal.setText(rs.getString("total")+" Files");
					 }
				 }
			 }
		
			 connection = getdbConn();
			 if(connection!=null){
				 System.out.println("2 conn");
				 PreparedStatement statement = connection.prepareStatement(selectSql2);
				 ResultSet rs = statement.executeQuery();
				 while(rs.next()){
					 System.out.println("3 conn");
					 if("M".equalsIgnoreCase(rs.getString("gender"))){
						 lblM.setText(rs.getString("total")+" Files");
					 }else if("F".equalsIgnoreCase(rs.getString("gender"))){
						 lblFM.setText(rs.getString("total")+" Files");
					 }
				 }
			 }
		 }
		 
		 catch(Exception e){
			 System.out.println("x occured");
			 connection.close();
		 	}
		
	}


	private void setSearchContentInContentPanel() {
		contentPanel.removeAll();
		setUploadContentInContentPanel("Search");
		contentPanel.setBackground(Color.decode("#FF8A33"));
	}


	private void setUploadContentInContentPanel(String From) {
		searchDate = "";
		contentPanel.removeAll();
		contentPanel.setBackground(Color.decode("#33FFA8"));
		
		lblCaseType=new JLabel("File Type");
		lblCaseType.setBounds(40, 60, 300, 40);
		lblCaseType.setFont(new Font("Monospaced",Font.BOLD, 20));
		
		
		caseTypeComboBox =new JComboBox<String>(caseTypes);  

		caseTypeComboBox.setBounds(350, 60,190,40);  
		
		lblSerchByDate = new JLabel("Search By Date:");
		lblSerchByDate.setBounds(40, 120, 300, 40);
		lblSerchByDate.setFont(new Font("Monospaced",Font.BOLD, 20));
		
		datePicker1.setBounds(350, 120, 200, 40);
//		  contentPanel.add(datePicker1);
		datePicker1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Date selectedDate = (Date) datePicker1.getModel().getValue();
				String pattern = "dd-MMM-yy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				searchDate = simpleDateFormat.format(selectedDate);
			}
		});
		
		
		
		lblPatientName=new JLabel("Patient's Name");
		lblPatientName.setBounds(40, 170, 300, 40);
		lblPatientName.setFont(new Font("Monospaced",Font.BOLD, 20));
		
		
		txtPatientName = new JTextField();
		txtPatientName.setBounds(350, 170, 300, 40);
		
		relationTypeComboBox =new JComboBox<String>(relationTypes);  

		relationTypeComboBox.setBounds(350, 60,190,40);  
		
		lblFnm = new JLabel("Father's Name");
		//lblFnm.setFont(new Font("Monospaced",Font.BOLD, 20));
		relationTypeComboBox.setBounds(40, 220, 300, 40);
		
		txtFnm = new JTextField();
		txtFnm.setBounds(350, 220, 300, 40);
		
		lblPtId=new JLabel("Patient's ID");
		lblPtId.setBounds(40, 270, 300, 40);
		lblPtId.setFont(new Font("Monospaced",Font.BOLD, 20));
		

		
		txtPtId = new JTextField();
		txtPtId.setBounds(350, 270, 300, 40);
		txtPtId.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
		           int key = e.getKeyCode();
		           if (key == KeyEvent.VK_ENTER) {
		        	   searchFiles();
		              }
		           }
		});
		
		lblGen = new JLabel("Gender");
		lblGen.setFont(new Font("Monospaced",Font.BOLD, 20));
		lblGen.setBounds(40, 320, 300, 40);
		
		btnMale = new JRadioButton("Male");
		btnMale.setBounds(350, 320, 100, 30);
		btnMale.setSelected(true);
		btnFem = new JRadioButton("Female");
		btnFem.setBounds(450, 320, 100, 30);
		
		btngrp = new ButtonGroup();
		btngrp.add(btnMale);
		btngrp.add(btnFem);
		
		 btnChooseFile= new JButton("Choose File");
		 btnChooseFile.setBounds(40,370,100,40);
		 btnChooseFile.setBackground(Color.CYAN);
		 btnChooseFile.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
		           int key = e.getKeyCode();
		           if (key == KeyEvent.VK_ENTER) {
		        	   callFileChooser();
		              }
		           }
		});
		 btnChooseFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				callFileChooser();
			}
		});
		

		 txtFileName= new JTextField();
		 txtFileName.setText("No file Selected");
		 txtFileName.setBounds(350,370,300,40);
		 txtFileName.setEnabled(false);
		 
		 btnSearch = new JButton("Search");
		 btnSearch.setBounds(40,320,100,40);	
		 btnSearch.setBackground(Color.CYAN);
		 
		 btnUpload= new JButton("Upload");
		 btnUpload.setBounds(40,420,100,40);

		 btnUpload.setBackground(Color.CYAN);
		 
		 btnUpload.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
		           int key = e.getKeyCode();
		           if (key == KeyEvent.VK_ENTER) {
						try {
							 validateAndSaveDataToDatabase();
							renameAndUploadFile();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		              }
		           }
		});
		 
		 btnUpload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//Validate Data, Save Into Database
					validateAndSaveDataToDatabase();
					renameAndUploadFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		 
		 
		 btnSearch.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
		           int key = e.getKeyCode();
		           if (key == KeyEvent.VK_ENTER) {
		        	   searchFiles();
		              }
		           }
		});
		 
		 btnSearch.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					searchFiles();
				}
			});
		
		contentPanel.add(lblCaseType);
		contentPanel.add(relationTypeComboBox);
		contentPanel.add(caseTypeComboBox);
		contentPanel.add(lblPatientName);
		
		contentPanel.add(txtFnm);
		contentPanel.add(txtPatientName);
	
		if("Upload".equalsIgnoreCase(From)){
			contentPanel.remove(datePicker1);
			contentPanel.add(btnUpload);
			contentPanel.add(lblGen);
			contentPanel.add(btnMale);
			contentPanel.add(btnFem);
			//contentPanel.add(lblAge);
			//contentPanel.add(txtPatientAge);
			contentPanel.add(btnChooseFile);
			//contentPanel.add(lblUpload);
			contentPanel.add(txtFileName);
			mainFrame.setVisible(true);
		}
		if("Search".equalsIgnoreCase(From)){
			contentPanel.repaint();
			contentPanel.add(datePicker1);
		  contentPanel.add(btnSearch); 
		  contentPanel.add(lblPtId);
		  contentPanel.add(txtPtId);
		  contentPanel.add(lblSerchByDate);

		  mainFrame.setVisible(true);
		}
	}


	public void callFileChooser() {
		
		JFileChooser jfc = new JFileChooser(new File("Documents"));
		jfc.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "*.doc,*.docx";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
	                return true;
	            }
	            final String name = f.getName();
	            return name.endsWith(".doc") || name.endsWith(".docx");
			}
		});
		int returnValue = jfc.showOpenDialog(null);
		if(returnValue==JFileChooser.APPROVE_OPTION) {
			selectedFile = jfc.getSelectedFile();
			txtFileName.setText(selectedFile.getName());
		}
	}
	
	private void validateAndSaveDataToDatabase() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("resource")
	private void renameAndUploadFile() throws IOException {
		
		String patientName =txtPatientName.getText().toLowerCase();
		String fName=txtFnm.getText().toLowerCase();
		String fileType=caseTypeComboBox.getSelectedItem().toString();
		String relationType=relationTypeComboBox.getSelectedItem().toString();
		System.out.println("relation type"+relationType);
		patientName=patientName.trim();
		if(patientName.isEmpty()){
			JOptionPane.showMessageDialog(null, "Please Enter Patient's Name");
			return;
		}
		fName=fName.trim();
		if(fName.isEmpty()){
			JOptionPane.showMessageDialog(null, "Please Enter Patient's Relative Name");
			return;
		}

		if(caseTypeComboBox.getSelectedItem().toString().equals("Select Case Type")) {
			JOptionPane.showMessageDialog(null, "Please select any File Type");
			return;
		}
		if(txtFileName.getText().equals("No file Selected")){
			JOptionPane.showMessageDialog(null, "Please select any file");
			return;
		}
		
		//File name to stored, It will always be unique as we used timestamp
		String finalName=patientName+"__"+new Date().getTime();
		//New Folder Will be created with FILETYPE Like CTSCAN, ULTRASOUND,POLICECASE
		directoryPath=directoryPath+"\\"+fileType;
		File mainDirectory = new File(directoryPath);
		File serverFile = null;
		//If directory is not created , Make is
		if(!mainDirectory.exists())
		{
			mainDirectory.mkdirs();			
		}
		String extension = "";
		//For getting extension,there are other method alse
		if(selectedFile!=null) {
			long space=selectedFile.length()/1024*1024*10;
			//PUT LOGIC FOR SPACE HERE, IF SIZE EXCEED SHOW ERROR MESSAGE

			
			int i = selectedFile.getName().lastIndexOf('.');
			if (i > 0) {
			    extension = selectedFile.getName().substring(i+1);
			}
			//File to be saved
			 serverFile = new File(mainDirectory + "/" + finalName+"."+extension);

			 String gen = "";
				if(btnMale.isSelected()){
					gen = "M";
				}else if(btnFem.isSelected()){
					gen = "F";
				}
				
			 
		}

		directoryPath=baseDirectoryPath;

		String fatherName = txtFnm.getText().toString().toLowerCase();
		String gen = "";
		if(btnMale.isSelected()){
			gen = "M";
		}else if(btnFem.isSelected()){
			gen = "F";
		}
		  
		  try {
			 connection = getdbConn();
			 System.out.println("connection success");
			 String selectSql = "Insert into PatientDetails values (?,?,?,?,?,?,?,?,?,?)";
			 
			 String countRecord="Select count(*) as count from PatientDetails";
			 if(connection!=null){
				 Random rand = new Random(); 
				 int rand_int1 = rand.nextInt(100000) + rand.nextInt(10);
			 PreparedStatement stmt=connection.prepareStatement(countRecord);
			 ResultSet rs = stmt.executeQuery();
			 if(rs.next())
			 {
			  rand_int1=rs.getInt(1);
			 }
			 
			 
			 PreparedStatement statement = connection.prepareStatement(selectSql);
			 statement.setInt(1, rand_int1+1);
			 statement.setString(2, patientName);
			 statement.setInt(3, 0);
			 statement.setString(4, fatherName);
			 statement.setString(5, gen);
			 statement.setString(6, fileType);
			 statement.setString(7, serverFile.getAbsolutePath());
			 statement.setDate(8, new java.sql.Date(new Date().getTime()));
			 statement.setString(9, selectedFile.getName());
			 statement.setString(10, relationType);
			 
			 statement.executeQuery();
			 copyFileUsingStream(selectedFile,serverFile,patientName,fName,gen,extension,rand_int1+1,relationType);
			 Desktop desktop = Desktop.getDesktop();
             File fl = new File(serverFile.getAbsolutePath());
             if(fl.exists()){
 				try {
 					desktop.open(fl);
 				} catch (IOException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				}
             }
//			 
//	            if(statement.execute()){
//	            	 System.out.println("data inserted successfully");
////	             	JOptionPane.showMessageDialog(null, "File Uploaded Successfully"+"\n The PatientId is: "+rand_int1);
//	             	setDashboardContentInContentPanel();
//	            }
////	            else
////	            {
////	            	JOptionPane.showMessageDialog(null, "Erro Uploading File");
////	            }
////	           
	            // Print results from select statement
            	//Now go to dashboard
	            
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(patientAge+patientName+fileType);
		
	}
	
	DefaultTableModel dtm;
	 JTable tbl ;
	 JScrollPane scrollPane;
	public void searchFiles() {
		String patientName =txtPatientName.getText().toLowerCase();
//		String patientAge=txtPatientAge.getText();
		String fileType=caseTypeComboBox.getSelectedItem().toString();
		String fatherName = txtFnm.getText().toString().toLowerCase();
		patientName=patientName.trim();
		String patientId = txtPtId.getText().toString().toLowerCase();
		String date = searchDate;
		tbl=null;
		tbl= new JTable();
		 
		 String header[] =  {"Patient ID","Date", "Patient Name", "Relative Name",
		            "File Type", "File Name","File Path","Action"};
		 //reseting after every search
		 dtm=null;
		 if(scrollPane != null) {
			 contentPanel.remove(scrollPane);
		 }
		 scrollPane =null;
		 
        dtm = new DefaultTableModel(0, 0);
		dtm.setColumnIdentifiers(header);
		ResultSet rs = getResult(fileType, patientName, fatherName, date, patientId);
		System.out.println("noe result set is 0"+rs);
		try {

	    
	        	
	        	 JButton btn;
	        	 while(rs.next()){
	        		
	        		 dtm.addRow(new Object[]{
	        			rs.getString("Patientid"),
	        			rs.getString("EntryDate"),
	        			rs.getString("PatientName"),
	        			rs.getString("FatherName"),
	        			rs.getString("FileType"),
	        			rs.getString("FileName"),
	        			rs.getString("FilePath"),
	        			"Open",
	        		 });
	        	 }
	        	// tbl.setBounds(40,320,600,400);
	        	 tbl.setModel(dtm);
	        	 
	        	 tbl.addMouseListener(new MouseAdapter() 
	    	      {
	    	    	public void mouseClicked(MouseEvent evt)
	    	    	{
	    	    		int row=tbl.rowAtPoint(evt.getPoint());
	    	    		int column=tbl.columnAtPoint(evt.getPoint());
	    	    	
	    	    		String strEname=String.valueOf( tbl.getValueAt(row, 6));

	    	    		
	    	    		 Desktop desktop = Desktop.getDesktop();
	    	             File fl = new File(strEname.toString());
	    	             if(fl.exists()){
	    	 				try {
	    	 					desktop.open(fl);
	    	 				} catch (IOException e1) {
	    	 					// TODO Auto-generated catch block
	    	 					e1.printStackTrace();
	    	 				}
	    	             }
	    	    	}
	    		});
	        	 
	        	 
	        	 tbl.getColumn("Action").setCellRenderer(new ButtonRenderer());
	        	 tbl.addMouseListener(new JTableButtonMouseListener(tbl)); 
	        	 tbl.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
	        	   TableColumn column = tbl.getColumnModel().getColumn(6);
	        	   column.setPreferredWidth(250);
	        	   TableColumn column1 = tbl.getColumnModel().getColumn(1);
	        	   column.setPreferredWidth(250);
	               tbl.setEnabled(false);
	        	 scrollPane = new JScrollPane(tbl);
	        	 scrollPane.setBounds(100, 370, 700, 200);
	        	 contentPanel.add(scrollPane);
	        	 mainFrame.setVisible(true);

		} catch (Exception e) {
			// TODO: handle exception
		}
		
	 
 }

	
	public ResultSet getResult(String fileType,String patientName, String fatherName, String date, String patientId) {
		 ResultSet rs = null;
		 Connection con = null;
		 PreparedStatement stmt = null;
		 System.out.println("inside getresult");
		 String query= null;
		 if(patientId != null && patientId != "" && !patientId.isEmpty()) {
			 query = "select * from PatientDetails where Patientid = ?";
			 System.out.println("only id casse0"+query);
			 try {
				con = getdbConn();
				stmt = con.prepareStatement(query);
				stmt.setString(1, patientId);
				rs = stmt.executeQuery();
			} catch (Exception e) {
				 System.out.println("exception occred 1");
			}
		 } else if(  date != null && date != "" && !date.isEmpty() && fileType == "Select Case Type") {
			 System.out.println("only date casse0"+date);
			 query = "select * from PatientDetails where EntryDate = ?";
			 try {
				con = getdbConn();
				stmt = con.prepareStatement(query);
				stmt.setString(1, date);
				rs = stmt.executeQuery();
			} catch (Exception e) {
				 System.out.println("exception occred 2");
			}
		 }
		 else if(  date != null && date != "" && !date.isEmpty() && !fileType.isEmpty() && fileType != "" && fileType != null) {
			 System.out.println("only date2 casse0"+query);
			 query = "select * from PatientDetails where EntryDate = ? and FileType like ?";
			 try {
				con = getdbConn();
				stmt = con.prepareStatement(query);
				stmt.setString(1, date);
				stmt.setString(2, "%"+fileType+"%");
				rs = stmt.executeQuery();
			} catch (Exception e) {
				 System.out.println("exception occred 2");
			}
		 }
		 else {
			 query = "select * from PatientDetails where PatientName like ? and FileType like ? and FatherName like ?";
			 System.out.println("other casse0"+query);
			 try {
				 con = getdbConn();
				 stmt = con.prepareStatement(query);
				 stmt.setString(1, "%"+patientName+"%");
				 stmt.setString(2, "%"+fileType+"%");
				 stmt.setString(3, "%"+fatherName+"%");
				 rs = stmt.executeQuery();
			 }
			 catch (Exception e) {
				 System.out.println("exception occred 3"+e);
				// TODO: handle exception
			}
		 }
		 System.out.println("query executed");
		return rs;
	}

	private void  searchFiles2(){
		String patientName =txtPatientName.getText();
//		String patientAge=txtPatientAge.getText();
		String fileType=caseTypeComboBox.getSelectedItem().toString();
		String fatherName = txtFnm.getText().toString();
		patientName=patientName.trim();
		
		System.out.println("patientName"+patientName+"fileType"+fileType+"fatherName"+fatherName);
		 
		 if(txtPtId.getText() != null && txtPtId.getText() !="" && !txtPtId.getText().isEmpty()){
			 String selectSql =" select * from PatientDetails where Patientid = ?";
			 try {
				 connection = getdbConn();
				 if(connection != null) {
					 PreparedStatement statement = connection.prepareStatement(selectSql);
					 statement.setString(1, txtPtId.getText());
					 final JTable tbl = new JTable();
			            ResultSet rs=statement.executeQuery();
			            //System.out.println(rs.next()+"rss");
			            System.out.println(rs.getFetchSize()+"rss");
//			            if(rs.next()){
			            	System.out.println("Paras");
			        
			            	 DefaultTableModel dtm = new DefaultTableModel(0, 0);
			            	 String header[] =  {"Patient ID","Date", "Patient Name", "Relative Name",
			            	            "File Type", "File Name","File Path","Action"};
			            	 dtm.setColumnIdentifiers(header);
			            	 JButton btn;
			            	 while(rs.next()){
			            		 System.out.println("testppp");
			            		 dtm.addRow(new Object[]{
			            			rs.getString("Patientid"),
			            			rs.getString("EntryDate"),
			            			rs.getString("PatientName"),
			            			rs.getString("FatherName"),
			            			rs.getString("FileType"),
			            			rs.getString("FileName"),
			            			rs.getString("FilePath"),
			            			"Open",
			            		 });
			            	//	 tbl.getColumn("Action").setCellRenderer(new ButtonRenderer());
			            //		 System.out.println(rs.getString("PatientName"));
			            	 }
			            	// tbl.setBounds(40,320,600,400);
			            	 tbl.setModel(dtm);
			            	 
			            	 tbl.addMouseListener(new MouseAdapter() 
			        	      {
			        	    	public void mouseClicked(MouseEvent evt)
			        	    	{
			        	    		int row=tbl.rowAtPoint(evt.getPoint());
			        	    		int column=tbl.columnAtPoint(evt.getPoint());
			        	    	
			        	    		String strEname=String.valueOf( tbl.getValueAt(row, 6));
			        	    		System.out.println(row + " "+ column);
			        	    		System.out.println("Value at"+strEname);
//			        	    		new WelcomeFrame(empno);
			        	    		
			        	    		 Desktop desktop = Desktop.getDesktop();
			        	             File fl = new File(strEname.toString());
			        	             if(fl.exists()){
			        	 				try {
			        	 					desktop.open(fl);
			        	 				} catch (IOException e1) {
			        	 					// TODO Auto-generated catch block
			        	 					e1.printStackTrace();
			        	 				}
			        	             }
			        	    	}
			        		});
			            	 
			            	 
			            	 tbl.getColumn("Action").setCellRenderer(new ButtonRenderer());
			            	 tbl.addMouseListener(new JTableButtonMouseListener(tbl)); 
			            	 tbl.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
			            	   TableColumn column = tbl.getColumnModel().getColumn(6);
			            	   column.setPreferredWidth(250);
			            	   TableColumn column1 = tbl.getColumnModel().getColumn(1);
			            	   column.setPreferredWidth(250);
	                           tbl.setEnabled(false);
			            	 JScrollPane scrollPane = new JScrollPane(tbl);
			            	 scrollPane.setBounds(100, 370, 700, 200);
			            	 contentPanel.add(scrollPane);
			          //  }
			            System.out.println("Search successfully"+rs);
			            
			            System.out.println("Table added");
					 
				 }
			 }
			 catch (Exception e) {
				// TODO: handle exception
			}
		 } else {
			 String selectSql = "select * from PatientDetails where PatientName like ? and FileType like ? and FatherName like ? "; 
			 if(searchDate != null && searchDate!= "" && !searchDate.isEmpty()){
				 selectSql += " and EntryDate = ?";
			 }
			 System.out.println(txtPtId.getText()+"----------"+selectSql);
			  try {
					 connection = getdbConn();
					 System.out.println("connection success");
					 	 
					 if(connection!=null){
					 PreparedStatement statement = connection.prepareStatement(selectSql);
					 
					 statement.setString(1, "%"+patientName+"%");
//					 statement.setInt(2, Integer.parseInt(patientAge));
					 statement.setString(2, "%"+fileType+"%");
					 statement.setString(3, "%"+fatherName+"%");
					 if(txtPtId.getText() != null && txtPtId.getText() !="" && !txtPtId.getText().isEmpty()){
						 statement.setString(4, txtPtId.getText());					 
					 }
					 if(searchDate != null && searchDate!= "" && !searchDate.isEmpty()){
						 if(txtPtId.getText() != null && txtPtId.getText() !="" && !txtPtId.getText().isEmpty()){
						 statement.setString(5,searchDate);
						 }else{
							 statement.setString(4,searchDate); 
						 }
					 }
					 final JTable tbl = new JTable(); ;
			            ResultSet rs=statement.executeQuery();
			            //System.out.println(rs.next()+"rss");
			            System.out.println(rs.getFetchSize()+"rss");
//			            if(rs.next()){
			            	System.out.println("Paras");
			        
			            	 DefaultTableModel dtm = new DefaultTableModel(0, 0);
			            	 String header[] =  {"Patient ID","Date", "Patient Name", "Relative Name",
			            	            "File Type", "File Name","File Path","Action"};
			            	 dtm.setColumnIdentifiers(header);
			            	 JButton btn;
			            	 while(rs.next()){
			            		 System.out.println("testppp");
			            		 dtm.addRow(new Object[]{
			            			rs.getString("Patientid"),
			            			rs.getString("EntryDate"),
			            			rs.getString("PatientName"),
			            			rs.getString("FatherName"),
			            			rs.getString("FileType"),
			            			rs.getString("FileName"),
			            			rs.getString("FilePath"),
			            			"Open",
			            		 });
			            	//	 tbl.getColumn("Action").setCellRenderer(new ButtonRenderer());
			            //		 System.out.println(rs.getString("PatientName"));
			            	 }
			            	// tbl.setBounds(40,320,600,400);
			            	 tbl.setModel(dtm);
			            	 
			            	 tbl.addMouseListener(new MouseAdapter() 
			        	      {
			        	    	public void mouseClicked(MouseEvent evt)
			        	    	{
			        	    		int row=tbl.rowAtPoint(evt.getPoint());
			        	    		int column=tbl.columnAtPoint(evt.getPoint());
			        	    	
			        	    		String strEname=String.valueOf( tbl.getValueAt(row, 6));
			        	    		System.out.println(row + " "+ column);
			        	    		System.out.println("Value at"+strEname);
//			        	    		new WelcomeFrame(empno);
			        	    		
			        	    		 Desktop desktop = Desktop.getDesktop();
			        	             File fl = new File(strEname.toString());
			        	             if(fl.exists()){
			        	 				try {
			        	 					desktop.open(fl);
			        	 				} catch (IOException e1) {
			        	 					// TODO Auto-generated catch block
			        	 					e1.printStackTrace();
			        	 				}
			        	             }
			        	    	}
			        		});
			            	 
			            	 
			            	 tbl.getColumn("Action").setCellRenderer(new ButtonRenderer());
			            	 tbl.addMouseListener(new JTableButtonMouseListener(tbl)); 
			            	 tbl.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
			            	   TableColumn column = tbl.getColumnModel().getColumn(6);
			            	   column.setPreferredWidth(250);
			            	   TableColumn column1 = tbl.getColumnModel().getColumn(1);
			            	   column.setPreferredWidth(250);
	                           tbl.setEnabled(false);
			            	 JScrollPane scrollPane = new JScrollPane(tbl);
			            	 scrollPane.setBounds(100, 370, 700, 200);
			            	 contentPanel.add(scrollPane);
			          //  }
			            System.out.println("Search successfully"+rs);
			            
			            System.out.println("Table added");
		            	
					 }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		
		 
	
	}

//	private Connection getdbConn() {
//		String connectionUrl =
//                "jdbc:sqlserver://localhost:1433;user=sa;password=enter;database=Test";
//		Connection conn = null;
//		try {
//			 conn = DriverManager.getConnection(connectionUrl);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return conn;
//	}
	
  public Connection getdbConn()
  {
  	OracleDataSource ods;
  	Connection conn = null;
  	try {
			ods=new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@localhost:1521:xe");
			conn=ods.getConnection("ankit","icsd");
			System.out.println("connection established");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	return conn;
  }
  
	
	 private static void copyFileUsingStream(File source, File dest, String patientName, String fName, String gen, String extension, int index, String relationType) throws IOException {
		   Date d = new Date();
		   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		   String date = sdf.format(d);
		   String text="";
		   if(fName!=null && !fName.isEmpty())
		   {
		    text="S.NO: "+ index+" NAME: "+patientName.toUpperCase()+" " +relationType.toUpperCase() + ": " +fName.toUpperCase()+" GENDER: "+gen + " DATE: "+ date;
		   }
		   else
		   {
			    text=" S.NO: "+ index+" NAME: "+patientName.toUpperCase()+" " +" GENDER: "+gen + " DATE: "+ date;
		   }
		    HWPFDocument doc =null;
		    XWPFDocument document = null;
		    CharacterRun run =null;
		    XWPFRun run1 =null;
		    if(extension.equalsIgnoreCase("Doc"))
		    {
		    	try {
		    		doc = new HWPFDocument(new FileInputStream(source));
					 Range range = doc.getRange();
					 	
					  run = range.insertBefore(text);
					  //for new line characater
					 	range.insertBefore("\013");
					 	range.insertBefore("\013");
					 	range.insertBefore("\013");
					 	range.insertBefore("\013");
					 	range.insertBefore("\013");
					 	range.insertBefore("\013");
					 	range.insertBefore("\013");
					 	range.insertBefore("\013");
					 	range.insertBefore("\013");
					 	
					  
					   
					    run.setFontSize(24);
					    run.setBold(true);
					    OutputStream out = new FileOutputStream(dest);
					    doc.write(out);
					    
					    out.flush();
					    out.close();
					    System.out.println("file written");
		    	}
		    	finally {
		    		doc.close();
		    	}
		    	 
		    }
		    else if(extension.equalsIgnoreCase("Docx")){
		    	try {
		       	 document = new XWPFDocument(new FileInputStream(source));
				 
		   				 XWPFParagraph paragraph = document.createParagraph();
		   			     run1 = paragraph.createRun();
		   			  
		   				String myText="Name : demo2 , father name :demo \n";
		   					
		   				run1.setText(text, 0); 
		   				    OutputStream out = new FileOutputStream(dest);
		   				document.write(out);
		   				    out.flush();
		   				    out.close();
		   				    System.out.println("file written doxs");
		   			
		    	}
		    	finally {
		    		document.close();
		    	}
		    }		   
		}	
}
