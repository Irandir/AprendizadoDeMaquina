package MLP;

import java.util.Random;

public class Adaline {
	public double emq = 0.0;
	Random random = new Random();
	double[][] input;
	double[] output;
	private double[] pesos;
	private double bias;
	private double[] outputAdaline;
	double max, min;
	boolean sigmoid = false;
	public Adaline(double[][] input, double[] output, int tamanhoDaJanela, double max, double min,boolean sigmoid) {
		this.input = input;
		this.output = output;
		pesos = gerandoPesosAleatorio(tamanhoDaJanela);
		bias = random.nextFloat();
		outputAdaline = new double[output.length];
		this.max = max;
		this.min = min;
		this.sigmoid = sigmoid;
	}

	public void train(double n, int interacoes) {

		double saida = 0;
		int i = 0;
		int j = 0;
		int count = 0;
		double erro = 0;
		double outputN = 0;
		while (count < interacoes) {
			saida = 0;
			erro = 0;
			emq = 0;
			for (i = 0; i < input.length; i++) {
				// cada instancia da base
				saida = 0;

				for (j = 0; j < input[0].length; j++) {
					saida += input[i][j] * pesos[j];
				}
				saida += bias;
				if(sigmoid == true){
					saida = sigmoid(saida);
				}
				outputN= normazindoDado(output[i], min, max);
				erro = outputN - saida;

				bias = bias + n * erro;

				for (j = 0; j < pesos.length; j++) {
					pesos[j] = pesos[j] + n * erro * input[i][j];
				}

				emq += Math.pow(erro, 2);

			}

			emq = emq / (input.length);

			count++;
		}
		
	}
	public void interation() {
		double saida = 0;
		double outputN = 0;
		emq = 0;
		for (int i = 0; i < input.length; i++) {
			saida = 0;
			for (int k2 = 0; k2 < input[0].length; k2++) {
				saida += input[i][k2] * pesos[k2];
			}
			saida += bias;
			if(sigmoid == true){
				saida = sigmoid(saida);
			}
			outputAdaline[i] =  desnormalizar(saida, min, max);;
			outputN= normazindoDado(output[i], min, max);
			emq += outputN - saida;
		}
		emq = emq/output.length;
	}
	
	// gera vaolores aleatorios entre 0 e 1
	public double[] gerandoPesosAleatorio(int qN) {
		double pesos[] = new double[qN];

		for (int i = 0; i < pesos.length; i++) {
			pesos[i] = random.nextDouble();
		}

		return pesos;
	}
	
	private static double desnormalizar(double dadoNormal, double min, double max) {
		return dadoNormal * (max - min) + min;
	}
	
	public static double normazindoDado(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}
	
	private static double sigmoid(double d) {
		return 1.0 / (1 + Math.exp(-d));
	}

	public double[] getOutputAdaline() {
		return outputAdaline;
	}

	public void setOutputAdaline(double[] outputAdaline) {
		this.outputAdaline = outputAdaline;
	}

	public double[] getPesos() {
		return pesos;
	}

	public void setPesos(double[] pesos) {
		this.pesos = pesos;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}
	
}
