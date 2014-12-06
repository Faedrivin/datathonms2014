package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.utils.LuceneUtils;

import org.apache.lucene.index.IndexWriter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import company.Company;
import company.CompanyLogo;

public class Crawler {

    public static IndexWriter idxWriter = null;

    /**
     * Initializes the wiki crawling.
     * In case <code>Config.LOAD_ALL == false<code> a parameter can be supplied.
     * 
     * @param args Optional: a list of strings representating wikipedia lists to
     *            be parsed.
     */
    public static void main(String[] args) {
        List<String> addresses = new ArrayList<>();
        if (args.length > 0) {
            for (String arg : args)
                addresses.add(Config.BASE_PATH + arg.replace(" ", "_"));
        } else {
            if (Config.LOAD_ALL) {
                for (String address : Config.COMPANY_LISTS) {
                    addresses.add(Config.BASE_PATH + address.replace(" ", "_"));
                }
            } else {
                addresses.add(Config.BASE_PATH
                        + "List_of_companies_of_the_United_States");
            }
        }

        try {
            idxWriter = LuceneUtils.createIndexWriter(Config.IMAGE_INDEX,
                    Config.RENEW_INDEX);
        } catch (IOException e) {
            log(e);
            return;
        }
        addresses.forEach(Crawler::crawlList);
        try {
            idxWriter.close();
        } catch (IOException e) {
            log(e);
        }
    }

    /**
     * Crawls each element of the list given behind crawling.
     * 
     * @param address The web address to call by jsoup.
     */
    public static void crawlList(String address) {
        status("Crawling: " + address);
        Connection connection = null;
        try {
            connection = Jsoup.connect(address);
        } catch (Exception e) {
            log("Can't parse " + address);
            return;
        }
        if (connection == null) return;

        int count = 0;
        try {
            Element content = connection.get()
                    .getElementById("mw-content-text");
            Elements hrefElements = content.getElementsByAttribute("href");
            for (Element hrefElement : hrefElements) {
                if (hrefElement.childNodes().size() == 1
                        && hrefElement.hasAttr("title")
                        && hrefElement.attr("title").startsWith(
                                hrefElement.text())
                        && !hrefElement.text().startsWith("List of")
                        && hrefElement.attr("href").startsWith("/wiki/")) {
                    retrieveAndStoreLogos(Config.BASE_PATH
                            + hrefElement.attr("href").substring(6));
                    if (++count == Config.LIMIT) break;
                    if (count % Config.STATUS_COUNTER == 0)
                        status("Parsed " + count + " entries.");
                }
            }

            status("Database contains " + idxWriter.numDocs() + " logos.");
        } catch (IOException e) {
            log(e);
        }
    }

    /**
     * Searches the DOM for logos (applies some heuristics like searching for
     * infobox, vcard, etc).
     * 
     * @param companypage which shall be searched.
     */
    public static void retrieveAndStoreLogos(String companypage) {
        Connection connection = Jsoup.connect(companypage);
        try {
            Document doc = connection.get();
            Elements infoboxes = doc.getElementsByClass("infobox");
            if (infoboxes.size() == 0) {
                Elements vcards = doc.getElementsByClass("vcard");
                for (Element vcard : vcards) {
                    infoboxes.add(vcard);
                }
            }
            if (infoboxes.size() < 1) {
                log("Found no infoboxes for " + companypage);
            } else {

                Elements name = doc.getElementsByClass("fn");
                String companyName = "";
                if (name.size() > 0) {
                    companyName = name.get(0).text();
                }
                if (companyName.length() == 0) {
                    log("Can't find company name.");
                    return;
                }
                Elements logos = infoboxes.get(0)
                        .getElementsByAttributeValueContaining("alt", "logo");
                if (logos.size() == 0) {
                    logos = infoboxes.get(0)
                            .getElementsByAttributeValueContaining("alt",
                                    companyName);
                }
                if (logos.size() == 0) {
                    Elements tables = infoboxes.get(0).getElementsByClass(
                            "logo");
                    if (tables.size() > 0) {
                        logos = tables.get(0).getElementsByTag("img");
                    }
                }
                if (logos.size() == 0) {
                    Elements images = doc.getElementsByTag("img");
                    for (Element image : images) {
                        if (image.attr("alt").contains("logo")) {
                            logos.add(image);
                        }
                    }
                }
                if (logos.size() == 0) {
                    log("No logos for " + companyName + ", ignoring it.");
                    return;
                }

                Company company = new Company(companyName);

                for (Element logo : logos) {
                    String logoPath = "http:" + logo.attr("src");

                    // try to load image
                    if (logoPath.length() > 0) {
                        BufferedImage image = null;
                        try {
                            URL url = new URL(logoPath);
                            image = ImageIO.read(url);
                        } catch (IOException e) {
                            log(e);
                        } catch (IllegalArgumentException e) {
                            log("Can't read image.");
                            log(e);
                        }

                        if (image != null) {
                            CompanyLogo compLogo = new CompanyLogo(image,
                                    company);
                            company.addLogo(compLogo);
                        }
                    }
                }
                storeOrUpdateCompany(company);
            }
        } catch (IOException e) {
            log(e);
        }
    }

    /**
     * Stores a company and its logo(s) by calling the respective call routines.
     *
     * @param company the company to be stored.
     */
    public static void storeOrUpdateCompany(Company company) {
        try {
            int num = 0;
            for (CompanyLogo logo : company.getLogos()) {
                idxWriter.addDocument(logo.createFeatures(num));
                logo.store(Config.IMAGE_BASE_DIR, num);
                ++num;
            }
        } catch (IOException e) {
            log(e);
        }
        company.store(Config.COMPANY_DIR);

        log("Stored " + company);
    }

    /** 
     * Prints, depending on <code>Config.DEBUG</code>, the provided message.
     * @param msg a message to print.
     */
    public static void log(String msg) {
        if (Config.DEBUG) System.out.println(msg);
    }

    /**
     * Prints a message independtly of <code>Config.DEBUG</code>.
     * @param msg The message to be printed.
     */
    public static void status(String msg) {
        System.out.println(msg);
    }

    /**
     * Logs errors or prints the stack trace in debug mode.
     * @param e An exception to be logged.
     */
    public static void log(Exception e) {
        if (Config.DEBUG)
            e.printStackTrace();
        else
            System.err.println(e.getMessage());
    }
}
