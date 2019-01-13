package teste_2;

import java.util.Random;

public class MLP_DUPLA {
	private double[][] pesos;
	private double[] bias;
	private double[] pesosE;
	private double biasE;

	private double[][] inputTrain;
	private double[] outputTrain;
	private double[][] inputValidation;
	private double[] outputValidation;
	private double[][] inputTest;
	private double[] outputTest;
	private double nets[];
	private double fnets[];
	private double netsO;
	private double fnetsO;
	private double max = 0;
	private double min = 0;
	
	// resposta
	//private double emq;
	private double[] answerTrain;
	private double[] answerValidation;
	private double[] answerTest;
	Random rand = new Random();
	
	public MLP_DUPLA(double[][] inputTrain, double[] outputTrain,double[][] inputValidation ,double[] outputValidation,double[][] inputTest ,double[] outputTest ,int qNE, int tamanhoDaJanela, double max, double min) {
		this.inputTrain = inputTrain;
		this.outputTrain = outputTrain;
		this.inputValidation = inputValidation;
		this.outputValidation = outputValidation;
		this.inputTest = inputTest;
		this.outputTest = outputTest;
		nets = new double[qNE];
		fnets = new double[qNE];
		netsO = 0;
		fnetsO = 0;
		pesos = new double[qNE][tamanhoDaJanela];
		bias = new double[qNE];
		pesosE = new double[qNE];
		biasE = 0;
		answerTrain = new double[outputTrain.length];
		this.max = max;
		this.min = min;
		gerandoPesosAleatorio();
	
	}
	public void gerandoPesosAleatorio() {
		for (int i = 0; i < pesos.length; i++) {
			for (int j = 0; j < pesos[0].length; j++) {
				pesos[i][j] = 0;
			}
		}
		for (int i = 0; i < bias.length; i++) {
			bias[i] = 0;
		}
		for (int j = 0; j < pesosE.length; j++) {
			pesosE[j] = 0;
		}
		
		biasE = 0;
		
	}
	public double[] getALLPesos() {
		
		int x = (pesos[0].length * pesos.length + pesos.length) + (pesos.length * 1) + 1;
		double[] all = new double[x];
		int contX = 0;
		for (int i = 0; i < pesos.length; i++) {
			for (int j = 0; j < pesos[0].length; j++) {
				all[contX] = pesos[i][j];
				contX++;
			}
		}
		for (int i = 0; i < bias.length; i++) {
			all[contX] = bias[i];
			contX++;
		}
		for (int j = 0; j < pesosE.length; j++) {
			all[contX] = pesosE[j];
			contX++;
		}

		all[contX] = biasE;
		contX++;
		return all;
	}
	
	public void setAllPesos(double[] values) {
		int contX = 0;
		for (int i = 0; i < pesos.length; i++) {
			for (int j = 0; j < pesos[0].length; j++) {
				pesos[i][j] = values[contX];
				contX++;
			}
		}
		for (int i = 0; i < bias.length; i++) {
			bias[i] = values[contX];
			contX++;
		}
		for (int j = 0; j < pesosE.length; j++) {
			pesosE[j] = values[contX];
			contX++;
		}

		biasE = values[contX];
		contX++;
	}
	

	public void train(double n,int epocas) {
		double s = 0;
		double erro;
		double g0 = 0;
		double[] gH = null;
		double outputND  = 0;
		answerTrain = new double[outputTrain.length];
		double emqValidationAnt = Double.MAX_VALUE;
		double emqValidationNext = 0;
		int cont = 0;
		loop:for (int m = 0; m < epocas; m++) {
			// cada instancia (entrada)
			for (int y = 0; y < inputTrain.length; y++) {
				// resetando a saida
				netsO = 0;
				fnetsO = 0;
				// resert NET(s)
				for (int i = 0; i < nets.length; i++) {
					nets[i] = 0;
					fnets[i] = 0;
				}

				for (int i = 0; i < nets.length; i++) {

					for (int j = 0; j < pesos[0].length; j++) {
						nets[i] += pesos[i][j] * inputTrain[y][j];
					}
					nets[i] += bias[i];
					fnets[i] = f(nets[i]);

				}
				// alimentando a(s) saida(s)
				for (int j = 0; j < nets.length; j++) {
					netsO += fnets[j] * pesosE[j];
				}
				netsO += biasE;
				//fnetsO = netsO;
				fnetsO = f(netsO);
				
				//answerTrain[y] = desnormalizar(fnetsO, min, max);
				answerTrain[y] = fnetsO;
				
				// Gradiente 0(Gradiente Saida)
				g0 = 0;
				
				// Gradiente H(Gradiente Layer escondida)
				gH = new double[nets.length];

				// calculando os erros
				//outputND = normazindoDado(outputTrain[y], min, max);
				outputND  = outputTrain[y];
				erro = outputND - fnetsO;
				
				//g0 = erro;
				g0 = erro * fnetsO * (1 - fnetsO);
				
				// calculando o grandiete H
				for (int i = 0; i < gH.length; i++) {

					s = 0;

					for (int j = 0; j < pesosE.length; j++) {
						s += g0 * pesosE[j];
					}

					gH[i] = fnets[i] * (1 - fnets[i]) * s;

				}

				// atualização dos pesos (Demonio)
				for (int j = 0; j < pesosE.length; j++) {
					pesosE[j] = pesosE[j] + n * g0 * fnets[j];
				} // bias E
				biasE = biasE + n * g0 * 1;


				for (int i = 0; i < pesos.length; i++) {
					for (int j = 0; j < pesos[0].length; j++) {
						pesos[i][j] = pesos[i][j] + n * gH[i] * inputTrain[y][j];
					}
				}

				// atuaizar o bias
				for (int i = 0; i < bias.length; i++) {
					bias[i] = bias[i] + n * gH[i] * 1;
				}
			}
			/*emqValidationNext = validation();
			System.out.println(m+"  "+emqValidationNext);
			if (emqValidationNext > emqValidationAnt) {
				cont++;
			}else{
				cont = 0;
			}
			if (cont>=5) {
				break loop;
			}
			emqValidationAnt = emqValidationNext;*/
		}
		
	}
	
	public double validation() {
		double emq = 0;
		answerValidation = new double[outputValidation.length];
		// cada instancia (entrada)
		for (int y = 0; y < inputValidation.length; y++) {
			// resetando a saida
			netsO = 0;
			fnetsO = 0;
			// resert NET(s)
			for (int i = 0; i < nets.length; i++) {
				nets[i] = 0;
				fnets[i] = 0;
			}

			for (int i = 0; i < nets.length; i++) {

				for (int j = 0; j < pesos[0].length; j++) {
					nets[i] += pesos[i][j] * inputValidation[y][j];
				}
				nets[i] += bias[i];
				fnets[i] = f(nets[i]);

			}
			// alimentando a(s) saida(s)
			for (int j = 0; j < nets.length; j++) {
				netsO += fnets[j] * pesosE[j];
			}
			netsO += biasE;
			fnetsO = netsO;

			answerValidation[y] = desnormalizar(fnetsO, min, max);
			// EQ
			emq += Math.pow((outputValidation[y] - answerValidation[y]), 2);

		} // final da interacoes das instancias

		emq = emq / outputValidation.length;
		return emq;
	}
	
	public double test() {
		double emq = 0;
		answerTest = new double[outputTest.length];
		// cada instancia (entrada)
		for (int y = 0; y < inputTest.length; y++) {
			// resetando a saida
			netsO = 0;
			fnetsO = 0;
			// resert NET(s)
			for (int i = 0; i < nets.length; i++) {
				nets[i] = 0;
				fnets[i] = 0;
			}

			for (int i = 0; i < nets.length; i++) {

				for (int j = 0; j < pesos[0].length; j++) {
					nets[i] += pesos[i][j] * inputTest[y][j];
				}
				nets[i] += bias[i];
				fnets[i] = f(nets[i]);

			}
			// alimentando a(s) saida(s)
			for (int j = 0; j < nets.length; j++) {
				netsO += fnets[j] * pesosE[j];
			}
			netsO += biasE;
			fnetsO = netsO;

			answerTest[y] = desnormalizar(fnetsO, min, max);
			// EQ
			emq += Math.pow((outputTest[y] - answerTest[y]), 2);

		} // final da interacoes das instancias

		emq = emq / outputTest.length;
		return emq;
	}
	
	public double train() {
		double emq = 0;
		answerTrain = new double[outputTrain.length];
		for (int y = 0; y < inputTrain.length; y++) {
			// resetando a saida
			netsO = 0;
			fnetsO = 0;
			// resert NET(s)
			for (int i = 0; i < nets.length; i++) {
				nets[i] = 0;
				fnets[i] = 0;
			}

			for (int i = 0; i < nets.length; i++) {

				for (int j = 0; j < pesos[0].length; j++) {
					nets[i] += pesos[i][j] * inputTrain[y][j];
				}
				nets[i] += bias[i];
				fnets[i] = f(nets[i]);

			}
			// alimentando a(s) saida(s)
			for (int j = 0; j < nets.length; j++) {
				netsO += fnets[j] * pesosE[j];
			}
			netsO += biasE;
			fnetsO = netsO;

			answerTrain[y] = desnormalizar(fnetsO, min, max);
			// EQ
			emq += Math.pow((outputTrain[y] - answerTrain[y]), 2);

		} // final da interacoes das instancias

		emq = emq / outputTrain.length;
		return emq;
	}
	
	private static double f(double d) {
		return 1.0 / (1 + Math.exp(-d));
	}

	private static double desnormalizar(double dadoNormal, double min, double max) {
		return dadoNormal * (max - min) + min;
	}
	public static double normazindoDado(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}

	public double[] getAnswerTrain() {
		return answerTrain;
	}

	public void setAnswerTrain(double[] answerTrain) {
		this.answerTrain = answerTrain;
	}

	public double[] getAnswerValidation() {
		return answerValidation;
	}

	public void setAnswerValidation(double[] answerValidation) {
		this.answerValidation = answerValidation;
	}

	public double[] getAnswerTest() {
		return answerTest;
	}

	public void setAnswerTest(double[] answerTest) {
		this.answerTest = answerTest;
	}
	
	
	
}
