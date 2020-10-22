import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;


public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	private String sourceImage="";
	
	Path pathDefault = FileSystems.getDefault().getPath("PrintImage.jpg");//save file-default in Current Folder of this Application

	private String saveAs= pathDefault.toString();//save file - default in Current Folder of this Application
	
	private JButton btnOpenSource = new JButton("Open Source Photo");//Image file as Opening source
	private JButton btnSaveAsFile = new JButton("...");//File Dialog to get path to save image 
	private JButton btnExit = new JButton("Exit");//Collage-Image to be printed 
	private JButton btnSaveImg = new JButton("Save");//Execute after configure/select image area, then save image in file
	private JButton btnPreview = new JButton("Preview");//Execute after configure/select image area 
	private JLabel labelSaveAs = new JLabel("Save PassPhoto as:");//Execute after configure/select image area 
	private JTextField textSaveAs = new JTextField();//Execute after configure/select image area 
	private JPanel menuSave = new JPanel(); //WorkingPanel to put btnOpen ("Open Source Photo") and btnSave ("Save PassPhoto as:")
	
	private String pathSource= "";
	private String pathSave= "";
	private JLabel previewImage = new JLabel();
	
	backgroundImg backgrdPanel = new backgroundImg();
	backgroundImg previewPanel = new backgroundImg();
	ReferenceLine glass =new ReferenceLine();//set transparent 
	ImageIcon imgIcon = null;
	Image img = null;
	private JLayeredPane lp = new JLayeredPane();

	//Getting Screen Size, to dimensioning size of Windows of Application
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//get screen size of the user's computer
	private int windowHeight = (int)(screenSize.height);
	private int windowWidth = (int)(screenSize.width);
	
	private JPanel menuPanel = new JPanel();

	public Window() {
		super();
		setLayout(null);
		setTitle("Biommetric Photo Print");
		setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icon.JPG"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		previewImage.setOpaque(true);
		previewImage.setBackground(Color.WHITE);
		previewImage.setHorizontalAlignment(JLabel.CENTER);
		previewImage.setVerticalAlignment(JLabel.CENTER);
		
		menuPanel.setLayout(new GridLayout(1,3));
		menuPanel.add(btnOpenSource);
		menuPanel.add(btnPreview);
		menuPanel.add(btnExit);
		menuPanel.setBounds(0,0,windowWidth/3,30);

		menuSave.setLayout(null);		
		menuSave.add(btnSaveAsFile);//btnSave to put in window
		menuSave.add(btnSaveImg);//btnExe to save in file
		menuSave.add(labelSaveAs);//labelSaveAs to put in window
		menuSave.add(textSaveAs);//textSaveAs to put in jp
		menuSave.setBounds(windowWidth/3+100,0,windowWidth-backgrdPanel.getWidth()-50,30);
		Dimension size = btnOpenSource.getPreferredSize();
		labelSaveAs.setPreferredSize(size);
		labelSaveAs.setBounds(0, 0, 120, size.height);
		textSaveAs.setPreferredSize(size);
		textSaveAs.setBounds(130, 0, 310, size.height);
		btnSaveAsFile.setPreferredSize(size);
		btnSaveAsFile.setBounds(480, 0, 30, size.height);
		btnSaveImg.setPreferredSize(size);
		btnSaveImg.setBounds(520, 0, 120, size.height);

		btnSaveImg.setEnabled(false);
		btnPreview.setEnabled(false);
		textSaveAs.setEnabled(false);
		
		btnOpenSource.addActionListener(new ButtonListener());
		btnSaveAsFile.addActionListener(new ButtonListener());
		btnExit.addActionListener(new ButtonListener());
		btnSaveImg.addActionListener(new ButtonListener());
		btnPreview.addActionListener(new ButtonListener());

		setSize(windowWidth, windowHeight);
		
        lp.setLayout(null);

		
		lp.add(backgrdPanel, Integer.valueOf(1));//layer 1
		lp.add(glass, Integer.valueOf(2));//layer 2

		backgrdPanel.setSize(windowWidth/3,windowHeight-30); // initial size
		backgrdPanel.setLocation(0, 0);
		glass.setSize(windowWidth/3,windowHeight-30); // initial size
		glass.setLocation(0, 0);
		
		lp.setBounds(0, 0, windowWidth/3,windowHeight-30);
		add(menuPanel);
		add(lp);
		add(previewPanel);
		previewPanel.setVisible(false);
		lp.setVisible(false);

		add(menuSave);

		setVisible(true);
	}

	private void addBackGroundPanel() 
	{
		ResizeImage resizedImage= new ResizeImage(sourceImage);
		
		imgIcon = new ImageIcon(resizedImage.getBufferedImage());
		img = imgIcon.getImage();
		
		backgrdPanel.setImage(img);
		
		System.out.println("image width: "+img.getWidth(getParent()));
		System.out.println("image height: "+img.getHeight(getParent()));

		backgrdPanel.setSize(img.getWidth(getParent()),img.getHeight(getParent())-30); // Size is needed here, as there is no layout in lp
		menuSave.setLocation(backgrdPanel.getWidth()+100,0);
		addRefPanel();
	}

	private void addRefPanel() {
		glass.configure(sourceImage,saveAs);
		glass.setOpaque(false); // Set to true to see it
		glass.setBackground(Color.GREEN);
		glass.setSize(img.getWidth(getParent()),img.getHeight(getParent())-30);
	}
	
	class ButtonListener implements ActionListener {
		ButtonListener() {
		}

		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand().equals("Open Source Photo")) {
				
				JFileChooser sourceFile = null;
				if (pathSource.equals("")) {
					sourceFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				} else {
					sourceFile = new JFileChooser(pathSource);
				}
				sourceFile.setDialogTitle("Open Source File Image:");
				int returnValue = sourceFile.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = sourceFile.getSelectedFile();
					pathSource= ""+ sourceFile.getCurrentDirectory()+ "\\";
					if (pathSave.equals("")) {
						pathSave = pathSource;
					}
					sourceImage=selectedFile.getAbsolutePath();
					addBackGroundPanel();
					lp.setVisible(true);
					btnSaveImg.setEnabled(true);
					btnPreview.setEnabled(true);
					textSaveAs.setEnabled(true);
				}
				System.out.println("#1. pathSource: "+pathSource);
			}

			if (e.getActionCommand().equals("...")) {
				JFileChooser saveFile = null;
				if (pathSave.equals("")) {
					saveFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				} else {
					saveFile = new JFileChooser(pathSave);
				}

				saveFile.setDialogTitle("Save File As:");
				int returnValue = saveFile.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = saveFile.getSelectedFile();
					pathSave=  ""+ saveFile.getCurrentDirectory()+ "\\";
					System.out.println("#2. pathSave: "+pathSave);
					textSaveAs.setText(selectedFile.getAbsolutePath());
				}

			}
			
			if (e.getActionCommand().equals("Save")) {
				if(textSaveAs.getText().equals(""))
				{
					//image will be saved in current program folder
					String path="";
					try {
						path = new File(".").getCanonicalPath();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					saveAs=path+'\\'+saveAs;
					textSaveAs.setText(saveAs);
				} else
				{
					saveAs=textSaveAs.getText();//if there is without path, only filename, image will be saved in current program folder
				}
				System.out.println("File wil be save at: "+saveAs);
				glass.setSaveAs(saveAs);
				glass.execute(false);
//				JOptionPane.showMessageDialog(null, new JLabel("Finish",JLabel.CENTER));
				JOptionPane.showMessageDialog(null, "Finish!");
			}
			
			if (e.getActionCommand().equals("Preview")) {
				glass.setSaveAs(saveAs);
				glass.execute(true);
				BufferedImage imgPreview= glass.getPreviewImage();
				imgIcon = new ImageIcon(imgPreview);
				Image imgx = imgIcon.getImage();

				previewPanel.setImage(imgx);
				
				int w,h;
				w=imgx.getWidth(getParent());
				h=imgx.getHeight(getParent());
				previewPanel.setBounds(glass.getWidth()+5,50,w,h); // Size is needed here, as there is no layout in lp
	
//				Border blackline, raisedetched, loweredetched,
//			       raisedbevel, loweredbevel, empty;
//				Border blackline, raisedetched, loweredetched,
//			       raisedbevel, loweredbevel, empty;
//
//				blackline = BorderFactory.createLineBorder(Color.black);
//				raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
//				loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
//				raisedbevel = BorderFactory.createRaisedBevelBorder();
//				loweredbevel = BorderFactory.createLoweredBevelBorder();
//				empty = BorderFactory.createEmptyBorder();
				
//				TitledBorder title;
//				title = BorderFactory.createTitledBorder("title");				
//				title = BorderFactory.createTitledBorder(blackline, "title");
//				previewPanel.setBorder(raisedetched);
//				menuSave.setBorder(blackline);
				
//				lp.add(previewPanel, Integer.valueOf(2));//layer 2
				previewPanel.setVisible(false);
				add(previewPanel);
				previewPanel.setVisible(true);

			}
			
			if (e.getActionCommand().equals("Exit")) {
				System.exit(0); 
			}
			
		}
	}
	

}
