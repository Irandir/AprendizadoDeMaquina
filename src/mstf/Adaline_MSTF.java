package mstf;

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

import MLP.*;
import grafico.Grafico;


public class Adaline_MSTF {

	public static void main(String[] args) {
		int tamanhoDaJanela = 1;

		
		List<Double> dados = leituraJSON();
		
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
		g.mostrar2(saida, saidaV, saidaT, output, outputV, outputT, "Adaline Não Linear", "PETR4.SA");
		
		
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
		
		g.mostrar2(saida, saidaV, saidaT, output4, outputV5, outputT6, "Adaline Linear", "PETR4.SA");
		JOptionPane.showMessageDialog(null, ""+adaline5.getEmq()+" "+adaline6.getEmq());
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
	public static ArrayList<Double> leituraJSON() {
		JSONObject jsonObject;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONParser parser = new JSONParser();
		// TODO Auto-generated method stub
        int a = 0;
        ArrayList<Double> dados = new ArrayList<Double>();
		try {
            //Salva no objeto JSONObject o que o parse tratou do arquivo
			String path = MLP_MSTF.class.getResource("mstf.json").getPath();
			jsonObject = (JSONObject) parser.parse(new FileReader(path));
			jsonObject2 = (JSONObject)jsonObject.get("Time Series (Daily)");
			Calendar calendar = Calendar.getInstance();
			int aux = 0;
			String data;
			int fechar = 0;
			Double value;
			do{
				calendar.set(2018, 01, 01+aux);
				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				data = sdf.format(date);
				jsonObject3 = (JSONObject)jsonObject2.get(data);
				if(jsonObject3!=null){
					value = Double.parseDouble(jsonObject3.get("4. close").toString());
					if(value!=0){
						dados.add(value);
						a++;
						System.out.println(a);
					}
					fechar = 0;
				}
				fechar++;
				aux++;
				
			}while(fechar<=10);
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
