package com.sem.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final String defaultFolder;
    private final String defaultImage;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret,
            @Value("${cloudinary.default-folder}") String defaultFolder,
            @Value("${cloudinary.default-image}") String defaultImage
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
        this.defaultFolder = defaultFolder;
        this.defaultImage = defaultImage;
    }

    public String uploadAvatar(MultipartFile file, UUID userId) throws IOException {
        if (file == null || file.isEmpty()) {
            return generateDefaultAvatarUrl();
        }

        Map<String, String> options = ObjectUtils.asMap(
                "folder", defaultFolder,
                "public_id", "avatar_" + userId,
                "overwrite", true,
                "resource_type", "image",
                "transformation", "c_fill,g_face,h_200,w_200"
        );

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return (String) uploadResult.get("secure_url");
    }

    public void deleteAvatar(UUID userId) throws IOException {
        String publicId = defaultFolder + "/avatar_" + userId;
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public String generateDefaultAvatarUrl() {
         try {
             return cloudinary.url()
                 .publicId(defaultFolder + "/" + defaultImage)
                 .generate();
         } catch (Exception e) {
             return fallbackUrl();
         }
    }

    private String fallbackUrl() {
        return "https://ui-avatars.com/api/?name=Back&Ground=random ";
    }
}
