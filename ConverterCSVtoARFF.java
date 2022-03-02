package programinhasimportarcsv;
/**
 *
 * @author moises
 */

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class ConverterCSVtoARFF {
  
  public static void main(String diretorio, String resposta) throws Exception {
    
    // load CSV
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File(diretorio+resposta+".csv"));
    Instances data = loader.getDataSet();

    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
    saver.setFile(new File(diretorio+resposta+"‪‪.arff"));
    saver.setDestination(new File(diretorio+resposta+"‪‪.arff"));
    saver.writeBatch();
  }
}
