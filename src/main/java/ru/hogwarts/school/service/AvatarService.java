package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository) {

        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public Collection<Avatar> getAllAvatars(int page, int size) {

        logger.info("Was invoked method for get all avatars");

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    public void uploadAvatar(Long studentId, @NotNull MultipartFile avatarFile) throws IOException {

        logger.info("Was invoked method for upload avatar");

        Student student = studentRepository.getById(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        student.setAvatar(avatar);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generatePreview(filePath));
        avatarRepository.save(avatar);
        studentRepository.save(student);
    }

    private byte @NotNull [] generatePreview(@NotNull Path filePath) throws IOException {

        logger.info("Was invoked method for generate preview");

        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D g = preview.createGraphics();
            g.drawImage(image, 0, 0, 100, height, null);
            g.dispose();
            ImageIO.write(preview, getExtensions(filePath.getFileName().toString()), bos);
            return bos.toByteArray();
        }
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for find avatar by student id");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private @NotNull String getExtensions(@NotNull String fileName) {
        logger.info("Was invoked method for get extension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
