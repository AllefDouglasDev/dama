package main;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tabuleiro extends JPanel
{
	private Dama dama;
	private PecaMelhor[][] tabuleiro;
	private int jogadorAtual;
	private int currentX = -1;
	private int currentY = -1;

	public Tabuleiro()
	{
		dama = new Dama();
		jogadorAtual = 1;
		
		setLayout(new GridLayout(8,8));
		tabuleiro = new PecaMelhor[8][8];

		for(int l=0;l<8;l++)
			for(int c=0;c<8;c++)
			{
				tabuleiro[c][l] = new PecaMelhor();
				add(tabuleiro[c][l]);
				tabuleiro[c][l].addActionListener(onPecaClicada(c, l));
			}

		
		Estado[][] posicoesIniciais = dama.posicoesIniciais();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				tabuleiro[j][i].setEstado(posicoesIniciais[j][i]);
			}
		}
	}
	
	public ActionListener onPecaClicada(final int x, final int y) {
		return e -> {
			PecaMelhor pecaClicada = tabuleiro[x][y];
			
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
			// Define a ultima peca clicada
			currentX = x;
			currentY = y;
		};
	}

	public void pecaVaziaClicada(PecaMelhor pecaClicada, int x, int y) {
		if (currentX < 0) return;
		
		Casa casaAMover = new Casa(x, y);
		PecaMelhor pecaAMover = tabuleiro[currentX][currentY];
		pecaAMover.setCasa(new Casa(currentX, currentY));
		
		if ((jogadorAtual == 1 && pecaAMover.getEstado() != Estado.BRANCO) ||
			(jogadorAtual == 2 && pecaAMover.getEstado() != Estado.PRETO))
			return;
		
		if (dama.eCasaPermitida(casaAMover) && pecaAMover.getCasasProximas().contains(casaAMover)) {
			tabuleiro[x][y].setEstado(pecaAMover.getEstado());
			tabuleiro[currentX][currentY].setEstado(Estado.VAZIO);
			jogadorAtual = jogadorAtual == 1 ? 2 : 1;
			return;
		} 
		
		boolean moveu = moverParaCasaDistante(casaAMover, pecaAMover);
		
		if (moveu) {
			jogadorAtual = jogadorAtual == 1 ? 2 : 1;
		}
		
		calcularPecas();
	}
	
	public void pecaBrancaClicada(PecaMelhor pecaClicada, int x, int y) {
		// System.out.println("x " + x + " y " + y);
	}
	
	public void pecaPretaClicada(PecaMelhor pecaClicada, int x, int y) {
		
	}
	
	public boolean moverParaCasaDistante(Casa casaAMover, PecaMelhor pecaAMover) {
		boolean moveu = false;
		ArrayList<Casa> casasDistantes = pecaAMover.getCasasDistantes();
		ArrayList<Casa> casasProximas = pecaAMover.getCasasProximas();
		
		if (casasDistantes.contains(casaAMover)) {
			if (pecaAMover.getEstado() == Estado.BRANCO) {
				Casa jumpUpLeft = casasDistantes.get(0);
				Casa jumpUpRight = casasDistantes.get(1);
				
				if (casaAMover.equals(jumpUpRight)) {
					Casa upRight = casasProximas.get(1);
					PecaMelhor pecaASerComida = tabuleiro[upRight.getX()][upRight.getY()];
					if (pecaASerComida.getEstado() == Estado.PRETO) {
						tabuleiro[upRight.getX()][upRight.getY()].setEstado(Estado.VAZIO);
						tabuleiro[casaAMover.getX()][casaAMover.getY()].setEstado(pecaAMover.getEstado());
						tabuleiro[currentX][currentY].setEstado(Estado.VAZIO);
						moveu = true;
					}
				} else if (casaAMover.equals(jumpUpLeft)) {
					Casa upLeft = casasProximas.get(0);
					PecaMelhor pecaASerComida = tabuleiro[upLeft.getX()][upLeft.getY()];
					if (pecaASerComida.getEstado() == Estado.PRETO) {
						tabuleiro[upLeft.getX()][upLeft.getY()].setEstado(Estado.VAZIO);
						tabuleiro[casaAMover.getX()][casaAMover.getY()].setEstado(pecaAMover.getEstado());
						tabuleiro[currentX][currentY].setEstado(Estado.VAZIO);
						moveu = true;
					}
				}
			} else {
				
			}
		}
		
		return moveu;
	}
	
	public void calcularPecas() {
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
		
		System.out.println("Pretas: " + pretas);
		System.out.println("Brancas: " + brancas);
	}
}