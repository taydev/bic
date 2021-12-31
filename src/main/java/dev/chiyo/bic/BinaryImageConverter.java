package dev.chiyo.bic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

public class BinaryImageConverter {

    // this is purely because I don't like having methods be static
    private static BinaryImageConverter instance = new BinaryImageConverter();

    private final String BASE64_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static void main(String[] args) {
        if (args.length < 2) {
            instance.postInvalidFormat();
        }

        String input = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));

        if (args[0].equalsIgnoreCase("--image") || args[0].equalsIgnoreCase("-i")) {
            instance.convertImageToText(input);
        } else if (args[0].equalsIgnoreCase("--text") || args[0].equalsIgnoreCase("-t")) {
            instance.convertTextToImage(input);
        } else {
            instance.postInvalidFormat();
        }
    }

    private void postInvalidFormat() {
        System.out.println("Invalid syntax. BIC supports one of the following argument combos:" +
                "\n- bic --image (path to image)" +
                "\n- bic --text (text to convert)" +
                "\n\nPlease re-run the program with the correct arguments based on your preferred function. :)");
        System.exit(1);
    }

    private void convertImageToText(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            System.out.printf("Image found. Size: %spx x %spx%nProcessing pixels...%n", image.getWidth(), image.getHeight());
            StringBuilder msg = new StringBuilder();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int red = (image.getRGB(x, y) & 0xff0000) >> 16;
                    msg.append(this.mapValueToCharacter(red));
                }
            }
            System.out.println("B64 message: " + msg.toString().trim());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void convertTextToImage(String text) {
        text = text.replaceAll("\\\\n", "\n");
        System.out.println("text: " + text);
        System.out.println("Converting text to B64...");
        String b64 = Base64.getEncoder().encodeToString(text.getBytes());
        System.out.println("B64 length: " + b64.length());
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int val = this.mapCharacterToValue(b64.charAt((y * 64) + x));
                System.out.println("char: " + b64.charAt((y * 64) + x) + " - val: " + val);
                image.setRGB(x, y, new Color(val, val, val).getRGB());
            }
        }
        File outputFile = new File("output.png");
        try {
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image written.");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private char mapValueToCharacter(int value) {
        return this.BASE64_CHARSET.charAt((int) Math.ceil(value/4d));
    }

    private int mapCharacterToValue(char character) {
        return Math.max((this.BASE64_CHARSET.indexOf(character) * 4) - 3, 0);
    }

}
