package it.ddbdev.ticketsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

@Service
public class FileService {

    public boolean checkSize(MultipartFile file, long size){
        return !file.isEmpty() && file.getSize() <= size;
    }

    public BufferedImage fromMultipartFileToBufferedImage(MultipartFile file){
        BufferedImage bufferedImage;
        try{
            bufferedImage = ImageIO.read(file.getInputStream());
            return bufferedImage;
        }catch (IOException e) {
            return null;
        }
    }

    public boolean checkWeight(BufferedImage bufferedImage, int width, int height ){

        if (bufferedImage == null)
            return false;

        return bufferedImage.getWidth() <= width && bufferedImage.getHeight() <= height;
    }

    public boolean checkExtension(MultipartFile file, String[] extensions) {
        ImageInputStream img = null;
        try {
            img = ImageIO.createImageInputStream(file.getInputStream());
        } catch (IOException e) {
            return false;
        }

        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(img);

        while (imageReaders.hasNext()) {
            ImageReader reader = (ImageReader) imageReaders.next();
            try {
                for (String extension : extensions) {
                    if (reader.getFormatName().equalsIgnoreCase(extension)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
}
