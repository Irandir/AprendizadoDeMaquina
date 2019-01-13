package teste;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AG.AlgoritmoGeneticoReal;
import MLP.MLP;
import grafico.Grafico;

public class Principal {
	static Random rand = new Random();

	public static int porcentagemDoDados(int tamanhoDosDados, double por) {
		int i = 0;
		i = (int) (tamanhoDosDados * por);
		return i;
	}

	// gera vaolores aleatorios entre 0 e 1
	public static double[][] gerandoPesosAleatorio(int qN, int n) {
		double pesos[][] = new double[qN][n];

		for (int i = 0; i < pesos.length; i++) {
			for (int j = 0; j < pesos[0].length; j++) {
				pesos[i][j] = rand.nextDouble();
			}
		}

		return pesos;
	}

	// gera vaolores aleatorios entre 0 e 1
	public static double[] gerandoPesosAleatorio(int qN) {
		double pesos[] = new double[qN];

		for (int i = 0; i < pesos.length; i++) {
			pesos[i] = rand.nextDouble();
		}

		return pesos;
	}

	public static double normazindoDado(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}
	
	private static double[][]normalizaValues(double[][]values,double min,double max){
		double[][] valuesN = new double[values.length][values[0].length];
		for (int i = 0; i < valuesN.length; i++) {
			for (int j = 0; j < valuesN[0].length; j++) {
				valuesN[i][j] = normazindoDado(values[i][j], min, max);
			}
		}
		return valuesN;
	}
	
	private static double[]normalizaValues(double[]values,double min,double max){
		double[] valuesN = new double[values.length];
		for (int i = 0; i < valuesN.length; i++) {
			valuesN[i] = normazindoDado(values[i], min, max);
		}
		return valuesN;
	}
	
	public static void run(){
		System.out.println("Aguarde");
		int tamanhoDaJanela = 12;

		// qNE é o numero de neuronios da Camada de Escondida
		int qNE = 5;

		// qNS é o numero de neuronios da Camada de Saida
		int qNS = 1;
		
		String nomeDaBase = "leite.txt";
		List<Double> dados = leituraDeArquivo2(nomeDaBase);	
		//List<Double> dados2 = leituraDeArquivo2(nomeDaBase,r);
		int dadosTamanho = dados.size() - tamanhoDaJanela;
		
		// tamaho Do Conjunto de Treino
		int treinoLegth = porcentagemDoDados(dadosTamanho, 0.5);
		
		// tamaho Do Conjunto de validacao
		int validaLegth = porcentagemDoDados(dadosTamanho, 0.3);

		// tamaho Do Conjunto de validacao
		int testeLegth = porcentagemDoDados(dadosTamanho, 0.2);
		
		//extremos
		double min = dados.get(0);
		double max = dados.get(0);
		for (int i = 0; i < dados.size(); i++) {
			if (min > dados.get(i)) {
				min = dados.get(i);
			}
			if (max < dados.get(i)) {
				max = dados.get(i);
			}
		}
		
		// dados
		double entrada[][] = new double[treinoLegth][tamanhoDaJanela];
		double saida[] = new double[treinoLegth];
		double entradaV[][] = new double[validaLegth][tamanhoDaJanela];
		double saidaV[] = new double[validaLegth];
		double entradaT[][] = new double[testeLegth][tamanhoDaJanela];
		double saidaT[] = new double[testeLegth];
		for (int i = 0; i < entrada.length; i++) {
			for (int j = 0; j < entrada[0].length; j++) {
				entrada[i][j] = dados.get(j + i);
			}
			saida[i] = dados.get(entrada[0].length + i);
		}
		for (int i = 0; i < entradaV.length; i++) {
			for (int j = 0; j < entradaV[0].length; j++) {
				entradaV[i][j] = dados.get(j + i + entrada.length);
			}
			saidaV[i] = dados.get(entradaV[0].length + i + entrada.length);
		}
		for (int i = 0; i < entradaT.length; i++) {
			for (int j = 0; j < entradaT[0].length; j++) {
				entradaT[i][j] = dados.get(j + i + entrada.length + entradaV.length);
			}
			saidaT[i] = dados.get(entradaT[0].length + i + entrada.length + entradaV.length);
		}
		/*double real[] = new double[r2];
		for (int i = 0; i < real.length; i++) {
			real[i] = dados2.get(i);
		}*/
		
		// Normaliza
		double entradaN[][] = normalizaValues(entrada, min, max);
		double entradaVN[][] = normalizaValues(entradaV, min, max);
		double entradaTN[][] = normalizaValues(entradaT, min, max);
		double saidaN[] = normalizaValues(saida, min, max);
		double saidaVN[] = normalizaValues(saidaV, min, max);
		double saidaTN[] = normalizaValues(saidaT, min, max);
		
		//respostas
		double treino[] = new double[saida.length];
		double validacao[] = new double[saidaV.length];
		double teste[] = new double[saidaT.length];
		//double prevision[] = new double[real.length];
		
		
		//otimizador
		int numeroDeGeracao =2000;
		int x = (tamanhoDaJanela * qNE + qNE) + (qNE * qNS) + qNS;
		double errosMediosQuadraticos[];
		double melhorIndFit[] = new double[numeroDeGeracao];
		AlgoritmoGeneticoReal ag = new AlgoritmoGeneticoReal();
		double[] individuo;
		double[][] populacaoCruzada;
		double[][] populacaoMudata;
		double[][] populacaoSelecionada;
		double[][] populacaoEletismo;
		double[] fitness = null;
		double[] melhorIndividuo = null;
		double[][] populacao = ag.gerandoPopulacao(10, x, -1, 1);
		MLP mlp = new MLP(entradaN, saida, qNE,tamanhoDaJanela,min, max);
		MLP mlpV = new MLP(entradaVN, saidaV, qNE, tamanhoDaJanela,min, max);
		double[] rsmeVectorValida = new double[numeroDeGeracao];
		double pesos2[] = null;
		double rsmeValidaMenor = 9999999;
		for (int cont = 0; cont < numeroDeGeracao; cont++) {

			if (cont > 0) {
				populacaoSelecionada = ag.selecao(fitness, populacao, 3);
				populacaoCruzada = ag.crossoverAritmetico(populacaoSelecionada, 0.7);
				populacaoMudata = ag.mutacaoUniforme(populacaoCruzada, 0.1, -1, 1);
				populacaoEletismo = ag.eletismo(melhorIndividuo, melhorIndFit[cont - 1], populacaoMudata, fitness);
				populacao = populacaoEletismo;
			}

			errosMediosQuadraticos = new double[populacao.length];

			for (int contEMQ = 0; contEMQ < errosMediosQuadraticos.length; contEMQ++) {
				individuo = populacao[contEMQ];
				mlp.setAllPesos(individuo);
				mlp.interation();
				errosMediosQuadraticos[contEMQ] = mlp.getEmq();
			}
			fitness = ag.fitness(errosMediosQuadraticos);
			melhorIndividuo = ag.melhorIndividuo(populacao, fitness);
			melhorIndFit[cont] = ag.melhorIndividuoFit(fitness);
			
			mlpV.setAllPesos(melhorIndividuo);
			mlpV.interation();
			rsmeVectorValida[cont] = mlpV.getEmq();
			if (rsmeVectorValida[cont]<rsmeValidaMenor) {
				rsmeValidaMenor = rsmeVectorValida[cont];
				pesos2 = melhorIndividuo;
			}/*
			//System.out.println("V="+rsmeValida);
			if (rsmeValida>rsmeValida2) {
				System.out.println("Cont -->"+cont);
			}
			rsmeValida2 = mlpV.getEqm();
*/
		}
		
		//otimizador fim
		mlp.setAllPesos(melhorIndividuo);
		mlp.interation();
		treino = mlp.getOutputMLP();
		
		mlpV.setAllPesos(melhorIndividuo);
		mlpV.interation();
		validacao = mlpV.getOutputMLP();
		
		MLP mlpT = new MLP(entradaTN, saidaT, qNE, tamanhoDaJanela,min, max);
		mlpT.setAllPesos(melhorIndividuo);
		mlpT.interation();
		teste = mlpT.getOutputMLP();
		
		Grafico g = new Grafico();
		g.mostrar2(saida, saidaV, saidaT, treino, validacao, teste, "AG+MLP", nomeDaBase,mlpT.getEmq());
		
		mlp.setAllPesos(melhorIndividuo);
		double allPesos[];
		mlp.train(0.01,1000);
		allPesos = mlp.getALLPesos();
		mlpV.setAllPesos(allPesos);
		
		allPesos = mlp.getALLPesos();
		treino = mlp.getOutputMLP();
		
		mlpV.setAllPesos(allPesos);
		mlpV.interation();
		validacao = mlpV.getOutputMLP();
		
		mlpT.setAllPesos(allPesos);
		mlpT.interation();
		teste = mlpT.getOutputMLP();
		
		g.mostrar2(saida, saidaV, saidaT, treino, validacao, teste, "AG+MLP+Gradiente", nomeDaBase,mlpT.getEmq());
		
		/*MLP mlpP = new MLP(entradaTN, real, qNE, tamanhoDaJanela,min, max);
		mlpP.setAllPesos(pesos2);*/
		//prevision = mlpP.prevision(entradaTN[entradaTN.length-1], r2);
		
		
//			
		//g.mostrar3(melhorIndFit, "melhor individuo fitness");
	//	g.mostrar3(rsmeVectorValida, "validação rsme");
/*		
		System.out.println("->"+rsmeValidaMenor);
		
		mlp.setAllPesos(pesos2);
		mlpV.setAllPesos(pesos2);
		mlpT.setAllPesos(pesos2);
		mlp.interation();
		mlpV.interation();
		mlpT.interation();
		treino = mlp.getOutputMLP();
		validacao = mlpV.getOutputMLP();
		teste = mlpT.getOutputMLP();*/
		//g.mostrar2(saida, saidaV, saidaT, treino, validacao, teste, "", nomeDaBase);
		
		//double antI = ant;
		//double prevUtima = prevision[prevision.length-1];
	
		/*//for (int i = 0; i < real.length; i++) {
		System.out.print("anterior --> " + antI + " ideal--> " + real[real.length-1] + "		output Ob-->" + prevUtima);
		if (prevUtima > antI && real[real.length-1] > antI) {
			System.out.println(" + -->V");
			v++;
		} else if (prevUtima <= antI && real[real.length-1] <= antI) {
			System.out.println(" - -->V");
			v++;
		} else {
			System.out.println(" - -->F");
			f++;
		}*/
			//antI = real[i];
			//antR = prevision[i];
		//}
		
		
		//return real[real.length-1];
	}
	
	public static void main(String[] args) {
		run();
		
	}
	
	
	
	public static ArrayList<Double> leituraDeArquivo2(String nome) {

		BufferedReader br = null;
		ArrayList<Double> dados = new ArrayList<Double>();
		try {

			String path = Principal.class.getResource(nome).getPath();
			br = new BufferedReader(new FileReader(path));
			String aux = "";
			String linha = null;
		
			while ((linha = br.readLine()) != null) {
				aux = linha.replaceAll(",", ".");
				dados.add(Double.parseDouble(aux));
				
			}

			br.close();
			return dados;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Double> leituraDeArquivo(String nome,int usaveis) {

		BufferedReader br = null;
		ArrayList<Double> dados = new ArrayList<Double>();
		try {

			String path = Principal.class.getResource(nome).getPath();
			br = new BufferedReader(new FileReader(path));
			int aux = 0;
			String linha = null;
			int cont= 0;
			while ((linha = br.readLine()) != null) {
				if (cont<usaveis) {
					dados.add(Double.parseDouble(linha));
				}
				cont++;
				
			}

			br.close();
			return dados;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
}
