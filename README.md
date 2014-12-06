datathonms2014
==============

This is a small one-day-project to crawl wikipedia for company logos, build a 
database out of it and query the database with photos of logos, to find out to 
which company it belongs.

The project is build in <a href="https://www.java.com/en/download/manual.jsp">
Java 8 (jre1.8.0_25)</a> and uses the <a href="http://jsoup.org/download">jsoup 
library (jsoup-1.7.3)</a> as well as the 
<a href="https://code.google.com/p/lire/downloads/list">lire library 
(Lire-0.9.4 beta_2)</a>. 


Crawling
--------

To run the software, you first have to run the crawler (```Crawler.main```), 
which crawls (```Config.LOAD_ALL = false```) the wikipedia 
<a href="http://en.wikipedia.org/wiki/List_of_companies_of_the_United_States">
List of companies of the United States</a> (Alternatively a wikipage can be 
used as an program parameter, e.g. ```java Crawler "List of companies of the 
United States"```). If you set ```Config.LOAD_ALL = true``` it will crawl all 
pages listed in ```Config.COMPANY_LISTS```.

Note that for the first run you should set ```Config.RENEW_INDEX = true```!

Please refrain from crawling the whole pages again and again.

Searching
---------

To query the database you need to simply run the identifier 
(```Identifier.main```). It will open a ```FileChooser``` which tries to load 
the selected file as an image and searches for similar images in the database.
