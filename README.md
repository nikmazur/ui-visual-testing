# UI Visual Testing
This project represents a group of tests for visual testing. It works by opening a local copy of a demo website in browser (Selenide), taking screenshots and comparing them to expected screenshots stored in files in the project directory. The pixel-by-pixel comparison is performed by an image-compare library, and if any changes are detected, a Gif of actual, expected and marked images is created and attached to the generated Allure report.

Below is an example of such Gif where the difference areas, which were manually edited on the expected image in Paint, are marked in red:
![alt text](https://github.com/nikmazur/ui-visual-testing/raw/master/bin/editedImage.gif "Image with Differences")

The tests can also compare only specific parts of the page, or ignore specific parts of it. Below is an example of another edited image, where I added 2 lines (blue and red), but since the red line is over the banner area which is ignored (marked as green), only the blue line is considered as difference (marked as red area):
![alt text](https://github.com/nikmazur/ui-visula-testing/raw/master/bin/pageWIgnoredArea.gif "Image with Ignored Area")

Finally we can also set an allowed percentage of differences in which the test will pass even if the images are different.

## Run
Java and Gradle need to be installed. Command to build and execute all tests:
```bash
gradle clean test downloadAllure allureServe
```
This will automatically set up the browser (Firefox by WebDriverManager), run all tests and present an Allure report with the results.
