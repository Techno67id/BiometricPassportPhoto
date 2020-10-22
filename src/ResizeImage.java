import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class ResizeImage {

	//Getting Screen Size, to dimensioning size of Windows of Application
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//get screen size of the user's computer
	private int IMG_HEIGHT = (int)(screenSize.height-30);
	private int IMG_WIDTH = (int)(screenSize.width-50);
	private BufferedImage bufferedImage=null;

	public ResizeImage(String loadFile) {
		
		BufferedImage originalImage=null;
		try {
			originalImage = ImageIO.read(new File(loadFile));
			System.out.println("image width  2#: "+originalImage.getWidth());
			System.out.println("image height 2#: "+originalImage.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

		double scaleX = (double) IMG_WIDTH / originalImage.getWidth();
		double scaleY = (double) IMG_HEIGHT / originalImage.getHeight();
		double scaleFactor = Math.min(scaleX, scaleY);

		BufferedImage resizedImage = new BufferedImage((int) (originalImage.getWidth() * scaleFactor),
				(int) (originalImage.getHeight() * scaleFactor), type);

		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, (int) (originalImage.getWidth() * scaleFactor),
				(int) (originalImage.getHeight() * scaleFactor), null);
		g.dispose();

		bufferedImage=resizedImage;
	}
	
	public BufferedImage getBufferedImage()
	{
		return bufferedImage;
		
	}

}