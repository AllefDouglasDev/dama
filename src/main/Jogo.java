package main;

import javax.swing.JFrame;

public class Jogo extends JFrame
{
	private static final long serialVersionUID = 1L;

	public Jogo()
	{
		super("Dama");
		getContentPane().add(new Tabuleiro());
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args)
	{
		new Jogo();
	}
}