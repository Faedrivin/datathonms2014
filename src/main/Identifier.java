package main;

import java.awt.BorderLayout;
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
    private static ImageSearchHits hits = null;

    /**
     * Starts the identifier which lets you choose a file and compares it to the database.
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
                    .open(new File(Config.IMAGE_INDEX)));
            System.out.println("Searching in " + idxReader.numDocs()
                    + " logos.");
            if (idxReader.numDocs() == 0) {
                JOptionPane.showMessageDialog(null, "No files in database!",
                        "Database empty", JOptionPane.ERROR_MESSAGE, null);
                return;
            }
            @SuppressWarnings("deprecation")
            ImageSearcher searcher = ImageSearcherFactory
                    .createSimpleSearcher(Config.MAX_RESULTS);
            Document doc = searchLogo.createFeatures();

            JFrame waitFrame = new JFrame("Please wait...");
            waitFrame.add(new JLabel("Please wait, searching..."));
            waitFrame.setPreferredSize(new Dimension(250, 50));
            waitFrame.setSize(new Dimension(250, 50));
            waitFrame.setMinimumSize(new Dimension(250, 50));
            waitFrame.setLocationRelativeTo(null);
            waitFrame.setVisible(true);

            hits = searcher.search(doc, idxReader);

            waitFrame.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showResults(image);

    }

    /**
     * Once the results are found, a result window with results appears.
     * @param searchImage The image which was searched for.
     */
    public static void showResults(BufferedImage searchImage) {
        JFrame resultFrame = new JFrame("Company Identifier");
        resultFrame.setLayout(new BoxLayout(resultFrame.getContentPane(),
                BoxLayout.Y_AXIS));

        JPanel searchImagePanel = new JPanel();
        searchImagePanel.add(new JLabel(new ImageIcon(searchImage
                .getScaledInstance(searchImage.getWidth() > 400 ? 400 : -1, -1,
                        Image.SCALE_SMOOTH))));
        resultFrame.add(searchImagePanel);

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
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setVisible(true);
    }

    public static void showImage(String name) {
        BufferedImage logo = null;
        try {
            logo = ImageIO
                    .read(new File(Config.IMAGE_BASE_DIR + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel lblimage = new JLabel(new ImageIcon(logo));

        mainPanel.add(lblimage);
        frame.add(mainPanel);

        frame.setSize(logo.getWidth(), logo.getHeight() + 50);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
