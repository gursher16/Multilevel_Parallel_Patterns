package uk.ac.standrews.cs.mpp.examples.util;

import java.awt.Color;
import java.awt.image.BufferedImage;

import uk.ac.standrews.cs.mpp.operations.Operation;

public class ImageConvolutionStage1 implements Operation<BufferedImage, double[][][]> {

	@Override
	public double[][][] execute(BufferedImage inputParam) throws Exception {
		//System.out.println("Transforming to array..");
		int width = inputParam.getWidth();
		int height = inputParam.getHeight();

		double[][][] image = new double[3][height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color color = new Color(inputParam.getRGB(j, i));
				image[0][i][j] = color.getRed();
				image[1][i][j] = color.getGreen();
				image[2][i][j] = color.getBlue();
			}
		}
		return image;
	}

}
