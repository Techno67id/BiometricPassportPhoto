import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

public class ReferenceLine extends JPanel {
	private static final long serialVersionUID = 1L;
	private int SIZE = 8;
	private Rectangle2D[] points = { new Rectangle2D.Double(200, 50, SIZE, SIZE),
			new Rectangle2D.Double(200, 100, SIZE, SIZE),new Rectangle2D.Double(200, 150, SIZE, SIZE) };
	Line2D lineV = new Line2D.Double();
	Line2D lineH = new Line2D.Double();

	private String sourceImage="";
	private String saveAs="";
	private BufferedImage previewImage= null;
	private printToJPG printImage = new printToJPG();

	ShapeResizeHandler shapeResize = new ShapeResizeHandler();
	
	public ReferenceLine() {
	}
	
	public void configure(String img,String saveImg) {
		addMouseListener(shapeResize);
		addMouseMotionListener(shapeResize);
		sourceImage=img;
		saveAs=saveImg;
	}
	
	public void execute(boolean preview) {

		// get position of point[0], point[1] and point[2]
		Rectangle2D[] p = { points[0], points[1], points[2] };//top - middle - bottom

		try {
			if(preview)
			{
				printImage.configure(p, sourceImage, saveAs,true);//"true" mean: do preview
				previewImage = printImage.getPreviewImage();//"true" mean: do preview
			} else
			{
				new printToJPG(p, sourceImage, saveAs,false);//"false" mean: save image
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
    public BufferedImage getPreviewImage()
    {
		return previewImage;
    }

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		for (int i = 0; i < points.length; i++) {
			g2.fill(points[i]);
		}

		//vertical blue-line
		g2.setColor(Color.RED);
		lineV.setLine(points[0].getCenterX(), points[0].getCenterY(), Math.abs(points[0].getCenterX()),
				Math.abs(points[1].getCenterY()));
		g2.draw(lineV);
		
		lineV.setLine(points[1].getCenterX(), points[1].getCenterY(), Math.abs(points[1].getCenterX()),
				Math.abs(points[2].getCenterY()));
		g2.draw(lineV);
		
		//horizontal red-line
		g2.setColor(Color.BLUE);
		lineH.setLine(points[0].getCenterX()-100, points[0].getCenterY(), Math.abs(points[0].getCenterX()+100),
				Math.abs(points[0].getCenterY()));
		g2.draw(lineH);
		
		lineH.setLine(points[1].getCenterX()-100, points[1].getCenterY(), Math.abs(points[1].getCenterX()+100),
				Math.abs(points[1].getCenterY()));
		g2.draw(lineH);
		lineH.setLine(points[2].getCenterX()-100, points[2].getCenterY(), Math.abs(points[2].getCenterX()+100),
				Math.abs(points[2].getCenterY()));
		g2.draw(lineH);

		//Rectangular - Cropping
		g2.setColor(Color.GREEN);
		double pos2 = points[2].getCenterY();//point-bottom
		double pos0 = points[0].getCenterY();//point-top
		int d20= (int) (pos2-pos0);//distant between between top-point to bottom

		int x,y;
		int width,height;
		
		double tol = 0.05;//percentage tolerance for position of bottom and top line
		int delta=(int)(tol*d20);//actual tolerance in pixel 

		//from reference, biometric photo (35*45mm), where 22 mm is allocated for eye-region and face-region, the rest is 23 mm
		y=(int) (points[0].getCenterY()-delta);//delta is equivalent about 2mm? 
		height= (int) (d20 + (23./22.)*d20)+delta*3;//3*delta, based on a figure from trial running

		//from reference, biometric photo (35*45mm) then, width: (35./45.) x height 
		width = (int) ((35./45.)*height);
		x=(int) (points[0].getCenterX()-width*0.5);
		
		g2.drawRect(x,y,width,height);
	}

	class ShapeResizeHandler extends MouseAdapter {

		Rectangle2D r = new Rectangle2D.Double(0, 0, SIZE, SIZE);
		private int pos = -1;

		public void mousePressed(MouseEvent event) {
			Point p = event.getPoint();

			for (int i = 0; i < points.length; i++) {
				if (points[i].contains(p)) {
					pos = i;
					return;
				}
			}
		}

		public void mouseReleased(MouseEvent event) {
			pos = -1;
		}

		public void mouseDragged(MouseEvent event) {
			if (pos == -1)
				return;

			switch (pos) {
			case 0:
				points[0].setRect(event.getPoint().x, event.getPoint().y, points[0].getWidth(),
						points[0].getHeight());
				points[1].setRect(event.getPoint().x, points[1].getY(), points[1].getWidth(),
						points[1].getHeight());
				points[2].setRect(event.getPoint().x, points[2].getY(), points[2].getWidth(),
						points[2].getHeight());
				break;
			case 1:
				points[0].setRect(event.getPoint().x, points[0].getY(), points[0].getWidth(),
						points[0].getHeight());
				points[1].setRect(event.getPoint().x, event.getPoint().y, points[1].getWidth(),
						points[1].getHeight());
				points[2].setRect(event.getPoint().x, points[2].getY(), points[2].getWidth(),
						points[2].getHeight());
				break;
			case 2:
				points[0].setRect(event.getPoint().x, points[0].getY(), points[0].getWidth(),
						points[0].getHeight());
				points[1].setRect(event.getPoint().x, points[1].getY(), points[1].getWidth(),
						points[1].getHeight());
				points[2].setRect(event.getPoint().x, event.getPoint().y, points[2].getWidth(),
						points[2].getHeight());
				break;
			}

			repaint();
		}
	}
	
	public void setSaveAs(String s)
	{
		saveAs=s;
	}

	public double getDistance(int p1,int p2)
	{
		double d01 =  points[p2].getCenterY()- points[p1].getCenterY();
		return d01;
	}

}
