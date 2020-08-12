package standrews.cs5099.mpp.tests;

import standrews.cs5099.mpp.operations.Operation;

public class ImageConvolutionStage2 implements Operation<double[][][], double[][]> {

	private static final double[][] FILTER_SCHARR_H = {{3, 10, 3}, {0, 0, 0}, {-3, -10, -3}};
	private static final double[][] FILTER_SOBEL_H = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
	
	@Override
	public double[][] execute(double[][][] inputParam) throws Exception {
		//System.out.println("Convolving Image..");
		double[][] redConv = convolutionType2(inputParam[0], inputParam[0].length, inputParam[0][0].length, FILTER_SCHARR_H, 3, 3, 1);
		double[][] greenConv = convolutionType2(inputParam[1], inputParam[1].length, inputParam[1][0].length, FILTER_SCHARR_H, 3, 3, 1);
		double[][] blueConv = convolutionType2(inputParam[2], inputParam[2].length, inputParam[2][0].length, FILTER_SCHARR_H, 3, 3, 1);
		double[][] finalConv = new double[redConv.length][redConv[0].length];
		for (int i = 0; i < redConv.length; i++) {
			for (int j = 0; j < redConv[i].length; j++) {
				finalConv[i][j] = redConv[i][j] + greenConv[i][j] + blueConv[i][j];
			}
		}
		return finalConv;
	}

	public double[][] convolutionType2(double[][] input, int width, int height, double[][] kernel, int kernelWidth,
			int kernelHeight, int iterations) {
		double[][] newInput = input.clone();
		double[][] output = input.clone();

		for (int i = 0; i < iterations; ++i) {
			output = convolution2DPadded(newInput, width, height, kernel, kernelWidth, kernelHeight);
			newInput = output.clone();
		}
		return output;
	}

	public double[][] convolution2DPadded(double[][] input, int width, int height, double[][] kernel, int kernelWidth,
			int kernelHeight) {
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

	public static double[][] convolution2D(double[][] input, int width, int height, double[][] kernel, int kernelWidth,
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

	public static double singlePixelConvolution(double[][] input, int x, int y, double[][] k, int kernelWidth,
			int kernelHeight) {
		double output = 0;
		for (int i = 0; i < kernelWidth; ++i) {
			for (int j = 0; j < kernelHeight; ++j) {
				output = output + (input[x + i][y + j] * k[i][j]);
			}
		}
		return output;
	}

}
