package uk.ac.standrews.cs.mpp.examples.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import uk.ac.standrews.cs.mpp.operations.Operation;

public class ImageConvolutionStage3 implements Operation<double[][], Object>{
	
	
	
	@Override
	public Object execute(double[][] inputParam) throws Exception {
		
		Random random = new Random();
		int count = random.nextInt() + random.nextInt(25);
		//System.out.println("Creating output..");
		 BufferedImage writeBackImage = new BufferedImage(inputParam[0].length, inputParam.length, BufferedImage.TYPE_INT_RGB);
		    for (int i = 0; i < inputParam.length; i++) {
		        for (int j = 0; j < inputParam[i].length; j++) {
		            Color color = new Color(fixOutOfRangeRGBValues(inputParam[i][j]),
		                    fixOutOfRangeRGBValues(inputParam[i][j]),
		                    fixOutOfRangeRGBValues(inputParam[i][j]));
		            writeBackImage.setRGB(j, i, color.getRGB());
		        }
		    }
		    File outputFile = new File("examples//src//main//resources//outputImages//" + count + ".jpeg");
		    ImageIO.write(writeBackImage, "jpeg", outputFile);
		    //System.out.println("File created!!");
		    return outputFile;
	}
	
	private int fixOutOfRangeRGBValues(double value) {
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
