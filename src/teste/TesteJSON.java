package teste;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;


public class TesteJSON {

	public static void main(String[] args) {
		JSONObject jsonObject;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONArray jsonArray;
		JSONParser parser = new JSONParser();
		// TODO Auto-generated method stub
		Object time;
        String sobrenome;
        String estado;
        String pais;
		try {
            //Salva no objeto JSONObject o que o parse tratou do arquivo
			String path = TesteJSON.class.getResource("petr4.json").getPath();
			jsonObject = (JSONObject) parser.parse(new FileReader(path));
			jsonObject2 = (JSONObject)jsonObject.get("Time Series (Daily)");
			Calendar calendar = Calendar.getInstance();
			int aux = 0;
			String data;
			int fechar = 0;
			do{
				
				calendar.set(2018, 10, 14-aux);
				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				data = sdf.format(date);
				jsonObject3 = (JSONObject)jsonObject2.get(data);
				if(jsonObject3!=null){
					System.out.println(jsonObject3.get("1. open"));
					fechar = 0;
				}
				fechar++;
				aux++;
				
			}while(fechar<=4);
			
			//jsonArray= (JSONArray) jsonObject.get("Time Series (Daily)");
			/*for (Object obj:jsonArray) {
				//jsonObject2 = (JSONObject) obj;
				//System.out.println(jsonObject2.get("time"));
			}*/
			
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
    
	}

}
