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
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;


/**
 * Hello world!
 *
 */
public class App 
{

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
		  //System.out.println(response.toByteString());
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
		
    	String filePath = "/home/andre/Downloads/Melhor.jpg";
    	PrintStream out = null;
        System.out.println( "Hello World!" );        
		App.detectText(filePath, out);
	}
	
	
    public static void main( String[] args ) throws IOException 
    {

        System.out.println( "Hello World!" );
        App teste = new App();
        teste.testeGoogle();

        
    }
}
