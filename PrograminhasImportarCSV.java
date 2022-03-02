package programinhasimportarcsv;

import java.io.*;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
import java.util.*;

public class PrograminhasImportarCSV {

    File arquivoCSV;
    Scanner leitor;
    String linhasDoArquivo;
    String[] valoresEntreVirgulas;
    String[][] tabela;
    String[][]baseNova; 
    String[][] tabela2;
    String[][] matDistancia;
    int colunas;
    int linhas;
    String caminho;
	private Scanner dados;
	private Scanner dadostexto;
	//String diretorio = "D:\\database\\";
	String diretorio = "/home/moises/Área de Trabalho/database/";

    public static void main(String[] args) throws ParseException, IOException, Exception {
        PrograminhasImportarCSV csv = new PrograminhasImportarCSV();
        csv.menu();
    }

    public void menu() throws IOException, Exception {
        int op = 0;
        String resposta = "";
        int respostaNum;
        dados = new Scanner(System.in);
        dadostexto = new Scanner(System.in);
        
        
        while (op < 99) {
            System.out.println("======================MENU=======================");
            System.out.println("1)Importar arquivo ------------------------------");
            System.out.println("2)Selecionar quem tem evolução cura ou óbito-----");
            System.out.println("3)Excluir registros sem PCR positivos------------");
            System.out.println("4)Tratar Sintomas e Condições--------------------");
            System.out.println("5)Criar atributos--------------------------------");
            System.out.println("6)Tratar atributos-------------------------------");
            System.out.println("10)Excluir atributos sem valor-------------------");
            System.out.println("82)Tratar exames---------------------------------");
            System.out.println("83)Formatar Datas--------------------------------");
            System.out.println("88)Tratamento FULL-------------------------------");
            System.out.println("90)Super Seleção---------------------------------");
            System.out.println("93)Selecionar base de dados de um município------");
            System.out.println("94)Adicionar mais uma base de dados--------------");
            System.out.println("95)Imprimir rótulo de coluna---------------------");
            System.out.println("96)Imprimir linhas-------------------------------");
            System.out.println("97)Exportar arquivo csv--------------------------");
            System.out.println("98)Converter arquivo exportado de csv para ARFF--");
            System.out.println("99)Sair------------------------------------------");
            System.out.println("Digite um numero: ");
            op = dados.nextInt();
            System.out.println("=================================================");
			
            if (op == 1) {
				System.out.println("Digite o nome do arquivo csv:");
				resposta = dadostexto.next();
				System.out.println("Trabalhando em: "+diretorio+resposta+".csv");
				tabela = importarArquivo(tabela, diretorio+resposta+".csv");
			}
			if (op == 2) {
				tabela = tratarEvolucao(tabela);
				System.out.println("Evoluções tratadas!");
			}
			if (op == 3) {
				tabela = selecionarPCRPositivo(tabela);
				System.out.println("Registros sem PCR positivos excluídos!");
			}
			if (op == 4) {
				tabela = tratarSintomas(tabela);
				tabela = tratarCondicoes(tabela);
				System.out.println("Sintomas e condições tratados!");
			}
			if (op == 5) {
				tabela = formatarDatas(tabela, 0); //Data de notificação
				tabela = formatarDatas(tabela, 1); //Data do início dos sintomas
				tabela = formatarDatas(tabela, 8); // Data do teste
				System.out.println("Datas formatadas!");
				tabela = criarAtbTempoNotifica(tabela);
				tabela = criarAtbTempoExame(tabela);
				tabela = criarAtbResidencia(tabela);
				System.out.println("Atributos criados!");
			}
			if (op == 6) {
				tabela = tratarProfSaude(tabela);
				tabela = tratarSexo(tabela);
				tabela = tratarIdade(tabela);
				tabela = tratarAtributoClasse(tabela);
				
				System.out.println("Atributos tratados!");
			}
			if (op == 10) {
				tabela = excluirAtributos(tabela);
				System.out.println("Excluídos atributos/colunas irrelevantes");
			}
			if (op == 82) {
				tabela = exames(tabela);
				System.out.println("Exames tratados!");
			}
			
			if (op ==83) {
				tabela = formatarDatas(tabela, 0); //Data de notificação
				tabela = formatarDatas(tabela, 1); //Data do início dos sintomas
				tabela = formatarDatas(tabela, 8); // Data do teste
				System.out.println("Datas formatadas!");
			}
			if (op==88) {
				System.out.println("Digite o nome do arquivo csv:");
				resposta = dadostexto.next();
				System.out.println("Trabalhando em: "+diretorio+resposta+".csv");
				tabela = importarArquivo(tabela, diretorio+resposta+".csv");
				tabela = tratarEvolucao(tabela);
				tabela = selecionarPCRPositivo(tabela);
				tabela = tratarSintomas(tabela);
				tabela = tratarCondicoes(tabela);
				tabela = formatarDatas(tabela, 0); //Data de notificação
				tabela = formatarDatas(tabela, 1); //Data do início dos sintomas
				tabela = formatarDatas(tabela, 8); // Data do teste
				tabela = criarAtbTempoNotifica(tabela);
				tabela = criarAtbTempoExame(tabela);
				tabela = criarAtbResidencia(tabela);
				System.out.println("Tratar atributos ProfSaude, sexo e idade: ");
				tabela = tratarProfSaude(tabela);
				tabela = tratarSexo(tabela);
				tabela = tratarIdade(tabela);
				tabela = tratarAtributoClasse(tabela);
				tabela = excluirAtributos(tabela);
				System.out.println("Qtde de registros: "+(tabela.length-1)+"| Qtde de atributos: "+tabela[0].length);
				System.out.println("Digite nome que pretende dar ao arquivo csv:");
				resposta = dadostexto.next();
				exportarMatrizParaCSV(tabela, resposta);
			}
			if (op == 90) {
				System.out.println("Digite a STRING de seleção: ");
				resposta = dadostexto.next();
				System.out.println("Digite a COLUNA a ser verificada: ");
				respostaNum =  Integer.parseInt(dadostexto.next());
				System.out.println("Coluna: "+respostaNum);
				tabela = pesquisarPorColuna(tabela, resposta, respostaNum);
			}
			if (op == 93) {
				System.out.println("Entre com o código do IBGE do Município: ");
				resposta = dadostexto.next();
				tabela = pesquisarPorColuna(tabela, resposta, 16);
			}
			
			if (op == 94) {
				System.out.println("====Vamos adicionar mais uma base de dados====");
				System.out.println("Digite o nome do arquivo csv:");
				resposta = dadostexto.next();
				System.out.println("Trabalhando em: "+diretorio+resposta);
				tabela = addBase(diretorio+resposta+".csv");
				System.out.println("Sucesso! Base agora tem Linhas: "+tabela.length+" Colunas: "+tabela[0].length);
			}
			if (op == 95) {
				System.out.println("Digite o número da coluna que deseja verificar: ");
				colunas = dados.nextInt();
				imprimirRotuloColuna(colunas);
			}

			if (op == 96) {
				System.out.println("==============IMPRIMINDO LINHAS==============");
				imprimirLinhas();
			}
			if (op == 97) {
				System.out.println("Digite nome que pretende dar ao arquivo csv:");
				resposta = dadostexto.next();
				exportarMatrizParaCSV(tabela, resposta);
			}
			if (op == 98) {
				System.out.println("Digite nome do arquivo que pretende converter:");
				resposta = dadostexto.next();
				ConverterCSVtoARFF.main(diretorio, resposta);
				System.out.println("Conversão realizada em: "+diretorio+resposta+".arff");
			}

		}

	}
   
    public String[][] importarArquivo(String[][] tab, String cam) { //método recebe um arquivo e coloca os valores numa matriz

        caminho = cam;
        arquivoCSV = new File(caminho);
        try {
            leitor = new Scanner(arquivoCSV); //cria um scanner para ler o arquivo

            linhasDoArquivo = leitor.nextLine(); //recebe cada linha do arquivo
            valoresEntreVirgulas = linhasDoArquivo.split(",", -1); //quebra a linha em vetores  

            colunas = valoresEntreVirgulas.length; //descobrindo o número de colunas da matrix 
            linhas = numLinhas(caminho); //descobrindo o número de linhas da matrix 
     
            tab = new String[linhas][colunas]; //Não precisei o numero de elementos
            
            for (int i = 0; i < colunas; i++) { //colocando a primeira linha na matriz
                tab[0][i] = valoresEntreVirgulas[i];
            }

            int cont = 1;

            while (linhas >= cont && leitor.hasNext()) {    //colocando os valores dos vetores na matriz             

                linhasDoArquivo = leitor.nextLine();  //recebe cada linha do arquivo
                valoresEntreVirgulas = linhasDoArquivo.split(",", -1); //quebra a linha em um vetor expressão regular para alocar espaço vazio 
                
                for (int i = 0; i < colunas; i++) { //colocando vetores na matriz
                tab[cont][i] = valoresEntreVirgulas[i];
                //System.out.print("L" + cont + " C" + i + " : " + valoresEntreVirgulas[i] + "\n");
				}
				cont = cont + 1;

			}
		} catch (FileNotFoundException e) {

		}
        System.out.println("Arquivo importado! Encoding "+System.getProperty("file.encoding"));
        System.out.println("Linhas: "+(tab.length-1)+" Colunas: "+tab[0].length);
        return tab;
    }

    public int numLinhas( String localizacao) throws FileNotFoundException {

    	int l = 0;
    	File arquivo2 = new File(localizacao);    	
    	Scanner scan = new Scanner(arquivo2);
    	while (scan.hasNextLine()) {
    	l++;
		scan.nextLine();
    	}
    	scan.close();
    	return l;
    }

    public String[][] addLinha(String[][] tab) {
        String[][] tabelaAux = new String[tab.length + 1][tab[0].length];

        for (int l = 0; l < tab.length; l++) {
            for (int c = 0; c < tab[0].length; c++) {
                tabelaAux[l][c] = tab[l][c];
            }
        }

        return tabelaAux;
    }

    public String[][] removeLinha(int linha, String[][] tab) {

        String[][] Aux = new String[tab.length - 1][tab[0].length];
        int linAux = 0;

        for (int l = 0; l < tab.length; l++) {
            for (int c = 0; c < tab[0].length; c++) {
                if (l == linha) {
                    linAux = linAux - 1;
                    break;
                }
                Aux[linAux][c] = tab[l][c];
            }
            linAux = linAux + 1;
        }
        return Aux;
    }

    public String[][] addColuna(String[][] tab) {
        String[][] tabelaAux = new String[tab.length][tab[0].length + 1];

        for (int l = 0; l < tab.length; l++) {
            for (int c = 0; c < tab[0].length; c++) {
                tabelaAux[l][c] = tab[l][c];
            }
        }

        return tabelaAux;
    }

    public String[][] removeColuna(int col, String[][] tab) {

        String[][] tabelaAux = new String[tab.length][tab[0].length - 1];
        int colAux;

        for (int l = 0; l < tab.length; l++) {
            colAux = 0;

            for (int c = 0; c <= tab[0].length; c++) {
                if (c == col) {
                    c = c + 1;
                }

                if (c == tab[0].length) {
                    break;
                }

                tabelaAux[l][colAux] = tab[l][c];
                colAux = colAux + 1;
            }
        }
        return tabelaAux;

    }

    public void imprimirLinhas() {
        int l = 0;
        for (l = 0; l < tabela.length; l++) {
            for (int c = 0; c < tabela[0].length; c++) {
                System.out.print(tabela[l][c] + " "); //imprime caracter a caracter  
            }
            System.out.println(" "); //muda de linha  
        }
        System.out.println("Total de " + (l - 1) + " linhas");
        
    }

    public void exportarMatrizParaCSV(String[][] tab, String nome) throws IOException {

// http://dbfconv.com/  site que converte DBF para CSV
        // https://convertio.co/pt/document-converter/     site para converter xls para csv
    	try (FileWriter escrever = new FileWriter(diretorio+nome+".csv")) {
            for (int l = 0; l < tab.length; l++) {
                for (int c = 0; c < tab[0].length; c++) {
                    escrever.append(tab[l][c]);
                escrever.append(","); 
                }
                escrever.append('\n');
            }
            escrever.flush();
            escrever.close();
        }
    	System.out.println("Arquivo exportado em: "+diretorio+nome+".csv");
    }

    public void imprimirMatriz(String[][] tab) {

        for (int l = 0; l < tab.length; l++) {
            for (int c = 0; c < tab[0].length; c++) {
                System.out.print(tab[l][c] + " "); //imprime caracter a caracter  
            }
            System.out.println(" "); //muda de linha  

        }

    }

    public void imprimirRotuloColuna(int c) {
        System.out.println(tabela[0][c]);

    }
    
    public String[][] addBase(String cam) {
    	
    	baseNova = importarArquivo(baseNova, cam); //matrix auxiliar recebe a nova base
    	String[][] tabelaAux = new String[tabela.length+baseNova.length-1][tabela[0].length];// Auxiliar do tamanho das duas 
    	
        for (int l = 0; l < tabela.length; l++) {  //tabelaAuxi recebe tabela (base da memória)
            for (int c = 0; c < tabela[0].length; c++) {
                tabelaAux[l][c] = tabela[l][c];
            }
        }
       
        for (int l = 1; l < baseNova.length; l++) {  //tabelaAuxi recebe tabela (base da memória)
            for (int c = 0; c < baseNova[0].length; c++) {
                tabelaAux[l+tabela.length-1][c] = baseNova[l][c];
            }
        }
    	return tabelaAux;
    }

	public String[][] pesquisarPorColuna(String[][] tab, String valor, int coluna) {

		// registros
		int cont = 1; // evitar pegar a coluna 0 de rótulo
		int linhaAux = 0;

		for (int l = 1; l < tab.length; l++) { // descobrir quantas linhas vai ter tabAux
			if ((tab[l][coluna]).contains(valor)) {
				linhaAux = linhaAux + 1;
			}
		}
		String[][] tabAux = new String[linhaAux + 1][tab[0].length]; // matrix auxiliar
		
		for (int c = 0; c < tab[0].length; c++) { // passando a coluna 0 de rótulo
			tabAux[0][c] = tab[0][c];
		}

		for (int l = 1; l < tab.length; l++) {
			if ((tab[l][coluna]).contains(valor)) { //
				for (int c = 0; c < tab[0].length; c++) { // passando os valores na linha
					tabAux[cont][c] = tab[l][c];
				}
				cont++;
			}
		}
		System.out.println(cont-1 + " registro(s) selecionado(s) com "+valor);
		return tabAux;
	}
    
        
    /* --------------------------INICIO---------------------------*/
    public String[][] tratarEvolucao (String[][] tab) {  
    	int cont = 1; //evitar pegar a coluna 0 de rótulo   
    	int linhaAux = 0;
    	
 
    	for (int l = 1; l < tab.length; l++) { //descobrir quantas linhas vai ter tabAux
    		if ( (tab[l][26]).equalsIgnoreCase("Cura") || (tab[l][26]).equalsIgnoreCase("bito") ) {
    			linhaAux = linhaAux + 1;
    		}
    	}
    	String [][] tabAux =  new String[linhaAux+1][tab[0].length];  // matrix auxiliar 
    		
    	for (int c = 0; c < tab[0].length; c++) { //passando a coluna 0
    		tabAux[0][c] = tab[0][c]; 
    	}
    	
    	for (int l = 1; l < tab.length; l++) {
    		if ( (tab[l][26]).equalsIgnoreCase("Cura") || (tab[l][26]).equalsIgnoreCase("bito") ) {
    			
    			for (int c = 0; c < tab[0].length; c++) { //passando os valores
    	    		tabAux[cont][c] = tab[l][c]; 
    	    	}
    		//	System.out.println("registros número "+cont+" "+tab[l][26]);
    			cont = cont + 1;
    		}
    	 }
    	System.out.println("[SelecionarEvolução] "+(cont-1)+" registros selecionados com dados sobre evolução cura ou óbito!");
    	return tabAux;
    }
    
    public String [][] formatarDatas(String[][] tab, int col) {

		for (int l = 1; l < tab.length; l++) {
		//	System.out.println(tab[l][col]);
			if( tab[l][col] != null && tab[l][col].length() >= 10) {
			tab[l][col] = tab[l][col].substring(0, 10);
			tab[l][col] = tab[l][col].replaceAll("-", "/");
		//	System.out.println("Linha: "+l+". Data tratada-> :"+tab[l][col]);
			}
		}
		return tab;
	}
    
	public String[][] selecionarPCRPositivo(String[][] tab) { // critério exame diferente de PCR coluna 9. 116+1
																// registros
		int cont = 1; // evitar pegar a coluna 0 de rótulo
		int linhaAux = 0;

		for (int l = 1; l < tab.length; l++) { // descobrir quantas linhas vai ter tabAux
			if ((tab[l][9]).contains("RT-PCR") && (tab[l][10]).contains("Positivo")) {
				linhaAux = linhaAux + 1;
			}
		}
		String[][] tabAux = new String[linhaAux + 1][tab[0].length]; // matrix auxiliar

		for (int c = 0; c < tab[0].length; c++) { // passando a coluna 0 de rótulo
			tabAux[0][c] = tab[0][c];
		}

		for (int l = 1; l < tab.length; l++) {
			if ((tab[l][9]).contains("RT-PCR") && (tab[l][10]).contains("Positivo")) { // se não tem PCR exclua
			//	System.out.println("Exame: " + tab[l][9] + ", resultado: " + tab[l][10] + ". Registro nº " + cont);

				for (int c = 0; c < tab[0].length; c++) { // passando os valores na linha
					tabAux[cont][c] = tab[l][c];
				}
				cont++;
			}
		}
		System.out.println("[SelecionarPCR] "+(cont-1)+ " registros selecionados com exame PCR Positivo");
		return tabAux;
	}
    
    public String[][] excluirPCRInconclusivos(String[][] tab) { // critério exame PCR inconclusivo 10 
        int cont = 0;
        for (int l = 1; l < tab.length; l++) {
            if (!((tab[l][10]).contains("Positivo") || (tab[l][10]).contains("Negativo")) ) { //se não for negativo ou positivo exclua        
                tab = removeLinha(l, tab);
                l--;
                cont++;
            }
        }
        System.out.println(cont + " registros excluídos com PCR algum erro");
     
        return tab;
    }
    
    public String[][] tratarSintomas(String[][] tab) {  
        int cont = 0;
        tab = addColuna(tab);
        tab[0][28] = "Assintomático";       
        tab = addColuna(tab);
        tab[0][29] = "Dor de cabeça";
        tab = addColuna(tab);
        tab[0][30] = "Febre";
        tab = addColuna(tab);
        tab[0][31] = "Distúrbios gustativos";
        tab = addColuna(tab);
        tab[0][32] = "Dor de garganta";
        tab = addColuna(tab);
        tab[0][33] = "Distúrbios olfativos";
        tab = addColuna(tab);
        tab[0][34] = "Dispnéia";
        tab = addColuna(tab);
        tab[0][35] = "Tosse";
        tab = addColuna(tab);
        tab[0][36] = "Coriza";
        tab = addColuna(tab);
        tab[0][37] = "Outros";
        
        for (int l = 1; l < tab.length; l++) {
        	if ((tab[l][3]).contains("Assintomtico")) {
        		tab[l][28] = "1.0";
        	}else {
        		tab[l][28] = "0.0";
        	}
        	
        	if ((tab[l][3]).contains("Dor de Cabea")) {
        		tab[l][29] = "1.0";
        	}else {
        		tab[l][29] = "0.0";
        	}
        	
        	if ((tab[l][3]).contains("Febre")) {
        		tab[l][30] = "1.0";
        	}else {
        		tab[l][30] = "0.0";
        	}
        	
        	if ((tab[l][3]).contains("Distrbios Gustativos")) {
        		tab[l][31] = "1.0";
        	}else {
        		tab[l][31] = "0.0";
        	}
        	
        	if ((tab[l][3]).contains("Dor de Garganta")) {
        		tab[l][32] = "1.0";
        	}else {
        		tab[l][32] = "0.0";
        	}
        	
        	if ((tab[l][3]).contains("Distrbios Olfativos")) {
        		tab[l][33] = "1.0";
        	}else {
        		tab[l][33] = "0.0";
        	}
        	
        	if ((tab[l][3]).contains("Dispneia")) {
        		tab[l][34] = "1.0";
        	}else {
        		tab[l][34] = "0.0";
        	}
             
        	if ((tab[l][3]).contains("Tosse")) {
        		tab[l][35] = "1.0";
        	}else {
        		tab[l][35] = "0.0";
        	}
        	
        	if ((tab[l][3]).contains("Coriza")) {
        		tab[l][36] = "1.0";
        	}else {
        		tab[l][36] = "0.0";
        	}
        	
        	if ((tab[l][3]).contains("Outros")) {
        		tab[l][37] = "1.0";
        	}else {
        		tab[l][37] = "0.0";
        	}
        	cont = cont + 1;
       
       }
        return tab;
    }
    
    public String[][] tratarCondicoes(String[][] tab) {  
      
        tab = addColuna(tab);
        tab[0][38] = "Respiratórias";  
        tab = addColuna(tab);
        tab[0][39] = "Renais";
        tab = addColuna(tab);
        tab[0][40] = "Cromossômicas ou Imuno";
        tab = addColuna(tab);
        tab[0][41] = "Cardíacadas";
        tab = addColuna(tab);
        tab[0][42] = "Diabetes";
        tab = addColuna(tab);
        tab[0][43] = "Imunosupressão";
        tab = addColuna(tab);
        tab[0][44] = "Gestante";
        tab = addColuna(tab);
        tab[0][45] = "Puérpera";
        tab = addColuna(tab);
        tab[0][46] = "Obesidade";
        
        for (int l = 1; l < tab.length; l++) {
        	if ((tab[l][6]).contains("Doenas respiratrias crnicas descompensadas")) {
        		tab[l][38] = "1.0";
        	}else {
        		tab[l][38] = "0.0";
        	}
        	
        	if ((tab[l][6]).contains("Doenas renais crnicas em estgio avanado (graus 3, 4 ou 5)")) {
        		tab[l][39] = "1.0";
        	}else {
        		tab[l][39] = "0.0";
        	}
        	
        	if ((tab[l][6]).contains("Portador de doenas cromossmicas ou estado de fragilidade imunolgica")) {
        		tab[l][40] = "1.0";
        	}else {
        		tab[l][40] = "0.0";
        	}
        	
        	if ((tab[l][6]).contains("Doenas cardacas crnicas")) {
        		tab[l][41] = "1.0";
        	}else {
        		tab[l][41] = "0.0";
        	}
        	
        	if ((tab[l][6]).contains("Diabetes")) {
        		tab[l][42] = "1.0";
        	}else {
        		tab[l][42] = "0.0";
        	}
        	
        	if ((tab[l][6]).contains("Imunossupresso")) {
        		tab[l][43] = "1.0";
        	}else {
        		tab[l][43] = "0.0";
        	}
        	
        	if ((tab[l][6]).contains("Gestante")) {
        		tab[l][44] = "1.0";
        	}else {
        		tab[l][44] = "0.0";
        	}
             
        	if ((tab[l][6]).contains("Purpera (at 45 dias do parto)")) {
        		tab[l][45] = "1.0";
        	}else {
        		tab[l][45] = "0.0";
        	}
        	
        	if ((tab[l][6]).contains("Obesidade")) {
        		tab[l][46] = "1.0";
        	}else {
        		tab[l][46] = "0.0";
        	}
       }
        return tab;
    }
    
    public String[][] exames(String[][] tab) {  // OFF
    	tab = addColuna(tab);
    	tab[0][47] = "RT-PCR";
        tab = addColuna(tab);
        tab[0][48] = "TR-Anticorpo";
        tab = addColuna(tab);
        tab[0][49] = "TR-Antígeno";
    
        for (int l = 1; l < tab.length; l++) {
        	if ((tab[l][9]).contains("RT-PCR")) {
        		if((tab[l][10]).contains("Positivo")) {
        			tab[l][47] = "1.0";
        		}
        		else if((tab[l][10]).contains("Negativo")) {
        			tab[l][47] = "0.0";
        		}else{
        			tab[l][47] = "0.5";
        		}
        		tab[l][48] = "0.5";
        		tab[l][49] = "0.5";
        	}
        	
        	if ((tab[l][9]).contains("TESTE RPIDO - ANTICORPO")) {
        		if((tab[l][10]).contains("Positivo")) {
        			tab[l][48] = "1.0";
        		}
        		else if((tab[l][10]).contains("Negativo")) {
        			tab[l][48] = "0.0";
        		}else {
        			tab[l][48] = "0.5";
        		}
        		tab[l][47] = "0.5";
        		tab[l][49] = "0.5";
        	}
        	if ((tab[l][9]).contains("TESTE RPIDO - ANTGENO")) {
        		if((tab[l][10]).contains("Positivo")) {
        			tab[l][49] = "1.0";
        		}
        		else if((tab[l][10]).contains("Negativo")) {
        			tab[l][49] = "0.0";
        		}else{
        			tab[l][49] = "0.5";
        		}
        		tab[l][47] = "0.5";
        		tab[l][48] = "0.5";
        	}
        	if ((tab[l][9]).contains("null") || ( !(tab[l][9]).contains("TESTE RPIDO - ANTICORPO") && !(tab[l][9]).contains("TESTE RPIDO - ANTGENO") && !(tab[l][9]).contains("RT-PCR") )) {
        		tab[l][47] = "0.5";
        		tab[l][48] = "0.5";
        		tab[l][49] = "0.5";
        	}
        }
        return tab;
    }
        	
    public String[][] criarAtbTempoExame (String[][] tab) {  
   	 
    	tab = addColuna(tab);
        tab[0][tab[0].length-1] = "tempoExame";
        
        int cont = 0;
		
        for (int l = 1; l < tab.length; l++) {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			
			if( (!tab[l][1].contains("null")) && (!tab[l][8].contains("null")) && (!tab[l][1].isEmpty()) && (!tab[l][8].isEmpty()) ) {
			LocalDate dt_exame = LocalDate.parse((tab[l][8]), formatter);
			LocalDate dt_sintoma = LocalDate.parse((tab[l][1]), formatter);
			Long diferencaEmDias = ChronoUnit.DAYS.between(dt_sintoma,dt_exame);
			
			tab[l][tab[0].length-1] = Long.toString(diferencaEmDias);
		
			//System.out.println("Linha "+l+" Data sintomas: "+dt_sintoma+" Data exame: "+dt_exame+" : "+diferencaEmDias+" dias para fazer exame!");
			} else {
				tab = removeLinha(l, tab);
				l = l-1;
				cont++;
			}
        }
        System.out.println("[tempoExame] Criando atributo tempo para fazer exame.");
        System.out.println("1) "+cont+" registros excluídos por ter datas vazias ou null!");
		
        cont=0;
		for (int l = 1; l < tab.length; l++) { //tempo de sintomas para começar investigar
			if (Integer.parseInt(tab[l][tab[0].length-1]) < 0 || Integer.parseInt(tab[l][tab[0].length-1]) > 21) {
				tab = removeLinha(l, tab);
				l = l-1;
				cont++;
			}
		}
		System.out.println("2) "+cont+" registros excluídos por tempo para realizar exame com anomalia [<0 e >21]");
        return tab;
    }
  
    public String[][] criarAtbTempoNotifica (String[][] tab) {  
      	 
    	tab = addColuna(tab);
        tab[0][tab[0].length-1] = "tempoNotifica";
        
        int cont = 0;
		
        for (int l = 1; l < tab.length; l++) {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			
			if( (!tab[l][0].contains("null")) && (!tab[l][1].contains("null")) && (!tab[l][1].isEmpty()) && (!tab[l][8].isEmpty()) ) {
			LocalDate dt_not = LocalDate.parse((tab[l][0]), formatter);
			LocalDate dt_sintoma = LocalDate.parse((tab[l][1]), formatter);
			Long diferencaEmDias = ChronoUnit.DAYS.between(dt_sintoma,dt_not);
			
			tab[l][tab[0].length-1] = Long.toString(diferencaEmDias);
		
			//System.out.println("Linha "+l+" Data sintomas: "+dt_sintoma+" Data de notificação: "+dt_not+" : "+diferencaEmDias+" dias para fazer exame!");
			} else {
				tab = removeLinha(l, tab);
				l = l-1;
				cont++;
			}
        }
        System.out.println("[tempoNotifica] Criando atributo tempo de notificação.");
        System.out.println("1) "+cont+" registros excluídos por ter datas vazias ou null!");
		
        cont=0;
		for (int l = 1; l < tab.length; l++) { //tempo de sintomas para começar investigar
			if (Integer.parseInt(tab[l][tab[0].length-1]) < 0 || Integer.parseInt(tab[l][tab[0].length-1]) > 50) {
				tab = removeLinha(l, tab);
				l = l-1;
				cont++;
			}
		}
		System.out.println("2) "+cont+" registros excluídos por tempo para investigar com anomalia [<0 e >50]");
		return tab;
    }
    
    public String[][] criarAtbResidencia (String[][] tab) {  
     	 
    	tab = addColuna(tab);
        tab[0][tab[0].length-1] = "resideCapital";
        
        for (int l = 1; l < tab.length; l++) { //tempo de sintomas para começar investigar
        	 if ( tab[l][16].contains("2704302") || tab[l][16].contains("2927408") || tab[l][16].contains("2304400")
        	 || tab[l][16].contains("2111300")|| tab[l][16].contains("2507507")|| tab[l][16].contains("2611606") ||
        	 tab[l][16].contains("2211001")|| tab[l][16].contains("2408102")|| tab[l][16].contains("2800308") ) {
        	
        		 tab[l][tab[0].length-1] = "1.0";
			}else {
				tab[l][tab[0].length-1] = "0.0";
			}
		}
        return tab;
    }

	public String[][] tratarSexo(String[][] tab) {
		int cont = 0;
		for (int l = 1; l < tab.length; l++) {
			if ((tab[l][12]).contains("Masculino")) {
				tab[l][12] = "1.0";
			} else if ((tab[l][12]).contains("Feminino")) {
				tab[l][12] = "0.0";
			} else {
				tab = removeLinha(l, tab);
				l = l - 1;
				cont = cont + 1;
			}
		}
		System.out.println("[tratarSexo] "+cont + " registros removidos sem definição sobre sexo!");
		return tab;
	}

	public String[][] tratarIdade(String[][] tab) {
		int cont = 0;
		for (int l = 1; l < tab.length; l++) {
			if (tab[l][24].contains("null") || tab[l][24].isEmpty()) {
				cont = cont + 1;
				tab = removeLinha(l, tab);
				l = l - 1;
			} else if (Integer.parseInt(tab[l][24]) > 110) {
				cont = cont + 1;
				tab = removeLinha(l, tab);
				l = l - 1;
			}

		}
		System.out.println("[tratarIdade] "+cont + " registros removidos com dados de idade anômalos [idade > 110; =null]");
		return tab;
	}
	
	public String[][] tratarProfSaude(String[][] tab) {  
    	int cont = 0;
        for (int l = 1; l < tab.length; l++) {
        	if ((tab[l][4]).contains("Sim")) {
        		tab[l][4] = "1.0";
        	}
        	else if ((tab[l][4]).contains("No")) {
        		tab[l][4] = "0.0";
        	}else if ((tab[l][4]) == null){
        	tab = removeLinha(l, tab);
        	l= l-1;
        	cont = cont +1;
        	}else {
        		tab = removeLinha(l, tab);
        		l= l-1;
            	cont = cont +1;
        	}
        }
        System.out.println("[tratarProfSaude] "+cont+" profissionais removidos sem definição sobre profissão de saúde");
        return tab;
    }
    
    
    
    public String[][] tratarAtributoClasse (String[][] tab) {  
    	 
    	tab = addColuna(tab);
    	tab[0][tab[0].length-1] = "classe";  
    	for (int l = 1; l < tab.length; l++) {
    		if ((tab[l][26]).contains("bito")) {
    			tab[l][tab[0].length-1] = "Óbito";
        	}
        	if ((tab[l][26]).contains("Cura")) {
        		tab[l][tab[0].length-1] = "Cura";
        	}
    		
    	 }
    	return tab;
    }
    
    
    	
    public String[][] excluirAtributos(String[][] tab){
    		tab = removeColuna(27, tab);
    		tab = removeColuna(26, tab);
    		tab = removeColuna(25, tab);
    		tab = removeColuna(23, tab);
    		tab = removeColuna(22, tab);
    		tab = removeColuna(21, tab);
    		tab = removeColuna(20, tab);
    		tab = removeColuna(19, tab);
    		tab = removeColuna(18, tab);
    		tab = removeColuna(17, tab);
    		tab = removeColuna(16, tab);
    		tab = removeColuna(15, tab);
    		tab = removeColuna(14, tab);
    		tab = removeColuna(13, tab);
    		tab = removeColuna(11, tab);
    		tab = removeColuna(10, tab);
    		tab = removeColuna(9, tab);
    		tab = removeColuna(8, tab);
    		tab = removeColuna(7, tab);
    		tab = removeColuna(6, tab);
    		tab = removeColuna(5, tab);
    		tab = removeColuna(3, tab);
    		tab = removeColuna(2, tab);
    		tab = removeColuna(1, tab);
    		tab = removeColuna(0, tab);
    		
    		return tab;
    }
    		
}
