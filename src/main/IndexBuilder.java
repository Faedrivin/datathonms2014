package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.utils.LuceneUtils;

import org.apache.lucene.index.IndexWriter;

import company.CompanyLogo;

public class IndexBuilder {

    public static void main(String[] args) {
        IndexWriter idxWriter = null;
        try {
            idxWriter = LuceneUtils.createIndexWriter(Config.FULL_INDEX, true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        File imageDir = new File(Config.IMAGE_BASE_DIR);
        File[] images = imageDir.listFiles();

        int count = 0;
        for (File image : images) {
            try {
                BufferedImage im = ImageIO.read(image);
                if(Config.GRAY_SCALE) {
                    im = convertToGray(im);
                }
                CompanyLogo logo = new CompanyLogo(im);
                String identifier = image.getName().substring(0,
                        image.getName().length() - 4);
                idxWriter.addDocument(logo.createAllFeatures(identifier));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if(++count % 500 == 0) System.out.println("Processed " + count);
        }
        System.out.println("Done.");

        try {
            idxWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static BufferedImage convertToGray(BufferedImage color) {
        BufferedImage gray = new BufferedImage(color.getWidth(), color.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = gray.getGraphics();
        g.drawImage(color, 0, 0, null);
        g.dispose();
        return gray;
    }

}
