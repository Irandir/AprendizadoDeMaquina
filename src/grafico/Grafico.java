package grafico;


import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Grafico extends JFrame {


	public static void mostrar(double[] desejadoTreino, double[] desejadoTeste,double[] saidas, double [] saida2,String nomeDaTecinca,String nomeDaBase) {
		DefaultCategoryDataset dados = new DefaultCategoryDataset();
		int k ;
		for (k = 0; k < saidas.length; k++) {
			dados.addValue(saidas[k],"Saida Obitida Treino",k+"");
		}
		for (int i = 0; i < saida2.length; i++) {
			dados.addValue(saida2[i],"Saida Obitida Previsão",(i+k)+"");
		}
		int j;
		
		for (j = 0; j < desejadoTreino.length; j++) {
			dados.addValue(desejadoTreino[j],"Saida Desejada ",""+j);
		}
		for (int j2 = 0; j2 < desejadoTeste.length; j2++) {
			dados.addValue(desejadoTeste[j2],"Saida Desejada ",""+(j2+j));
		}
		JFreeChart grafico = ChartFactory.createLineChart(nomeDaTecinca, "Nº", "Saída", dados, PlotOrientation.VERTICAL,
				true, true, true);
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.RED );
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(1, Color.BLACK );
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(2, Color.BLUE );
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(3, Color.WHITE );
        
        
		JFrame frame = new JFrame(nomeDaBase);
		frame.add(new ChartPanel(grafico));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void mostrar2(double[] desejadoTreino,double[] desejadoValidacao, double[] desejadoTeste,double[] treino, double [] validacao,double[] teste,String nomeDaTecinca,String nomeDaBase) {
		DefaultCategoryDataset dados = new DefaultCategoryDataset();
		int k ,i,p;
		for (k = 0; k < treino.length; k++) {
			dados.addValue(treino[k],"Saida Obitida Treino",k+"");
		}
		for (i = 0; i < validacao.length; i++) {
			dados.addValue(validacao[i],"Saida Obitida Validação",(i+k)+"");
		}
		for (p = 0; p < teste.length; p++) {
			dados.addValue(teste[p],"Saida Obitida Previsão",(i+k+p)+"");
		}
		int j,j2,j3;
		
		for (j = 0; j < desejadoTreino.length; j++) {
			dados.addValue(desejadoTreino[j],"Saida Desejada ",""+j);
		}
		for (j2 = 0; j2 < desejadoValidacao.length; j2++) {
			dados.addValue(desejadoValidacao[j2],"Saida Desejada ",""+(j2+j));
		}
		for (j3 = 0; j3 < desejadoTeste.length; j3++) {
			dados.addValue(desejadoTeste[j3],"Saida Desejada ",""+(j2+j+j3));
		}
		JFreeChart grafico = ChartFactory.createLineChart(nomeDaTecinca, "Nº", "Saída", dados, PlotOrientation.VERTICAL,
				true, true, true);
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.RED );
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(1, Color.BLACK );
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(2, Color.BLUE );
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(3, Color.ORANGE );
        
        
		JFrame frame = new JFrame(nomeDaBase);
		frame.add(new ChartPanel(grafico));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	public static void mostrar3(double[] vector,String nome) {

		DefaultCategoryDataset d = new DefaultCategoryDataset();
		
		int k;
		for (k = 0; k < vector.length; k++) {
			d.addValue(vector[k], nome, k + "");
		}

		JFreeChart grafico = ChartFactory.createLineChart("", "geracao", "fitness", d, PlotOrientation.VERTICAL, true,
				true, true);
		grafico.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.RED);

		JFrame frame = new JFrame();
		frame.add(new ChartPanel(grafico));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
