package uk.ac.standrews.cs.mpp.examples.mpj;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import uk.ac.standrews.cs.mpp.core.MPPSkelLib;
import uk.ac.standrews.cs.mpp.core.MppDistLib;
import uk.ac.standrews.cs.mpp.examples.util.ImageConvolutionStage1Dist;
import uk.ac.standrews.cs.mpp.examples.util.ImageConvolutionStage2;
import uk.ac.standrews.cs.mpp.examples.util.ImageConvolutionStage3;
import uk.ac.standrews.cs.mpp.operations.Operation;
import uk.ac.standrews.cs.mpp.skeletons.PipelineSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.SequentialOpSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;

public class MPPDistImgConvolution {

	public static void main(String args[]) {
		////////////////////////// MPP INIT////////////////////////////////

		MppDistLib mppDist = new MppDistLib(args);
		double startTime = 0;
		int rank = mppDist.getRankOfCurrentProcess();
		int size = mppDist.getNumberOfProcesses();
		int workerInputSize;
		List<BufferedImage> globalInput;
		List<byte[]> serialisedGlobalInput;
		Object[] globalInputArr=null;
		int[] globalInputArrSize = new int[1];
		// create array for input
		// Master worker generates input values
		if (rank == 0) {
			// read images and create an array of objects
			globalInput = readImages();
			serialisedGlobalInput = new ArrayList<>();
			// serialise each image
			for(BufferedImage i : globalInput) {
				byte[] serialisedImg = mppDist.serialiseOutgoingObject(i, "jpg");
				serialisedGlobalInput.add(serialisedImg);				
			}
			startTime = System.currentTimeMillis();
			// convert list of serialised images to array
			globalInputArr = serialisedGlobalInput.toArray();
			globalInputArrSize[0] = globalInputArr.length;			
		}
		// broadcast global input array size to all non root processes
		mppDist.broadcastToAllProcesses(globalInputArrSize, 1, int.class, 0);
		if(rank!=0) {
			// create empty global input buffer for non root processes
			globalInputArr = new Object[globalInputArrSize[0]];
		}
		// Wait till master worker generates input
		mppDist.mpiBarrier();

		// Buffer which contains input values for each worker (assumes even no of workers and inputs)
		Object[] inputBufArr = new Object[globalInputArrSize[0]/size];

		// Scatter input to all workers
		System.out.println("Process: " + rank + " invoked scatter");
		mppDist.sendToAllProcesses(globalInputArr, inputBufArr.length, Object.class, inputBufArr, inputBufArr.length,
				Object.class, 0);

		// Deserialise input
		List<Object> inputBuf = Arrays.asList(inputBufArr);
		List<Object> deserialisedInputBuf = new ArrayList();
		for(Object o: inputBuf) {
			byte[] rec = (byte[])o;
			Object img = mppDist.deserialiseIncomingObject(rec, "jpg");
			deserialisedInputBuf.add(img);
		}

		// Skeleton library computation
		Object[] resultArr = mppRun(deserialisedInputBuf);
		if(resultArr == null) {
			try {
				System.out.println("Received null from skeleton computation");
				mppDist.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		Object[] finalResult = new Object[globalInputArrSize[0]];

		mppDist.receiveFromAllProcesses(resultArr, resultArr.length, Object.class, finalResult, resultArr.length,
				Object.class, 0);

		if (rank == 0) {
			System.out.println("Final Result Size: " + finalResult.length);
			double endTime = System.currentTimeMillis();
			System.out.println("Total Parallel Execution time Taken: " + (endTime - startTime));
		}

		try {
			mppDist.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Object[] mppRun(List<Object> inputBuf) {
		long startTime = 0;
		long endTime = 0;
		List<Object> result = null;
		Object[] resultArr = null;
	
		MPPSkelLib mpp = new MPPSkelLib();
		Operation o1 = new ImageConvolutionStage1Dist();
		Operation o2 = new ImageConvolutionStage2();
		Operation o3 = new ImageConvolutionStage3();

		Skeleton stage1 = new SequentialOpSkeleton<Object, double[][][]>(o1, double[][][].class);
		Skeleton stage2 = new SequentialOpSkeleton<double[][][], double[][]>(o2, double[][].class);

		Skeleton stage3 = new SequentialOpSkeleton<double[][], Object>(o3, Object.class); 
		Skeleton[] stages = { stage1, stage2, stage3 };
		Skeleton pipeLine = new PipelineSkeleton<>(stages, ArrayList.class);
		startTime = System.currentTimeMillis();
		Future<List<Object>> outputFuture = pipeLine.submitData(inputBuf);
		try {
			Object r = outputFuture.get();
			if(r instanceof Exception) {
				System.out.println("***********************");
				//((Exception)r).printStackTrace();				
			}
			else {
			result = (List<Object>)r;
			resultArr= result.toArray();
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Execution time Taken: " + (endTime - startTime));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("EXCEPTION OCCURRED -!");
			
			
		} finally {
			mpp.shutDown();
			System.out.println("SKELETON EXECUTION - FINISHED");			
		}		
		return resultArr;
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
