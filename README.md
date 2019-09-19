# RFDHDscraper
Scrapes the Red Flag Deals Hot Deals forum for view-worthy content.

## Installation
Clone the master branch into your workspace.<br>Compile and package using Maven.
```bash
mvn clean compile package
```

## Usage
Edit the configuration.json to your needs.<br>
You must set your gmail and password as environment variables.<br>
In my case, my prod machine was running on Linux and my test machines were running on Mac and Windows.<br>
Those settings come from the ConfigurationLoader.java.

The main class "com.rfdhd.scraper.App" is used for scraping the forum.<br>
```bash
java -cp *.jar com.rfdhd.scraper.App
```
The main class "com.rfdhd.scraper.DigestCreator" is used for sending the daily digest email. It will take the content of dailyDigest.json as source.
```bash
java -cp *.jar com.rfdhd.scraper.DigestCreator
```
## Roadmap
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
* [ ]  Sort descending by votes before sending email
* [ ]  Make use of original post date and/or view/post counts when filtering
* [ ]  Read from config and template within the jar
* [ ]  Write tests
* [ ]  Improve styling of email template

##### Phase 4
* [ ]  Utilize Pushbullet API or Discord Webhook to send real-time notifications (top scraping of each hour)

##### Phase 5
* [ ]  Integrate MongoDB or lowdb for a database

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Acknowledgment
Those who provided me moral support on my first real project!

## License
[GNU AGPLv3](https://choosealicense.com/licenses/agpl-3.0)

## Project status
To me, I believe this project is 90% finished, the core features are all there. What's left to do are nice extras.