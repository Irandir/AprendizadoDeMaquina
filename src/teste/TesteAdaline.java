package teste;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;

import MLP.Adaline;
import grafico.Grafico;
import pert4.MLP_PETR4;

public class TesteAdaline {

	public static void main(String[] args) {
		int tamanhoDaJanela = 1;

		
		List<Double> dados = leituraDeArquivo2("leite.txt");
		
		// List<Double> dados2 = leituraDeArquivo2(nomeDaBase,r);
		int dadosTamanho = dados.size() - tamanhoDaJanela;

		// tamaho Do Conjunto de Treino
		int treinoLegth = porcentagemDoDados(dadosTamanho, 0.5);

		// tamaho Do Conjunto de validacao
		int validaLegth = porcentagemDoDados(dadosTamanho, 0.3);

		// tamaho Do Conjunto de validacao
		int testeLegth = porcentagemDoDados(dadosTamanho, 0.2);

		// extremos
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

		// Normaliza
		double entradaN[][] = normalizaValues(entrada, min, max);
		double entradaVN[][] = normalizaValues(entradaV, min, max);
		double entradaTN[][] = normalizaValues(entradaT, min, max);

		Adaline adaline = new Adaline(entradaN, saida, tamanhoDaJanela, max, min,true);
		adaline.train(0.01, 1000);
		adaline.interation();
		double[] output = adaline.getOutputAdaline();
		double pesos[] = adaline.getPesos();
		double bias = adaline.getBias();
		
		//com sigmoid
		Adaline adaline2 = new Adaline(entradaVN, saidaV, tamanhoDaJanela, max, min,true);
		adaline2.setBias(bias);
		adaline2.setPesos(pesos);
		adaline2.interation();
		double[] outputV = adaline2.getOutputAdaline();
		
		Adaline adaline3 = new Adaline(entradaTN, saidaT, tamanhoDaJanela, max, min,true);
		adaline3.setBias(bias);
		adaline3.setPesos(pesos);
		adaline3.interation();
		double[] outputT = adaline3.getOutputAdaline();
		
		
		Grafico g = new Grafico();
		//g.mostrar2(saida, saidaV, saidaT, output, outputV, outputT, "Adaline Não Linear", "PETR4.SA",mlpT.getEmq());
		
		
		Adaline adaline4 = new Adaline(entradaN, saida, tamanhoDaJanela, max, min,false);
		adaline4.train(0.01, 1000);
		adaline4.interation();
		double[] output4 = adaline4.getOutputAdaline();
		double pesos4[] = adaline4.getPesos();
		double bias4 = adaline4.getBias();
		
		//linear
		Adaline adaline5 = new Adaline(entradaVN, saidaV, tamanhoDaJanela, max, min,false);
		adaline5.setBias(bias4);
		adaline5.setPesos(pesos4);
		adaline5.interation();
		double[] outputV5 = adaline5.getOutputAdaline();
		
		Adaline adaline6 = new Adaline(entradaTN, saidaT, tamanhoDaJanela, max, min,false);
		adaline6.setBias(bias4);
		adaline6.setPesos(pesos4);
		adaline6.interation();
		double[] outputT6 = adaline6.getOutputAdaline();
		
		//g.mostrar2(saida, saidaV, saidaT, output4, outputV5, outputT6, "Adaline Linear", "PETR4.SA");
		
		JOptionPane.showMessageDialog(null, ""+adaline5.getEmq()+adaline6.getEmq());
	}

	public static int porcentagemDoDados(int tamanhoDosDados, double por) {
		int i = 0;
		i = (int) (tamanhoDosDados * por);
		return i;
	}

	private static double[][] normalizaValues(double[][] values, double min, double max) {
		double[][] valuesN = new double[values.length][values[0].length];
		for (int i = 0; i < valuesN.length; i++) {
			for (int j = 0; j < valuesN[0].length; j++) {
				valuesN[i][j] = normazindoDado(values[i][j], min, max);
			}
		}
		return valuesN;
	}

	public static double normazindoDado(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
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
}
