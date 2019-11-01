package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

public class Peca extends JButton
{
	private static final long serialVersionUID = 1L;

	private static int tamanho = 64;
	
	private Estado estado;
	
	public Peca()
	{
		super();
		estado = Estado.VAZIO;
	}

	public Dimension getMaximumSize() { return getPreferredSize(); }
	
	public Dimension getMinimumSize() { return getPreferredSize(); }
	
	public Dimension getPreferredSize() { return new Dimension(tamanho,tamanho); }
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		// Não preenchemos botões vazios.
		if (estado != Estado.VAZIO)
		{
			if (estado == Estado.BRANCO) g2d.setColor(Color.WHITE);
			else if (estado == Estado.PRETO) g2d.setColor(Color.BLACK);
			g2d.fillOval(6,6,getWidth()-12,getHeight()-12);
		}
		// Pintamos a borda da peça independente do estado.
		g2d.setColor(Color.GRAY);
		g2d.drawOval(6,6,getWidth()-12,getHeight()-12);
	}
	
	public void setEstado(Estado e)
	{
		estado = e;
	}
}