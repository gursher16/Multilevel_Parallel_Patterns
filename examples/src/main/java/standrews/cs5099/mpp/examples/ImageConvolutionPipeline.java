package standrews.cs5099.mpp.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import standrews.cs5099.mpp.core.MPPSkelLib;
import standrews.cs5099.mpp.operations.Operation;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class ImageConvolutionPipeline {

	public static void main(String args[]) {
		long startTime = 0;
		long endTime = 0;
		List<File> result = new ArrayList<>();
		// Read Images
		List<BufferedImage> imageList = readImages();

		/////////////////// PARALLEL VERSION////////////////////////
		MPPSkelLib mpp = new MPPSkelLib();
		Operation o1 = new ImageConvolutionStage1();
		Operation o2 = new ImageConvolutionStage2();
		Operation o3 = new ImageConvolutionStage3();

		Skeleton stage1 = new SequentialOpSkeleton<BufferedImage, double[][][]>(o1, double[][][].class);
		Skeleton stage2 = new SequentialOpSkeleton<double[][][], double[][]>(o2, double[][].class);

		Skeleton stage3 = new SequentialOpSkeleton<double[][], File>(o3, null); // passing null value in outputType
																				// since result is not expected
		Skeleton[] stages = { stage1, stage2, stage3 };
		Skeleton pipeLine = new PipelineSkeleton<List<BufferedImage>, List<File>>(stages, null);// passing null value in
																								// outputType since
																								// result is not
																								// expected

		startTime = System.currentTimeMillis();
		Future<List<File>> outputFuture = pipeLine.submitData(imageList);
		try {
			result = outputFuture.get();
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Execution time Taken: " + (endTime - startTime));

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mpp.shutDown();
		}
		System.out.println("EXECUTION - FINISHED");

		////////////////////////////////////////////////////////////
	}

	private static List<BufferedImage> readImages() {
		List<BufferedImage> imageCollection = null;
		try {
			imageCollection = new ArrayList<>();
			File path = new File("E:\\StAndrews_artefacts\\Dissertation\\Sample_Images\\1920x1080");

			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) { // this line weeds out other directories/folders
					System.out.println("Reading Image..");
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

}
