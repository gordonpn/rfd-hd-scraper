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
Please visit the [wiki section](https://gitlab.com/gpnn/rfdhdscraper/wikis/Development-phases) for ideas that I wrote down for this project.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Acknowledgment
Those who provided me moral support on my first real project!

## License
[GNU AGPLv3](https://choosealicense.com/licenses/agpl-3.0)

## Project status
To me, I believe this project is 90% finished, the core features are all there. What's left to do are nice extras.