package teste;

import AG.AlgoritmoGeneticoReal;

public class TesteAG {
	public static void main(String[] args) {
		int entrada[][] = { { 1, 1,4}, { 1, 2,8}, { 1, 3,12}, { 1, 4 ,24} };
		double saida[] = { 20, 40, 60, 80 };
		double eqm[] = new double[10];
		double saidaO = 0;
		AlgoritmoGeneticoReal agr = new AlgoritmoGeneticoReal();
		double [][] populacaoCruzada;
		double [][] populacaoMudata;
		double [][] populacaoSelecionada;
		double[] fitness;
		double melhor[] = null;
		double melhorIndFit;
		double[][] populacao = agr.gerandoPopulacao(eqm.length, entrada[0].length, -50, 50);
		for (int i = 0; i < 2000; i++) {
			System.out.println("_______Populacao______");
			for (int u = 0; u < populacao.length; u++) {
				for (int j = 0; j < populacao[0].length; j++) {
					System.out.print(populacao[u][j]+" ");
				}
				System.out.println();
			}
			for (int j = 0; j < eqm.length; j++) {
				eqm[j] = 0;
				//entrada/saida inicio
				for (int j2 = 0; j2 < entrada.length; j2++) {
					saidaO = 0;
					for (int k = 0; k < entrada[0].length; k++) {
						saidaO += entrada[j2][k] * populacao[j][k];
					}
					eqm[j] += Math.pow((saida[j2] - saidaO), 2);
				}//entrada/saida fim
				eqm[j] /= entrada[0].length;
			}
			//atualizar
			fitness = agr.fitness(eqm);
			melhor = agr.melhorIndividuo(populacao, fitness);
			melhorIndFit = agr.melhorIndividuoFit(fitness);
			populacaoSelecionada = agr.selecao(fitness, populacao, 4);
			populacaoCruzada = agr.crossoverAritmetico(populacaoSelecionada, 0.7);
			populacaoMudata = agr.mutacaoUniforme(populacaoCruzada, 0.1, -50, 50);
			populacao = populacaoMudata;
			populacao = agr.eletismo(melhor,melhorIndFit,populacao, fitness);
			
		}
		System.out.println("_______Populacao______");
		for (int i = 0; i < populacao.length; i++) {
			for (int j = 0; j < populacao[0].length; j++) {
				System.out.print(populacao[i][j]+" ");
			}
			System.out.println();
		}
		double tetas[] = agr.melhorIndividuo(populacao, eqm);
		System.out.println("____________Resposta________");
		for (int i = 0; i < entrada.length; i++) {
			saidaO = 0;
			for (int k = 0; k < entrada[0].length; k++) {
				saidaO += entrada[i][k] * tetas[k];
			}
			System.out.println(saidaO);
		}
		
	}
	
}
