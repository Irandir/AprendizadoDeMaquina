package AG;

public class MLP {
	private double[][] pesos;
	private double[] bias;
	private double[] pesosE;
	private double biasE;

	private double[][] input;
	private double[] outputD;
	private double nets[];
	private double fnets[];
	private double netsO;
	private double fnetsO;

	// resposta
	private double eqm;
	private double[] outputMLP;
	private double max = 0;
	private double min = 0;

	public MLP(double[][] input, double[] output, int qNE, int tamanhoDaJanela, double max, double min) {
		this.input = input;
		this.outputD = output;
		nets = new double[qNE];
		fnets = new double[qNE];
		netsO = 0;
		fnetsO = 0;
		pesos = new double[qNE][tamanhoDaJanela];
		bias = new double[qNE];
		pesosE = new double[qNE];
		biasE = 0;
		outputMLP = new double[outputD.length];
		this.max = max;
		this.min = min;
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
		outputMLP = new double[outputD.length];
		for (int m = 0; m < epocas; m++) {
			// cada instancia (entrada)
			for (int y = 0; y < input.length; y++) {
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
						nets[i] += pesos[i][j] * input[y][j];
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
				
				outputMLP[y] = desnormalizar(fnetsO, min, max);
				
				// Gradiente 0(Gradiente Saida)
				g0 = 0;

				// Gradiente H(Gradiente Layer escondida)
				gH = new double[nets.length];

				// calculando os erros
				outputND = normazindoDado(outputD[y], min, max);
				erro = outputND - fnetsO;
				
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
						pesos[i][j] = pesos[i][j] + n * gH[i] * input[y][j];
					}
				}

				// atuaizar o bias
				for (int i = 0; i < bias.length; i++) {
					bias[i] = bias[i] + n * gH[i] * 1;
				}
			}
		}
		
	}

	public void interation() {
		eqm = 0;

		// cada instancia (entrada)
		for (int y = 0; y < input.length; y++) {
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
					nets[i] += pesos[i][j] * input[y][j];
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

			outputMLP[y] = desnormalizar(fnetsO, min, max);
			// EQ
			eqm += Math.pow((outputD[y] - outputMLP[y]), 2);

		} // final da interacoes das instancias

		eqm = eqm / outputD.length;

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

	public double getEqm() {
		return eqm;
	}

	public void setEqm(double eqm) {
		this.eqm = eqm;
	}

	public double[] getOutputMLP() {
		return outputMLP;
	}

	public void setOutputMLP(double[] outputMLP) {
		this.outputMLP = outputMLP;
	}

}
