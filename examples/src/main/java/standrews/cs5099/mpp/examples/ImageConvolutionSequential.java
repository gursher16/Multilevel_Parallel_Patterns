package standrews.cs5099.mpp.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageConvolutionSequential {

	private static final double[][] FILTER_SCHARR_H = { { 3, 10, 3 }, { 0, 0, 0 }, { -3, -10, -3 } };
	private static final double[][] FILTER_SOBEL_H = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
	static int count;

	public static void main(String args[]) {
		long startTime = 0;
		long endTime = 0;
		List<File> result = new ArrayList<>();
		// Read Images
		List<BufferedImage> imageList = readImages();
		/////////////////// SEQUENTIAL VERSION///////////////////////

		try {
			startTime = System.currentTimeMillis();
			for (BufferedImage image : imageList) { // stage1
				double[][][] img = transformImageToArray(image); // stage2
				double[][] convolvedImg = convolveImage(img); // stage 3
				File output = createImage(convolvedImg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		endTime = System.currentTimeMillis();
		System.out.println("SEQUENTIAL TIME TAKEN: " + (endTime - startTime));
	}

	private static List<BufferedImage> readImages() {
		List<BufferedImage> imageCollection = null;
		try {
			imageCollection = new ArrayList<>();
			File path = new File("E:\\StAndrews_artefacts\\Dissertation\\Sample_Images\\1920x1080");

			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) { // this line weeds out other directories/folders
					//System.out.println("Reading Image..");
					imageCollection.add(loadImage(files[i]));
					// imageCollection.add(loadImage(files[i]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR!!!");
		}
		return imageCollection;
	}

	private static BufferedImage loadImage(File file) throws IOException {
		BufferedImage img = ImageIO.read(file);
		return img;
	}

	private static double[][][] transformImageToArray(BufferedImage bufferedImage) {
		//System.out.println("Transforming to array..");
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		double[][][] image = new double[3][height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color color = new Color(bufferedImage.getRGB(j, i));
				image[0][i][j] = color.getRed();
				image[1][i][j] = color.getGreen();
				image[2][i][j] = color.getBlue();
			}
		}
		return image;
	}

	private static double[][] convolveImage(double[][][] inputParam) throws Exception {
		//System.out.println("Convolving Image..");
		double[][] redConv = convolutionType2(inputParam[0], inputParam[0].length, inputParam[0][0].length,
				FILTER_SCHARR_H, 3, 3, 1);
		double[][] greenConv = convolutionType2(inputParam[1], inputParam[1].length, inputParam[1][0].length,
				FILTER_SCHARR_H, 3, 3, 1);
		double[][] blueConv = convolutionType2(inputParam[2], inputParam[2].length, inputParam[2][0].length,
				FILTER_SCHARR_H, 3, 3, 1);
		double[][] finalConv = new double[redConv.length][redConv[0].length];
		for (int i = 0; i < redConv.length; i++) {
			for (int j = 0; j < redConv[i].length; j++) {
				finalConv[i][j] = redConv[i][j] + greenConv[i][j] + blueConv[i][j];
			}
		}
		return finalConv;
	}

	private static double[][] convolutionType2(double[][] input, int width, int height, double[][] kernel,
			int kernelWidth, int kernelHeight, int iterations) {
		double[][] newInput = input.clone();
		double[][] output = input.clone();

		for (int i = 0; i < iterations; ++i) {
			output = convolution2DPadded(newInput, width, height, kernel, kernelWidth, kernelHeight);
			newInput = output.clone();
		}
		return output;
	}

	private static double[][] convolution2DPadded(double[][] input, int width, int height, double[][] kernel,
			int kernelWidth, int kernelHeight) {
		int smallWidth = width - kernelWidth + 1;
		int smallHeight = height - kernelHeight + 1;
		int top = kernelHeight / 2;
		int left = kernelWidth / 2;

		double[][] small = convolution2D(input, width, height, kernel, kernelWidth, kernelHeight);
		double large[][] = new double[width][height];
		for (int j = 0; j < height; ++j) {
			for (int i = 0; i < width; ++i) {
				large[i][j] = 0;
			}
		}
		for (int j = 0; j < smallHeight; ++j) {
			for (int i = 0; i < smallWidth; ++i) {
				large[i + left][j + top] = small[i][j];
			}
		}
		return large;
	}

	private static double[][] convolution2D(double[][] input, int width, int height, double[][] kernel, int kernelWidth,
			int kernelHeight) {
		int smallWidth = width - kernelWidth + 1;
		int smallHeight = height - kernelHeight + 1;
		double[][] output = new double[smallWidth][smallHeight];
		for (int i = 0; i < smallWidth; ++i) {
			for (int j = 0; j < smallHeight; ++j) {
				output[i][j] = 0;
			}
		}
		for (int i = 0; i < smallWidth; ++i) {
			for (int j = 0; j < smallHeight; ++j) {
				output[i][j] = singlePixelConvolution(input, i, j, kernel, kernelWidth, kernelHeight);
			}
		}
		return output;
	}

	private static double singlePixelConvolution(double[][] input, int x, int y, double[][] k, int kernelWidth,
			int kernelHeight) {
		double output = 0;
		for (int i = 0; i < kernelWidth; ++i) {
			for (int j = 0; j < kernelHeight; ++j) {
				output = output + (input[x + i][y + j] * k[i][j]);
			}
		}
		return output;
	}

	private static File createImage(double[][] inputParam) throws Exception {
		//System.out.println("Creating output..");
		BufferedImage writeBackImage = new BufferedImage(inputParam[0].length, inputParam.length,
				BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < inputParam.length; i++) {
			for (int j = 0; j < inputParam[i].length; j++) {
				Color color = new Color(fixOutOfRangeRGBValues(inputParam[i][j]),
						fixOutOfRangeRGBValues(inputParam[i][j]), fixOutOfRangeRGBValues(inputParam[i][j]));
				writeBackImage.setRGB(j, i, color.getRGB());
			}
		}
		File outputFile = new File(
				"E:\\StAndrews_artefacts\\Dissertation\\Sample_Images\\Output\\edgesTmp_" + count + ".jpeg");
		ImageIO.write(writeBackImage, "jpeg", outputFile);
		count++;
		return outputFile;
	}

	private static int fixOutOfRangeRGBValues(double value) {
		if (value < 0.0) {
			value = -value;
		}
		if (value > 255) {
			return 255;
		} else {
			return (int) value;
		}
	}
}
