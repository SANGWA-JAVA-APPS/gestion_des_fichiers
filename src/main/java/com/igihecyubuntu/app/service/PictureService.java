package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.entity.Picture;
import com.igihecyubuntu.app.dto.projection.PictureProjection;
import com.igihecyubuntu.app.repository.PictureRepository;
import com.igihecyubuntu.app.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PictureService {

    private final PictureRepository pictureRepository;

    public List<PictureProjection> getAllPictures() {
        return pictureRepository.findAllProjectedBy();
    }

    public Optional<PictureProjection> getPictureById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid picture ID");
        }
        return pictureRepository.findProjectedById(id);
    }

    public List<PictureProjection> getPicturesByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new BadRequestException("Picture type is required");
        }
        return pictureRepository.findByTypeOrderByDateTimeDesc(type);
    }

    public List<PictureProjection> getPicturesByUser(Long doneBy) {
        if (doneBy == null || doneBy <= 0) {
            throw new BadRequestException("Invalid user ID");
        }
        return pictureRepository.findByDoneByOrderByDateTimeDesc(doneBy);
    }

    public Picture createPicture(Picture picture) {
        validatePicture(picture);
        picture.setDateTime(LocalDateTime.now());
        return pictureRepository.save(picture);
    }

    public Picture updatePicture(Long id, Picture picture) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid picture ID");
        }
        
        Picture existingPicture = pictureRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Picture not found"));
        
        validatePicture(picture);
        
        existingPicture.setType(picture.getType());
        existingPicture.setPath(picture.getPath());
        existingPicture.setAltText(picture.getAltText());
        existingPicture.setDoneBy(picture.getDoneBy());
        
        return pictureRepository.save(existingPicture);
    }

    public void deletePicture(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid picture ID");
        }
        
        if (!pictureRepository.existsById(id)) {
            throw new BadRequestException("Picture not found");
        }
        
        pictureRepository.deleteById(id);
    }

    // Dashboard methods
    public List<Object[]> getPictureCountsByType() {
        return pictureRepository.findPictureCountsByType();
    }

    public List<Object[]> getPictureUploadsByDate() {
        return pictureRepository.findPictureUploadsByDate();
    }

    private void validatePicture(Picture picture) {
        if (picture == null) {
            throw new BadRequestException("Picture data is required");
        }
        if (picture.getType() == null || picture.getType().trim().isEmpty()) {
            throw new BadRequestException("Picture type is required");
        }
        if (picture.getPath() == null || picture.getPath().trim().isEmpty()) {
            throw new BadRequestException("Picture path is required");
        }
        if (picture.getDoneBy() == null || picture.getDoneBy() <= 0) {
            throw new BadRequestException("Valid user ID is required");
        }
    }
}