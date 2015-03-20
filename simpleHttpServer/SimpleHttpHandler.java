import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SimpleHttpHandler {
	
	private String rootDirectory;

	public SimpleHttpHandler(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
	
	/**
	 * After you accepted a connection in SimpleHttpServer, you have a socket leading to the client (remote).
	 * Your server communicates with the client through the socket.
	 * 
	 * Tasks:
	 * #1. Read request message from buffered reader object (You may do it in other ways).
	 * #2. Parse the request and get the request file name.
	 * #3. Check if the file exists or not by requestFile()  (You may do it in other ways).
	 * #4. If the file exists, then you need to create a response message according to assignment
	 *     description, and put the content to the message body.
	 * #5. If the file does not exist, then you create a error message according to assignment description.
	 * #6. Send response message to client by PrintStream object (You may do it in other ways).
	 * 
	 */
	public void handle(Socket remote) {

		try {
			// Create in and out streams
			BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
			PrintStream out = new PrintStream(remote.getOutputStream());

			// HTTP requests should be resolved here based on the protocol
			// After you parsed the request message, you will have a requested file path
			// You may use requestFile() to load the file and respond the content of it to the client
			String[] initiallineParts = in.readLine().split(" ");
			String path = initiallineParts[1];
			File file = this.requestFile(rootDirectory, path);
			if(file == null){
				out.println("HTTP/1.0 500 Internal Server Error");
				out.println();
				out.println("^_^ Internal Server Error. ");
			}else{
				out.println("HTTP/1.0 200 OK");
				out.println();
				FileInputStream fileInputStream = new FileInputStream(file);
				while(true){
					int b = fileInputStream.read();
					if(b==-1)break;
					out.write(b);
				}
				fileInputStream.close();
			}

			// Close the remote socket and r/w objects
			in.close();
			out.close();
			remote.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * You can use this method without any modification.
	 * Just put in rootDirectory and the relative path in request message.
	 * It will return the corresponding file object, or null if it does not exist. 
	 */
	public File requestFile(String rootDirectory, String path) {
		// Construct a full path by connecting <rootDirectory> and requested relative path
		File file = new File(rootDirectory, path);
		
		// If it is a directory, e.g. http://www.cs.bham.ac.uk/
		// Then we load the file index.html, e.g. http://www.cs.bham.ac.uk/index.html
		if (file.isDirectory()) {
			file = new File(file, "index.html");
		}
		
		// If the file exists, the file object is returned
		// Otherwise null is returned
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

}
