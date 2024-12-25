package microservice.chat_service.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import microservice.chat_service.dto.CloudinaryResponse;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Transactional
    public CloudinaryResponse uploadFile(final MultipartFile file, final String fileName) {
        try {
            final Map result = cloudinary.uploader()
                    .upload(file.getBytes(), Map.of("public_id", fileName));
            final String url = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url)
                    .build();

        } catch (final Exception e) {
            throw new AppException(ErrorCode.UPLOAD_IMAGE_FAIL);
        }
    }

    public void delete(String publicId){
        try {
            cloudinary.uploader().destroy(publicId,Map.of());
        } catch (IOException e) {
            throw new AppException(ErrorCode.DELETE_IMAGE_FAIL);
        }
    }
}