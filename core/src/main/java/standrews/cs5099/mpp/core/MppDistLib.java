package standrews.cs5099.mpp.core;

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

	public Datatype createDerivedDatatypeVector(int numBlocks, int blocklength, int stride, Datatype baseType) {
		return Datatype.Vector(numBlocks, blocklength, stride, baseType);

	}

	public Datatype createDerivedDatatypeIndexed(int[] arrayOfBlockLengths, int[] arrayOfDisplacements,
			Datatype baseType) {
		return Datatype.Indexed(arrayOfBlockLengths, arrayOfDisplacements, baseType);

	}

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

	public void sendToAllProcesses(Object globalSendBuffer, int numObjectsToSend, Class sendDatatype,
			Object processReceiveBuffer, int numObjectsToReceive, Class receiveDatatype, int rootWorkerRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);

		MPI.COMM_WORLD.Scatter(globalSendBuffer, 0, numObjectsToSend, mpiSendDatatype, processReceiveBuffer, 0,
				numObjectsToReceive, mpiReceiveDatatype, rootWorkerRank);

	}

	public void sendToAllProcesses(Object globalSendBuffer, int sendOffset, int numObjectsToSend, Class sendDatatype,
			Object processReceiveBuffer, int receiveOffset, int numObjectsToReceive, Class receiveDatatype,
			int rootWorkerRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);

		MPI.COMM_WORLD.Scatter(globalSendBuffer, sendOffset, numObjectsToSend, mpiSendDatatype, processReceiveBuffer,
				receiveOffset, numObjectsToReceive, mpiReceiveDatatype, rootWorkerRank);

	}

	public void receiveFromAllProcesses(Object processSendBuffer, int numObjectsToSend, Class sendDatatype,
			Object globalReceiveBuffer, int numObjectsToReceivePerProcess, Class receiveDatatype, int rootWorkerRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);

		MPI.COMM_WORLD.Gather(processSendBuffer, 0, numObjectsToSend, mpiSendDatatype, globalReceiveBuffer, 0,
				numObjectsToReceivePerProcess, mpiReceiveDatatype, rootWorkerRank);
	}

	public void receiveFromAllProcesses(Object processSendBuffer, int sendOffset, int numObjectsToSend,
			Class sendDatatype, Object globalReceiveBuffer, int receiveOffset, int numObjectsToReceivePerProcess,
			Class receiveDatatype, int rootWorkerRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);

		MPI.COMM_WORLD.Gather(processSendBuffer, sendOffset, numObjectsToSend, mpiSendDatatype, globalReceiveBuffer,
				receiveOffset, numObjectsToReceivePerProcess, mpiReceiveDatatype, rootWorkerRank);
	}

	public void sendToProcess(Object sendBuffer, int numObjectsToSend, Class sendDatatype, int destinationProcessRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		MPI.COMM_WORLD.Send(sendBuffer, 0, numObjectsToSend, mpiSendDatatype, destinationProcessRank, 0);
	}

	public void sendToProcess(Object sendBuffer, int offset, int numObjectsToSend, Class sendDatatype,
			int destinationProcessRank, int tag) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		MPI.COMM_WORLD.Send(sendBuffer, offset, numObjectsToSend, mpiSendDatatype, destinationProcessRank, tag);
	}

	public void receiveFromProcess(Object receiveBuffer, int numObjectsToReceive, Class receiveDatatype,
			int sourceProcessRank) {
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);
		MPI.COMM_WORLD.Recv(receiveBuffer, 0, numObjectsToReceive, mpiReceiveDatatype, sourceProcessRank, 0);
	}

	public void receiveFromProcess(Object receiveBuffer, int offset, int numObjectsToReceive, Class receiveDatatype,
			int sourceProcessRank, int tag) {
		Datatype mpiReceiveDatatype = getMpiDatatype(receiveDatatype);
		MPI.COMM_WORLD.Recv(receiveBuffer, offset, numObjectsToReceive, mpiReceiveDatatype, sourceProcessRank, tag);
	}

	public void broadcastToAllProcesses(Object sendBuffer, int numOfObjectsToSend, Class sendDatatype,
			int rootProcessRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		MPI.COMM_WORLD.Bcast(sendBuffer, 0, numOfObjectsToSend, mpiSendDatatype, rootProcessRank);
	}

	public void broadcastToAllProcesses(Object sendBuffer, int offset, int numOfObjectsToSend, Class sendDatatype,
			int rootProcessRank) {
		Datatype mpiSendDatatype = getMpiDatatype(sendDatatype);
		MPI.COMM_WORLD.Bcast(sendBuffer, offset, numOfObjectsToSend, mpiSendDatatype, rootProcessRank);
	}

	public void mpiBarrier() {
		MPI.COMM_WORLD.Barrier();
	}

	public Datatype getMpiDatatype(Class type) {
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

}
