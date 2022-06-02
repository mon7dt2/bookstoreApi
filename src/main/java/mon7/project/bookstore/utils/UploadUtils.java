package mon7.project.bookstore.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageException;
import com.google.firebase.cloud.StorageClient;

public class UploadUtils {
    public static String uploadFile(String dir, String fileName,
                                    byte[] data,
                                    String contentType) throws StorageException {
        Blob avatarFile = StorageClient.getInstance()
                .bucket()
                .create(dir+"/"+fileName, data, contentType);
        return getDownloadUrl(avatarFile.getBucket(), avatarFile.getName());
    }

    public static String getDownloadUrl(String bucketUrl, String fileName) {
        return "https://storage.googleapis.com/" + bucketUrl + "/" + fileName;
    }
}
