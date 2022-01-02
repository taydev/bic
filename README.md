# bic
###### what's a publicity stunt?

BIC (BinaryImageConverter) is a custom-made encoder designed to turn messages into images via the use of Base64. I know, I know, the project name says "binary" and it doesn't actually use binary... my original plans were to use binary, but I figured that wouldn't have been ideal for images since they would be much larger in pixel count and thus may lose data due to compression.

For converting text into an image, it converts the text input via the `--text` (or `-t`) command line argument into Base64, then calculates RGB values for each character in the converted string based on that character's position in the Base64 character set (`ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmonpqrstuvwxyz0123456789+/=`). That calculated value is then written into all three colour channels for that specific pixel, looping through every character until the string has been converted.

For converting an image into text, it reads the image's file path via the `--image` (or `-i`) command line argument, iterating through every pixel in the found image and reading its red colour channel for its value. It then converts that value back into a character index that can be used to locate the representing Base64 character from its charset, assembling the string that way before outputting it to console.

It has a few known issues, such as:
- arbitrary pre-set image size when creating new images (64px x 64px), thus only allowing for a static 4096 character Base64-encoded input
- no error handling on mismatched sizes when creating new images
- no proper file path checking
- any sort of error handling at all, for that matter

This project was simply designed as a small educational experiment to both teach myself image manipulation/processing and to create a small ARG-esque cryptography puzzle for a Discord server, so the issues will most likely remain unfixed. This repository just exists for archival purposes.
