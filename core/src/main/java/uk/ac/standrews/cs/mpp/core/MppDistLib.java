package uk.ac.standrews.cs.mpp.core;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import mpi.Datatype;
import mpi.MPI;

/**
 * Wrapper class for accessing commonly used {@link MPI} methods and variables
 * 
 * @author Gursher
 *
 */
public class MppDistLib implements Closeable {

	public MppDistLib(String args[]) {
		MPI.Init(args);
	}

	public int getNumberOfProcesses() {
		return MPI.COMM_WORLD.Size();
	}

	public int getRankOfCurrentProcess() {
		return MPI.COMM_WORLD.Rank();
	}

	/**
	 * Wrapper method for the <code>SCATTER</code> MPI operation
	 * 
	 * @param globalSendBuffer     The object buffer containing items to be
	 *                             scattered
	 * @param numObjectsToSend     The number of items in the
	 *                             <code>globalInputBuffer</code> to be sent to each
	 *                             process
	 * @param sendDatatype         The data type of the
	 *                             <code>globalSendBuffer</code>
	 * @param processReceiveBuffer The object buffer of the receiving processes
	 * @param numObjectsToReceive  The number of objects to be received by each
	 *                             process
	 * @param receiveDatatype      The data type of the
	 *                             <code>processReceiveBuffer</code>
	 * @param rootWorkerRank       The rank of the master(root) process
	 */
	public void sendToAllProcesses(Object globalSendBuffer, int numObjectsToSend, Class sendDatatype,
			Object processReceiveBuffer, int numObjectsToReceive, Class receiveDatatype, int rootWorkerRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);

		MPI.COMM_WORLD.Scatter(globalSendBuffer, 0, numObjectsToSend, mpiSendDatatype, processReceiveBuffer, 0,
				numObjectsToReceive, mpiReceiveDatatype, rootWorkerRank);

	}

	/**
	 * Wrapper method for the <code>SCATTER</code> MPI operation with offset
	 * 
	 * @param globalSendBuffer     The object buffer containing items to be
	 *                             scattered
	 * @param sendOffset           The number of items in the
	 *                             <code>globalInputBuffer</code> to skip while
	 *                             sending
	 * @param numObjectsToSend     The number of items in the
	 *                             <code>globalInputBuffer</code> to be sent to each
	 *                             process
	 * @param sendDatatype         The data type of the
	 *                             <code>globalSendBuffer</code>
	 * @param processReceiveBuffer The object buffer of the receiving processes
	 * @param receiveOffset        The number of items in the
	 *                             <code>processReceiveBuffer</code> to skip while
	 *                             receiving
	 * @param numObjectsToReceive  The number of objects to be received by each
	 *                             process
	 * @param receiveDatatype      The data type of the
	 *                             <code>processReceiveBuffer</code>
	 * @param rootWorkerRank       The rank of the master(root) process
	 */
	public void sendToAllProcesses(Object globalSendBuffer, int sendOffset, int numObjectsToSend, Class sendDatatype,
			Object processReceiveBuffer, int receiveOffset, int numObjectsToReceive, Class receiveDatatype,
			int rootWorkerRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);

		MPI.COMM_WORLD.Scatter(globalSendBuffer, sendOffset, numObjectsToSend, mpiSendDatatype, processReceiveBuffer,
				receiveOffset, numObjectsToReceive, mpiReceiveDatatype, rootWorkerRank);

	}

	/**
	 * Wrapper method for the <code>GATHER</code> MPI operation
	 * 
	 * @param processSendBuffer             The object buffer of a process
	 *                                      containing items to be gathered
	 * @param numObjectsToSend              The number of items in the
	 *                                      <code>processSendBuffer</code> to be
	 *                                      sent from each process
	 * @param sendDatatype                  The data type of the
	 *                                      <code>processSendBuffer</code>
	 * @param globalReceiveBuffer           The object buffer into which items will
	 *                                      be received from all processes
	 * @param numObjectsToReceivePerProcess The number of objects to be received
	 *                                      from each process
	 * @param receiveDatatype               The data type of the
	 *                                      <code>globalReceiveBuffer</code>
	 * @param rootWorkerRank                The rank of the master(root) process
	 */
	public void receiveFromAllProcesses(Object processSendBuffer, int numObjectsToSend, Class sendDatatype,
			Object globalReceiveBuffer, int numObjectsToReceivePerProcess, Class receiveDatatype, int rootWorkerRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);

		MPI.COMM_WORLD.Gather(processSendBuffer, 0, numObjectsToSend, mpiSendDatatype, globalReceiveBuffer, 0,
				numObjectsToReceivePerProcess, mpiReceiveDatatype, rootWorkerRank);
	}

	/**
	 * Wrapper method for the <code>GATHER</code> MPI operation with offset
	 * 
	 * @param processSendBuffer             The object buffer of a process
	 *                                      containing items to be gathered
	 * @param sendOffset                    The number of items in the
	 *                                      <code>processSendBuffer</code>to skip
	 *                                      while sending
	 * @param numObjectsToSend              The number of items in the
	 *                                      <code>processSendBuffer</code> to be
	 *                                      sent from each process
	 * @param sendDatatype                  The data type of the
	 *                                      <code>processSendBuffer</code>
	 * @param globalReceiveBuffer           The object buffer into which items will
	 *                                      be received from all processes
	 * @param receiveOffset                 The number of items in the
	 *                                      <code>globalReceiveBuffer</code> to skip
	 *                                      while receiving
	 * @param numObjectsToReceivePerProcess The number of objects to be received
	 *                                      from each process
	 * @param receiveDatatype               The data type of the
	 *                                      <code>globalReceiveBuffer</code>
	 * @param rootWorkerRank                The rank of the master(root) process
	 */
	public void receiveFromAllProcesses(Object processSendBuffer, int sendOffset, int numObjectsToSend,
			Class sendDatatype, Object globalReceiveBuffer, int receiveOffset, int numObjectsToReceivePerProcess,
			Class receiveDatatype, int rootWorkerRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);

		MPI.COMM_WORLD.Gather(processSendBuffer, sendOffset, numObjectsToSend, mpiSendDatatype, globalReceiveBuffer,
				receiveOffset, numObjectsToReceivePerProcess, mpiReceiveDatatype, rootWorkerRank);
	}

	/**
	 * Wrapper method for the <code>SEND</code> MPI operation
	 * 
	 * @param sendBuffer             The object buffer containing items to send
	 * @param numObjectsToSend       The number of items in the
	 *                               <code>sendBuffer</code> to send
	 * @param sendDatatype           The data type of the <code>sendBuffer</code>
	 * @param destinationProcessRank The rank of the receiving process
	 */
	public void sendToProcess(Object sendBuffer, int numObjectsToSend, Class sendDatatype, int destinationProcessRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		MPI.COMM_WORLD.Send(sendBuffer, 0, numObjectsToSend, mpiSendDatatype, destinationProcessRank, 0);
	}

	/**
	 * Wrapper method for the <code>SEND</code> MPI operation with offset
	 * 
	 * @param sendBuffer             The object buffer containing items to send
	 * @param sendOffset             The number of items in the
	 *                               <code>sendBuffer</code> to skip while sending
	 * @param numObjectsToSend       The number of items in the
	 *                               <code>sendBuffer</code> to send
	 * @param sendDatatype           The data type of the <code>sendBuffer</code>
	 * @param destinationProcessRank The rank of the receiving process
	 * @param tag                    User-defined tag to help identify the sent
	 *                               message
	 */
	public void sendToProcess(Object sendBuffer, int offset, int numObjectsToSend, Class sendDatatype,
			int destinationProcessRank, int tag) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		MPI.COMM_WORLD.Send(sendBuffer, offset, numObjectsToSend, mpiSendDatatype, destinationProcessRank, tag);
	}

	/**
	 * Wrapper method for the <code>RECEIVE</code> MPI operation
	 * 
	 * @param receiveBuffer       The object buffer for receiving items
	 * @param numObjectsToReceive The number of items to receive from the
	 *                            <code>receiveBuffer</code>
	 * @param receiveDatatype     The data type of the <code>receiveBuffer</code>
	 * @param sourceProcessRank   The rank of the sending process
	 */
	public void receiveFromProcess(Object receiveBuffer, int numObjectsToReceive, Class receiveDatatype,
			int sourceProcessRank) {
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);
		MPI.COMM_WORLD.Recv(receiveBuffer, 0, numObjectsToReceive, mpiReceiveDatatype, sourceProcessRank, 0);
	}

	/**
	 * Wrapper method for the <code>RECEIVE</code> MPI operation with offset
	 * 
	 * @param receiveBuffer       The object buffer for receiving items
	 * @param offset              The number of items in the
	 *                            <code>receiveBuffer</code> to skip while receiving
	 * @param numObjectsToReceive The number of items to receive from the
	 *                            <code>receiveBuffer</code>
	 * @param receiveDatatype     The data type of the <code>receiveBuffer</code>
	 * @param sourceProcessRank   The rank of the sending process
	 * @param tag                 User-defined tag to help identify the sent message
	 */
	public void receiveFromProcess(Object receiveBuffer, int offset, int numObjectsToReceive, Class receiveDatatype,
			int sourceProcessRank, int tag) {
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);
		MPI.COMM_WORLD.Recv(receiveBuffer, offset, numObjectsToReceive, mpiReceiveDatatype, sourceProcessRank, tag);
	}

	/**
	 * Wrapper method for the <code>BCAST</code> MPI operation
	 * 
	 * @param sendBuffer       The object buffer for boradcast items
	 * @param numObjectsToSend The number of items to send from the
	 *                         <code>sendBuffer</code>
	 * @param sendDatatype     The data type of the <code>sendBuffer</code>
	 * @param rootProcessRank  The rank of the root process
	 */
	public void broadcastToAllProcesses(Object sendBuffer, int numOfObjectsToSend, Class sendDatatype,
			int rootProcessRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		MPI.COMM_WORLD.Bcast(sendBuffer, 0, numOfObjectsToSend, mpiSendDatatype, rootProcessRank);
	}

	/**
	 * Wrapper method for the <code>BCAST</code> MPI operation with offset
	 * 
	 * @param sendBuffer       The object buffer for broadcast items
	 * @param offset           The number of items in the <code>sendBuffer</code> to
	 *                         skip while broadcasting
	 * @param numObjectsToSend The number of items to send from the
	 *                         <code>sendBuffer</code>
	 * @param sendDatatype     The data type of the <code>sendBuffer</code>
	 * @param rootProcessRank  The rank of the root process
	 */
	public void broadcastToAllProcesses(Object sendBuffer, int offset, int numOfObjectsToSend, Class sendDatatype,
			int rootProcessRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		MPI.COMM_WORLD.Bcast(sendBuffer, offset, numOfObjectsToSend, mpiSendDatatype, rootProcessRank);
	}

	/**
	 * Wrapper method for the <code>MPI_BARRIER<code> operation
	 */
	public void mpiBarrier() {
		MPI.COMM_WORLD.Barrier();
	}

	/**
	 * Returns the {@link MPI} data type for the corresponding Java data type
	 * 
	 * @param type The {@link Class} object for a Java data type (Eg. Integer.class)
	 * @return The corresponding {@link MPI} data type
	 */
	public Datatype getMpiDatatype(Class<?> type) {
		// the following chunk of if-else statements needs to be replaced with a more
		// elegant solution
		if (type == Integer.class || type == int.class) {
			return MPI.INT;
		}
		if (type == Long.class || type == long.class) {
			return MPI.LONG;
		}
		if (type == Character.class || type == char.class) {
			return MPI.CHAR;
		}
		if (type == Boolean.class || type == boolean.class) {
			return MPI.BOOLEAN;
		}
		if (type == Double.class || type == double.class) {
			return MPI.DOUBLE;
		}
		if (type == Float.class || type == float.class) {
			return MPI.FLOAT;
		}
		if (type == Short.class || type == short.class) {
			return MPI.SHORT;
		}
		if (type == null) {
			return MPI.NULL;
		}
		if (type == byte[][].class || type == byte[].class || type == byte.class) {
			return MPI.BYTE;
		}
		if (type == Object.class) {
			return MPI.OBJECT;
		}
		return null;
	}

	@Override
	public void close() throws IOException {
		MPI.Finalize();

	}

	public Datatype createDerivedDatatypeVector(int numBlocks, int blocklength, int stride, Datatype baseType) {
		return Datatype.Vector(numBlocks, blocklength, stride, baseType);

	}

	public Datatype createDerivedDatatypeIndexed(int[] arrayOfBlockLengths, int[] arrayOfDisplacements,
			Datatype baseType) {
		return Datatype.Indexed(arrayOfBlockLengths, arrayOfDisplacements, baseType);

	}

	/**
	 * Utility method for serialising an outgoing {@link Object}
	 * 
	 * @param object The object to serialise
	 * @return A bye array of the given object
	 */
	public byte[] serialiseOutgoingObject(Object object) {
		byte[] serialisedObject = null;
		try (// Initialise output stream
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				// Initialise writer that writes to output stream
				ObjectOutput out = new ObjectOutputStream(outStream);) {

			// Write object to output stream
			out.writeObject(object);
			// Convert output stream to byte array
			serialisedObject = outStream.toByteArray();
			out.close();
			outStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return serialisedObject;
	}

	/**
	 * Utility method for serialising outgoing images
	 * 
	 * @param object        The {@link BufferedImage} object to be serialised
	 * @param imgFormatName Format string of the image (Eg. jpeg)
	 * @return A byte array of the given {@link BufferedImage}
	 */
	public byte[] serialiseOutgoingObject(Object object, String imgFormatName) {
		byte[] serialisedObject = null;
		try (// Initialise output stream
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// Initialise writer that writes to output stream
		) {
			ImageIO.write((BufferedImage) object, imgFormatName, outStream);
			// Write object to output stream
			// Convert output stream to byte array
			serialisedObject = outStream.toByteArray();
			outStream.close();
		} catch (IOException | ClassCastException e) {
			e.printStackTrace();
		}

		return serialisedObject;
	}

	/**
	 * Utility method for deserialising incoming byte stream
	 * 
	 * @param bytes The byte stream to be deserialised into an {@link Object}
	 * @return An {@link Object} instance
	 */
	public Object deserialiseIncomingObject(byte[] bytes) {
		Object deserialisedObject = null;
		try ( // Initialise input stream
				ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
				// Initialise reader that reads from input stream
				ObjectInput in = new ObjectInputStream(inStream);) {
			// Read object from input stream
			deserialisedObject = in.readObject();
			in.close();
			inStream.close();
		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();
		}
		return deserialisedObject;

	}

	/**
	 * Utility method for deserialising incoming byte stream into a specified image
	 * format
	 * 
	 * @param bytes         The byte stream to be deserialised into a
	 *                      {@link BufferedImage}
	 * @param imgFormatName Format string for the image (Eg. jpeg)
	 * @return An {@link Object} instance
	 */
	public Object deserialiseIncomingObject(byte[] bytes, String imgFormatName) {
		Object deserialisedObject = null;
		try ( // Initialise input stream
				ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
		// Initialise reader that reads from input stream
		) {
			// Read object from input stream
			deserialisedObject = ImageIO.read(inStream);
			inStream.close();
		} catch (IOException | ClassCastException e) {

			e.printStackTrace();
		}
		return deserialisedObject;

	}

}
