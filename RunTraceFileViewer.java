import javax.swing.SwingUtilities;

public class RunTraceFileViewer implements Runnable {

	/**
 	* Starts the Trace File Viewer
 	*/
	public void run() {
		TraceFileViewer traceFileViewerWindow = new TraceFileViewer();
	}

	/**
 	* Initialises the Trace File Viewer in a new thread
 	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new RunTraceFileViewer());
	}
}