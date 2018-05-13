import javax.swing.SwingUtilities;

public class RunTraceFileViewer implements Runnable {

	public void run() {
		TraceFileViewer traceFileViewerWindow = new TraceFileViewer();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new RunTraceFileViewer());
	}
}