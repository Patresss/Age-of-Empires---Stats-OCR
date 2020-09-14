# Age of Empires - Stats OCR
Application for conversion of images with Age f Empires 2 stats into machine-encoded text

## Steps
1. Prepare a directory with images per Civilizations
```
directory
└───Aztecs
│   │   sortedImagedWithScore1.jpg
│   │   sortedImagedWithMilitary1.jpg
│   │   sortedImagedWithEconomy1.jpg
│   │   sortedImagedWithTechnology1.jpg
│   │   sortedImagedWithSociety1.jpg
│   │   sortedImagedWithTimeline1.jpg
│   │   sortedImagedWithScore2.jpg
│   │   sortedImagedWithMilitary2.jpg
│   │   sortedImagedWithEconomy2.jpg
│   │   sortedImagedWithTechnology2.jpg
│   │   sortedImagedWithSociety2.jpg
│   │   sortedImagedWithTimeline2.jpg
│   │   ...
└───Berbers
│   │   ...
```
2. Rename files using `ImageFileRenamer(applicationSettings.pathToDirectory, <civilization>).rename()`. It will change files to:
```
directory
└───Aztecs
│   │   Aztecs vs Aztecs - Score.jpg
│   │   Aztecs vs Aztecs - Military.jpg
│   │   Aztecs vs Aztecs - Economy.jpg
│   │   Aztecs vs Aztecs - Technology.jpg
│   │   Aztecs vs Aztecs - Society.jpg
│   │   Aztecs vs Aztecs - Timeline.jpg
│   │   Aztecs vs Berbers - Score.jpg
│   │   Aztecs vs Berbers - Military
│   │   Aztecs vs Berbers - Economy.jpg
│   │   Aztecs vs Berbers - Technology.jpg
│   │   Aztecs vs Berbers - Society.jpg
│   │   Aztecs vs Berbers - Timeline.jpg
│   │   ...
└───Berbers
│   │   ...
```
3. Convert images to text:
   * Only for one type. Example: `val stats = ExcelStatsCreator(applicationSettings).createStats(Civilization.TEUTONS, Civilization.ITALIANS, StatsType.SOCIETY)`
   * Only for one game. Example: `val stats = ExcelStatsCreator(applicationSettings).createStats(Civilization.TEUTONS, Civilization.ITALIANS)`
   * For one civilization. Example: `val stats = ExcelStatsCreator(applicationSettings).createStats(Civilization.TEUTONS)`

## Accuracy
Unfortunately, OCR is not 100% accurate. If you care about speed, just set the properties:
```
replaceTheMostProbablyCharacters=false
useOnlyColoredImage=waves
```
Then the program will only use the original photos and return the result. However, from my observations, the program sometimes returns the result with errors (although it is rare).

To have more control over the results, you can set a property:
```
useOnlyColoredImage=true
```
The program will use: 
- Color picture: 
![](https://github.com/Patresss/Age-of-Empires---Stats-OCR/blob/master/examples/test%20Society%20PLAYER1%203%20-%20null.png)
- Low-level binarized image: 
![](https://github.com/Patresss/Age-of-Empires---Stats-OCR/blob/master/examples/test%20Society%20PLAYER1%203%20-%20115.png)
- Medium-degree binarized image: 
![](https://github.com/Patresss/Age-of-Empires---Stats-OCR/blob/master/examples/test%20Society%20PLAYER1%203%20-%20200.png)
- Highly binarized image: 
![](https://github.com/Patresss/Age-of-Empires---Stats-OCR/blob/master/examples/test%20Society%20PLAYER1%203%20-%20300.png)

If OCR returns other results, for example 
- `3995` - Color picture,
- `3995` - Low-level binarized image,
- `3995` - Medium-degree binarized image,
- `3998` - Highly binarized image.

This will return `FIXME [3995, 3998]` .

However, I noticed that when the program returns different results, sometimes only one character out of a pair (or list) of characters is correct.
* If `0` or `6` then `6`,
* if `0` or `1` then `0`,
* if `0` or `9` then `9`,
* if `1` or `7` then `7`,
* if `2` or `5` then `5`,
* if `3` or `4` then `4`,
* if `3` or `5` then `5`,
* if `3` or `8` then `8`,
* if `4` or `9` then `9`
* if `0` or `1` or `11` then `0`

To enable auto-correcting errors according to the above rule, set a property.
```
replaceTheMostProbablyCharacters=true
```
