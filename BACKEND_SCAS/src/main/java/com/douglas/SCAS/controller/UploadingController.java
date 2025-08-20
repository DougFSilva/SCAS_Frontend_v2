package com.douglas.SCAS.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/upload")
public class UploadingController {

	@Value("${uploadPhotoLocation}")
	private String uploadPhotoLocation;

//	@PreAuthorize("hasRole('ADMIN')")
//	@PostMapping(value = "/{folder}/{name}")
//	public ResponseEntity<Void> uploadFile(@RequestParam("image") MultipartFile[] uploadingFiles,
//			@PathVariable String folder, @PathVariable String name) throws IllegalStateException, IOException {
//		InputStream inputStream = uploadingFiles[0].getInputStream();
//		Files.copy(inputStream, Paths.get(this.uploadPhotoLocation + "/" + folder + "/" + name),
//				StandardCopyOption.REPLACE_EXISTING);
//		return ResponseEntity.ok().build();
//
//	}

//	@PreAuthorize("hasRole('ADMIN')")
//	@PostMapping(value = "/{folder}/{name}")
//	public ResponseEntity<Void> uploadFile(@RequestParam("image") MultipartFile[] uploadingFiles,
//			@PathVariable String folder, @PathVariable String name) throws IOException {
//		Path directoryPath = Paths.get(this.uploadPhotoLocation, folder);
//
//// Garante que a pasta existe
//		if (!Files.exists(directoryPath)) {
//			Files.createDirectories(directoryPath);
//		}
//
//// Obtém o InputStream e copia o arquivo
//		InputStream inputStream = uploadingFiles[0].getInputStream();
//		Files.copy(inputStream, directoryPath.resolve(name), StandardCopyOption.REPLACE_EXISTING);
//
//		return ResponseEntity.ok().build();
//	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/{folder}/{name}")
	public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile[] uploadingFiles,
	        @PathVariable String folder, @PathVariable String name) {
	    try {
	        Path directoryPath = Paths.get(this.uploadPhotoLocation, folder);

	        // Garante que a pasta existe
	        if (!Files.exists(directoryPath)) {
	            Files.createDirectories(directoryPath);
	        }

	        // Obtém o InputStream e copia o arquivo
	        InputStream inputStream = uploadingFiles[0].getInputStream();
	        Files.copy(inputStream, directoryPath.resolve(name), StandardCopyOption.REPLACE_EXISTING);

	        return ResponseEntity.ok("Arquivo enviado com sucesso.");
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Erro ao salvar o arquivo: " + e.getMessage());
	    }
	}
}
