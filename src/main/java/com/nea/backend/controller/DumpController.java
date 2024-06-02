package com.nea.backend.controller;

import com.nea.backend.exception.ApiError;
import com.nea.backend.model.Dump;
import com.nea.backend.model.DumpToFiles;
import com.nea.backend.model.File;
import com.nea.backend.model.User;
import com.nea.backend.repository.DumpRepository;
import com.nea.backend.repository.DumpToFileRepository;
import com.nea.backend.repository.FileRepository;
import com.nea.backend.response.SuccessImageUploadResponse;
import com.nea.backend.security.CurrentUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dump")
@Tag(name = "Свалки")
@Transactional
@RequiredArgsConstructor
public class DumpController {
    private final FileRepository fileRepository;
    private final DumpRepository dumpRepository;
    private final DumpToFileRepository dumpToFileRepository;
    private final CurrentUser currentUser;

    private final Path root = Paths.get("uploads");

    @GetMapping
    public Page<Dump> getAll(Pageable pageable) {
        return dumpRepository.findAll(pageable);
    }

    @GetMapping("{id}")
    public Dump getAll(@PathVariable("id") Integer id) {
        return dumpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Нет такой сущности"));
    }

    @PostMapping
    public SuccessImageUploadResponse uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("comment") String comment,
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude
    ) {
        User user = currentUser.getUser();

        List<Integer> uploadImagesIds = Arrays.stream(files).map(i -> {
            try {
                try {
                    Files.copy(i.getInputStream(), this.root.resolve(i.getOriginalFilename()));
                } catch (FileAlreadyExistsException ex) {}
                return new File(
                        i.getName(),
                        i.getOriginalFilename(),
                        i.getContentType()
                );
            } catch (IOException e) {
                throw new ApiError.FileUploadException();
            }
        }).map(file -> {
            File savedFile = fileRepository.save(file);
            fileRepository.flush();
            return savedFile.getId();
        }).toList();

        Dump dump = new Dump(
                comment,
                user,
                Double.valueOf(longitude),
                Double.valueOf(latitude)
        );
        Dump newDump = dumpRepository.save(dump);
        dumpRepository.flush();
        Integer newDumpId = newDump.getId();

        List<DumpToFiles> list = uploadImagesIds.stream()
                .map(i -> new DumpToFiles(newDumpId, i))
                .toList();
        dumpToFileRepository.saveAll(list);

        return new SuccessImageUploadResponse(
                uploadImagesIds
        );
    }
}
