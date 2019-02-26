package technology.tabula.UseCases;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Rectangle;
import technology.tabula.Table;
import technology.tabula.UtilsForTesting;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;
import technology.tabula.writers.CSVWriter;
import technology.tabula.writers.JSONWriter;
import technology.tabula.writers.TSVWriter;

public class UseCase1 {

	//static DocumentBuilder builder;
	String output_csv_file="output_table_csv.csv";
	String output_tsv_file="output_table_tsv.csv";
	static String output_json_file="output_table_json.csv";
	static String pdfPath="/Users/asmaadala/git/tabula-java/src/main/resources/technology/tabula/UseCases/T100_Report.pdf";
		    
	// extract tables with NurminenDetectionAlgorithm() and return Map<page-number, tables-in-that-page)
	public static Map<Integer, List<Rectangle>> DetectTables(File pdf) throws Exception {
		        

				// tabula extractors
				PDDocument pdfDocument = PDDocument.load(pdf);
		        ObjectExtractor extractor = new ObjectExtractor(pdfDocument);
		        
		        // now find tables detected by tabula-java
		        Map<Integer, List<Rectangle>> detectedTables = new HashMap<>();

		        // the algorithm we're going to use
		        NurminenDetectionAlgorithm detectionAlgorithm = new NurminenDetectionAlgorithm();
		        

		        PageIterator pages = extractor.extract();
		        while (pages.hasNext()) {
		            Page page = pages.next();
		         
		            List<Rectangle> tablesOnPage = detectionAlgorithm.detect(page);
		            if (tablesOnPage.size() > 0) {
		                detectedTables.put(new Integer(page.getPageNumber()), tablesOnPage);
		            	
		            }
		        }
		        return detectedTables;
			}
	
		        
	 private static void printTables(Map<Integer, List<Rectangle>> tables) {
		            for (Integer page : tables.keySet()) {
		                System.out.println("Page " + page.toString());
		                for (Rectangle table : tables.get(page)) {
		                    System.out.println(table);
		                }
		            }
		       }
	 private static List<Table> getTables(Map<Integer, List<Rectangle>> tables, String pdfPath) throws IOException {
		 
		 List<Table> tableslist=new ArrayList<Table>();
		 
		  for (int page_number:tables.keySet() ) {
			  Page page = UtilsForTesting.getPage(pdfPath,page_number );
			  BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
			  Table table = bea.extract(page).get(page_number);
			  tableslist.add(table);
			  
		  }
             return tableslist;
         }
    
		        
		        
		        //extract a table from a pdf page with BasicExtractionAlgorithm()
	 private Table getTable(Page page) throws IOException {
		            //Page page = UtilsForTesting.getAreaFromFirstPage("src/test/resources/technology/tabula/argentina_diputados_voting_record.pdf", 269.875f, 12.75f, 790.5f, 561f);
		            BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
		            Table table = bea.extract(page).get(0);
		            return table;
		        }
		        //extract all tables from pdf page with SpreadsheetExtractionAlgorithm()
     private static List<Table> getTables(Page page) throws IOException {

		            //Page page = UtilsForTesting.getPage("src/test/resources/technology/tabula/twotables.pdf", 1);
		            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
		            
		            return sea.extract(page);
		        }

		        
	 public void CSVWriter(Table table) throws IOException {
		        	//String expectedCsv = UtilsForTesting.loadCsv("src/test/resources/technology/tabula/csv/argentina_diputados_voting_record.csv");
		            //Table table = this.getTable();
		        	//StringBuilder sb = new StringBuilder();
		        	File file = new File(output_csv_file);
		        	
		        	BufferedWriter writer = new BufferedWriter(new FileWriter(file)); 
		        	    //writer.write(sb.toString());
		        	//}
		        	(new CSVWriter()).write(writer, table);
		            //(new CSVWriter()).write(sb, table);
		            //String s = sb.toString();
		            //String[] lines = s.split("\\r?\\n");
		            //assertEquals(lines[0], EXPECTED_CSV_WRITER_OUTPUT);
		            //assertEquals(expectedCsv, s);
		        }

	public void TSVWriter(Table table ) throws IOException {
		            
		            StringBuilder sb = new StringBuilder();
		            (new TSVWriter()).write(sb, table);
		            //String s = sb.toString();
		            File file = new File(output_tsv_file);
		        	
		        	try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		        	    writer.write(sb.toString());
		        	}
		            
		            //System.out.println(s);
		            //String[] lines = s.split("\\r?\\n");
		            //assertEquals(lines[0], EXPECTED_CSV_WRITER_OUTPUT);
		        }  
		        
    public static void JSONWrite(Table table) throws IOException {
		            //String expectedJson = UtilsForTesting.loadJson("src/test/resources/technology/tabula/json/argentina_diputados_voting_record.json");
		            //Table table = this.getTable();
		            StringBuilder sb = new StringBuilder();
		            File file = new File(output_json_file);
		        	
		        	try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		        	    writer.write(sb.toString());
		        	}
		            
		            (new JSONWriter()).write(sb, table);
		            //String s = sb.toString();
		            //assertEquals(expectedJson, s);
		        }

		        
		        
		        
		        
		
			
     public static void main(String[] args) throws Exception {
			
			
				File pdffile= new File(pdfPath);
				/*
				try {
					printTables(DetectTables(pdffile));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				*/
				//Map<Integer, List<Rectangle>> tablesmap = DetectTables(pdffile);
		        
				//long i = 0;
				
				
				List<Table> listtables = getTables(DetectTables(pdffile), pdfPath);
				
				
				for (Table t: listtables)
				{
					JSONWrite(t);
					
				}
					
					

		}
     
     

	}
     



