package com.nea.backend.controller;

import com.nea.backend.exception.ApiError;
import com.nea.backend.model.Dump;
import com.nea.backend.model.DumpToFiles;
import com.nea.backend.model.Picture;
import com.nea.backend.model.User;
import com.nea.backend.repository.DumpRepository;
import com.nea.backend.repository.DumpToFileRepository;
import com.nea.backend.repository.FileRepository;
import com.nea.backend.response.SuccessImageUploadResponse;
import com.nea.backend.security.CurrentUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
@CrossOrigin
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

    @PostMapping
    public SuccessImageUploadResponse createDump(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("comment") String comment,
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude
    ) {
        User user = currentUser.getUser();

        if (!root.toFile().exists()) {
            root.toFile().mkdir();
        }

        List<Integer> uploadImagesIds = Arrays.stream(files).map(i -> {
            try {
                try {
                    Files.copy(i.getInputStream(), this.root.resolve(i.getOriginalFilename()));
                } catch (FileAlreadyExistsException ex) {
                    return fileRepository.findByContent(i.getOriginalFilename())
                            .orElse(new Picture(
                                    i.getName(),
                                    i.getOriginalFilename(),
                                    i.getContentType()
                            ));
                }
                return new Picture(
                        i.getName(),
                        i.getOriginalFilename(),
                        i.getContentType()
                );
            } catch (IOException e) {
                log.error("Ошибка {}", e);
                throw new ApiError.FileUploadException();
            }
        }).map(picture -> {
            Picture savedPicture = fileRepository.save(picture);
            fileRepository.flush();
            return savedPicture.getId();
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
