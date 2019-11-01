package main;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tabuleiro extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Dama dama;
	private Peca[][] tabuleiro;
	private int jogadorAtual = 1;
	private Peca pecaAMover;

	public Tabuleiro()
	{
		dama = new Dama();
		
		setLayout(new GridLayout(8,8));
		tabuleiro = new Peca[8][8];

		for(int l = 0; l < 8; l++)
			for(int c = 0; c < 8; c++)
			{
				Peca peca = new Peca();
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
	
	public ActionListener onPecaClicada(final Peca pecaClicada, final int x, final int y) {
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

	public void pecaVaziaClicada(Peca pecaClicada, int x, int y) {
		if (pecaAMover == null) return;
		
		Casa casaAMover = new Casa(x, y);
		
		// Se o jogador atual não tiver clicando nas suas peças
		// Se a casa para onde a peça vai não for válida
		if ((jogadorAtual == 1 && pecaAMover.getEstado() != Estado.BRANCO) ||
			(jogadorAtual == 2 && pecaAMover.getEstado() != Estado.PRETO) ||
			!dama.eCasaPermitida(casaAMover))
			return;
		
		if (pecaAMover.getCasasProximas().contains(casaAMover)) {
			mover(pecaAMover, x, y);
			jogadorAtual = jogadorAtual == 1 ? 2 : 1;
			return;
		} 
		
		if (moverParaCasaDistante(casaAMover, pecaAMover)) {
			jogadorAtual = jogadorAtual == 1 ? 2 : 1;
		}
		
		calcularPecas();
	}
	
	
	public void pecaBrancaClicada(Peca pecaClicada, int x, int y) {
		pecaAMover = pecaClicada;
		pecaAMover.setCasa(new Casa(x, y));
	}
	
	
	public void pecaPretaClicada(Peca pecaClicada, int x, int y) {
		pecaAMover = pecaClicada;
		pecaAMover.setCasa(new Casa(x, y));
	}
	
	public boolean moverParaCasaDistante(Casa casaAMover, Peca pecaAMover) {
		boolean moveu = false;
		ArrayList<Casa> casasDistantes = pecaAMover.getCasasDistantes();
		ArrayList<Casa> casasProximas = pecaAMover.getCasasProximas();
		
		if (!casasDistantes.contains(casaAMover)) return false;
		
		moveu = (pecaAMover.getEstado() == Estado.BRANCO)
			? capturarComPecaBranca(casaAMover, pecaAMover, casasDistantes, casasProximas)
			: capturarComPecaPreta(casaAMover, pecaAMover, casasDistantes, casasProximas); 
		return moveu;
	}

	private boolean capturarComPecaBranca(Casa casaAMover, Peca pecaAMover,
			ArrayList<Casa> casasDistantes, ArrayList<Casa> casasProximas) {
		boolean moveu = false;
		Casa casaAMoverEsquerda = casasDistantes.get(0);
		Casa casaAMoverDireita = casasDistantes.get(1);
		
		if (casaAMover.equals(casaAMoverDireita)) {
			Casa casaDireita = casasProximas.get(1);
			Peca pecaASerCapturada = tabuleiro[casaDireita.getX()][casaDireita.getY()];
			
			if (pecaASerCapturada.getEstado() == Estado.PRETO) {
				tabuleiro[casaDireita.getX()][casaDireita.getY()].setEstado(Estado.VAZIO);
				mover(pecaAMover, casaAMover.getX(), casaAMover.getY());
				moveu = true;
			}
		} else if (casaAMover.equals(casaAMoverEsquerda)) {
			Casa casaEsquerda = casasProximas.get(0);
			Peca pecaASerCapturada = tabuleiro[casaEsquerda.getX()][casaEsquerda.getY()];
			
			if (pecaASerCapturada.getEstado() == Estado.PRETO) {
				tabuleiro[casaEsquerda.getX()][casaEsquerda.getY()].setEstado(Estado.VAZIO);
				mover(pecaAMover, casaAMover.getX(), casaAMover.getY());
				moveu = true;
			}
		}
		
		return moveu;
	}
	
	private boolean capturarComPecaPreta(Casa casaAMover, Peca pecaAMover,
			ArrayList<Casa> casasDistantes, ArrayList<Casa> casasProximas) {
		boolean moveu = false;
		
		Casa casaAMoverEsquerda = casasDistantes.get(2);
		Casa casaAMoverDireita = casasDistantes.get(3);
		
		if (casaAMover.equals(casaAMoverDireita)) {
			Casa casaDireita = casasProximas.get(3);
			Peca pecaASerCapturada = tabuleiro[casaDireita.getX()][casaDireita.getY()];
			
			if (pecaASerCapturada.getEstado() == Estado.BRANCO) {
				tabuleiro[casaDireita.getX()][casaDireita.getY()].setEstado(Estado.VAZIO);
				mover(pecaAMover, casaAMover.getX(), casaAMover.getY());
				moveu = true;
			}
		} else if (casaAMover.equals(casaAMoverEsquerda)) {
			Casa casaEsquerda = casasProximas.get(2);
			Peca pecaASerCapturada = tabuleiro[casaEsquerda.getX()][casaEsquerda.getY()];
			
			if (pecaASerCapturada.getEstado() == Estado.BRANCO) {
				tabuleiro[casaEsquerda.getX()][casaEsquerda.getY()].setEstado(Estado.VAZIO);
				mover(pecaAMover, casaAMover.getX(), casaAMover.getY());
				moveu = true;
			}
		}
		
		return moveu;
	}
	
	public void mover(Peca peca, int paraX, int paraY) {
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