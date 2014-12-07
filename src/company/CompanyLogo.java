package company;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.impl.SurfDocumentBuilder;

import org.apache.lucene.document.Document;

public class CompanyLogo {
    private final BufferedImage logo;
    private Company company;
    private Document siftFeatures = null;

    /**
     * Creates a logo with an assignment to a company.
     * This method is used by the Crawler so that the logo can be
     * processed with the company's name and its unique number as
     * an identifier. This makes lookups in the database possible.
     * 
     * @param logo the image file representing the company's logo.
     * @param parent the company belonging to the logo.
     */
    public CompanyLogo(BufferedImage logo, Company parent) {
        this.logo = logo;
        this.company = parent;
    }

    /**
     * This constructor creates the logo without a company attachment.
     * It is used for the Identifier. It is not possible to store
     * images created by this constructor into the database.
     * 
     * @param logo the logo to later search for.
     */
    public CompanyLogo(BufferedImage logo) {
        this.logo = logo;
    }

    /**
     * A debug method to show a JFrame containing this logo.
     */
    public void show() {
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel lblimage = new JLabel(new ImageIcon(logo));

        mainPanel.add(lblimage);
        frame.add(mainPanel);

        frame.setSize(logo.getWidth(), logo.getHeight() + 50);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Returns the feature list containing SurfFeatures.
     * 
     * @param identifier The identifier to be used in the database. This should
     *            be the company ID, as it helps the lookup.
     * @return the SurfFeatures
     */
    public Document createFeatures(String identifier) {
        ChainedDocumentBuilder documentBuilder = new ChainedDocumentBuilder();
        documentBuilder.addBuilder(new SurfDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory
                .getEdgeHistogramBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory
                .getJCDDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory
                .getColorLayoutBuilder());
        try {
            siftFeatures = documentBuilder.createDocument(logo, identifier);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return siftFeatures;
    }
    
    /**
     * Returns the feature list with all available features.
     * 
     * @param identifier The identifier to be used in the database.
     * 
     * @return the SurfFeatures
     */
    public Document createAllFeatures(String identifier) {
        ChainedDocumentBuilder documentBuilder = new ChainedDocumentBuilder();
        documentBuilder.addBuilder(DocumentBuilderFactory.getAutoColorCorrelogramDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getCEDDDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getEdgeHistogramBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getColorHistogramDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getColorLayoutBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getFCTHDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getGaborDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getHashingCEDDDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getJCDDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getJointHistogramDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getJpegCoefficientHistogramDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getLuminanceLayoutDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getOpponentHistogramDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getPHOGDocumentBuilder());
        documentBuilder.addBuilder(DocumentBuilderFactory.getTamuraDocumentBuilder());
        
        try {
            siftFeatures = documentBuilder.createDocument(logo, identifier);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return siftFeatures;
    }

    /**
     * This method creates features without any specific identifier and sets the
     * identifier to a default of <code>"SearchImage"</code>. This is used to
     * calculate the features of the search images.
     * 
     * @return the SurfFeatures
     */
    public Document createFeatures() {
        return createFeatures("SearchImage");

    }

    /**
     * This method creates features with any specific identifier. This is used
     * to calculate the surf features of each image in the database.
     * 
     * @param id the id to be used.
     * @return the SurfFeatures
     */
    public Document createFeatures(int id) {
        return createFeatures(company.getId() + "_" + id);
    }

    /**
     * Writes the image to the file system for future lookups.
     * The file name is set by the company id and the provided id.
     * 
     * The files are stored as <code>*.png</code> files.
     * 
     * @param imageBaseDir the image database directory
     * @param id the id of the image
     */
    public void store(String imageBaseDir, int id) {
        if (company == null) {
            System.err.println("Can't store image created without a company!");
            return;
        }
        try {
            ImageIO.write(logo, "png", new File(imageBaseDir + company.getId()
                    + "_" + id + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
