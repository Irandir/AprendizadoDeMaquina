package teste_2;

import java.util.Random;

public class MLP_Mul_Out {
	private double[][] pesos;
	private double[] bias;
	private double[][] pesosE;
	private double[] biasE;

	private double[][] inputTrain;
	private double[][] outputTrain;
	private double[][] inputValidation;
	private double[] outputValidation;
	private double[][] inputTest;
	private double[] outputTest;
	private double nets[];
	private double fnets[];
	private double netsO[];
	private double fnetsO[];
	private double max = 0;
	private double min = 0;
	
	// resposta
	//private double emq;
	private double[][] answerTrain;
	private double[] answerValidation;
	private double[] answerTest;
	Random rand = new Random();
	
	public MLP_Mul_Out(double[][] inputTrain, double[][] outputTrain,double[][] inputValidation ,double[] outputValidation,double[][] inputTest ,double[] outputTest ,int qNE, int tamanhoDaJanela, double max, double min) {
		this.inputTrain = inputTrain;
		this.outputTrain = outputTrain;
		this.inputValidation = inputValidation;
		this.outputValidation = outputValidation;
		this.inputTest = inputTest;
		this.outputTest = outputTest;
		nets = new double[qNE];
		fnets = new double[qNE];
		netsO = new double[outputTrain[0].length];
		fnetsO = new double[outputTrain[0].length];
		pesos = new double[qNE][tamanhoDaJanela];
		bias = new double[qNE];
		pesosE = new double[outputTrain[0].length][qNE];
		biasE = new double[outputTrain[0].length];
		answerTrain = new double[outputTrain.length][outputTrain[0].length];
		this.max = max;
		this.min = min;
		gerandoPesosAleatorio();
		
	}
	public void gerandoPesosAleatorio() {
		for (int i = 0; i < pesos.length; i++) {
			for (int j = 0; j < pesos[0].length; j++) {
				pesos[i][j] = rand.nextDouble();
			}
		}
		for (int i = 0; i < bias.length; i++) {
			bias[i] = rand.nextDouble();
		}
		for (int i = 0; i < pesosE.length; i++) {
			for (int j = 0; j < pesosE[0].length; j++) {
				pesosE[i][j] = rand.nextDouble();
			}
		}
		for (int i = 0; i < biasE.length; i++) {
			biasE[i] = rand.nextDouble();
		}
	}

	public void train(double n,int epocas) {
		double s = 0;
		double[] erro  = new double[outputTrain[0].length];
		double g0[] = new double[outputTrain[0].length];

		double[] gH = null;
		double[] outputND  = new double[outputTrain[0].length];
		answerTrain = new double[outputTrain.length][outputTrain[0].length];
		double emqValidationAnt = Double.MAX_VALUE;
		double emqValidationNext = 0;
		double custo[] = new double[outputTrain[0].length];
		int cont = 0;
		loop:for (int m = 0; m < epocas; m++) {
			// cada instancia (entrada)
			for (int y = 0; y < inputTrain.length; y++) {
				// resetando a saida
				for (int i = 0; i < netsO.length; i++) {
					netsO[i] = 0;
					fnetsO[i] = 0;
				}
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
				for (int i = 0; i < netsO.length; i++) {
					for (int j = 0; j < nets.length; j++) {
						netsO[i] += fnets[j] * pesosE[i][j];
					}
					netsO[i] += biasE[i];
					fnetsO[i] = f(netsO[i]);
				}
				for (int i = 0; i < fnetsO.length; i++) {
					answerTrain[y][i] = fnetsO[i];
				}	
				
				// Gradiente 0(Gradiente Saida)
				g0 = new double[outputTrain[0].length];
				
				// Gradiente H(Gradiente Layer escondida)
				gH = new double[nets.length];
				
				erro  = new double[outputTrain[0].length];
				
				// calculando os erros
				for (int i = 0; i < outputTrain[0].length; i++) {
					outputND[i]  = outputTrain[y][i];
				}
				for (int i = 0; i < erro.length; i++) {
					erro[i] = outputTrain[y][i] - fnetsO[i];
				}
			
				for (int i = 0; i < g0.length; i++) {
					g0[i] = erro[i] * fnetsO[i] * (1 - fnetsO[i]);
				}
				
				s = 0;
				for (int i = 0; i < g0.length; i++) {
					for (int j = 0; j < pesosE.length; j++) {
						s += g0[i] * pesosE[i][j];
					}
				}
				
				// calculando o grandiete H
				for (int i = 0; i < gH.length; i++) {
					gH[i] = fnets[i] * (1 - fnets[i]) * s;
				}

				// atualização dos pesos (Demonio)
				for (int i = 0; i < pesosE.length; i++) {
					for (int j = 0; j < pesosE[0].length; j++) {
						pesosE[i][j] = pesosE[i][j] + n * g0[i] * fnets[i];
					}
				}
				 // bias E
				for (int i = 0; i < biasE.length; i++) {
					biasE[i] = biasE[i] + n * g0[i] * 1;
				}

				for (int i = 0; i < pesos.length; i++) {
					for (int j = 0; j < pesos[0].length; j++) {
						pesos[i][j] = pesos[i][j] + n * gH[i] * inputTrain[y][j];
					}
				}

				// atuaizar o bias
				for (int i = 0; i < bias.length; i++) {
					bias[i] = bias[i] + n * gH[i] * 1;
				}
				for (int i = 0; i < custo.length; i++) {
					custo[i] += Math.pow(erro[i],2);
				}
			}
			
			//custo
			for (int i = 0; i < custo.length; i++) {
				custo[i] = custo[i]+outputTrain[0].length;
				System.out.print("eqm"+i+" -->"+custo[i]+" ");
			}
			System.out.println();
			custo = new double[outputTrain[0].length];
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
	public void train2(double n,int epocas,double linear) {
		double s = 0;
		double[] erro  = new double[outputTrain[0].length];
		double g0[] = new double[outputTrain[0].length];

		double[] gH = null;
		double[] outputND  = new double[outputTrain[0].length];
		answerTrain = new double[outputTrain.length][outputTrain[0].length];
		double emqValidationAnt = Double.MAX_VALUE;
		double emqValidationNext = 0;
		double custo[] = new double[outputTrain[0].length];
		int cont = 0;
		loop:for (int m = 0; m < epocas; m++) {
			// cada instancia (entrada)
			for (int y = 0; y < inputTrain.length; y++) {
				// resetando a saida
				for (int i = 0; i < netsO.length; i++) {
					netsO[i] = 0;
					fnetsO[i] = 0;
				}
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
				for (int i = 0; i < netsO.length; i++) {
					for (int j = 0; j < nets.length; j++) {
						netsO[i] += fnets[j] * pesosE[i][j];
					}
					netsO[i] += biasE[i];
					fnetsO[i] = f2(netsO[i],linear);
				}
				for (int i = 0; i < fnetsO.length; i++) {
					answerTrain[y][i] = fnetsO[i];
				}	
				
				// Gradiente 0(Gradiente Saida)
				g0 = new double[outputTrain[0].length];
				
				// Gradiente H(Gradiente Layer escondida)
				gH = new double[nets.length];
				
				erro  = new double[outputTrain[0].length];
				
				// calculando os erros
				for (int i = 0; i < outputTrain[0].length; i++) {
					outputND[i]  = outputTrain[y][i];
				}
				for (int i = 0; i < erro.length; i++) {
					erro[i] = outputTrain[y][i] - fnetsO[i];
				}
			
				for (int i = 0; i < g0.length; i++) {
					g0[i] = erro[i];
				}
				
				s = 0;
				for (int i = 0; i < g0.length; i++) {
					for (int j = 0; j < pesosE.length; j++) {
						s += g0[i] * pesosE[i][j];
					}
				}
				
				// calculando o grandiete H
				for (int i = 0; i < gH.length; i++) {
					gH[i] = fnets[i] * (1 - fnets[i]) * s;
				}

				// atualização dos pesos (Demonio)
				for (int i = 0; i < pesosE.length; i++) {
					for (int j = 0; j < pesosE[0].length; j++) {
						pesosE[i][j] = pesosE[i][j] + n * g0[i] * fnets[i];
					}
				}
				 // bias E
				for (int i = 0; i < biasE.length; i++) {
					biasE[i] = biasE[i] + n * g0[i] * 1;
				}

				for (int i = 0; i < pesos.length; i++) {
					for (int j = 0; j < pesos[0].length; j++) {
						pesos[i][j] = pesos[i][j] + n * gH[i] * inputTrain[y][j];
					}
				}

				// atuaizar o bias
				for (int i = 0; i < bias.length; i++) {
					bias[i] = bias[i] + n * gH[i] * 1;
				}
				for (int i = 0; i < custo.length; i++) {
					custo[i] += Math.pow(erro[i],2);
				}
			}
			
			//custo
			for (int i = 0; i < custo.length; i++) {
				custo[i] = custo[i]+outputTrain[0].length;
				System.out.print("eqm"+i+" -->"+custo[i]+" ");
			}
			System.out.println();
			custo = new double[outputTrain[0].length];
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
	/*public double validation() {
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
	}*/
	
	private static double f(double d) {
		return 1.0 / (1 + Math.exp(-d));
	}
	private static double f2(double d,double liniar) {
		if (d>=liniar) {
			return 1.0;
		}else{
			return 0;
		}
	}
	private static double desnormalizar(double dadoNormal, double min, double max) {
		return dadoNormal * (max - min) + min;
	}
	public static double normazindoDado(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}

	public double[][] getAnswerTrain() {
		return answerTrain;
	}
	public void setAnswerTrain(double[][] answerTrain) {
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
