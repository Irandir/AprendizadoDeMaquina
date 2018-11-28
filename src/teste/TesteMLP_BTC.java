package teste;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;

import MLP.MLP;
import grafico.Grafico;

public class TesteMLP_BTC {
	static Random rand = new Random();

	
	public static void main(String[] args) {
		int tamanhoDaJanela = 1;

		// qNE é o numero de neuronios da Camada de Escondida
		int qNE = 10;

		// qNS é o numero de neuronios da Camada de Saida
		int qNS = 1;
		
		String nomeDaBase = "btc.json";
		List<Double> dados = leituraJSON(nomeDaBase);
		
		
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
	
		
		// Normaliza
		double entradaN[][] = normalizaValues(entrada, min, max);
		double entradaVN[][] = normalizaValues(entradaV, min, max);
		double entradaTN[][] = normalizaValues(entradaT, min, max);
		
		int pesosLength = (tamanhoDaJanela * qNE + qNE) + (qNE * qNS) + qNS;
		double[]allPesos = gerandoPesosAleatorio(pesosLength);
		
		MLP mlp = new MLP(entradaN, saida, qNE,tamanhoDaJanela,max, min);
		mlp.setAllPesos(allPesos);
		mlp.train(0.01,2000);
		double [] pesos = mlp.getALLPesos();
		double []treino = mlp.getOutputMLP();
	
		
		MLP mlpV = new MLP(entradaVN, saidaV, qNE, tamanhoDaJanela,max, min);
		mlpV.setAllPesos(pesos);
		mlpV.interation();
		double []validacao = mlpV.getOutputMLP();
		
		MLP mlpT = new MLP(entradaTN, saidaT, qNE, tamanhoDaJanela,max, min);
		mlpT.setAllPesos(pesos);
		mlpT.interation();
		double []teste = mlpT.getOutputMLP();
		
		Grafico g = new Grafico();
		g.mostrar2(saida, saidaV, saidaT, treino, validacao, teste, "MLP", nomeDaBase);

	}
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
	
	public static ArrayList<Double> leituraDeArquivo(String nome) {

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
	
	public static ArrayList<Double> leituraJSON(String nome) {
		JSONObject jsonObject;
		JSONObject jsonObject2;
		JSONArray jsonArray;
		JSONParser parser = new JSONParser();
		// TODO Auto-generated method stub
		Object time;
        String sobrenome;
        String estado;
        String pais;
        ArrayList<Double> dados = new ArrayList<Double>();
		try {
            //Salva no objeto JSONObject o que o parse tratou do arquivo
			String path = TesteJSON.class.getResource(nome).getPath();
			jsonObject = (JSONObject) parser.parse(new FileReader(path));
			jsonArray= (JSONArray) jsonObject.get("Response");
			for (Object obj:jsonArray) {
				jsonObject2 = (JSONObject) obj;
				Double value;
				try{
					value = (Double)jsonObject2.get("close");
				}catch(java.lang.ClassCastException w){
					Long aux = (Long)jsonObject2.get("close");
					value = (double)aux;
				}
				dados.add(value);
			}
			return dados;
        } 
        //Trata as exceptions que podem ser lançadas no decorrer do processo
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

		return null;
	}
}
