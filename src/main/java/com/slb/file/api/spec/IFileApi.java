package com.slb.file.api.spec;

import com.slb.file.api.model.FileDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"/file/v1/"})
@Api(value = "Gestion Sys. Fichiers")

public interface IFileApi {


    @ApiOperation(value = "Afficher contenu de dossier")
    @GetMapping(path = {"/display-all-files/"})
    List<FileDto> displayFolderContent() throws Exception;

    @ApiOperation(value = "Chercher un fichier")
    @GetMapping(path = {"/search/{nom}/{extension}"})
    List<FileDto> search(@PathVariable(value = "nom" )  String nom,
                         @PathVariable(value = "extension" ) String extension ) throws Exception;


    @ApiOperation(value = "Charger un fichier")
    @RequestMapping
            (
            path = "/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
            )
    public ResponseEntity<String> upload(@RequestPart("file") MultipartFile file, @RequestParam("someId") Long someId ) ;


    @ApiOperation(value = "Télécharger")
    @RequestMapping(path = "/download/{fullname}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@RequestParam("fullname") String fullname ) throws IOException;

    @ApiOperation(value = "Archiver")
    @RequestMapping(path = "/archive/{fullname}", method = RequestMethod.POST)
    public ResponseEntity<Resource> archive(@RequestParam("fullname") String fullname ) throws IOException;

    @ApiOperation(value = "Envoyer par mail")
    @RequestMapping(path = "/mail/{fullname}", method = RequestMethod.POST)
    public ResponseEntity<Resource> sendMail(@RequestParam("fullname") String fullname ) throws IOException;


    @ApiOperation(value = "Envoyer par Sftp")
    @RequestMapping(path = "/sftp/{fullname}", method = RequestMethod.POST)
    public ResponseEntity<Resource> sendSftp(@RequestParam("fullname") String fullname ) throws IOException;

}