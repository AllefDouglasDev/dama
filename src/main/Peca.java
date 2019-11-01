package main;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;

public class Peca extends JButton
{
	private static int tamanho = 64;
	
	private Estado estado;
	private Casa casa;
	
	public Peca()
	{
		super();
		setBackground(new Color(40,200,0));
		estado = Estado.VAZIO;
	}

	public void setEstado(Estado e) { estado = e; }
	
	public Estado getEstado() { return estado; }

	public Dimension getMaximumSize() { return getPreferredSize(); }
	
	public Dimension getMinimumSize() { return getPreferredSize(); }
	
	public Dimension getPreferredSize() { return new Dimension(tamanho, tamanho); }
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Não preenchemos botões vazios.
		if (estado != Estado.VAZIO)
		{
			Color[] cores = new Color[2];
			if (estado == Estado.BRANCO)
			{ 
				cores[0] = Color.WHITE; 
				cores[1] = new Color(220,220,220); 
			}
			else if (estado == Estado.PRETO)
			{ 
				cores[0] = new Color(100,100,100); 
				cores[1] = Color.BLACK; 
			}
			RadialGradientPaint paint =
					new RadialGradientPaint(new Point2D.Double(tamanho / 3, tamanho / 3),
							2 * tamanho / 3, new float[]{0f,1f}, cores);
			g2d.setPaint(paint);
			g2d.fillOval(6, 6, getWidth()-12, getHeight()-12);
		}
		// Pintamos a borda da peça independente do estado.
		g2d.setColor(new Color(20,150,0));
		g2d.setStroke(new BasicStroke(3f));
		g2d.drawOval(6, 6, getWidth()-12, getHeight()-12);
	}
	
	public void setCasa(Casa casa) {
		this.casa = casa;
	}
	
	public Casa getCasa() {
		return casa;
	}
	
	/**
	 * 0 = up left
	 * 1 = up right
	 * 2 = down left
	 * 3 = down right
	 * @return
	 */
	public ArrayList<Casa> getCasasProximas() {
		ArrayList<Casa> casasProximas = new ArrayList<>();
		
		Casa upLeft = new Casa(casa.getX() - 1, casa.getY() - 1);
		Casa upRight = new Casa(casa.getX() + 1, casa.getY() - 1);
		Casa downLeft = new Casa(casa.getX() - 1, casa.getY() + 1);
		Casa downRight = new Casa(casa.getX() + 1, casa.getY() + 1);
		
		casasProximas.add(upLeft);
		casasProximas.add(upRight);
		casasProximas.add(downLeft);
		casasProximas.add(downRight);
		
		return casasProximas;
	}
	
	/**
	 * 0 = up left
	 * 1 = up right
	 * 2 = down left
	 * 3 = down right
	 * @return
	 */
	public ArrayList<Casa> getCasasDistantes() {
		ArrayList<Casa> casasDistantes = new ArrayList<>();
		
		Casa upLeft = new Casa(casa.getX() - 2, casa.getY() - 2);
		Casa upRight = new Casa(casa.getX() + 2, casa.getY() - 2);
		Casa downLeft = new Casa(casa.getX() - 2, casa.getY() + 2);
		Casa downRight = new Casa(casa.getX() + 2, casa.getY() + 2);
		
		casasDistantes.add(upLeft);
		casasDistantes.add(upRight);
		casasDistantes.add(downLeft);
		casasDistantes.add(downRight);
		
		return casasDistantes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PecaMelhor [estado=");
		builder.append(estado);
		builder.append("]\n");
		return builder.toString();
	}
}
