package com.slb.file.api.ressource;


import com.slb.file.api.config.ApplicationProperties;
import com.slb.file.api.mail.EmailingService;
import com.slb.file.api.model.FileDto;
import com.slb.file.api.spec.IFileApi;
import com.slb.file.api.utils.FUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.constraints.Email;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@CrossOrigin(origins = "http://localhost:4010")
public class FileApiController implements IFileApi {

    private final Logger logger = LoggerFactory.getLogger(FileApiController.class);


    @Autowired
    ApplicationProperties applicationProperties ;

    @Autowired
    EmailingService emailingService;

    @Override
    public List<FileDto> displayFolderContent() throws Exception {
        return  Files.list(Paths.get(applicationProperties.getWork()))
                     .map(Path::toFile)
                     .map(file -> FUtils.toDto(file))
                     .collect(Collectors.toList());
    }

    @Override
    public List<FileDto> search(String nom, String extension) throws Exception {
        Predicate<? super FileDto> predicate = file -> {
            if (!StringUtils.isBlank(nom) && !StringUtils.isBlank(extension))
                return file.getFileName().contains(nom) && file.getExtension().equals(extension);
            if (StringUtils.isBlank(nom) && !StringUtils.isBlank(extension)) {
                return file.getExtension().equals(extension);
            }
            if (!StringUtils.isBlank(nom) && StringUtils.isBlank(extension)) {
                return file.getFileName().contains(nom);
            }
            if (StringUtils.isBlank(nom) && StringUtils.isBlank(extension)) {
                return true;
            }
            return true;
        };
        return Files.list(Paths.get(applicationProperties.getWork()))
                .map(Path::toFile)
                .map(file -> FUtils.toDto(file))
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<String> upload(MultipartFile file, Long someId) {
        logger.debug("Single file upload!");
        if (file.isEmpty()) {
            return new ResponseEntity("You must select a file!", HttpStatus.OK);
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(applicationProperties.getWork()
                                    + "/UPLOADED" + RandomStringUtils.randomNumeric(4)
                                    + "_" + file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            logger.debug("Fails to UpLoad");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.debug("Successfully uploaded ");
        return new ResponseEntity("Successfully uploaded - " + file.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> download(String fullname) throws IOException {
        logger.debug("Start Dowload");
        HttpHeaders headers = new HttpHeaders();
        File file = null;
        ByteArrayResource resource = null;
        try {
            file = new File(applicationProperties.getWork()  + "/"+ fullname);
            if (file.exists()) {

                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                headers.add("Content-Disposition", "attachment; filename=\"" + fullname + "\"");

                Path path = Paths.get(file.getAbsolutePath());
                resource = new ByteArrayResource(Files.readAllBytes(path));

            }else{
                logger.debug("Fails to Download");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.debug("Fails to Download");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.debug("Successfully dowloaded");
        return ResponseEntity.ok().headers(headers).contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
    }

    @Override
    public ResponseEntity<Resource> archive(String fullname) throws IOException {
        logger.debug("Start Archiving");
        File file = null;
        ByteArrayResource resource = null;
        try {
            file = new File(applicationProperties.getWork() + "/" + fullname);
            if (file.exists()) {
                File targetFile = new File(applicationProperties.getArchive() + "/" + fullname);
                com.google.common.io.Files.move(file, targetFile);
                int BUFFER = 2048;
                byte buffer[] = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(targetFile  + ".zip");
                ZipOutputStream zos = new ZipOutputStream(fos);
                ZipEntry ze = new ZipEntry(fullname);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(targetFile);
                int lec;
                while ((lec = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, lec);
                }
                in.close();
                zos.closeEntry();
                zos.close();
                targetFile.delete();
            } else {
                logger.debug("Fails to Archive");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException ex) {
            logger.debug("Fails to Archive");
            ex.printStackTrace();
        }
        logger.debug("Successfully archived");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> sendMail(MultipartFile file, @Email String email) {
        if (file != null && email != null) {
            if (file.isEmpty()) {
                return new ResponseEntity("You must select a file!", HttpStatus.OK);
            }
            try {
                byte[] bytes = file.getBytes();
                String newpath = applicationProperties.getWork()
                        + "/UPLOADED" + RandomStringUtils.randomNumeric(4)
                        + "_" + file.getOriginalFilename();
                Path path = Paths.get(newpath);
                Files.write(path, bytes);


                File attachement = new File(newpath);
                emailingService.sendEmailWithAttachement(email, attachement, "[Your file]", file.getOriginalFilename() );
                return new ResponseEntity("File Send : " + file.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

            } catch (IOException | MessagingException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Resource> sendSftp(String fullname) throws IOException {
        return null;
    }
}
