# Red Flag Deals Hot Deals Scraper

## Description

This project aims to scrape the content of the [Hot Deals forums](http://forums.redflagdeals.com/hot-deals-f9/), keep track of all interesting and relevant deals, as well as archive all other deals. All relevant deals are emailed daily to a mailing list and then archived. 
There also exists a front-end at http://deals.gordon-pn.com to view the current relevant deals.

---
![Website](https://img.shields.io/website?style=flat-square&url=http%3A%2F%2Fdeals.gordon-pn.com)
![GitHub](https://img.shields.io/github/license/gpnn/rfd-hd-scraper?style=flat-square)

![GitHub top language](https://img.shields.io/github/languages/top/gpnn/rfd-hd-scraper?style=flat-square)
![GitHub language count](https://img.shields.io/github/languages/count/gpnn/rfd-hd-scraper?style=flat-square)

![GitHub commit activity](https://img.shields.io/github/commit-activity/m/gpnn/rfd-hd-scraper?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/gpnn/rfd-hd-scraper?style=flat-square)

## Technologies

* Maven: Dependency management
* Boostrap: CSS framework for front-end
* jQuery: front-end
* Javalin: Web framework for Java for the back-end
* Spring Framework: Utilized Thymeleaf for email templates as well as some dependency injection
* jsoup: library to parse HTML documents

## Prerequisites

* Java 8+
* Apache Maven 3.6+

## Installation
Clone the master branch into your workspace.<br>Compile and package using Maven.
```bash
mvn clean compile package
```

## Configuration
Edit the configuration.json to your needs.<br>
You must set your gmail and password as environment variables.<br>
In my case, my prod machine was running on Linux and my test machines were running on Mac and Windows.<br>
Those settings come from the ConfigurationLoader.java.

## Usage

The main class `com.rfdhd.scraper.App` is used for scraping the forum.<br>
```bash
java -cp *.jar com.rfdhd.scraper.App
```
The main class `com.rfdhd.scraper.DigestCreator` is used for sending the daily digest email. It will take the content of dailyDigest.json as source.
```bash
java -cp *.jar com.rfdhd.scraper.DigestCreator
```
The main class `com.rfdhd.scraper.Start` is used to start the back-end to respond to the HTTP requests.
```bash
java -cp *.jar com.rfdhd.scraper.Start
```

## Use case

The Scraper and the DigestCreator are both automated in Jenkins in order to have the most up to date information on deals.

## Roadmap/Todo

##### Phase 1
* [x]  Use the Jsoup library to scrape data correctly.
* [x]  Save all the scraped data in a map.
* [x]  Save the unfiltered map into scrapings.json
* [x]  Try to read scrapings.json
* [x]  Remove duplicates before saving again
* [x]  Utility class to calculate information from a map.
* [x]  Filter the raw map using the utility class.
* [x]  Save the filtered map into currentLinks.json

##### Phase 2
* [x]  Parse direct link to product
* [x]  Create a configuration file in resources with the property of pages to scrape.
* [x]  Spring framework beans for configuration
* [x]  Refactor currentLinks to dailyDigest
* [x]  Refactor pastLinks to archive
* [x]  Add mailing list to configuration
* [x]  Add email settings to configuration
* [x]  When filtering for dailyDigest, read scrapings and get median votes count of all
* [x]  When scrapings are filtered, it must check with archive if an item has already been processed
* [x]  When email service reads from dailyDigest, move items to archive
* [x]  Set up Mail service
* [x]  Set up Thymeleaf engine
* [x]  Environment variables getter
* [x]  Implement Spring framework beans 

##### Phase 3
* [x]  Add content body under h2
* [x]  Add a good readme.md
* [x]  Sort descending by votes before sending email
* [x]  Record thread start time
* [x]  Parse post date
* [x]  Keep the most recent version of the scraped posts
* [x]  Fix logic with scrapings (threads not going to dailyDigest if it was previously scraped with a low vote score (because it was they wre already in scrapings))
    *  To fix these two issues: 
        * [x]  Make use of LinkedHashMap to preserve the order of insertion. 
        * [x]  Try to read the existing files before scraping. And put into those existing maps, thus updating values with identical keys.
        * [x]  Only filter based on the median of pages scraped, not entire scrapings json.
        * [x]  Save the interesting threads in dailyDigest disregarding the duplicates found in scrapings. 
* [x]  When preparing the email, remove the duplicates by comparing with archive.
* [x]  Filter out threads older than 72 hours when preparing email.
* [x]  Read from config and template within the jar

##### Phase 4
* [ ]  Add commandline flags to differentiate testing on prod and prod
* [x]  A front-end
* [ ]  Finish implementing back-end for signing up
* [ ]  Refactor how the configurations are acquired.

##### Phase 5
* [ ]  Write tests
* [ ]  Improve styling of email template
* [ ]  Integrate MongoDB or lowdb for a database

## License
[MIT License](https://choosealicense.com/licenses/mit/)