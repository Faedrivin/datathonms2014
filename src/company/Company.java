package company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Company {
    private List<CompanyLogo> logos = new ArrayList<>();

    private final String id;
    private final String name;

    /**
     * Creates a company representation and initializes its name and id.
     * The id is just the String obtained from
     * <code>String.valueOf(name.hashCode())</code>.
     * 
     * @param name The company's name.
     */
    public Company(String name) {
        this.name = name;
        this.id = String.valueOf(name.hashCode());
    }

    /**
     * Adds a logo to the list of logos.
     * 
     * @param logo The company's logo.
     */
    public void addLogo(CompanyLogo logo) {
        getLogos().add(logo);
    }

    /**
     * Stores the company <code>logos.size()</code> times into the database,
     * by using keys of the form of the id (<code>name.hashCode()</code>),
     * and the numbers from 0 to logos.size()-1, respectively.
     * 
     * The files, ending on <code>.company</code> just hold the name of the
     * company.
     * 
     * An example name would be:
     * -4126323424_0.company
     * 
     * @param companyDir the directory in which the company data shall be
     *            stored.
     */
    public void store(String companyDir) {
        for (int i = 0; i < logos.size(); i++) {
            File out = new File(companyDir + id + "_" + i + ".company");

            try (FileWriter writer = new FileWriter(out)) {
                writer.write(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return this.name + " (" + getLogos().size() + " logo"
                + (logos.size() > 1 ? "s" : "") + ")";
    }

    /**
     * Returns the company's name.
     * 
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the company's id, <code>String.valueOf(name.hashCode());</code>.
     * 
     * @return the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the logo list.
     * 
     * @return the logo list.
     */
    public List<CompanyLogo> getLogos() {
        return logos;
    }

}
