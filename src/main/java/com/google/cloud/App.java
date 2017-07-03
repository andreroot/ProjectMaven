package com.google.cloud;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.Page;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.cloud.vision.v1.Word;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

/**
* Hello world!
*
*/

public class App{
	
	
	public static void detectDocumentText(String filePath, PrintStream out) throws IOException {
		  List<AnnotateImageRequest> requests = new ArrayList<>();

		  ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		  Image img = Image.newBuilder().setContent(imgBytes).build();
		  Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
		  AnnotateImageRequest request =
		      AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		  requests.add(request);

		  BatchAnnotateImagesResponse response =
		      ImageAnnotatorClient.create().batchAnnotateImages(requests);
		  List<AnnotateImageResponse> responses = response.getResponsesList();

		  for (AnnotateImageResponse res : responses) {
		    if (res.hasError()) {
		    	out.printf("Error: %s\n", res.getError().getMessage());
		      return;
		    }

		    // For full list of available annotations, see http://g.co/cloud/vision/docs
		    TextAnnotation annotation = res.getFullTextAnnotation();
		    for (Page page: annotation.getPagesList()) {
		      String pageText = "";
		      for (Block block : page.getBlocksList()) {
		        String blockText = "";
		        for (Paragraph para : block.getParagraphsList()) {
		          String paraText = "";
		          for (Word word: para.getWordsList()) {
		            String wordText = "";
		            for (Symbol symbol: word.getSymbolsList()) {
		              wordText = wordText + symbol.getText();
		            }
		            paraText = paraText + wordText;
		          }
		          // Output Example using Paragraph:
		          System.out.println("Paragraph: \n" + paraText);
		          System.out.println("Bounds: \n" + para.getBoundingBox() + "\n");
		          blockText = blockText + paraText;
		        }
		        pageText = pageText + blockText;
		      }
		    }
		    System.out.println(annotation.getText());
		  }
		}
	
	public static void detectText(String filePath, PrintStream out) throws IOException {
		  List<AnnotateImageRequest> requests = new ArrayList<AnnotateImageRequest>();

		  ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		  Image img = Image.newBuilder().setContent(imgBytes).build();
		  Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
		  AnnotateImageRequest request =
		      AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		  requests.add(request);

		  BatchAnnotateImagesResponse response =
		      ImageAnnotatorClient.create().batchAnnotateImages(requests);
		  
		  List<AnnotateImageResponse> responses = response.getResponsesList();
		  
		  for (AnnotateImageResponse res : responses) {
		    if (res.hasError()) {
		    	System.out.printf("Error: %s\n", res.getError().getMessage());
		      return;
		    }

		    // For full list of available annotations, see http://g.co/cloud/vision/docs
		    for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
		    	System.out.printf("Text: %s\n", annotation.getDescription());
		    	System.out.printf("Position : %s\n", annotation.getBoundingPoly());
		    }
		  }
		}
	
	public void testeGoogle() throws IOException {
		
  	String filePath = "/home/andre/Documentos/cloud/teste/IMG_20170703_110154589.jpg";
  	PrintStream out = null;
      System.out.println( "Hello World!" );        
		App.detectDocumentText(filePath, out);
	}
	
	
  public static void main( String[] args ) throws IOException 
  {

      System.out.println( "Hello World!" );
      App teste = new App();
      teste.testeGoogle();

      
  }
}
