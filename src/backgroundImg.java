import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class backgroundImg extends JPanel{
	private static final long serialVersionUID = -4559408638276405147L;
	private String imageFile;
	private Image imageSource;

	public backgroundImg() {
	}

	public backgroundImg(String imgFile) {
		setImageFile(imgFile);
	}

	public backgroundImg(Image img) {
		this.imageSource = img;
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(imageSource, 0, 0, this);
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	public void setImage(Image img) {
		imageSource=img;
	}
}
