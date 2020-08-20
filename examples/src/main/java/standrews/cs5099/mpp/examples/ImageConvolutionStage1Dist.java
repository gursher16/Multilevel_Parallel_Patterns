package standrews.cs5099.mpp.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;

import standrews.cs5099.mpp.operations.Operation;

public class ImageConvolutionStage1Dist implements Operation<Object, double[][][]> {

	@Override
	public double[][][] execute(Object inputParam) throws Exception {
		//System.out.println("Transforming to array..");
		BufferedImage img = (BufferedImage)inputParam;
		int width = img.getWidth();
		int height = img.getHeight();

		double[][][] image = new double[3][height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color color = new Color(img.getRGB(j, i));
				image[0][i][j] = color.getRed();
				image[1][i][j] = color.getGreen();
				image[2][i][j] = color.getBlue();
			}
		}
		return image;
	}

}
