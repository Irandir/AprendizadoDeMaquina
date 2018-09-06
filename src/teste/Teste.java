package teste;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import AG.Principal;

public class Teste {

	public static void main(String[] args) {
		List<Double> dados = Principal.leituraDeArquivo2("/AG/leite.txt",0);
		double x[] = new double[dados.size()];
		for (int i = 0; i < x.length; i++) {
			x[i] = dados.get(i);
		}
		for (int i = 0; i < x.length; i++) {
			System.out.println(x[i]);
		}
		plot(x);

	}
	public static void plot(double[] ampHour) {

		Plot2DPanel plot = new Plot2DPanel();

		double x[] = new double[ampHour.length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
		}
		plot.addLinePlot("Real", x, ampHour);
		
		JFrame frame = new JFrame("Leite");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
}
