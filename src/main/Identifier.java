package main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import company.CompanyLogo;

public class Identifier {

    /**
     * Starts the identifier which lets you choose a file and compares it to the
     * database.
     * 
     * @param args
     */
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        URL url = null;
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                JOptionPane
                        .showMessageDialog(null, "Can't open file!",
                                "Unable to open file.",
                                JOptionPane.ERROR_MESSAGE, null);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Can't open file!",
                    "Unable to open file.", JOptionPane.ERROR_MESSAGE, null);
            return;
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can't open file!",
                    "Unable to open file.", JOptionPane.ERROR_MESSAGE, null);
            e.printStackTrace();
            return;
        }

        CompanyLogo searchLogo = null;
        if (image != null) {
            searchLogo = new CompanyLogo(image);
        } else {
            JOptionPane.showMessageDialog(null, "Can't open file!",
                    "Unable to open file.", JOptionPane.ERROR_MESSAGE, null);
            return;
        }

        try {
            IndexReader idxReader = DirectoryReader.open(FSDirectory
                    .open(new File(Config.USE_INDEX)));
            System.out.println("Searching in " + idxReader.numDocs()
                    + " logos.");
            if (idxReader.numDocs() == 0) {
                JOptionPane.showMessageDialog(null, "No files in database!",
                        "Database empty", JOptionPane.ERROR_MESSAGE, null);
                return;
            }

            ImageSearcher edgeSearcher = ImageSearcherFactory
                    .createEdgeHistogramImageSearcher(Config.MAX_RESULTS);
            ImageSearcher colorSearcher = ImageSearcherFactory
                    .createColorLayoutImageSearcher(Config.MAX_RESULTS);
            ImageSearcher jcdSearcher = ImageSearcherFactory
                    .createJCDImageSearcher(Config.MAX_RESULTS);

            Document doc = searchLogo.createFeatures();

            showSearchImage(image);

            // replace these lines with whatever you are searching for
            ImageSearchHits edgeHits = edgeSearcher.search(doc, idxReader);
            showResults(edgeHits, "Edge features");

            ImageSearchHits colorHits = colorSearcher.search(doc, idxReader);
            showResults(colorHits, "Color features");

            ImageSearchHits jcdHits = jcdSearcher.search(doc, idxReader);
            showResults(jcdHits, "JCD features");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the search image in a separate window.
     * This window closes the application on exit, thus allowing for quickly
     * closing multiple result sets.
     * 
     * @param searchImage The search image.
     */
    public static void showSearchImage(BufferedImage searchImage) {
        JFrame frame = new JFrame("Searching for...");
        JPanel searchImagePanel = new JPanel();
        searchImagePanel.add(new JLabel(new ImageIcon(searchImage
                .getScaledInstance(
                        searchImage.getWidth() > 400
                                && searchImage.getWidth() >= searchImage
                                        .getHeight() ? 400 : -1,
                        searchImage.getHeight() > searchImage.getWidth()
                                && searchImage.getHeight() > 200 ? 200 : -1,
                        Image.SCALE_SMOOTH))));
        frame.add(searchImagePanel);
        frame.pack();
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    /**
     * Presents the results in a simple window.
     * 
     * @param hits The result set to be viewed.
     * @param title The window title.
     */
    public static void showResults(ImageSearchHits hits, String title) {
        JFrame resultFrame = new JFrame(title);
        resultFrame.setLayout(new BoxLayout(resultFrame.getContentPane(),
                BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 4, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(panel);
        resultFrame.add(scrollPane);

        for (int i = 0; i < hits.length(); i++) {
            String id = hits.doc(i)
                    .getField(DocumentBuilder.FIELD_NAME_IDENTIFIER)
                    .stringValue();
            String companyName = "";

            try (BufferedReader br = new BufferedReader(new FileReader(
                    new File(Config.COMPANY_DIR + id + ".company")))) {
                companyName = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            BufferedImage image = null;
            try {
                image = ImageIO.read(new File(Config.IMAGE_BASE_DIR + id
                        + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            JLabel imageLabel = new JLabel(
                    new ImageIcon(
                            image.getScaledInstance(
                                    image.getWidth() > 200
                                            && image.getWidth() >= image
                                                    .getHeight() ? 200 : -1,
                                    image.getHeight() > image.getWidth()
                                            && image.getHeight() > 80 ? 80 : -1,
                                    Image.SCALE_SMOOTH)));
            JLabel textLabel = new JLabel(
                    "<html><body><p style=\"width:160px;max-width:160px;min-height:80px\">"
                            + companyName + "<br />Score: " + hits.score(i)
                            + "</p></body></html>");
            panel.add(imageLabel);
            panel.add(textLabel);
        }

        Dimension dim = new Dimension(1200, 800);
        resultFrame.setPreferredSize(dim);
        resultFrame.setSize(dim);
        resultFrame.setMinimumSize(dim);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setVisible(true);
    }
}
