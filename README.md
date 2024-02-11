# UI Visual Testing
This project represents a group of tests for visual testing. It works by opening a local copy of a demo website in browser (Selenide), taking screenshots and comparing them to expected screenshots stored in files in the project directory. The pixel-by-pixel comparison is performed by an image-compare library, and if any changes are detected, a GIF of actual, expected and marked images is created and attached to the generated Allure report.

Below is an example of such GIF where the difference areas, which were manually edited on the expected image in Paint, are marked in red:
![alt text](https://github.com/nikmazur/ui-visual-testing/raw/master/bin/editedImage.gif "Image with Differences")

The tests can also compare only specific parts of the page (based on element location), or ignore specific parts of it. Below is an example of another edited image, where I added a blue line, but since a part of that line is over the banner area which is ignored (marked as green), only the rest of the line is considered as difference (marked as red area):
![alt text](https://github.com/nikmazur/ui-visual-testing/raw/master/bin/pageWIgnoredArea.gif "Image with Ignored Area")

There is also an option for comparing a page in multiple custom resolutions (browser window is resized to a specific resolution for each check).

Finally, we can also set an allowed amount of differentiating pixels, and so the test will pass even if the images are different.

## Run
Java and Gradle need to be installed. Command to build and execute all tests:
```bash
gradle clean test downloadAllure allureServe
```
This will automatically set up the browser (Firefox by WebDriverManager), run all tests and present an Allure report with the results.
