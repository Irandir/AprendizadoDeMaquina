package AG;

import java.util.Random;
import java.util.stream.DoubleStream;

public class AlgoritmoGeneticoReal {

	private Random rand = new Random();

	public double[][]gerandoPopulacao(int tamanhoDaPopulacao, int tamanhoDoGene,double min,double max) {
		if (tamanhoDaPopulacao % 2 != 0) {
			tamanhoDaPopulacao++;
		}
		double[][] populacao = new double[tamanhoDaPopulacao][tamanhoDoGene];
		for (int i = 0; i < populacao.length; i++) {
			for (int j = 0; j < populacao[0].length; j++) {
				populacao[i][j] = M(min, max);
			}
		}
		return populacao;
	}
	
	public double[]fitness(double[]value){
		double []fitness = new double[value.length];
		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = 1000/(1+value[i]);
		}
		return fitness;
	}
	
	//selecao torneio
	public double[][] selecao(double[] fitness, double[][] populacao,int nJanelaTorneio) {
		double populacaoSelecionado[][] = new double[populacao.length][populacao[0].length];
		int indiceAux = 0;
		int ind = 0;
		double aux = 0;
		for (int i = 0; i < fitness.length; i++) {
			indiceAux = rand.nextInt(fitness.length);
			aux = fitness[indiceAux];
			ind = indiceAux;
			for (int j = 0; j < nJanelaTorneio; j++) {
				indiceAux = rand.nextInt(fitness.length);
				if (aux < fitness[indiceAux]) {
					aux = fitness[indiceAux];
					ind = indiceAux;
				}	
			}
			for (int j = 0; j < populacaoSelecionado[0].length; j++) {
				populacaoSelecionado[i][j] = populacao[ind][j];
			}
		}
		return populacaoSelecionado;
	}
	//melhor individuo
	public double[] melhorIndividuo(double populacao[][],double fitness[]){
		int indice = 0;
		double value = fitness[0];
		for (int i = 0; i < fitness.length; i++) {
			if (value < fitness[i]) {
				value = fitness[i];
				indice = i;
			}
		}
		double melhorIndividuo[] = new double[populacao[0].length];
		for (int i = 0; i < populacao[0].length; i++) {
			melhorIndividuo[i] = populacao[indice][i];
		}
		return melhorIndividuo;
	}
	//melhor individuo fit
	public double melhorIndividuoFit(double fitness[]){
		double value = fitness[0];
		for (int i = 0; i < fitness.length; i++) {
			if (value < fitness[i]) {
				value = fitness[i];
			}
		}
		return value;
	}
	// eletismo
	public double[][] eletismo(double[] melhorIndividuo,double fitnessDoMelhor,double populacao[][],double[]fitness){
		int indice = 0;
		double value = Double.MAX_VALUE;
		for (int i = 0; i < fitness.length; i++) {
			if (value > fitness[i]) {
				value = fitness[i];
				indice = i;
			}
		}
		double populacaoPosEletismo[][] = new double[populacao.length][populacao[0].length];
		for (int i = 0; i < populacaoPosEletismo.length; i++) {
			for (int j = 0; j < populacao[0].length; j++) {
				populacaoPosEletismo[i][j] = populacao[i][j];
			}
		}
		for (int j = 0; j < populacao[0].length; j++) {
			populacaoPosEletismo[indice][j] = melhorIndividuo[j];
		}
		return populacaoPosEletismo;
	}
	
	// crossover aritmético
	public double[][] crossoverAritmetico(double populacaoSelecionado[][], double probDoCrossover) {
		double populacaoPosCrossover[][] = new double[populacaoSelecionado.length][populacaoSelecionado[0].length];
		double prob = 0;
		double a = 0;
		for (int i = 0; i < populacaoPosCrossover.length / 2; i++) {
			prob = rand.nextDouble();
			if (prob <= probDoCrossover) {
				a = rand.nextDouble();
				for (int j = 0; j < populacaoPosCrossover[0].length; j++) {
					double delete = a * populacaoSelecionado[i * 2][j];
					double delete2 = (1 - a) * populacaoSelecionado[i * 2 + 1][j];
					populacaoPosCrossover[i * 2][j] = delete+delete2;
					populacaoPosCrossover[i * 2 + 1][j] = (1 - a) * populacaoSelecionado[i * 2][j]
							+ a * populacaoSelecionado[i * 2 + 1][j];
				}
			} else {
				for (int j = 0; j < populacaoPosCrossover[0].length; j++) {
					populacaoPosCrossover[i * 2][j] = populacaoSelecionado[i * 2][j];
					populacaoPosCrossover[i * 2 + 1][j] = populacaoSelecionado[i * 2 + 1][j];
				}

			}
		}
		return populacaoPosCrossover;
	}

	// Mutação Uniforme: x’ = x + M
	public double[][] mutacaoUniforme(double populacaoPosCruzamento[][], double probMutacao, double min, double max) {
		double[][] populacaoPosMutacao = new double[populacaoPosCruzamento.length][populacaoPosCruzamento[0].length];
		double prob = 0;
		for (int i = 0; i < populacaoPosMutacao.length; i++) {
			for (int j = 0; j < populacaoPosMutacao[0].length; j++) {
				prob = rand.nextDouble();
				if (prob <= probMutacao) {
					populacaoPosMutacao[i][j] = populacaoPosCruzamento[i][j]+M(min,max);
				}else{
					populacaoPosMutacao[i][j] = populacaoPosCruzamento[i][j];
				}
			}
		}
		return populacaoPosMutacao;
	}

	private double M(double min, double max) {
		DoubleStream r = rand.doubles(1,min, max);
		double[] a = r.toArray();
		return a[0];
	}
}
