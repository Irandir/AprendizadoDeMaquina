package teste_2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;

import MLP.MLP;
import pert4.MLP_PETR4;

public class TESTE_MLP_DUPLA {

	public static void main(String[] args) {
		List<Double> values = leituraJSON();
		int t = 1;
		double [][] inputTrain = new double[200][t];
		for (int i = 0; i < inputTrain.length; i++) {
			for (int j = 0; j < inputTrain[0].length; j++) {
				inputTrain[i][j] = values.get(j+i);
			}
		}
		
		//extremos
		double min = inputTrain[0][0];
		double max = inputTrain[0][0];
		for (int i = 0; i < inputTrain.length; i++) {
			for (int j = 0; j < inputTrain[0].length; j++) {
				if (min > inputTrain[i][j]) {
					min = values.get(i);
				}
				if (max <= inputTrain[i][j]) {
					max = values.get(i);
				}
			}
		}
		
		double entradaN[][] = normalizaValues(inputTrain, min, max);
		
		double [] outputTrain = new double[200];
		for (int i = 0; i < outputTrain.length; i++) {
			System.out.print(inputTrain[i][inputTrain[0].length-1]+"   "+values.get(i+t)+"  ");
			if (inputTrain[i][inputTrain[0].length-1]<values.get(i+t)) {
				outputTrain[i] = 1;
				System.out.println("1");
			}else if (inputTrain[i][inputTrain[0].length-1]>values.get(i+t)) {
				outputTrain[i] = 0;
				System.out.println("0");
			}else{
				outputTrain[i] = 0.5;
				System.out.println("0.5");
			}
		}
		
		double [][]inputValidation = {{0,0}};
		double []outputValidation = {0};
		double [][] inputTest = {{0,0}};
		double [] outputTest= {0};
		
		MLP_DUPLA mlp = new MLP_DUPLA(entradaN, outputTrain, inputValidation, outputValidation, inputTest, outputTest, 10, inputTrain[0].length, 0, 0);
		mlp.train(0.01,10000);
		double a [] = mlp.getAnswerTrain();
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]+"    "+outputTrain[i]);
		}
		
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
			String path = TESTE_MLP_DUPLA.class.getResource("Apple.json").getPath();
			jsonObject = (JSONObject) parser.parse(new FileReader(path));
			jsonObject2 = (JSONObject)jsonObject.get("Time Series (Daily)");
			Calendar calendar = Calendar.getInstance();
			int aux = 0;
			String data;
			int fechar = 0;
			Double value;
			do{
				calendar.set(2017, 01, 01+aux);
				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				data = sdf.format(date);
				jsonObject3 = (JSONObject)jsonObject2.get(data);
				if(jsonObject3!=null){
					value = Double.parseDouble(jsonObject3.get("4. close").toString());
					if(value!=0){
						dados.add(value);
						a++;
						//System.out.println(a);
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
	private static double[][]normalizaValues(double[][]values,double min,double max){
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
}
