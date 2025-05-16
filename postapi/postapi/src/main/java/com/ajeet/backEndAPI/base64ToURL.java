package com.ajeet.backEndAPI;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class base64ToURL {
	

	
	

		public static void main(String[] args) {
			 String base64Data = "UEsDBBQAAAAIAD"  ;   
			String fileName = "dwsample1-zip.zip"; // Replace with the desired file name and extension

			        // Decode Base64 data
			        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

			        try {
			            // Define the path to save the file
			            Path filePath = Paths.get(fileName);

			            // Write the decoded bytes to a file
			            Files.write(filePath, decodedBytes);

			            // Get the downloadable URL of the saved file
			            String downloadableURL = filePath.toUri().toURL().toString();

			            System.out.println("File saved: " + filePath.toAbsolutePath());
			            System.out.println("Downloadable URL: " + downloadableURL);
			        } catch (IOException e) {
			            System.err.println("Error saving the file: " + e.getMessage());
			        }
		}    
}


