package standrews.cs5099.mpp.tests;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import standrews.cs5099.mpp.operations.Operation;

public class ImageConvolutionStage3 implements Operation<double[][], File>{
	
	private int count;
	public ImageConvolutionStage3() {
		count=0;
	}
	@Override
	public File execute(double[][] inputParam) throws Exception {
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
		    File outputFile = new File("E:\\StAndrews_artefacts\\Dissertation\\Sample_Images\\Output_Par\\edgesTmp_" + count + ".jpeg");
		    ImageIO.write(writeBackImage, "png", outputFile);
		    count++;
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
