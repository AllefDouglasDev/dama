package main;

import java.util.ArrayList;

public class Dama {
	
	private ArrayList<Casa> casasPermitidas;
	
	public Estado[][] posicoesIniciais() {
		Estado[][] posicoes = new Estado[8][8];
		
		limparPosicoes(posicoes);
		
		preencherLinhaTracejada(posicoes, 0, Estado.PRETO, true);
		preencherLinhaTracejada(posicoes, 1, Estado.PRETO, false);
		preencherLinhaTracejada(posicoes, 2, Estado.PRETO, true);
		
		preencherLinhaTracejada(posicoes, 5, Estado.BRANCO, false);
		preencherLinhaTracejada(posicoes, 6, Estado.BRANCO, true);
		preencherLinhaTracejada(posicoes, 7, Estado.BRANCO, false);
		
		return posicoes;
	}
	
	public void limparPosicoes(Estado[][] posicoes) {
		for (int i = 0; i < 8; i++) {
			preencherLinha(posicoes, i, Estado.VAZIO);
		}
	}
	
	public void preencherLinha(Estado[][] posicoes, int linha, Estado estado) {
		for (int i = 0; i < 8; i++) {
			posicoes[i][linha] = estado;
		}
	}
	
	public void preencherLinhaTracejada(Estado[][] posicoes, int linha, Estado estado, boolean inicioPreenchido) {
		int inicio = inicioPreenchido ? 0 : 1;
		for (int i = inicio; i < 8; i += 2) {
			posicoes[i][linha] = estado;
		}
	}
	
	public boolean eCasaPermitida(Casa casa) {
		return getCasasPermitidas().contains(casa);
	}
	
	public ArrayList<Casa> getCasasPermitidas() {
		if (casasPermitidas != null) return casasPermitidas;
		
		casasPermitidas = new ArrayList<>();
		
		for (int i = 0; i < 8; i += 2) 
			for (int j = 0; j < 8; j += 2) 
				casasPermitidas.add(new Casa(i, j));
		
		for (int i = 1; i < 8; i += 2) 
			for (int j = 1; j < 8; j += 2) 
				casasPermitidas.add(new Casa(i, j));
		
		return casasPermitidas;
	}
}
