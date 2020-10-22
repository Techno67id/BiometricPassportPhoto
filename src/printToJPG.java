import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class printToJPG {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//get screen size of the user's computer
	private int IMG_HEIGHT = (int)(screenSize.height-30);
	private int IMG_WIDTH = (int)(screenSize.width-50);
	private BufferedImage previewImage= null;

	public printToJPG() 
	{
		
	}
	public printToJPG(Rectangle2D[] p,String imgFile, String saveAs, boolean preview) throws IOException
	{
		print2(p,imgFile,saveAs,preview);
	}
	
	public void configure(Rectangle2D[] p,String imgFile, String saveAs, boolean preview) throws IOException
	{
		print2(p,imgFile,saveAs,preview);
	}

	public void print2 (Rectangle2D[] p,String loadFile,String fileOut,boolean preview) throws IOException
    {
        BufferedImage originalImage = ImageIO.read(new File(loadFile));
 
		double scaleX = (double) IMG_WIDTH / originalImage.getWidth();
		double scaleY = (double) IMG_HEIGHT / originalImage.getHeight();
		double scaleFactor = Math.min(scaleX, scaleY);

		double pos2 = p[2].getCenterY();
		double pos1 = p[1].getCenterY();
		double pos0 = p[0].getCenterY();

		int orgimgH=originalImage.getHeight();
		
		int p2y =  (int)(pos2/IMG_HEIGHT*orgimgH);
		int p1y =  (int)(pos1/IMG_HEIGHT*orgimgH);
		int p0y =  (int)(pos0/IMG_HEIGHT*orgimgH);

		int d2o= p2y-p1y;
		int d1o= p1y-p0y;
		
		//from p[2] to bottom-line of cropping is 22 mm
		//bottom line:
		int bottomLine = (int) (p2y+(22./10.*d2o));
		
		//total height : 45 mm (on paper)
		int topLine = (int) (p1y-d1o);
		
		//Left and Right-Line
		//get actual position in original picture:
		
		int lineVScreen=(int) p[0].getCenterX();
		int lineVActual= (int) (lineVScreen*originalImage.getWidth()/(scaleFactor*originalImage.getWidth()));
		
		double tol = 0.05;//tolerance for position of bottom and top line
		int delta=(int)(tol*(bottomLine-topLine));

		topLine=topLine-delta;
		bottomLine=bottomLine+delta*3;
		
		//width = 35 mm (on paper)
		int leftLine = lineVActual-(int)((35./45.)*(bottomLine-topLine)*0.5);
		int rightLine = lineVActual+(int)((35./45.)*(bottomLine-topLine)*0.5);
 
		int cropWidth = rightLine-leftLine;
		int cropHeigh = bottomLine-topLine;
		
        BufferedImage cropImg = originalImage.getSubimage(leftLine, topLine, cropWidth,cropHeigh);//Sannyo

        //double scale = 3.50/4.50;//scale of biometric photo
 
        ImageIcon img=new ImageIcon(cropImg);

		//paper size : 100x150 mm mm (paper size A6: 105 x 148 mm) 
        //w=100 mm= 3.937.. inch -> setSize : 3.937*72 = 284 
		//w=150 mm= 5.09551.. inch -> setSize : 5.09551*72 = 425 
        int paperWidth = 850;//2x425
        int paperHeigh = 568;//2x284
        
        BufferedImage bufferedImage = new BufferedImage(paperWidth, paperHeigh, BufferedImage.TYPE_INT_RGB);

        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
 
        // fill all the image with white
        g2d.setColor(Color.white);
        
        int margin=0;
		g2d.fillRect(margin, margin, paperWidth-margin*2, paperHeigh-margin*2);

		double imageWidth = img.getIconWidth();//Double: width of crop-image 
		double imageHeight = img.getIconHeight();//Double: height of crop-image

		double scaleXX = (double)paperWidth / imageWidth;
		double scaleYY = (double)paperHeigh / imageHeight;

		int w=(int) imageWidth;//Integer: width of crop-image 
		int h=(int) imageHeight;//Integer: height of crop-image
		double scaleFactor2 = Math.min(scaleXX / 2, scaleYY / 2);//divided by 2, because 2 row of image

		g2d.scale(scaleFactor2*0.85, scaleFactor2*0.85);// 0.85, because draw area only 85% of paper size

		//paper size : 100x150 mm mm (paper size A6: 105 x 148 mm)
		//https://www.papersizes.org/a-sizes-in-pixels.htm
		//https://www.papersizes.org/a-sizes-all-units.htm
		//in printer 1440 PPI, A6: in  pixels: 5953 x 8391

		margin = 0;//margin between two images
        int edgeLR=530;//edge Left-Right (pixels)
        int edgeTB=450;//edge Top-Bottom (pixels)

        // draw image to graphics row 1 (x4)
        //g2d.drawImage(img, x, y, observer);//x,y, coordinate
		g2d.drawImage(img.getImage(), w*0 + margin*0+edgeLR*1, h*0+margin*0+edgeTB*1, null);
		g2d.drawImage(img.getImage(), w*1 + margin*1+edgeLR*1, h*0+margin*0+edgeTB*1, null);
		g2d.drawImage(img.getImage(), w*2 + margin*2+edgeLR*1, h*0+margin*0+edgeTB*1, null);
		g2d.drawImage(img.getImage(), w*3 + margin*3+edgeLR*1, h*0+margin*0+edgeTB*1, null);

		// draw image to graphics row 2 (x4)
		g2d.drawImage(img.getImage(), w*0 + margin*0+edgeLR*1, h*1+margin*1+edgeTB*1, null);
		g2d.drawImage(img.getImage(), w*1 + margin*1+edgeLR*1, h*1+margin*1+edgeTB*1, null);
		g2d.drawImage(img.getImage(), w*2 + margin*2+edgeLR*1, h*1+margin*1+edgeTB*1, null);
		g2d.drawImage(img.getImage(), w*3 + margin*3+edgeLR*1, h*1+margin*1+edgeTB*1, null);

        g2d.setColor(Color.LIGHT_GRAY);
		// draw border to graphics - row 1 (x4)
		g2d.drawRect(w*0 + margin*0+edgeLR*1, h*0+margin*0+edgeTB*1, w , h);
		g2d.drawRect(w*1 + margin*1+edgeLR*1, h*0+margin*0+edgeTB*1, w , h);
		g2d.drawRect(w*2 + margin*2+edgeLR*1, h*0+margin*0+edgeTB*1, w , h);
		g2d.drawRect(w*3 + margin*3+edgeLR*1, h*0+margin*0+edgeTB*1, w , h);

		// draw border to graphics - row 2 (x4)
		g2d.drawRect(w*0 + margin*0+edgeLR*1, h*1+margin*1+edgeTB*1, w , h);
		g2d.drawRect(w*1 + margin*1+edgeLR*1, h*1+margin*1+edgeTB*1, w , h);
		g2d.drawRect(w*2 + margin*2+edgeLR*1, h*1+margin*1+edgeTB*1, w , h);
		g2d.drawRect(w*3 + margin*3+edgeLR*1, h*1+margin*1+edgeTB*1, w , h);

		// draw lines for cropping-guideline
		//HORIZONTAL
		int x1=0;
		int x2=(int)(20/(scaleFactor2*0.85));

		int y1=h*0+margin*0+edgeTB*1;
		int y2=y1;
        //horizontal row 1 -Left Side
		g2d.drawLine(x1, y1, x2, y2);

		y1=h*1+margin*0+edgeTB*1;
		y2=y1;
	    //horizontal row 2 -Left Side
		g2d.drawLine(x1, y1, x2, y2);

		y1=h*2+margin*0+edgeTB*1;
		y2=y1;
	    //horizontal row 3 -Left Side
		g2d.drawLine(x1, y1, x2, y2);
		
		
		// draw lines for cropping-guideline
		//VERTICAL
		x1=(int)((paperWidth-20)/(scaleFactor2*0.85));
		x2=(int)(paperWidth/(scaleFactor2*0.85));
		
		y1=h*0+margin*0+edgeTB*1;
		y2=y1;
		//horizontal row 1 -Right Side
		g2d.drawLine(x1, y1, x2, y2);

		
		y1=h*1+margin*0+edgeTB*1;
		y2=y1;
	    //horizontal row 2 -Right Side
		g2d.drawLine(x1, y1, x2, y2);

		y1=h*2+margin*0+edgeTB*1;
		y2=y1;
	    //horizontal row 3 -Right Side
		g2d.drawLine(x1, y1, x2, y2);

		
		//vertical 1-5 - Top Side
		x1=x2=w*0 + margin*0+edgeLR*1;
		y1=0;
		y2=(int)(20/(scaleFactor2*0.85));
		g2d.drawLine(x1, y1, x2, y2);

		x1=x2=w*1 + margin*0+edgeLR*1;
		g2d.drawLine(x1, y1, x2, y2);

		x1=x2=w*2 + margin*0+edgeLR*1;
		g2d.drawLine(x1, y1, x2, y2);

		x1=x2=w*3 + margin*0+edgeLR*1;
		g2d.drawLine(x1, y1, x2, y2);

		x1=x2=w*4 + margin*0+edgeLR*1;
		g2d.drawLine(x1, y1, x2, y2);

		//vertical 1-5 - Bottom Side
		x1=x2=w*0 + margin*0+edgeLR*1;
		y1=(int)((paperHeigh-20)/(scaleFactor2*0.85));
		y2=(int)((paperWidth)/(scaleFactor2*0.85));
		g2d.drawLine(x1, y1, x2, y2);

		x1=x2=w*1 + margin*0+edgeLR*1;
		g2d.drawLine(x1, y1, x2, y2);

		x1=x2=w*2 + margin*0+edgeLR*1;
		g2d.drawLine(x1, y1, x2, y2);

		x1=x2=w*3 + margin*0+edgeLR*1;
		g2d.drawLine(x1, y1, x2, y2);

		x1=x2=w*4 + margin*0+edgeLR*1;
		g2d.drawLine(x1, y1, x2, y2);

       // Disposes of this graphics context and releases any system resources that it is using. 
        g2d.dispose();
        
    	previewImage=bufferedImage;
        if(!preview)
        {
        	toFile(bufferedImage,fileOut);
        }
    }
    
    public void toFile(BufferedImage bufferedImage,String fileOut) throws IOException
    {
		File outputfile = new File(fileOut);
		ImageIO.write(bufferedImage, "jpg", outputfile);
    }

    public BufferedImage getPreviewImage()
    {
		return previewImage;
    }
}
