package uk.ac.standrews.cs.mpp.examples.skeletons;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import uk.ac.standrews.cs.mpp.core.MPPSkelLib;
import uk.ac.standrews.cs.mpp.examples.util.ImageConvolutionStage1;
import uk.ac.standrews.cs.mpp.examples.util.ImageConvolutionStage2;
import uk.ac.standrews.cs.mpp.examples.util.ImageConvolutionStage3;
import uk.ac.standrews.cs.mpp.operations.Operation;
import uk.ac.standrews.cs.mpp.skeletons.PipelineSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.SequentialOpSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;

public class ImageConvolutionPipeline {

	public static void main(String args[]) {
		System.out.println("Started: " + ImageConvolutionPipeline.class.getSimpleName());

		MPPSkelLib mpp = null;
		int inputMode = 0;
		int numCores = 0;
		String imageFolderName = null;
		if (args.length == 2) {
			numCores = Integer.parseInt(args[0]);
			inputMode = Integer.parseInt(args[1]);
			if (numCores <= Runtime.getRuntime().availableProcessors() && numCores >= 2) {
				mpp = new MPPSkelLib(numCores);
			} else {
				System.err.println("Invalid number of processors!");
				System.exit(-2);
			}
			switch (inputMode) {
			case 1:
				imageFolderName = "800x600";
				break;
			case 2:
				imageFolderName = "1280x720";
				break;
			case 3:
				imageFolderName = "test";
				break;
			default:
				System.err.println("Invalid inputMode -- should be 1 for 800x600 or 2 for 1280x720");
				System.exit(-3);
			}
		} else {
			System.err.println("Invalid number of arguments!");
			System.exit(-1);
		}

		long startTime = 0;
		long endTime = 0;
		Object result;
		List<File> resultList = new ArrayList<>();
		// Read Images
		List<BufferedImage> imageList = readImages(imageFolderName);

		/////////////////// PARALLEL VERSION////////////////////////
		System.out.println("Executing Parallel Operation..");
		Operation o1 = new ImageConvolutionStage1();
		Operation o2 = new ImageConvolutionStage2();
		Operation o3 = new ImageConvolutionStage3();

		Skeleton stage1 = new SequentialOpSkeleton<BufferedImage, double[][][]>(o1, double[][][].class);
		Skeleton stage2 = new SequentialOpSkeleton<double[][][], double[][]>(o2, double[][].class);

		Skeleton stage3 = new SequentialOpSkeleton<double[][], File>(o3, null); // passing null value in outputType
																				// since result is not expected
		Skeleton[] stages = { stage1, stage2, stage3 };
		Skeleton pipeLine = new PipelineSkeleton(stages, ArrayList.class);

		startTime = System.currentTimeMillis();
		Future<List<File>> outputFuture = pipeLine.submitData(imageList);
		try {
			result = outputFuture.get();
			if (result instanceof Exception) {
				((Exception) result).printStackTrace();
			} else {
				resultList = (List) result;
				if (resultList.size() == imageList.size()) {
					System.out.println("****SAME SIZE***");
				} else {
					System.out.println("****INCORRECT****");
				}
			}
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Execution time Taken: " + (endTime - startTime));

		} catch (Exception e) {
			System.out.println("EXCEPTION!!!");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mpp.shutDown();
		}
		System.out.println("FINISHED");

		////////////////////////////////////////////////////////////
	}

	private static List<BufferedImage> readImages(String imageFolderName) {
		List<BufferedImage> imageCollection = null;
		try {
			imageCollection = new ArrayList<>();
			File path = new File("//home//gg63//mpp_lib//mpp//examples//src//main//resources//inputImages//" + imageFolderName);
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) { // this line weeds out other directories/folders
					// System.out.println("Reading Image..");
					imageCollection.add(loadImage(files[i]));
					// imageCollection.add(loadImage(files[i]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error reading file");
			System.exit(-5);
		}
		return imageCollection;
	}

	private static BufferedImage loadImage(File file) throws IOException {
		BufferedImage img = ImageIO.read(file);
		return img;
	}

}
