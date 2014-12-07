package main;

import java.io.File;

public class Config {
    public static final String BASE_PATH = "http://en.wikipedia.org/wiki/";
    public static final String DATABASE_DIR = "data" + File.separator;
    public static final String IMAGE_BASE_DIR = DATABASE_DIR + "logos"
            + File.separator;
    public static final String IMAGE_INDEX = DATABASE_DIR + "index"
            + File.separator;
    public static final String SURF_INDEX = DATABASE_DIR + "surfonly"
            + File.separator;
    public static final String FULL_INDEX = DATABASE_DIR + "fullindex"
            + File.separator;
    
    public static final String USE_INDEX = IMAGE_INDEX;
    
    public static final String COMPANY_DIR = DATABASE_DIR + "companies"
            + File.separator;
    public static final int LIMIT = -1;
    public static final boolean GRAY_SCALE = true;
    
    public static final boolean DEBUG = false;
    public static final int MAX_RESULTS = 30;
    public static final boolean RENEW_INDEX = false;
    public static final boolean LOAD_ALL = false;
    public static final int STATUS_COUNTER = 250;
    public static String[] COMPANY_LISTS = {
            "List of companies of Abkhazia",
            "List of companies of Afghanistan", "List of companies of Albania",
            "List of companies of Algeria", "List of companies of Andorra",
            "List of companies of Angola", "List of companies of Anguilla",
            "List of companies of Antigua and Barbuda",
            "List of companies of Argentina", "List of companies of Armenia",
            "List of companies of Australia", "List of companies of Austria",
            "List of companies of Azerbaijan",
            "List of companies of the Bahamas", "List of companies of Bahrain",
            "List of companies of Bangladesh", "List of companies of Barbados",
            "List of companies of Belarus", "List of companies of Belgium",
            "List of companies of Belize", "List of companies of Benin",
            "List of companies of Bermuda", "List of companies of Bhutan",
            "List of companies of Bosnia and Herzegovina",
            "List of companies of Botswana", "List of companies of Brazil",
            "List of companies of Brunei", "List of companies of Bulgaria",
            "List of companies of Burma", "List of companies of Cambodia",
            "List of companies of Cameroon", "List of companies of Canada",
            "List of companies of Cape Verde",
            "List of companies of the Central African Republic",
            "List of companies of Chad", "List of companies of China",
            "List of companies of Colombia", "List of companies of Costa Rica",
            "List of companies of Croatia", "List of companies of Cuba",
            "List of companies of Cyprus",
            "List of companies of the Czech Republic",
            "List of companies of Denmark", "List of companies of Dominica",
            "List of companies of the Dominican Republic",
            "List of companies of Egypt", "List of companies of El Salvador",
            "List of companies of Equatorial Guinea",
            "List of companies of Estonia", "List of companies of Ethiopia",
            "List of companies of the Faroe Islands",
            "List of companies of Finland", "List of companies of France",
            "List of companies of Georgia (country)",
            "List of companies of Germany", "List of companies of Ghana",
            "List of companies of Greece", "List of companies of Greenland",
            "List of companies of Guatemala",
            "List of companies based in Guinea-Bissau",
            "List of companies of Guyana", "List of companies of Haiti",
            "List of companies of Honduras", "List of companies of Hong Kong",
            "List of companies of Hungary", "List of companies of Iceland",
            "List of companies of Indonesia", "List of companies of Iran",
            "List of companies of Iraq", "List of companies of Ireland",
            "List of companies of the Isle of Man",
            "List of companies of Israel", "List of companies of Italy",
            "List of companies of the Ivory Coast",
            "List of companies of Jamaica", "List of companies of Japan",
            "List of companies of Jordan", "List of companies of Kazakhstan",
            "List of companies of Kenya", "List of companies of North Korea",
            "List of companies of South Korea", "List of companies of Kuwait",
            "List of companies of Laos", "List of companies of Latvia",
            "List of companies of Libya", "List of companies of Lesotho",
            "List of companies of Liberia", "List of companies of Luxembourg",
            "List of companies of Lithuania", "List of companies of Macau",
            "List of companies of Madagascar", "List of companies of Malaysia",
            "List of companies of Mali", "List of companies of Malta",
            "List of companies of Mauritius", "List of companies of Mexico",
            "List of companies of Mongolia", "List of companies of Montenegro",
            "List of companies of Morocco",
            "List of companies of the Nagorno-Karabakh Republic",
            "List of companies of Namibia", "List of companies of Nepal",
            "List of companies of the Netherlands",
            "List of companies of New Zealand",
            "List of companies of Nicaragua", "List of companies of Nigeria",
            "List of companies of Norway", "List of companies of Oman",
            "List of companies of Pakistan", "List of companies of Panama",
            "List of companies of Peru",
            "List of companies of the Philippines",
            "List of companies of Poland", "List of companies of Portugal",
            "List of companies of Puerto Rico", "List of companies of Qatar",
            "List of companies of Romania", "List of companies of Russia",
            "List of companies of Rwanda",
            "List of companies of Saint Kitts and Nevis",
            "List of companies of Saint Lucia",
            "List of companies of São Tomé and Príncipe",
            "List of companies of Saudi Arabia",
            "List of companies of Scotland", "List of companies of Senegal",
            "List of companies of Serbia", "List of companies of Singapore",
            "List of companies of Slovakia", "List of companies of Slovenia",
            "List of companies of Somalia",
            "List of companies of South Africa", "List of companies of Spain",
            "List of companies of Sri Lanka", "List of companies of Swaziland",
            "List of companies of Sweden", "List of companies of Switzerland",
            "List of companies of Syria", "List of companies of Taiwan",
            "List of companies of Tanzania", "List of companies of Thailand",
            "List of companies of Trinidad and Tobago",
            "List of companies of Turkey", "List of companies of Ukraine",
            "List of companies of the United Arab Emirates",
            "List of companies of the United States",
            "List of companies of Uzbekistan",
            "List of companies of Venezuela", "List of companies of Vietnam",
            "List of companies of Wales", "List of companies of Zambia",
            "List of companies of Zimbabwe"
    };

}
