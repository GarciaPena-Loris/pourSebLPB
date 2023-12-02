package m1.archi.restclient.clientInterface.swingInterface;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private Color borderColor; // Nouvel attribut pour la couleur de la bordure
    private int cornerRadius = 15;

    public RoundedPanel(LayoutManager layout, int radius) {
        super(layout);
        cornerRadius = radius;
    }

    public RoundedPanel(LayoutManager layout, int radius, Color bgColor, Color borderColor) {
        super(layout);
        cornerRadius = radius;
        backgroundColor = bgColor;
        this.borderColor = borderColor; // Initialiser la couleur de la bordure
    }

    public RoundedPanel(int radius) {
        super();
        cornerRadius = radius;
    }

    public RoundedPanel(int radius, Color bgColor, Color borderColor) {
        super();
        cornerRadius = radius;
        backgroundColor = bgColor;
        this.borderColor = borderColor; // Initialiser la couleur de la bordure
    }

    // Méthode pour définir la couleur de la bordure
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint(); // Redessiner le composant avec la nouvelle couleur de la bordure
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner le panneau arrondi avec des bordures colorées.
        if (backgroundColor != null) {
            graphics.setColor(backgroundColor);
        } else {
            graphics.setColor(getBackground());
        }
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height); // Peindre le fond

        // Dessiner la bordure avec la couleur spécifiée
        if (borderColor != null) {
            graphics.setColor(borderColor);
            graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height); // Dessiner la bordure
        }
    }
}
