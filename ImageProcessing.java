import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {
    public static void main(String[] args) {

        // load image using a URL
        int[][] imageData = imgToTwoD(
                "https://content.codecademy.com/projects/project_thumbnails/phaser/bug-dodger.png");
        viewImageData(imageData);
        // int[][] trimmed = trimBorders(imageData, 60);
        // twoDToImage(trimmed, "./trimmed_img.jpg");
        // int[][] neg = negativeColor(trimmed);
        // twoDToImage(neg, "./negative_img.jpg");
        // int[][] sh = stretchHorizontally(trimmed);
        // twoDToImage(sh, "./sh_img.jpg");
        // int[][] sv = shrinkVertically(imageData);
        // twoDToImage(sv, "./sv_img.jpg");

        int[][] allFilters = stretchHorizontally(
                shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(imageData), 50)), 200, 20, 40)));

        // Painting with random pixels
        int[][] blankCanvas = new int[500][500];
        int[][] randomImg = paintRandomImage(blankCanvas);
        twoDToImage(randomImg, "./ri.jpg");

        // Painting a rectangle
        int[] rgba = { 255, 255, 0, 255 };
        int[][] rectangleImg = paintRectangle(randomImg, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
        twoDToImage(rectangleImg, "./rectangle.jpg");

        // paint random rectangles
        int[][] randRects = generateRectangles(blankCanvas, 3000);
        twoDToImage(rectangleImg, "./randrects.jpg");

    }

    // trims off borders of the image
    // returns modified image 2D array
    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        // Example Method
        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
            for (int i = 0; i < trimmedImg.length; i++) {
                for (int j = 0; j < trimmedImg[i].length; j++) {
                    trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
                }
            }
            return trimmedImg;
        } else {
            System.out.println("Cannot trim that many pixels from the given image.");
            return imageTwoD;
        }
    }

    // create negative version of the image
    public static int[][] negativeColor(int[][] imageTwoD) {

        int[][] modif = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < modif.length; i++) {
            for (int j = 0; j < modif[0].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];
                modif[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return modif;
    }

    // copy original pixel and save two duplicates sidebyside into new mod
    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        int[][] mod = new int[imageTwoD.length][imageTwoD[0].length * 2];
        int it = 0;
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                it = j * 2;
                mod[i][it] = imageTwoD[i][j];
                mod[i][it + 1] = imageTwoD[i][j];
            }
        }
        return mod;
    }

    // select every other pixel down each column
    public static int[][] shrinkVertically(int[][] imageTwoD) {
        int[][] mod = new int[imageTwoD.length / 2][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD[0].length; i++) {
            for (int j = 0; j < imageTwoD.length - 1; j += 2) {
                mod[j / 2][i] = imageTwoD[j][i];
            }
        }
        return mod;
    }

    // flips image vertically and horizontally
    public static int[][] invertImage(int[][] imageTwoD) {
        int[][] mod = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                mod[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
            }
        }
        return mod;

    }

    // change individual rgb values to apply a filter
    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue,
            int blueChangeValue) {
        int[][] mod = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {

                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);

                int newRed = rgba[0] + redChangeValue;
                int newGreen = rgba[1] + greenChangeValue;
                int newBlue = rgba[2] + blueChangeValue;

                if (newRed > 255) {
                    newRed = 255;
                } else if (newRed < 0) {
                    newRed = 0;
                }
                if (newGreen > 255) {
                    newGreen = 255;
                } else if (newGreen < 0) {
                    newGreen = 0;
                }
                if (newBlue > 255) {
                    newBlue = 255;
                } else if (newBlue < 0) {
                    newBlue = 0;
                }

                rgba[0] = newRed;
                rgba[1] = newGreen;
                rgba[2] = newBlue;

                mod[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return mod;
    }

    // replace image pixel with a random pixel
    public static int[][] paintRandomImage(int[][] canvas) {
        Random rand = new Random();
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                int randR = rand.nextInt(256);
                int randG = rand.nextInt(256);
                int randB = rand.nextInt(256);
                int[] rgba = { randR, randG, randB, 255 };
                canvas[i][j] = getColorIntValFromRGBA(rgba);

            }
        }
        return canvas;
    }

    // draw a rectangle from a given position of a given size
    public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition,
            int color) {
        for (int i = rowPosition; i < rowPosition + height; i++) {
            for (int j = colPosition; j < colPosition + width; j++) {
                canvas[i][j] = color;
            }
        }
        return canvas;
    }

    // creat abstract geometric art utilizing paint Rectangle method
    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        Random rand = new Random();
        for (int i = 0; i < numRectangles; i++) {

            int randomWidth = rand.nextInt(canvas[0].length);
            int randomHeight = rand.nextInt(canvas.length);
            int randomRowPos = rand.nextInt(canvas.length - randomHeight);
            int randomColPos = rand.nextInt(canvas[0].length - randomWidth);
            int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), rand.nextInt(256) };
            int randomColor = getColorIntValFromRGBA(rgba);

            canvas = paintRectangle(canvas, randomWidth, randomHeight, randomRowPos, randomColPos, randomColor);
        }
        return canvas;
    }

    /*
     * accepts a string (file path of image URL) returns 2D array of ints that
     * contain every pixel stored as int hexadecimal values containing RGBA for the
     * pixel
     */
    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
                URL imageUrl = new URL(inputFileOrLink);
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Failed to get image from provided URL.");
                }
            } else {
                image = ImageIO.read(new File(inputFileOrLink));
            }
            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }
            return pixelData;
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getLocalizedMessage());
            return null;
        }
    }

    /*
     * accepts a 2D array of ints and a String for tile name. It converts the 2D
     * array of int pixel data into an image and saves it
     */
    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }
            File output = new File(fileName);
            ImageIO.write(result, "jpg", output);
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e.getLocalizedMessage());
        }
    }

    /*
     * method accepts an int value representing the pixel hexadecimal value and
     * returns a 4 element int array consisting of RGBA values (0, 255)
     */
    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color pixelColor = new Color(pixelColorValue);
        return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
    }

    /*
     * method accepts an array of ints that represent RGBA values and convert it
     * into a single int value representing the pixel hexadecimal
     */
    public static int getColorIntValFromRGBA(int[] colorData) {
        if (colorData.length == 4) {
            Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
            return color.getRGB();
        } else {
            System.out.println("Incorrect number of elements in RGBA array.");
            return -1;
        }
    }

    /*
     * method is used to view the structure of the image data in both the raw pixel
     * form and the extracted RGBA form.
     */
    public static void viewImageData(int[][] imageTwoD) {
        if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
            int[][] rawPixels = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rawPixels[i][j] = imageTwoD[i][j];
                }
            }
            System.out.println("Raw pixel data from the top left corner.");
            System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
            int[][][] rgbPixels = new int[3][3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
                }
            }
            System.out.println();
            System.out.println("Extracted RGBA pixel data from top the left corner.");
            for (int[][] row : rgbPixels) {
                System.out.print(Arrays.deepToString(row) + System.lineSeparator());
            }
        } else {
            System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
        }
    }
}