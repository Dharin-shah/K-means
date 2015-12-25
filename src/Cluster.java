import java.awt.Color;
import java.awt.Graphics;

public class Cluster {
static final int shapeRadius = 12;
	Color color;
	int x;
	int y;
	void draw(Graphics g) {
		g.setColor(this.color);
		g.fillOval(this.x, this.y, shapeRadius, shapeRadius);
		g.setColor(Color.black);
		g.drawOval(this.x, this.y, shapeRadius, shapeRadius);
	  }
}
