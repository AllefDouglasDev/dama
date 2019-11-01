package main;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tabuleiro extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Dama dama;
	private PecaMelhor[][] tabuleiro;
	private int jogadorAtual = 1;
	private PecaMelhor pecaAMover;

	public Tabuleiro()
	{
		dama = new Dama();
		
		setLayout(new GridLayout(8,8));
		tabuleiro = new PecaMelhor[8][8];

		for(int l = 0; l < 8; l++)
			for(int c = 0; c < 8; c++)
			{
				PecaMelhor peca = new PecaMelhor();
				peca.setCasa(new Casa(c, l));
				peca.addActionListener(onPecaClicada(peca, c, l));
				
				tabuleiro[c][l] = peca;
				
				add(tabuleiro[c][l]);
			}

		preencherTabuleiro();
	}
	
	public void preencherTabuleiro() {
		Estado[][] posicoesIniciais = dama.getPosicoesIniciais();
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				tabuleiro[j][i].setEstado(posicoesIniciais[j][i]);
	}
	
	public ActionListener onPecaClicada(final PecaMelhor pecaClicada, final int x, final int y) {
		return e -> {
			switch (pecaClicada.getEstado()) {
				case VAZIO:
					pecaVaziaClicada(pecaClicada, x, y);
					break;
				case BRANCO:
					pecaBrancaClicada(pecaClicada, x, y);
					break;
				case PRETO:
					pecaPretaClicada(pecaClicada, x, y);
					break;
			}
			
		};
	}

	public void pecaVaziaClicada(PecaMelhor pecaClicada, int x, int y) {
		if (pecaAMover == null) return;
		
		Casa casaAMover = new Casa(x, y);
		
		if ((jogadorAtual == 1 && pecaAMover.getEstado() != Estado.BRANCO) ||
			(jogadorAtual == 2 && pecaAMover.getEstado() != Estado.PRETO))
			return;
		
		if (dama.eCasaPermitida(casaAMover) && pecaAMover.getCasasProximas().contains(casaAMover)) {
			mover(pecaAMover, x, y);
			jogadorAtual = jogadorAtual == 1 ? 2 : 1;
			return;
		} 
		
		if (moverParaCasaDistante(casaAMover, pecaAMover)) {
			jogadorAtual = jogadorAtual == 1 ? 2 : 1;
		}
		
		calcularPecas();
	}
	
	
	public void pecaBrancaClicada(PecaMelhor pecaClicada, int x, int y) {
		pecaAMover = pecaClicada;
		pecaAMover.setCasa(new Casa(x, y));
	}
	
	
	public void pecaPretaClicada(PecaMelhor pecaClicada, int x, int y) {
		pecaAMover = pecaClicada;
		pecaAMover.setCasa(new Casa(x, y));
	}
	
	public boolean moverParaCasaDistante(Casa casaAMover, PecaMelhor pecaAMover) {
		boolean moveu = false;
		ArrayList<Casa> casasDistantes = pecaAMover.getCasasDistantes();
		ArrayList<Casa> casasProximas = pecaAMover.getCasasProximas();
		
		if (!casasDistantes.contains(casaAMover)) return false;
		
		if (pecaAMover.getEstado() == Estado.BRANCO) 
			moveu = comerComPecaBranca(casaAMover, pecaAMover, casasDistantes, casasProximas); 
		else
			moveu = comerComPecaPreta(casaAMover, pecaAMover, casasDistantes, casasProximas); 
		return moveu;
	}

	private boolean comerComPecaBranca(Casa casaAMover, PecaMelhor pecaAMover,
			ArrayList<Casa> casasDistantes, ArrayList<Casa> casasProximas) {
		boolean moveu = false;
		
		Casa jumpUpLeft = casasDistantes.get(0);
		Casa jumpUpRight = casasDistantes.get(1);
		
		if (casaAMover.equals(jumpUpRight)) {
			Casa upRight = casasProximas.get(1);
			PecaMelhor pecaASerComida = tabuleiro[upRight.getX()][upRight.getY()];
			
			if (pecaASerComida.getEstado() == Estado.PRETO) {
				tabuleiro[upRight.getX()][upRight.getY()].setEstado(Estado.VAZIO);
				mover(pecaAMover, casaAMover.getX(), casaAMover.getY());
				moveu = true;
			}
		} else if (casaAMover.equals(jumpUpLeft)) {
			Casa upLeft = casasProximas.get(0);
			PecaMelhor pecaASerComida = tabuleiro[upLeft.getX()][upLeft.getY()];
			
			if (pecaASerComida.getEstado() == Estado.PRETO) {
				tabuleiro[upLeft.getX()][upLeft.getY()].setEstado(Estado.VAZIO);
				mover(pecaAMover, casaAMover.getX(), casaAMover.getY());
				moveu = true;
			}
		}
		
		return moveu;
	}
	
	private boolean comerComPecaPreta(Casa casaAMover, PecaMelhor pecaAMover,
			ArrayList<Casa> casasDistantes, ArrayList<Casa> casasProximas) {
		boolean moveu = false;
		
		Casa jumpDownLeft = casasDistantes.get(2);
		Casa jumpDownRight = casasDistantes.get(3);
		
		if (casaAMover.equals(jumpDownRight)) {
			Casa downRight = casasProximas.get(3);
			PecaMelhor pecaASerComida = tabuleiro[downRight.getX()][downRight.getY()];
			
			if (pecaASerComida.getEstado() == Estado.BRANCO) {
				tabuleiro[downRight.getX()][downRight.getY()].setEstado(Estado.VAZIO);
				mover(pecaAMover, casaAMover.getX(), casaAMover.getY());
				moveu = true;
			}
		} else if (casaAMover.equals(jumpDownLeft)) {
			Casa downLeft = casasProximas.get(2);
			PecaMelhor pecaASerComida = tabuleiro[downLeft.getX()][downLeft.getY()];
			
			if (pecaASerComida.getEstado() == Estado.BRANCO) {
				tabuleiro[downLeft.getX()][downLeft.getY()].setEstado(Estado.VAZIO);
				mover(pecaAMover, casaAMover.getX(), casaAMover.getY());
				moveu = true;
			}
		}
		
		return moveu;
	}
	
	public void mover(PecaMelhor peca, int paraX, int paraY) {
		tabuleiro[paraX][paraY].setCasa(peca.getCasa());
		tabuleiro[paraX][paraY].setEstado(peca.getEstado());
		
		tabuleiro[peca.getCasa().getX()][peca.getCasa().getY()].setEstado(Estado.VAZIO);
	}
	
	
	public int[] calcularPecas() {
		int pretas = 0;
		int brancas = 0;
		for (int i = 0; i < 8; i++) { 
			for (int j = 0; j < 8; j++) { 
				if (tabuleiro[i][j].getEstado() == Estado.PRETO) 
					pretas++;
				else if (tabuleiro[i][j].getEstado() == Estado.BRANCO)
					brancas++;
			}
		}
		
		return new int[]{ pretas, brancas };
	}
}